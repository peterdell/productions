#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <openssl/sha.h>

#include "CartDef.h"
#include "MyOpts.h"
#include "MiscUtils.h"

enum OutType {
	e8kAtr = 0,
	eDDAtr = 1,
};

OutType outType = e8kAtr;

unsigned int totalBanks = 0;

unsigned int currentBank = 0;

unsigned int maxBank = 0;
unsigned int maxBlock;

// full cart data
unsigned char* data = NULL;

// empty bank
unsigned char* emptyBank = NULL;

// 384 bytes boot sectors
#define BOOT_SECTORS_SIZE 384
unsigned char bootSectors[BOOT_SECTORS_SIZE];

// ATR header
#define ATR_HEADER_SIZE 16
unsigned char atrHeader[ATR_HEADER_SIZE];

// image header
unsigned char* header = NULL;

unsigned char* romData = NULL;
unsigned long romSize;
unsigned int romBankSize;
bool romNeedAlign;

// image name
char img_name[IMG_NAME_LEN];
char img_timestamp[IMG_TIMESTAMP_LEN];

// default boot code for DD ATRs
unsigned char bootCode[] = {
	0,
	1,	// 1 sector
	0, 7,	// load address $0700
	7, 7,	// return address $0707
	0x38,	// sec
	0x60	// rts
};

void set_image_name(const char* name)
{
	int dst = 0;
	memset(img_name, 0xff, IMG_NAME_LEN);
	char c;
	while ((c = *(name++)) && dst < IMG_NAME_LEN) {
		if (c < 32 || c == 96 || c == 123 || c >= 125) {
			continue;
		}
		img_name[dst++] = c;
	}
	if (dst < IMG_NAME_LEN) {
		img_name[dst] = 155;
	}
}

void set_image_timestamp()
{
	time_t t = time(NULL);
	struct tm *tm = localtime(&t);

	strftime(img_timestamp, IMG_TIMESTAMP_LEN,
		"%Y-%m-%d %H:%M:%S", tm);
	img_timestamp[IMG_TIMESTAMP_LEN - 1] = 155;
}

void alloc_data()
{
	data = (unsigned char*) malloc(totalBanks * BANKSIZE);
	memset(data, 0xff, totalBanks * BANKSIZE);
	emptyBank = (unsigned char*) malloc(BANKSIZE);
	memset(emptyBank, 0xff, BANKSIZE);
	header = (unsigned char*) malloc(BANKSIZE);
	memset(header, 0, BANKSIZE);
	memset(bootSectors, 0, BOOT_SECTORS_SIZE);
	memcpy(bootSectors, bootCode, sizeof(bootCode));
}

void free_rom()
{
	if (romData != NULL) {
		free(romData);
		romData = NULL;
	}
}

// try to read ROM file.
// sets up romData, romSize, romBankSize, romNeedAlign and returns true on success
bool read_rom(const char* filename)
{
	free_rom();
	FILE *f;
	f = fopen(filename, "rb");
	if (!f) {
		printf("error opening %s\n", filename);
		return false;
	}

	romSize = MiscUtils::get_file_size(f);

	romNeedAlign = true;

	switch (romSize) {
	case BANKSIZE:
	case 2*BANKSIZE:
	case 4*BANKSIZE:
	case 8*BANKSIZE:
	case 16*BANKSIZE:
	case 32*BANKSIZE:
	case 64*BANKSIZE:
	case 128*BANKSIZE:
	case 256*BANKSIZE:
        	fseek(f, 0L, SEEK_SET);
		break;
	case 16 + BANKSIZE:
	case 16 + 2*BANKSIZE:
	case 16 + 4*BANKSIZE:
	case 16 + 8*BANKSIZE:
	case 16 + 16*BANKSIZE:
	case 16 + 32*BANKSIZE:
	case 16 + 64*BANKSIZE:
	case 16 + 128*BANKSIZE:
	case 16 + 256*BANKSIZE:
        	fseek(f, 16L, SEEK_SET);
		romSize -= 16;
		break;
	default:
        	fseek(f, 0L, SEEK_SET);
		if (romSize % 8192 != 0) {
			printf("unsupported file length %ld in %s\n", romSize, filename);
			fclose(f);
			return false;
		}
		romNeedAlign = false;
		break;
	}

	romBankSize = romSize / BANKSIZE;

	romData = (unsigned char*) malloc(romSize);
	
	if (fread(romData, BANKSIZE, romBankSize, f) != romBankSize) {
		printf("error reading %s\n", filename);
		fclose(f);
		return false;
	}
	fclose(f);
	return true;
}

// setup currentBank so that the rom will be aligned
void align_bank(unsigned int bankcount)
{
	currentBank = (currentBank + bankcount - 1) & (~(bankcount - 1));
}

// setup currentbank so that it is aligned and doesn't collide
// with the hash area
void set_currentbank_aligned(unsigned int bankcount, bool doAlign)
{
	if (doAlign) {
		align_bank(bankcount);
	}
	// check for overlap with checksum area
	if (currentBank >= (HASH_BLOCK_START + HASH_BLOCK_COUNT) * BANKS_PER_BLOCK ||
		currentBank + bankcount <= HASH_BLOCK_START * BANKS_PER_BLOCK) {
		return;
	}
	currentBank = (HASH_BLOCK_START + HASH_BLOCK_COUNT) * BANKS_PER_BLOCK;
	if (doAlign) {
		align_bank(bankcount);
	}
}

bool try_add_rom(const char* filename)
{
	if (read_rom(filename)) {
		set_currentbank_aligned(romBankSize, romNeedAlign);
		if (currentBank + romBankSize > totalBanks) {
			printf("not enough space for %s\n", filename);
			return true;
		}
		printf("%5d - %5d %s\n", currentBank, currentBank + romBankSize - 1, filename);

		memcpy(data + currentBank * BANKSIZE, romData, romSize);
		free_rom();

		currentBank += romBankSize;
		if (currentBank > maxBank) {
			maxBank = currentBank;
		}
		return true;
	} else {
		return false;
	}
}

void calculate_hashes()
{
	//printf("calculating hashes of %d blocks\n", maxBlock);
	for (unsigned int i = 0; i < maxBlock; i++) {
		if (i >= HASH_BLOCK_START && i < HASH_BLOCK_START + HASH_BLOCK_COUNT) {
			continue;
		}
		//printf("checksum block %d\n", i);
		unsigned int hashOfs = HASH_BLOCK_START*BLOCKSIZE + i * HASH_LEN;
		SHA512(data + i * BLOCKSIZE, BLOCKSIZE, data + hashOfs);
	}
}

void add_thecart_signature()
{
	memcpy(data + SIGNATURE_OFS, SIGNATURE_DATA, SIGNATURE_LEN);
	data[BLOCKUSE_OFS] = maxBlock & 0xff;
	data[BLOCKUSE_OFS+1] = maxBlock >> 8;
	memcpy(data + IMGNAME_OFS, img_name, IMG_NAME_LEN);
}

void calculate_usage_map()
{
	unsigned char inuse;
	memset(header + HDR_BITMAP_OFS, 0, HDR_BITMAP_LEN);
	for (unsigned int block = 0; block < maxBlock * 2; block++) {
		inuse = 0;
		for (unsigned int i = 0; i < 8; i++) {
			if (memcmp(data + block * BANKSIZE * 8 + i * BANKSIZE, emptyBank, BANKSIZE) != 0) {
				inuse |= (1 << i);
			}
		}
		header[HDR_BITMAP_OFS + block] = inuse;
	}
}

// TODO: support multi-image files
void setup_base_header()
{
	SHA512(data + HASH_BLOCK_START * BLOCKSIZE,
		HASH_BLOCK_COUNT * BLOCKSIZE, header + HDR_HASH_OFS);
	memcpy(header + HDR_SIGNATURE_OFS, HDR_SIGNATURE_DATA, HDR_SIGNATURE_LEN);
	header[HDR_BLOCKS_OFS] = maxBlock & 0xff;
	header[HDR_BLOCKS_OFS + 1] = maxBlock >> 8;
	header[HDR_TOTAL_PARTS_OFS] = 1;
	header[HDR_PARTNO_OFS] = 1;
	header[HDR_CS_START_OFS] = HASH_BLOCK_START & 0xff;
	header[HDR_CS_START_OFS+1] = HASH_BLOCK_START >> 8;
	header[HDR_CS_COUNT_OFS] = HASH_BLOCK_COUNT;
	memcpy(header + HDR_NAME_OFS, img_name, IMG_NAME_LEN);
	memcpy(header + HDR_TIMESTAMP_OFS, img_timestamp, IMG_TIMESTAMP_LEN);

	calculate_usage_map();
}

bool process_args(RCPtr<MyOpts> args)
{
	const char* arg;
	while ((arg = args->GetOpt())) {
		if (strncmp(arg, "-b", 2) == 0) {
			int newbank = MiscUtils::my_strtol(arg+2);
			if (newbank > 0 && newbank < (int) totalBanks) {
				currentBank = newbank;
			} else {
				printf("invalid new bank %d\n", newbank);
				return false;
			}
			continue;
		}
		if (strncmp(arg, "-n", 2) == 0) {
			set_image_name(arg+2);
			continue;
		}
		if (!try_add_rom(arg)) {
			return false;
		}
	}
	return true;
}

void createAtrHeader(unsigned long datasize, unsigned int sectorsize)
{
	unsigned long parSize = datasize / 16;

	memset(atrHeader, 0, ATR_HEADER_SIZE);
	atrHeader[0] = 0x96;
	atrHeader[1] = 0x02;
	atrHeader[2] = parSize & 0xff;
	atrHeader[3] = (parSize>>8) & 0xff;
	atrHeader[6] = (parSize>>16) & 0xff;
	atrHeader[7] = (parSize>>24) & 0xff;
	atrHeader[4] = (sectorsize & 0xff);
	atrHeader[5] = (sectorsize >> 8);
}

bool write_atr_header(FILE* f)
{
	return fwrite(atrHeader, ATR_HEADER_SIZE, 1, f) == 1;
}

bool write_boot_sectors(FILE* f)
{
	return fwrite(bootSectors, BOOT_SECTORS_SIZE, 1, f) == 1;
}

bool write_image_header(FILE* f)
{
	return fwrite(header, BANKSIZE, 1, f) == 1;
}

bool save_dd_part(const char* filename, unsigned int startblock, unsigned int numblocks)
{
	// 3 unused SD sectors
	// + 8k header
	// + data
	unsigned long dataSize = BOOT_SECTORS_SIZE + BANKSIZE + numblocks * BLOCKSIZE;
	createAtrHeader(dataSize, 256);

	FILE*f = fopen(filename, "wb");
	if (!f) {
		printf("cannot create output file %s\n", filename);
		return false;
	}

	if (!write_atr_header(f)) {
		goto writeErr;
	}
	if (!write_boot_sectors(f)) {
		goto writeErr;
	}
	if (!write_image_header(f)) {
		goto writeErr;
	}
	if (fwrite(data + startblock * BLOCKSIZE,
		BLOCKSIZE, numblocks, f) != numblocks) {
		goto writeErr;
	}
	fclose(f);
	return true;

writeErr:
	fclose(f);
	printf("error writing output file %s\n", filename);
	return false;
}

bool save_output_dd(const char* filebase, const char* ext)
{
	char filename[PATH_MAX];

	unsigned int num_atrs = (maxBlock + BLOCKS_PER_DD_ATR - 1) / BLOCKS_PER_DD_ATR;
	unsigned int atrno;

	header[HDR_TOTAL_PARTS_OFS] = num_atrs;

	// printf("will create %d ATRs\n", num_atrs);

	for (atrno = 1; atrno <= num_atrs; atrno++) {
		header[HDR_PARTNO_OFS] = atrno;

		snprintf(filename, PATH_MAX, "%s_%d%s",
			filebase, atrno, ext);
		unsigned int startblock = (atrno - 1) * BLOCKS_PER_DD_ATR;
		unsigned int blockcount = maxBlock - startblock;
		if (blockcount > BLOCKS_PER_DD_ATR) {
			blockcount = BLOCKS_PER_DD_ATR;
		}
		if (!save_dd_part(filename, startblock, blockcount)) {
			return false;
		}
		printf("successfully created %s (bank %5d - %5d)\n",
			filename,
			startblock * BANKS_PER_BLOCK,
			(startblock + blockcount) * BANKS_PER_BLOCK - 1
			);
	}

	return true;
}

bool save_output(const char* filename)
{
	unsigned long dataSize = maxBlock * BLOCKSIZE + BANKSIZE;
	createAtrHeader(dataSize, BANKSIZE);

	FILE*f = fopen(filename, "wb");
	if (!f) {
		printf("cannot create output file %s\n", filename);
		return false;
	}

	if (!write_atr_header(f)) {
		goto writeErr;
	}
	if (!write_image_header(f)) {
		goto writeErr;
	}

	if (fwrite(data, BLOCKSIZE, maxBlock, f) != maxBlock) {
		goto writeErr;
	}
	printf("successfully created %s (%d banks)\n",
		filename,
		maxBlock * BANKS_PER_BLOCK
		);
	fclose(f);
	return true;

writeErr:
	fclose(f);
	printf("error writing output file %s\n", filename);
	return false;
}

int main(int argc, char** argv)
{
	RCPtr<MyOpts> args;
	const char* arg;
	char* outfile;
	char* outext;
	int len;

	printf("romtool V0.4 (c) 2013 Matthias Reichl <hias@horus.com>\n");

	if (argc < 2) {
		goto usage;
	}
	args = new MyOpts(argc, argv, true);

	if (!(arg = args->GetOpt())) {
		goto usage;
	}
	if (strcasecmp(arg, "64") == 0) {
		outType = e8kAtr;
		totalBanks = TOTALBANKS_64MB;
	} else if (strcasecmp(arg, "64d") == 0) {
		outType = eDDAtr;
		totalBanks = TOTALBANKS_64MB;
	} else
	if (strcasecmp(arg, "128") == 0) {
		outType = e8kAtr;
		totalBanks = TOTALBANKS_128MB;
	} else
	if (strcasecmp(arg, "128d") == 0) {
		outType = eDDAtr;
		totalBanks = TOTALBANKS_128MB;
	} else {
		printf("unsupported type %s\n", arg);
		goto usage;
	}

	if (!(arg = args->GetOpt())) {
		goto usage;
	}
	if ((len = strlen(arg)) < 4) {
		goto usage;
	}
	if (strcasecmp(arg + len - 4, ".atr")) {
		printf("output file must end with .atr\n");
		goto usage;
	}

	outfile = strdup(arg);
	if (outType == eDDAtr) {
		outext = strdup(arg + len - 4);
		outfile[len - 4] = 0;
	}

	alloc_data();
	set_image_name("romtool cart");
	set_image_timestamp();

	if (!process_args(args)) {
		return 1;
	}
	maxBlock = (maxBank + BANKS_PER_BLOCK - 1) / BANKS_PER_BLOCK;
	if (maxBlock == 0) {
		printf("error: no data read!\n");
		return 1;
	}
	if (maxBlock < HASH_BLOCK_START + HASH_BLOCK_COUNT) {
		maxBlock = HASH_BLOCK_START + HASH_BLOCK_COUNT;
	}
	calculate_hashes();
	add_thecart_signature();

	setup_base_header();

	if (outType == eDDAtr) {
		save_output_dd(outfile, outext);
	} else {
		save_output(outfile);
	}

	return 0;
usage:
	printf("usage: romtool type output.atr romfiles...\n");
	printf("type: 64   (64MB, single 8k sector ATR)\n");
	printf("      64d  (64MB, multiple DD ATRs)\n");
	printf("      128  (128MB, single 8k sector ATR)\n");
	printf("      128d (128MB, multiple DD ATRs)\n");
	return 1;
}
