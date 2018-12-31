all: FreezerManualDe.dvi FreezerManualEn.dvi

.PHONY: clean pdf de en print print-de print-en

DVIPS_OPTS=-t a4 -Ppdf

pdf:	FreezerManualDe.pdf FreezerManualEn.pdf

html:	FreezerManualDe.html

de: FreezerManualDe.dvi

en: FreezerManualEn.dvi

print: print-de print-en

print-de: FreezerManualDe-print.pdf FreezerManualDe-print-book.ps FreezerManualDe-print-book.pdf
print-en: FreezerManualEn-print.pdf FreezerManualEn-print-book.ps FreezerManualEn-print-book.pdf

FreezerManualDe-print-book.ps: FreezerManualDe.ps
	psbook $< | psnup -n 2 > $@

FreezerManualDe-print-book.pdf: FreezerManualDe-print-book.ps
	./my-ps2pdf-landscape.sh $< $@

FreezerManualDe-print.pdf: FreezerManualDe.ps
	./my-ps2pdf.sh $< $@

FreezerManualEn-print-book.ps: FreezerManualEn.ps
	psbook $< | psnup -n 2 > $@

FreezerManualEn-print-book.pdf: FreezerManualEn-print-book.ps
	./my-ps2pdf-landscape.sh $< $@

FreezerManualEn-print.pdf: FreezerManualEn.ps
	./my-ps2pdf.sh $< $@

FreezerManualDe.dvi: FreezerManualDe.tex de/*.tex common-formats.tex
	latex FreezerManualDe.tex
	latex FreezerManualDe.tex
	latex FreezerManualDe.tex

FreezerManualDe.ps: FreezerManualDe.dvi
	dvips $(DVIPS_OPTS) $<

FreezerManualDe.pdf: FreezerManualDe.tex de/*.tex common-formats.tex
	pdflatex -shell-escape FreezerManualDe.tex
	pdflatex -shell-escape FreezerManualDe.tex
	pdflatex -shell-escape FreezerManualDe.tex

FreezerManualDe.html: FreezerManualDe.tex de/*.tex common-formats.tex
	tth -w2 -e2 $<

FreezerManualEn.dvi: FreezerManualEn.tex en/*.tex common-formats.tex
	latex FreezerManualEn.tex
	latex FreezerManualEn.tex
	latex FreezerManualEn.tex

FreezerManualEn.ps: FreezerManualEn.dvi
	dvips $(DVIPS_OPTS) $<

FreezerManualEn.pdf: FreezerManualEn.tex en/*.tex common-formats.tex
	pdflatex -shell-escape FreezerManualEn.tex
	pdflatex -shell-escape FreezerManualEn.tex
	pdflatex -shell-escape FreezerManualEn.tex

#FreezerManualDe.pdf: FreezerManualDe.ps
#	my-ps2pdf.sh $(PS2PDF_OPTS) $< $@

clean:
	rm -f *.aux de/*.aux en/*.aux *.toc *.dvi *.log *.out *.pdf *.ps
