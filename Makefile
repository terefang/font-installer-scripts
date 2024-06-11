#!/usr/bin/make

DESTDIR=
prefix=/opt/font-installer

DATAFILES=  \
   ./data/adf.urls  \
   ./data/adobe.urls  \
   ./data/cdnfonts.urls  \
   ./data/github.list  \
   ./data/mswin.list  \
   ./data/opensans.urls  \
   ./data/redhat.urls  \
   ./data/tex.urls  \
   ./data/urw.urls

SCRIPTFILES=  \
   ./scripts/fontesk.sh  \
   ./scripts/functions  \
   ./scripts/github.sh  \
   ./scripts/google.sh  \
   ./scripts/mswin.sh  \
   ./scripts/nerd.sh  \
   ./scripts/ttc_to_ttf.ff

EXECFILES= \
   ./install-fonts.sh

DOCFILES= \
   ./LICENSE \
   ./README.md

.ONESHELL:

check :
	@for x in uname curl xq jq aria2c tar zip gzip bzip xz cabextract shasum wget find; do \
  		(which $$x >/dev/null && echo $$x" ... OK.") || (echo $$x" ... not found." );\
  	done

install : install-data install-scripts install-exec install-doc
	@echo "installed."

install-scripts : $SCRIPTFILES
	install -d $DESTDIR$prefix/scripts
	install -t $DESTDIR$prefix/scripts $SCRIPTFILES

install-exec : $EXECFILES
	install -d $DESTDIR$prefix
	install -t $DESTDIR$prefix $EXECFILES

install-doc : $DOCFILES
	install -d $DESTDIR$prefix/docs
	install -t $DESTDIR$prefix/docs $DOCFILES

install-data : $DATAFILES
	install -d $DESTDIR$prefix/data
	install -t $DESTDIR$prefix/data $DATAFILES

