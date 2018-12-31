#include <stdio.h>
#include <string.h>
#include <openssl/sha.h>

#define BLOCKLEN 65536
#define HASHLEN 64

unsigned char buf[BLOCKLEN];

unsigned char md[HASHLEN];
SHA512_CTX sha_ctx;

void print_hash(const unsigned char* md)
{
	for (int i = 0; i < HASHLEN; i++) {
		printf("%02x", md[i]);
	}
	printf("\n");
}

int main(int argc, char** argv)
{
	if (argc != 2) {
		printf("usage: shatest file\n");
		return 1;
	}

	FILE* f;
	f = fopen(argv[1], "rb");
	if (!f) {
		printf("cannot open %s\n", argv[1]);
		return 1;
	}

	while (fread(buf, BLOCKLEN, 1, f) == 1) {
	/*
		SHA512_Init(&sha_ctx);
		SHA512_Update(&sha_ctx, buf, BLOCKLEN);
		SHA512_Final(md, &sha_ctx);
		print_hash(md);
		memset(md, 0, HASHLEN);
	*/
		print_hash(SHA512(buf, BLOCKLEN, md));
	//	print_hash(md);
	}
	fclose(f);
	return 0;
}
