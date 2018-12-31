#BINS = flash.com cartmenu.rom
#BINS = flash.com diag.com ramtest.com cartmenu.rom cartmenu-dbg.rom
BINS = flash.com diag.com ramtest.com cartmenu.rom

EXE = rom2atr8 rom2atr8.exe romtool romtool.exe

OPENSSL_MINGW = /opt/mingw/openssl-1.0.1e

.PHONY: all clean dep

all: thecart.atr cart.rom $(EXE)

ATASM ?= atasm

ASMINC = -Ihisio -Ilibflash16

ASMFLAGS ?=
#ASMFLAGS ?= -v -s

MINGW_CXX=i686-w64-mingw32-g++
MINGW_STRIP=i686-w64-mingw32-strip

HISIOSRC = hisio.src \
	hisio/hisiocode-break.src hisio/hisiocode-check.src hisio/hisiocode-cleanup.src \
	hisio/hisiocode-diag.src hisio/hisiocode-main.src hisio/hisiocode-receive.src \
	hisio/hisiocode-send.src hisio/hisiocode.src hisio/hisiocode-vbi.src \
	hisio/hisiodet.src hisio/hisio.inc

LIBFLASHSRC = libflash16/libflash.inc libflash16/libflash.src \
	libflash16/libflash-thecart.src libflash16/libflash-ramloc.src

FLASHSRC = flash.src flash.inc flashdef.inc flashtext.src flashio.src \
	flashdata.src flashutil.src flashincr.src flashraw.src \
	cio.inc cio.src iohelp.src iohelpfl.src numhelp.src \
	$(LIBFLASHSRC) $(HISIOSRC)

DIAGSRC = diag.src flash.inc flashdef.inc diagtext.src \
	flashdata.src flashutil.src \
	cio.inc cio.src iohelp.src iohelpfl.src numhelp.src \
	$(LIBFLASHSRC)

RAMTESTSRC = ramtest.src flash.inc flashdef.inc ramtext.src flashio.src \
	flashdata.src flashutil.src flashraw.src pokeyrnd.src \
	cio.inc cio.src iohelp.src iohelpfl.src numhelp.src \
	$(LIBFLASHSRC) $(HISIOSRC)

PRODSRC = prodtool.src flash.inc flashdef.inc cartswitch.src cartswitch-d406.src prodtext.src \
	flashdata.src flashutil.src \
	cio.inc cio.src iohelp.src \
	pokeyrnd.src spi.src \
	iohelpfl.src numhelp.src $(LIBFLASHSRC)

SPITESTSRC = spitest.src spi.src flash.inc cio.inc cio.src iohelp.src

SPITEST2SRC = spitest2.src spitext2.src spi.src flash.inc flashdef.inc \
	cio.inc cio.src iohelp.src iohelpfl.src numhelp.src flashdata.src \
	$(LIBFLASHSRC) 

CARTMENUSRC = cartmenu.src cartstart.src \
	spi.src numhelp.src \
	cio.inc flash.inc version.inc \
	mypdos-code-t.bin mydos-bootsec.bin mydos-dossys.bin flash.bin

flash.com: $(FLASHSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -o$@ $<

diag.com: $(DIAGSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -o$@ $<

ramtest.com: $(RAMTESTSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -o$@ $<

flash.bin: $(FLASHSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -dFLASH_BIN -r -o$@ $<

spitest.com: $(SPITESTSRC)
	$(ATASM) $(ASMFLAGS) -dTHECART -o$@ $<

cartmenu.rom: $(CARTMENUSRC)
	$(ATASM) $(ASMFLAGS) -r -o$@ $<

cartmenu-dbg.rom: $(CARTMENUSRC) hwdebug.inc hwdebsup.src
	$(ATASM) $(ASMFLAGS) -r -dHWDEBUG -o$@ $<


cart.rom: cartmenu.rom
	dd if=$< of=$@ bs=8k count=1

rom2atr8: rom2atr8.cpp
	g++ -W -Wall -o $@ $<

rom2atr8_32: rom2atr8.cpp
	g++ -m32 -W -Wall -o $@ $<

rom2atr8.exe: rom2atr8.cpp
	$(MINGW_CXX) -W -Wall -o $@ -static $<
	$(MINGW_STRIP) $@

thecart.atr: $(BINS)
	mkdir -p disk
	cp -f $(BINS) disk
	dir2atr -b MyDos4534 -d 720 $@ disk

prodtool.atr: $(PRODSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -r -o$@ $<

prodtool-d406.atr: $(PRODSRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -dCARTSWITCH_D406 -r -o$@ $<

spitest2.atr: $(SPITEST2SRC)
	$(ATASM) $(ASMINC) $(ASMFLAGS) -dTHECART -r -o$@ $<

shatest: shatest.cpp
	g++ -W -Wall -o $@ $< -lssl

shatest.exe: shatest.cpp Makefile
	#i586-mingw32msvc-g++ -W -Wall -I $(OPENSSL_MINGW)/include -o $@ $< -L $(OPENSSL_MINGW)/lib -static -leay32
	i586-mingw32msvc-g++ -W -Wall -I $(OPENSSL_MINGW)/include -o $@ -static $< -L $(OPENSSL_MINGW)/lib -lcrypto
	i586-mingw32msvc-strip $@

CXXFLAGS = -g -W -Wall -D_LARGEFILE64_SOURCE -DMYIDE_DEBUG

TOOLOBJ = romtool.o Error.o CmdlineOpts.o FileOpts.o MyOpts.o MiscUtils.o
LDFLAGS = -g

romtool: $(TOOLOBJ)
	$(CXX) $(LDFLAGS) -o $@ $^ -lcrypto

romtool_32: $(TOOLOBJ:%.o=%.cpp)
	$(CXX) $(LDFLAGS) -m32 -o $@ $^ -lcrypto

romtool.exe: $(TOOLOBJ:%.o=%.cpp)
	$(MINGW_CXX) -W -Wall -I $(OPENSSL_MINGW)/include -o $@ -static $^ -L $(OPENSSL_MINGW)/lib -lcrypto
	$(MINGW_STRIP) $@
clean:
	rm -f *.com thecart.atr spitest2.atr *.rom $(EXE)

dep:
	rm -f .depend
	for i in *.cpp ; do \
		$(CXX) -MM $$i >> .depend ; \
	done

-include .depend

