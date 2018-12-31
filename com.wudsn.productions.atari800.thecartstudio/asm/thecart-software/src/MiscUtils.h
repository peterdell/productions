#ifndef MISCUTILS_H
#define MISCUTILS_H

/*
   MiscUtils - some helper functions

   Copyright (C) 2008 Matthias Reichl <hias@horus.com>

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

namespace MiscUtils {
	// buffer must be at least 9 bytes (8 digits + 0)!
	void byte_to_bin(unsigned char byte, char* buf);
	long get_file_size(FILE *f);

	// like strtol(str, NULL, 0), but don't recognize a 0 as octal-prefix
	// so only hex and dec will be parsed
	long my_strtol(const char* str);
};

#endif
