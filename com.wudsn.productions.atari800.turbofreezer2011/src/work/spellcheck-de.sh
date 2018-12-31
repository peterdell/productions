#!/bin/sh

if [ $# -ne 1 } ; then
	echo "usage: $0 file.tex"
	exit 1
fi

aspell -d de-alt -t -c "$1"
