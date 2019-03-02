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

import static com.wudsn.tools.base.common.ByteArrayUtility.KB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wudsn.tools.base.atari.CartridgeType;
import com.wudsn.tools.base.repository.ValueSet;
import com.wudsn.tools.thecartstudio.ValueSets;

public final class FlashTargetType extends ValueSet {

	public final static FlashTargetType ATMAX_128;
	public final static FlashTargetType ATMAX_1024;
	public final static FlashTargetType MEGA_4096;
	public final static FlashTargetType THECART_32MB;
	public final static FlashTargetType THECART_64MB;
	public final static FlashTargetType THECART_128MB;
	public final static FlashTargetType TURBO_FREEZER_2005_448;
	public final static FlashTargetType USER_DEFINED;

	// Instances
	private static List<FlashTargetType> values;
	private static final Map<String, FlashTargetType> map;

	// Instance variables
	private CartridgeType cartridgeType;
	private int bankCount;
	private int bankSize;
	private List<CartridgeMenuType> supportedCartridgeMenuTypes;
	private List<ContentType> supportedContentTypes;

	private int[] supportedExportFormats;

	static {
		values = new ArrayList<FlashTargetType>();
		map = new TreeMap<String, FlashTargetType>();

		List<CartridgeMenuType> supportedCartridgeMenuTypes = new ArrayList<CartridgeMenuType>();
		List<ContentType> supportedContentTypes = new ArrayList<ContentType>();
		int sortKey = 0;

		// Atarimax 128 K and 1 MB
		supportedCartridgeMenuTypes.clear();
		supportedCartridgeMenuTypes.add(CartridgeMenuType.NONE);
		supportedCartridgeMenuTypes.add(CartridgeMenuType.EXTENDED);
		supportedContentTypes.clear();
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_8);
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_16);
		supportedContentTypes.add(ContentType.CARTRIDGE_ATMAX_128);

		int[] exportFormats = new int[] { ExportFormat.BIN_IMAGE,
				ExportFormat.CAR_IMAGE };
		ATMAX_128 = add("ATMAX_128", sortKey++,
				CartridgeType.CARTRIDGE_ATMAX_128, 0, 0,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);
		supportedContentTypes.add(ContentType.CARTRIDGE_ATMAX_1024);
		ATMAX_1024 = add("ATMAX_1024", sortKey++,
				CartridgeType.CARTRIDGE_ATMAX_1024, 0, 0,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);

		// MegaCart 4 MB.
		supportedCartridgeMenuTypes.clear();
		supportedCartridgeMenuTypes.add(CartridgeMenuType.NONE);
		supportedContentTypes.clear();
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_8);
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_16);
		exportFormats = new int[] { ExportFormat.BIN_IMAGE,
				ExportFormat.CAR_IMAGE };
		MEGA_4096 = add("MEGA_4096", sortKey++,
				CartridgeType.CARTRIDGE_MEGA_4096, 0, 0,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);

		// The!Cart 128 MB
		supportedCartridgeMenuTypes.clear();
		supportedCartridgeMenuTypes.add(CartridgeMenuType.NONE);
		supportedCartridgeMenuTypes.add(CartridgeMenuType.SIMPLE);
		supportedCartridgeMenuTypes.add(CartridgeMenuType.EXTENDED);
		supportedContentTypes.clear();
		for (ContentType contentType : ContentType.getValues()) {
			switch (contentType.getTheCartMode()) {
			case TheCartMode.TC_MODE_NOT_SUPPORTED:
				break;
			default:
				supportedContentTypes.add(contentType);
			}
		}

		exportFormats = new int[] { ExportFormat.BIN_IMAGE,
				ExportFormat.CAR_IMAGE, ExportFormat.ATR_IMAGE,
				ExportFormat.ATR_IMAGES };
		THECART_32MB = add("THECART_32MB", sortKey++,
				CartridgeType.CARTRIDGE_THECART_32M, 4096, 8192,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);
		THECART_64MB = add("THECART_64MB", sortKey++,
				CartridgeType.CARTRIDGE_THECART_64M, 8192, 8192,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);
		THECART_128MB = add("THECART_128MB", sortKey++,
				CartridgeType.CARTRIDGE_THECART_128M, 0, 0,
				supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);

		// TurboFreezer 2005.
		supportedCartridgeMenuTypes.clear();
		supportedCartridgeMenuTypes.add(CartridgeMenuType.NONE);
		supportedContentTypes.clear();
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_8);
		supportedContentTypes.add(ContentType.CARTRIDGE_STD_16);
		supportedContentTypes.add(ContentType.CARTRIDGE_OSS_M091_16);
		supportedContentTypes.add(ContentType.CARTRIDGE_SDX_64);

		exportFormats = new int[] { ExportFormat.BIN_IMAGE };
		TURBO_FREEZER_2005_448 = add("TURBO_FREEZER_2005_448", sortKey++,
				CartridgeType.UNKNOWN, 56, 8192, supportedCartridgeMenuTypes,
				supportedContentTypes, exportFormats);

		// User defined
		supportedCartridgeMenuTypes.clear();
		supportedCartridgeMenuTypes.add(CartridgeMenuType.NONE);
		supportedCartridgeMenuTypes.add(CartridgeMenuType.SIMPLE);
		supportedCartridgeMenuTypes.add(CartridgeMenuType.EXTENDED);
		supportedContentTypes.clear();
		supportedContentTypes.addAll(ContentType.getValues());

		exportFormats = new int[] { ExportFormat.BIN_IMAGE,
				ExportFormat.CAR_IMAGE, ExportFormat.ATR_IMAGE,
				ExportFormat.ATR_IMAGES };
		USER_DEFINED = add("USER_DEFINED", sortKey++, CartridgeType.UNKNOWN,
				128, 8192, supportedCartridgeMenuTypes, supportedContentTypes,
				exportFormats);

		values = Collections.unmodifiableList(values);

		initializeClass(FlashTargetType.class, ValueSets.class);
	}

	private FlashTargetType(String id, int sortKey,
			CartridgeType cartridgeType, int bankCount, int bankSize,
			List<CartridgeMenuType> supportedCartridgeMenuTypes,
			List<ContentType> supportedContentTypes, int[] supportedExportFormats) {
		super(id, sortKey);
		this.cartridgeType = cartridgeType;
		if (cartridgeType.getSizeInKB() > 0 && bankCount == 0 && bankSize == 0) {
			this.bankCount = cartridgeType.getSizeInKB() * KB
					/ cartridgeType.getBankSize();
			this.bankSize = cartridgeType.getBankSize();

		} else {
			this.bankCount = bankCount;
			this.bankSize = bankSize;
		}

		this.supportedCartridgeMenuTypes = Collections
				.unmodifiableList(new ArrayList<CartridgeMenuType>(
						supportedCartridgeMenuTypes));
		this.supportedContentTypes = Collections
				.unmodifiableList(new ArrayList<ContentType>(supportedContentTypes));
		this.supportedExportFormats = supportedExportFormats;

	}

	private static FlashTargetType add(String id, int sortKey,
			CartridgeType cartridgeType, int bankCount, int bankSize,
			List<CartridgeMenuType> supportedCartridgeMenuTypes,
			List<ContentType> supportedFileTypes, int[] supportedExportFormats) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}
		if (cartridgeType == null) {
			throw new IllegalArgumentException(
					"Parameter 'cartridgeType' must not be null.");
		}
		if (cartridgeType.getSizeInKB() == 0 && bankCount < 1) {
			throw new IllegalArgumentException(
					"Parameter 'bankCount' must be positive if the cartridge type does not specify the size. Specified value is "
							+ bankCount + ".");
		}

		if (supportedFileTypes == null) {
			throw new IllegalArgumentException(
					"Parameter 'supportedContentTypes' must not be null.");
		}
		if (supportedExportFormats == null) {
			throw new IllegalArgumentException(
					"Parameter 'supportedExportFormats' must not be null.");
		}

		FlashTargetType result = new FlashTargetType(id, sortKey,
				cartridgeType, bankCount, bankSize,
				supportedCartridgeMenuTypes, supportedFileTypes,
				supportedExportFormats);
		values.add(result);
		map.put(id, result);
		return result;
	}

	/**
	 * Gets the unmodifiable list of all values.
	 * 
	 * @return The unmodifiable list of all values, not <code>null</code>.
	 */
	public static List<FlashTargetType> getValues() {
		return values;
	}

	/**
	 * Gets a value set instance by its id.
	 * 
	 * @param id
	 *            The id, not <code>null</code>.
	 * @return The value set instance or <code>null</code>.
	 */
	public static FlashTargetType getInstance(String id) {
		if (id == null) {
			throw new IllegalArgumentException(
					"Parameter 'id' must not be null.");
		}
		return map.get(id);
	}

	/**
	 * Gets the {@link CartridgeType} which can is used by default to export
	 * this target type as ".CAR" file.
	 * 
	 * @return The cartridge type or {@link CartridgeType#UNKNOWN} if there is
	 *         not suitable cartridge type.
	 */
	public CartridgeType getCartridgeType() {
		return cartridgeType;
	}

	/**
	 * Gets the number of banks.
	 * 
	 * @return The number of banks, a non-negative integer.
	 */
	public int getBankCount() {
		return bankCount;
	}

	/**
	 * Gets the size of a bank in bytes.
	 * 
	 * @return The size of a bank in bytes, a non-negative integer.
	 */
	public int getBankSize() {
		return bankSize;
	}

	/**
	 * Determines if a given export format is supported by this flash target
	 * type.
	 * 
	 * @param exportFormat
	 *            The export format, see {@link ExportFormat}
	 * @return <code>true</code> if the export format is supported,
	 *         <code>false</code> otherwise.
	 */
	public boolean isExportFormatSupported(int exportFormat) {
		for (int supportedExportFormat : supportedExportFormats) {
			if (supportedExportFormat == exportFormat) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if a cartridge menu type is supported by the flash target
	 * type.
	 * 
	 * @param cartridgeMenuType
	 *            The cartridge menu type, not <code>null</code>.
	 * @return <code>true</code> if the cartridge menu type is supported by the
	 *         flash target type, <code>false</code> otherwise.
	 */
	public boolean isCartridgeMenuTypeSupported(
			CartridgeMenuType cartridgeMenuType) {
		if (cartridgeMenuType == null) {
			throw new IllegalArgumentException(
					"Parameter 'cartridgeMenuType' must not be null.");
		}
		return supportedCartridgeMenuTypes.contains(cartridgeMenuType);
	}

	/**
	 * Gets the unmodifiable list of supported cartridge menu types.
	 * 
	 * @return The unmodifiable list of supported cartridge menu types, not
	 *         empty and not <code>null</code>.
	 * 
	 */
	public List<CartridgeMenuType> getSupportedCartridgeMenuTypes() {
		return supportedCartridgeMenuTypes;
	}

	/**
	 * Determines if a content type is supported by the flash target type.
	 * 
	 * @param contentType
	 *            The content type, not <code>null</code>.
	 * @return <code>true</code> if the content type is supported by the flash
	 *         target type, <code>false</code> otherwise.
	 */
	public boolean isContentTypeSupported(ContentType contentType) {
		if (contentType == null) {
			throw new IllegalArgumentException(
					"Parameter 'fileType' must not be null.");
		}
		return supportedContentTypes.contains(contentType);
	}
}
