execute_install()
{
    VENDOR="mswin"
    make_filter_and_dest "$@"
    XXTMP="$XTMP/mswin"
    XYTMP="$XTMP/fonts"
    mkdir -p "$XXTMP" "$XYTMP"
    # https://www.freedesktop.org/software/fontconfig/webfonts/webfonts.tar.gz
    wget "https://www.freedesktop.org/software/fontconfig/webfonts/webfonts.tar.gz" -O "$XXTMP/webfonts.tar.gz"
    cd "$XXTMP" && tar -xvf "$XXTMP/webfonts.tar.gz" -C "$XXTMP"
    for x in $(find "$XXTMP" -name '*.exe' -type f); do
        cd "$XXTMP" && cabextract -L -d "$XXTMP" "$x"
    done
    mv -v $(find "$XXTMP" -name '*.?tf' -type f) "$XYTMP"
    find "$XXTMP" -type f -delete
    # https://web.archive.org/web/20171225132744if_/https://download.microsoft.com/download/E/6/7/E675FFFC-2A6D-4AB0-B3EB-27C9F8C8F696/PowerPointViewer.exe
    wget "https://web.archive.org/web/20171225132744if_/https://download.microsoft.com/download/E/6/7/E675FFFC-2A6D-4AB0-B3EB-27C9F8C8F696/PowerPointViewer.exe" -O "$XXTMP/PowerPointViewer.cab"
    cd "$XXTMP" && cabextract -L -d "$XXTMP" "$XXTMP/PowerPointViewer.cab"
    rm -f "$XXTMP/PowerPointViewer.cab"
    for x in $(find "$XXTMP" -name '*.cab' -type f); do
        cd "$XXTMP" && cabextract -L -d "$XXTMP" "$x"
    done
    mv -v $(find "$XXTMP" -name '*.ttf' -type f) "$XYTMP"
    mv -v $(find "$XXTMP" -name '*.ttc' -type f) "$XYTMP"
    find "$XXTMP" -type f -delete
    # https://sourceforge.net/projects/mscorefonts2/files/cabs/EUupdate.EXE
    wget "https://sourceforge.net/projects/mscorefonts2/files/cabs/EUupdate.EXE" -O "$XXTMP/EUupdate.cab"
    cd "$XXTMP" && cabextract -L -d "$XXTMP" "$XXTMP/EUupdate.cab"
    rm -f "$XXTMP/EUupdate.cab"
    for x in $(find "$XXTMP" -name '*.cab' -type f); do
          cd "$XXTMP" && cabextract -L -d "$XXTMP" "$x"
    done
    mv -v $(find "$XXTMP" -name '*.ttf' -type f) "$XYTMP"
    find "$XXTMP" -type f -delete
    # http://ftpmirror.your.org/pub/misc/ftp.microsoft.com/bussys/winnt/winnt-public/fixes/usa/NT40TSE/hotfixes-postSP3/Euro-fix/eurofixi.exe
    $XCURL "$XXTMP/eurofixi.cab" "http://ftpmirror.your.org/pub/misc/ftp.microsoft.com/bussys/winnt/winnt-public/fixes/usa/NT40TSE/hotfixes-postSP3/Euro-fix/eurofixi.exe"
    cd "$XXTMP" && cabextract -L -d "$XXTMP" "$XXTMP/eurofixi.cab"
    rm -f "$XXTMP/eurofixi.cab"
    for x in $(find "$XXTMP" -name '*.cab' -type f); do
       cd "$XXTMP" && cabextract -L -d "$XXTMP" "$x"
    done
    mv -v $(find "$XXTMP" -name '*.ttf' -type f) "$XYTMP"
    find "$XXTMP" -type f -delete

    # https://downloads.sourceforge.net/corefonts/OldFiles/IELPKTH.CAB
    wget -O "$XXTMP/IELPKTH.cab" "https://downloads.sourceforge.net/corefonts/OldFiles/IELPKTH.CAB"

    cd "$XXTMP" && cabextract -L -d "$XXTMP" "$XXTMP/IELPKTH.cab"

    rm -f "$XXTMP/IELPKTH.cab"

    for x in $(find "$XXTMP" -name '*.cab' -type f); do
       cd "$XXTMP" && cabextract -L -d "$XXTMP" "$x"
    done

    mv -v $(find "$XXTMP" -name '*.ttf' -type f) "$XYTMP"

    if [ "$DOZIP" -gt 0 ]; then
      mkdir -p "$DEST"
    else
      mkdir -p "$DEST/$VENDOR"
    fi

    #mv -v $(find "$XYTMP" -type f) "$DEST/$VENDOR"
    #find "$XYTMP" -name -type f -ls
    $XTTC "$XYTMP/cambria.ttc" 0 "$XYTMP/cambria.ttf"
    $XTTC "$XYTMP/cambria.ttc" 1 "$XYTMP/cambria-math.ttf"

    grep -v "^#" < "$XDIR/data/mswin.list" | \
    while read xfrom xto xrest; do
      if [ "$DOZIP" -gt 0 ]; then
          xvendor=$(tr -d -c '[[:alnum:]]' <<< "$VENDOR")
          cd $XYTMP/ && (mv -v "$xfrom" "$xto" ; zip -9 "$DEST/$xvendor.zip" "$xto")
      else
          install "$XYTMP/$xfrom" "$DEST/$VENDOR/$xto"
      fi
    done
    rm -rf "$XXTMP" "$XYTMP"
}