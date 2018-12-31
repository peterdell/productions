/*
   rom2atr8.cpp - create ATR with 8k sectors from ROM image

   Copyright (C) 2011 Matthias Reichl <hias@horus.com>

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

unsigned char buf[8192];

int main(int argc, char** argv)
{
	if (argc != 3) {
		printf("usage: rom2atr8 rom-file atr-file\n");
		return 1;
	}
	const char* infile=argv[1];
	const char* outfile=argv[2];
	FILE* in;
	FILE *out;
	unsigned long size;
	unsigned int blocks;

	if (!(in = fopen(infile, "rb"))) {
		printf("cannot open input file %s\n", infile);
		return 1;
	}
	if (fseek(in, 0, SEEK_END)) {
		printf("seeking to end of %s failed\n", infile);
		return 1;
	}
	size = ftell(in);
	if (fseek(in, 0, SEEK_SET)) {
		printf("seeking to beginning of %s failes\n", infile);
		return 1;
	}
	if (size % 8192) {
		printf("input size not multiple of 8k!\n");
		return 1;
	}
	blocks = size / 8192;
	if (!(out = fopen(outfile, "wb"))) {
		printf("cannot create output file %s\n", outfile);
		return 1;
	}
	printf("creating %s file with %d 8k sectors\n", outfile, blocks);

	// setup ATR header
	memset(buf, 0, 16);
	buf[0] = 0x96;
	buf[1] = 0x02;
	unsigned long parSize = size / 16;
	buf[2] = parSize & 0xff;
	buf[3] = (parSize>>8) & 0xff;
	buf[6] = (parSize>>16) & 0xff;
	buf[7] = (parSize>>24) & 0xff;
	buf[4] = 0;
	buf[5] = 32;	// 8k sector size

	if (fwrite(buf, 1, 16, out) != 16) {
		printf("cannot write to output file!\n");
		return 1;
	}
	unsigned int i;
	for (i = 0; i < blocks; i++) {
		if (fread(buf, 1, 8192, in) != 8192) {
			printf("reading from input file failed!\n");
			return 1;
		}
		if (fwrite(buf, 1, 8192, out) != 8192) {
			printf("writing to output file failed!\n");
			return 1;
		}
	}
	fclose(in);
	fclose(out);
	printf("done!\n");
	return 0;
}
