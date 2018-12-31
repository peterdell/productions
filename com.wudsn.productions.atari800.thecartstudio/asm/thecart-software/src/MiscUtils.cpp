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

#include <stdlib.h>
#include "AtariDebug.h"
#include "MiscUtils.h"


void MiscUtils::byte_to_bin(unsigned char byte, char* buf)
{
	int i;
	for (i=0;i<8;i++) {
		if (byte & (1<<(7-i))) {
			buf[i] = '1';
		} else {
			buf[i] = '0';
		}
	}
	buf[8] = 0;
}

long MiscUtils::get_file_size(FILE *f)
{
        long current_pos = ftell(f);
        fseek(f, 0L, SEEK_END);
        long end_pos = ftell(f);
        fseek(f, current_pos, SEEK_SET);
        return end_pos;
}

long MiscUtils::my_strtol(const char* str)
{
	if (!str) {
		Assert(false);
		return 0;
	}
	if (str[0] == '0' && str[1] == 'x') {
		return strtol(str, NULL, 16);
	} else {
		return strtol(str, NULL, 10);
	}
}
