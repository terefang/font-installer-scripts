execute_install()
{
    make_filter_and_dest "$@"

    VENDOR="mswin"
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
    mkdir -p "$DEST/$VENDOR"
    #mv -v $(find "$XYTMP" -type f) "$DEST/$VENDOR"
    find "$XYTMP" -name -type f -ls
    install "$XYTMP/calibri.ttf" "$DEST/$VENDOR/Calibri.ttf"
    install "$XYTMP/calibrib.ttf" "$DEST/$VENDOR/Calibri-Bold.ttf"
    install "$XYTMP/calibrii.ttf" "$DEST/$VENDOR/Calibri-Italic.ttf"
    install "$XYTMP/calibriz.ttf" "$DEST/$VENDOR/Calibri-Bold-Italic.ttf"
    #install "$XYTMP/cambria.ttf" "$DEST/$VENDOR/Cambria.ttf"
    install "$XYTMP/cambria.ttc" "$DEST/$VENDOR/Cambria.ttc"
    #install "$XYTMP/cambria-math.ttf" "$DEST/$VENDOR/Cambria-Math.ttf"
    install "$XYTMP/cambriab.ttf" "$DEST/$VENDOR/Cambria-Bold.ttf"
    install "$XYTMP/cambriai.ttf" "$DEST/$VENDOR/Cambria-Italic.ttf"
    install "$XYTMP/cambriaz.ttf" "$DEST/$VENDOR/Cambria-Bold-Italic.ttf"
    install "$XYTMP/candara.ttf" "$DEST/$VENDOR/Candara.ttf"
    install "$XYTMP/candarab.ttf" "$DEST/$VENDOR/Candara-Bold.ttf"
    install "$XYTMP/candarai.ttf" "$DEST/$VENDOR/Candara-Italic.ttf"
    install "$XYTMP/candaraz.ttf" "$DEST/$VENDOR/Candara-Bold-Italic.ttf"
    install "$XYTMP/consola.ttf" "$DEST/$VENDOR/Consolas.ttf"
    install "$XYTMP/consolab.ttf" "$DEST/$VENDOR/Consolas-Bold.ttf"
    install "$XYTMP/consolai.ttf" "$DEST/$VENDOR/Consolas-Italic.ttf"
    install "$XYTMP/consolaz.ttf" "$DEST/$VENDOR/Consolas-Bold-Italic.ttf"
    install "$XYTMP/constan.ttf" "$DEST/$VENDOR/Constantia.ttf"
    install "$XYTMP/constanb.ttf" "$DEST/$VENDOR/Constantia-Bold.ttf"
    install "$XYTMP/constani.ttf" "$DEST/$VENDOR/Constantia-Italic.ttf"
    install "$XYTMP/constanz.ttf" "$DEST/$VENDOR/Constantia-Bold-Italic.ttf"
    install "$XYTMP/corbel.ttf" "$DEST/$VENDOR/Corbel.ttf"
    install "$XYTMP/corbelb.ttf" "$DEST/$VENDOR/Corbel-Bold.ttf"
    install "$XYTMP/corbeli.ttf" "$DEST/$VENDOR/Corbel-Italic.ttf"
    install "$XYTMP/corbelz.ttf" "$DEST/$VENDOR/Corbel-Bold-Italic.ttf"

    install "$XYTMP/andalemo.ttf" "$DEST/$VENDOR/Andale-Mono.ttf"
    install "$XYTMP/ariblk.ttf" "$DEST/$VENDOR/Arial-Black.ttf"
    install "$XYTMP/comicbd.ttf" "$DEST/$VENDOR/Comic-Sans-MS-Bold.ttf"
    install "$XYTMP/comic.ttf" "$DEST/$VENDOR/Comic-Sans-MS.ttf"
    install "$XYTMP/courbd.ttf" "$DEST/$VENDOR/Courier-New-Bold.ttf"
    install "$XYTMP/courbi.ttf" "$DEST/$VENDOR/Courier-New-Bold-Italic.ttf"
    install "$XYTMP/couri.ttf" "$DEST/$VENDOR/Courier-New-Italic.ttf"
    install "$XYTMP/cour.ttf" "$DEST/$VENDOR/Courier-New.ttf"
    install "$XYTMP/georgiab.ttf" "$DEST/$VENDOR/Georgia-Bold.ttf"
    install "$XYTMP/georgiai.ttf" "$DEST/$VENDOR/Georgia-Italic.ttf"
    install "$XYTMP/georgia.ttf" "$DEST/$VENDOR/Georgia.ttf"
    install "$XYTMP/georgiaz.ttf" "$DEST/$VENDOR/Georgia-Bold-Italic.ttf"
    install "$XYTMP/impact.ttf" "$DEST/$VENDOR/Impact.ttf"
    install "$XYTMP/webdings.ttf" "$DEST/$VENDOR/Webdings.ttf"

    install "$XYTMP/arialbd.ttf" "$DEST/$VENDOR/Arial-Bold.ttf"
    install "$XYTMP/arialbi.ttf" "$DEST/$VENDOR/Arial-Bold-Italic.ttf"
    install "$XYTMP/ariali.ttf" "$DEST/$VENDOR/Arial-Italic.ttf"
    install "$XYTMP/arial.ttf" "$DEST/$VENDOR/Arial.ttf"
    install "$XYTMP/timesbd.ttf" "$DEST/$VENDOR/Times-New-Roman-Bold.ttf"
    install "$XYTMP/timesbi.ttf" "$DEST/$VENDOR/Times-New-Roman-Bold-Italic.ttf"
    install "$XYTMP/timesi.ttf" "$DEST/$VENDOR/Times-New-Roman-Italic.ttf"
    install "$XYTMP/times.ttf" "$DEST/$VENDOR/Times-New-Roman.ttf"
    install "$XYTMP/trebucbd.ttf" "$DEST/$VENDOR/Trebuchet-MS-Bold.ttf"
    install "$XYTMP/trebucbi.ttf" "$DEST/$VENDOR/Trebuchet-MS-Bold-Italic.ttf"
    install "$XYTMP/trebucit.ttf" "$DEST/$VENDOR/Trebuchet-MS-Italic.ttf"
    install "$XYTMP/trebuc.ttf" "$DEST/$VENDOR/Trebuchet-MS.ttf"
    install "$XYTMP/verdanab.ttf" "$DEST/$VENDOR/Verdana-Bold.ttf"
    install "$XYTMP/verdanai.ttf" "$DEST/$VENDOR/Verdana-Italic.ttf"
    install "$XYTMP/verdana.ttf" "$DEST/$VENDOR/Verdana.ttf"
    install "$XYTMP/verdanaz.ttf" "$DEST/$VENDOR/Verdana-Bold-Italic.ttf"

    install "$XYTMP/lucon.ttf" "$DEST/$VENDOR/Lucida-Console.ttf"
    install "$XYTMP/l_10646.ttf" "$DEST/$VENDOR/Lucida-Sans-Unicode.ttf"

    install "$XYTMP/tahoma.ttf" "$DEST/$VENDOR/Tahoma.ttf"
    install "$XYTMP/tahomabd.ttf" "$DEST/$VENDOR/Tahoma-Bold.ttf"

    install "$XYTMP/symbol.ttf" "$DEST/$VENDOR/Symbol.ttf"
    install "$XYTMP/wingding.ttf" "$DEST/$VENDOR/Wingdings.ttf"

    rm -rf "$XXTMP" "$XYTMP"
}