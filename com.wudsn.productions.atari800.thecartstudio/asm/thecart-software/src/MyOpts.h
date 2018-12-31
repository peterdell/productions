#ifndef MYOPTS_H
#define MYOPTS_H
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

#include "RefCounted.h"
#include "RCPtr.h"
#include "AbstractOpts.h"

class MyOpts : public RefCounted {
public:
	MyOpts(int argc, char** argv, bool enableAtFiles = false);
	virtual ~MyOpts();
	const char* GetOpt();
private:
	typedef RefCounted super;

	class OptParserList : public RefCounted {
	public:
		OptParserList(RCPtr<AbstractOpts> opts, RCPtr<OptParserList> prev = RCPtr<OptParserList>());
		virtual ~OptParserList();
		inline RCPtr<AbstractOpts>& GetOptParser();
		inline RCPtr<OptParserList> GetPrevOptParser();
	private:
		typedef RefCounted super;
		RCPtr<AbstractOpts> fOptParser;
		RCPtr<OptParserList> fPrev;
	};

	RCPtr<OptParserList> fCurrentOptParser;
	bool fEnableAtFileParsing;
};

inline RCPtr<AbstractOpts>& MyOpts::OptParserList::GetOptParser()
{
	return fOptParser;
}

inline RCPtr<MyOpts::OptParserList> MyOpts::OptParserList::GetPrevOptParser()
{
	return fPrev;
}

#endif
