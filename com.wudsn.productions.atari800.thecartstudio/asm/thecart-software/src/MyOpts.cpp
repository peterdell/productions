/*
   MyOpts - simple command line option abstraction class

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

#include "MyOpts.h"
#include "CmdlineOpts.h"
#include "FileOpts.h"
#include "AtariDebug.h"
#include "Error.h"

MyOpts::OptParserList::OptParserList(RCPtr<AbstractOpts> opts, RCPtr<OptParserList> prev)
	: super(),
	  fOptParser(opts),
	  fPrev(prev)
{
	Assert(fOptParser);
}

MyOpts::OptParserList::~OptParserList()
{
}

MyOpts::MyOpts(int argc, char** argv, bool enableAtFiles)
	: super(),
	  fEnableAtFileParsing(enableAtFiles)
{
	fCurrentOptParser = new OptParserList(new CmdlineOpts(argc, argv));
}

MyOpts::~MyOpts()
{
}

const char* MyOpts::GetOpt()
{
	while (fCurrentOptParser) {
		RCPtr<AbstractOpts> optParser = fCurrentOptParser->GetOptParser();
		const char* opt = optParser->GetOpt();
		if (opt) {
			if (fEnableAtFileParsing && *opt=='@') {
				RCPtr<AbstractOpts> fileOpt;
				try {
					fileOpt = new FileOpts(opt+1);
				}
				catch (ErrorObject& err) {
					printf("%s\n", err.AsString());
					continue;
				}
				fCurrentOptParser = new OptParserList(fileOpt, fCurrentOptParser);
			} else {
				return opt;
			}
		} else {
			fCurrentOptParser = fCurrentOptParser->GetPrevOptParser();
		}
	}
	return 0;
}

