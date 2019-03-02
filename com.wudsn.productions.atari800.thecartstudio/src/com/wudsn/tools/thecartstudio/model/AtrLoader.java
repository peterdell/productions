/**
 * Copyright (C) 2013 - 2019 <a href="http://www.wudsn.com" target="_top">Peter Dell</a>
 *
 * This file is part of The!Cart Studio distribution.
 * 
 * The!Cart Studio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The!Cart Studio distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with The!Cart Studio. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wudsn.tools.thecartstudio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wudsn.tools.base.atari.AtrFile;
import com.wudsn.tools.base.common.ByteArrayUtility;
import com.wudsn.tools.base.common.HexUtility;
import com.wudsn.tools.base.common.Log;
import com.wudsn.tools.thecartstudio.model.WorkbookEntry.Parameter;
import com.wudsn.tools.thecartstudio.model.atrfile.AtrFileMenu;

/**
 * Utility class to handle ATR image loading. ATR images are going through a
 * binary translation during the export. There all JMP and JSR to SIO and disk
 * vectors are patched to use a routine in RAM which loads from the cartridge.
 */
public final class AtrLoader {

	public static final class Constants {
		public static final String BASE = "base";
		public static final String DSKINV = "dskinv";
		public static final String SIOV = "siov";
		public static final String SELECTED_ITEM_NUMBER = "selected_item_number";

		public static final int DEFAULT_BASE = 0x100;
	}

	/**
	 * Container for describing the area in which patching shall be applied.
	 */
	public static final class PatchRange {
		public final int startOffset;
		public final int endOffset;

		/**
		 * Creates a new patch range.
		 * 
		 * @param startOffset
		 *            The inclusive start offset of the relevant area, a
		 *            non-negative integer.
		 * @param endOffset
		 *            The inclusive end offset of the relevant area, a
		 *            non-negative integer not less than the startOffset.
		 */
		public PatchRange(int startOffset, int endOffset) {
			if (startOffset < 0)
				throw new IllegalArgumentException(
						"Parameter startOffset must not be negative. Specified value is "
								+ startOffset + ".");
			if (endOffset < 0)
				throw new IllegalArgumentException(
						"Parameter endOffset must not be negative. Specified value is "
								+ endOffset + ".");
			if (startOffset > endOffset)
				throw new IllegalArgumentException(
						"Parameter startOffset must be larger then endOffset. Specified value for startOffset is "
								+ startOffset
								+ ". Specified endOffset is "
								+ endOffset + ".");

			this.startOffset = startOffset;
			this.endOffset = endOffset;
		}

		public boolean isContained(int offset) {
			if (offset < 0)
				throw new IllegalArgumentException(
						"Parameter offset must not be negative. Specified value is "
								+ offset + ".");
			return startOffset <= offset && offset <= endOffset;
		}

		public static boolean isContained(int offset,
				List<PatchRange> patchRanges) {
			for (PatchRange patchRange : patchRanges) {
				if (patchRange.isContained(offset)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return "[0x" + HexUtility.getLongValueHexString(startOffset)
					+ ",0x" + HexUtility.getLongValueHexString(endOffset) + "]";
		}
	}

	private static final class PatchLocation {

		public final String name;
		public final byte[] pattern;
		public final int patchOffset;

		public final boolean absolute;
		public final int absoluteAddress;
		public final int baseOffset;

		/**
		 * Creates a new entry point definition.
		 * 
		 * @param name
		 *            The name of the vector to be patched.
		 * @param pattern
		 *            The byte sequence with the JSR or JMP to the vector.
		 * @param patchOffset
		 *            The patch offset relative to the location where the
		 *            pattern was found.
		 * @param absolute
		 *            <code>true</code> if the absoluteAddress is relevant,
		 *            <code>false</code> if baseOffset is relevant.
		 * @param absoluteAddress
		 *            The absolute address.
		 * @param baseOffset
		 *            The execution offset relative to the BASE address.
		 */
		public PatchLocation(String name, byte[] pattern, int patchOffset,
				boolean absolute, int absoluteAddress, int baseOffset) {
			if (name == null) {
				throw new IllegalArgumentException(
						"Parameter 'name' must not be null.");
			}
			if (pattern == null) {
				throw new IllegalArgumentException(
						"Parameter 'pattern' must not be null.");
			}
			this.name = name;
			this.pattern = pattern;
			this.patchOffset = patchOffset;
			this.absolute = absolute;
			this.absoluteAddress = absoluteAddress;
			this.baseOffset = baseOffset;
		}

		public boolean hasPattern() {
			return pattern.length > 0;
		}

	}

	// List of entry points to be intercepted.
	private static List<PatchLocation> patchLocations;

	static {
		// Build the list of SIO entry points to be intercepted.
		patchLocations = new ArrayList<PatchLocation>();

		patchLocations.add(new PatchLocation(Constants.DSKINV, new byte[] {
				(byte) 0x20, (byte) 0x53, (byte) 0xe4 }, 1, false, -1, 3));
		patchLocations.add(new PatchLocation(Constants.DSKINV, new byte[] {
				(byte) 0x4c, (byte) 0x53, (byte) 0xe4 }, 1, false, -1, 3));
		patchLocations.add(new PatchLocation(Constants.SIOV, new byte[] {
				(byte) 0x20, (byte) 0x59, (byte) 0xe4 }, 1, false, -1, 6));
		patchLocations.add(new PatchLocation(Constants.SIOV, new byte[] {
				(byte) 0x4c, (byte) 0x59, (byte) 0xe4 }, 1, false, -1, 6));

		// Add symbolic patch locations
		patchLocations.add(new PatchLocation(Constants.SELECTED_ITEM_NUMBER,
				new byte[0], 0, true, 0x0000, 0));
	}

	/**
	 * Sets the default parameters based on the importable ATR file menu
	 * (optional) and the content of the ATR.
	 * 
	 * @param entry
	 *            The workbook entry to be updated.
	 * @param atrFileMenu
	 *            the importable ATR file menu or <code>null</code>
	 * @param content
	 *            The ATR content to be analyzed, may be empty, not
	 *            <code>null</code>.
	 */
	public static void setDefaultParameters(WorkbookEntry entry,
			AtrFileMenu atrFileMenu, byte[] content) {
		if (entry == null) {
			throw new IllegalArgumentException(
					"Parameter 'entry' must not be null.");
		}
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		if (!AtrFile.isHeader(content)) {
			throw new IllegalArgumentException(
					"Parameter 'content' must be an ATR file");
		}

		List<PatchRange> patchRanges = new ArrayList<PatchRange>();
		if (atrFileMenu != null) {
			patchRanges = atrFileMenu.getPatchRanges();
		} else {
			patchRanges.add(new PatchRange(0, content.length));
		}

		// Get actual SIO patch locations
		List<Parameter> parameters = determineDefaultParameters(content,
				patchRanges);

		// Add default base parameter.
		parameters.add(new Parameter(Constants.BASE, Constants.DEFAULT_BASE));

		// Add menu specific patches is available.
		if (atrFileMenu != null) {
			parameters.addAll(atrFileMenu.getPatchParameters());
		}

		Collections.sort(parameters);
		entry.setParameters(Parameter.getParametersString(parameters));
	}

	private static List<Parameter> determineDefaultParameters(byte[] content,
			List<PatchRange> patchRanges) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		if (!AtrFile.isHeader(content)) {
			throw new IllegalArgumentException(
					"Parameter 'content' must be an ATR file");
		}
		int startOffset = AtrFile.HEADER_SIZE;
		List<Parameter> result = new ArrayList<Parameter>();
		for (PatchLocation patchLocation : patchLocations) {
			// Only search for pattern based patches
			if (patchLocation.hasPattern()) {
				// Only search in actual content
				int offset = startOffset;
				while (offset >= 0 && offset < content.length
						&& PatchRange.isContained(offset, patchRanges)) {
					offset = ByteArrayUtility
							.getIndexOf(content, offset, content.length
									- startOffset, patchLocation.pattern);
					if (offset >= 0
							&& PatchRange.isContained(offset, patchRanges)) {
						result.add(new Parameter(offset
								+ patchLocation.patchOffset, patchLocation.name));
						offset += patchLocation.pattern.length;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Gets the base address from a workbook entry.
	 * 
	 * @param entry
	 *            The workbook entry, not <code>null</code>.
	 * @return The specific base address or the default if none is specified.
	 */
	public static int getBaseAddress(WorkbookEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException(
					"Parameter 'entry' must not be null.");
		}
		// Parse assignments.
		int base = -1;
		for (Parameter parameter : entry.getParametersList()) {
			if (parameter.key.equals(Constants.BASE)) {
				if (parameter.isValueInteger()) {
					base = parameter.getValueAsInteger();
				}
			}
		}
		if (base == -1) {
			base = Constants.DEFAULT_BASE;
		}
		return base;
	}

	/**
	 * Modify the content based on the patch location parameters.
	 * 
	 * @param entry
	 *            The workbook entry, not <code>null</code>.
	 * @param content
	 *            The content, not <code>null</code>.
	 * @return The modified content, not <code>null</code>.
	 */
	public static byte[] modifyContent(WorkbookEntry entry, byte[] content) {
		if (entry == null) {
			throw new IllegalArgumentException(
					"Parameter 'parametersList' must not be null.");
		}
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		if (!AtrFile.isHeader(content)) {
			throw new IllegalArgumentException(
					"Parameter 'content' must be an ATR file");
		}
		// Get base address from parameters.
		int base = getBaseAddress(entry);

		// Find all occurrences of patch locations.
		for (Parameter parameter : entry.getParametersList()) {

			// Apply parameters that are direct patches.
			if (parameter.isKeyInteger() && parameter.isValueInteger()) {
				modifyContent(content, parameter.getKeyAsInteger(),
						parameter.getValueAsInteger() & 0xff);
			}

			// Apply parameters that are SIO patches.
			for (PatchLocation patchLocation : patchLocations) {
				String value = parameter.value;
				int mode = 0; // Word, there is no support for bytes
				if (value.startsWith("<")) {
					mode = 1; // Low byte
				} else if (value.startsWith(">")) {
					mode = 2; // High byte
				}
				if (mode != 0) {
					value = value.substring(1);
				}
				if (parameter.isKeyInteger()
						&& value.equals(patchLocation.name)) {
					int offset = parameter.getKeyAsInteger();
					if (offset < content.length) {
						// Compute target address.
						int address = patchLocation.absolute ? patchLocation.absoluteAddress
								: base + patchLocation.baseOffset;

						// Patch target offset
						switch (mode) {
						case 0:
							modifyContent(content, offset, address & 0xff);
							modifyContent(content, offset + 1,
									address >>> 8 & 0xff);
							break;
						case 1:
							modifyContent(content, offset, address & 0xff);
							break;
						case 2:
							modifyContent(content, offset, address >>> 8 & 0xff);
							break;
						default:
							throw new RuntimeException("Undefined mode " + mode
									+ ".");
						}
						break; // for patchLocation
					}
				}
			}
		}

		// Add padding after ATR header to get correct page and bank
		// boundaries.
		// ATR header: offset $000, 16 bytes
		// Sector 1 : offset $080, 128 bytes (SD & DD)
		// Sector 2 : offset $100, 128 bytes (SD & DD)
		// Sector 3 : offset $180, 128 bytes (SD & DD)
		// Sector 4ff: offset $200, 128 (SD) or 256 bytes (DD)

		// Round size up to SECTOR_SIZE_SD
		int resultSize = ((content.length + AtrFile.SECTOR_SIZE_SD - 1) / AtrFile.SECTOR_SIZE_SD)
				* AtrFile.SECTOR_SIZE_SD;
		byte[] result = new byte[resultSize];

		// Copy header
		System.arraycopy(content, 0, result, 0, AtrFile.HEADER_SIZE);

		// Copy sectors
		System.arraycopy(content, AtrFile.HEADER_SIZE, result,
				AtrFile.SECTOR_SIZE_SD, content.length - AtrFile.HEADER_SIZE);
		return result;
	}

	private static void modifyContent(byte[] content, int offset, int value) {
		if (content == null) {
			throw new IllegalArgumentException(
					"Parameter 'content' must not be null.");
		}
		Log.logInfo(
				"Replacing {0} with {1} at offset {2}",
				new Object[] {
						HexUtility
								.getByteValueHexString(content[offset] & 0xff),
						HexUtility.getByteValueHexString(value),
						HexUtility.getLongValueHexString(offset) });

		content[offset] = (byte) (value & 0xff);
	}
}
