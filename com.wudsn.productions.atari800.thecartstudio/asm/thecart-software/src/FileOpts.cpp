/*
   FileOpts - read options from a file

   Copyright (C) 2006 Matthias Reichl <hias@horus.com>

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

#include <stdio.h>
#include <string.h>
#include "FileOpts.h"
#include "AtariDebug.h"
#include "Error.h"

FileOpts::FileOpts(const char* filename)
	: super()
{
	fStream.open(filename);
	if (!fStream.good()) {
		throw FileOpenError(filename);
	}
}

FileOpts::~FileOpts()
{
	if (fStream.good()) {
		fStream.close();
	}
}

const char* FileOpts::GetOpt()
{
	while (true) {
		if (!fStream.good()) {
			return 0;
		}
		if (fStream.eof()) {
			fStream.close();
			return 0;
		}
		fStream.getline(fBuf, eBufSize);
		int l = strlen(fBuf);
		if (l && fBuf[l-1] == '\r') {
			fBuf[--l] = 0;
		}
		if (l && fBuf[l-1] == '\n') {
			fBuf[--l] = 0;
		}
		if (l) {
			return fBuf;
		}
	}
}
