#!/bin/sh
if [ $# -lt 2 ] ; then
	echo "usage: $0 infile outfile [options...]"
	exit 1
fi
infile=$1
outfile=$2
shift 2

OPTIONS="-P- \
	-dSAFER \
	-dCompatibilityLevel=1.4 \
	-dPDFSETTINGS=/printer \
	-sProcessColorModel=DeviceGray \
	-dUseCIEColor=true"

gs \
	$OPTIONS \
	-q \
	-P- \
	-dNOPAUSE \
	-dBATCH \
	-sDEVICE=pdfwrite \
	-sstdout=%stderr \
	-sOutputFile="$outfile" \
	$OPTIONS \
	-c .setpdfwrite \
	-c "<</Orientation 3>> setpagedevice" \
	"$@" \
	-f "$infile"

	
