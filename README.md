# Font Installer Scripts

## Abstract

This package allows for easy installation of Fonts from the Web including:

* ADF Fonts
* Google Fonts
* Libertinus Fonts
* MS Core Fonts For Web
* Nerd Fonts
* Fira Fonts
* Plex Fonts
* Open Sans (with Noto updates)
* URW Base 35 Fonts

You will need an Internet connection during execution.

## Note

A precompiled Java-based Gui installer is available from the releases page requireing Java 11+.  

## Prerequisites

the following utilities are used:

* bash (obviously)
* curl
* wget
* aria2c
* jq
* xq
* tar
* unzip
* g(un)zip
* b(un)zip2
* cabextract

## SYNOPSIS

```
install-fonts [provider] [ ... options ... ]
```

### generic options

#### List available providers

```
install-fonts -list
```

#### List available fonts

```
install-fonts provider -list
```

#### install to system directories

```
install-fonts provider -system
```

#### install to local directories

```
install-fonts provider -local
```

#### install to user directories

```
install-fonts provider -user
```

#### install to specific directories

```
install-fonts provider -prefix /path/to/prefix/
```

#### create zip archive in  specific directories

```
install-fonts provider -prefix /path/to/prefix/ -zip
```

### adf, nerd, tex, urw, google, opensans, ...

```
install-fonts provider -list -filter [regex]
```

```
install-fonts provider -filter [regex]
```

### google

```
install-fonts google -family [name]
```

### github

```
install-fonts github -list
```

```
install-fonts github -list -repo ORG/REPO
```

```
install-fonts github -repo ORG/REPO
```

```
install-fonts github -url https://github.com/ORG/REPO/path/to/release/archive.zip
```

### fontesk

the fontesk provider is a little bit special due to the limitations of the api.

* it is not possible to list on font on the site
* always search for

```
install-fonts fontesk -list -query [match]
```

* and/or refine with a filter

```
install-fonts fontesk -list -query [match] -filter [regex]
```

* the known limitation is 20 entries max.

## TODO

* standalone Noto?, Roboto?, Droid?
* https://fontswan.com/wp-content/uploads/2023/05/azonix.zip
* maybe change cp to install

https://software.sil.org/downloads/r/andika/Andika-6.101.zip|Andika|Sans Serif|SIL|Readable Unicode Font|SIL OFL 1.1
https://software.sil.org/downloads/r/doulos/DoulosSIL-6.101.zip|Doulos|Serif|SIL|Unicode font with wide latin and cyrillic support|SIL OFL 1.1
https://software.sil.org/downloads/r/gentium/GentiumPlus-6.101.zip|Gentium|Serif|SIL|Unicode font targetting lating, cyrillic and greek|SIL OFL 1.1
https://software.sil.org/downloads/r/charis/CharisSIL-6.101.zip|Charis|Serif|SIL|Readable Unicode Font|SIL OFL 1.1
https://font.gohu.org/gohufont-2.1-otb.tar.gz|Gohu|Monospace|gohu.org|Font for Go code|WTFPL
https://download.jetbrains.com/fonts/JetBrainsMono-2.242.zip|Jetbrains Mono|Monospace|jetbrains|Font for developers|SIL OFL 1.1
https://github.com/cormullion/juliamono/releases/download/v0.044/JuliaMono-0.044.tar.gz|Julia Mono|Monospace||Font with technical and math symbols|SIL OFL 1.1
https://www.typographies.fr/N/luciole/Luciole-Regular.ttf|Luciole|Serif|typographies.fr|Font for visually impaired people|CC Attrib 4.0
https://github.com/antijingoist/opendyslexic/archive/v0.9.10/opendyslexic-0.9.10.tar.gz|Open Dyslexic|Serif||Font for readers with dyslexia|SIL OFL 1.1
https://downloads.sourceforge.net/sourceforge/terminus-font/terminus-font-4.47.tar.gz|Terminus|Monospace||Another Hacker Font|SIL OFL 1.1
https://github.com/ansilove/BlockZone/releases/download/1.004/BlockZone-1.004.zip|BlockZone|Monospace||Recreation of the original IBM VGA font|SIL OFL 1.1
https://download.gnome.org/sources/cantarell-fonts/0.303/cantarell-fonts-0.303.1.tar.xz|Cantarell|Serif|GNOME|GNOME humanist/sans-serif font|SIL OFL 1.1
https://github.com/polarsys/b612/archive/1.008/b612-1.008.tar.gz|B612|Sans Serif|Airbus/Intactile Design|Higly legible font for aircraft cockpits|SIL OFL 1.1
https://01.org/sites/default/files/downloads/clear-sans/clearsans-1.00.zip|Clear Sans|Sans-Serif|Intel|Legible font|Apache v2
https://github.com/Omnibus-Type/Chivo/archive/chivo-1.007-d2256458.tar.gz|Chivo|Sans Serif|Omnibus Type|Grotesque Sans-Serif|SIL OFL 1.1
https://github.com/skosch/Crimson/archive/fonts-october2014/Crimson-fonts-october2014.tar.gz|Crimson Fonts|Book||Antique Book style fonts|SIL OFL 1.1
https://github.com/madmalik/mononoki/releases/download/1.3/mononoki-1.3.zip|Mononoki|Monospace||Another Programmer's font|SIL OFL 1.1
https://www.thibault.org/fonts/isabella/Isabella-1.202-ttf.tar.gz|Isabella|Blackletter|John Stracke|Antique font from John Stracke. Based on the Isabella Breviary font|SIL OFL 1.1
https://www.thibault.org/fonts/rockets/Rockets-ttf.tar.gz|Rockets|Comic|John Stracke|Spacy font from John Stracke|LGPL,SIL OFL 1.1
https://www.thibault.org/fonts/staypuft/StayPuft.otf|StayPuft|Comic|John Stracke|Rounded comic font from John Stracke|LGPL,SIL OFL 1.1
https://www.thibault.org/fonts/engadget/engadget-1.001-1-ttf.tar.gz|Engadget|Serif|John Stracke|Engadget font from John Stracke|LGPL,SIL OFL 1.1
https://www.thibault.org/fonts/chromatic-etruscan/ChromaticEtruscan.otf|Chromatic Etruscan|Comic|John Stracke|Rounded comic font from John Stracke.|LGPL,SIL OFL 1.1
https://www.thibault.org/fonts/essays/essays1743-2.100-1-ttf.tar.gz|Essays 1743|Serif|John Stracke|Antique font from John Stracke. Based on that used in Montaigne's Essays|LGPL,SIL OFL 1.1
https://squaregear.net/fonts/free3of9.zip|Code39 Barcode|Barcode|SquareGear|Code 39 barcode font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/whitrabt.zip|White Rabbit|Sans Serif|SquareGear|Basic, clear, sans 'body' font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/fudd.zip|Fudd|Sans Serif|SquareGear|Soviet-style sans font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/fraktmod.zip|Fraktur Modern|Blackletter|SquareGear|Germanic blackletter font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/secrcode.zip|Secret Code|Light|SquareGear|Thin sans font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/stadium.zip|Stadium|Sans Serif|SquareGear|Art Deco font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/trtl.zip|TRTL|Display|SquareGear|Very slabby/blocky font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/propagnd.zip|Propaganda|Display|SquareGear|Soviet-style sans font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/led_real.zip|LED read|Display|SquareGear|LED/Speak-and-spell homage from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/elecpikl.zip|Electric Pickle|Display|SquareGear|Font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/never.zip|Never|Display|SquareGear|Thin creepy font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/college.zip|College|Display|SquareGear|American college football style font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/libby.zip|Libby|Sans Serif|SquareGear|Deco/20s style font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/tiny.zip|Tiny|Display|SquareGear|4-pixels high font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/tinier.zip|Tinier|Display|SquareGear|3-pixels high font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/geek.zip|Ancient Geek|Display|SquareGear|Faux Athenian font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/golong.zip|Go Long|Sans Serif|SquareGear|Tall font from Matthew Welch/SquareGear|MIT
https://squaregear.net/fonts/hitroad.zip|Hit The Road|Sans Serif|SquareGear|Highway/interstate style font from Matthew Welch/SquareGear|MIT
http://pixelspread.com/allerta/allerta.zip|Allerta|Display|Matt McInerney|Signage Font|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/Rrune_04-03-2018.zip|Rrune|Script|Wolfgang Wiebecke||SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/FraWiWi-28-02-2018.zip|FraWiWi|Music|Wolfgang Wiebecke|Script with musical and astrological symbols|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/treiboult-1.004.zip|Treiboult|Script|Wolfgang Wiebecke|Script for the german language|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/ArangaA_me-008.zip|ArangA|Display|Wolfgang Wiebecke|Derived from Allerta|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/ww_cubes_02_06_2021.zip|ww_cubes|Display|Wolfgang Wiebecke|Blocky font|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/ww_fawe_15-07-2010.zip|ww_fawe|Display|Wolfgang Wiebecke|Blocky font|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/PaLoWi_10_08_2020.zip|Palowi|Script|Wolfgang Wiebecke|Script font|SIL OFL 1.1
https://www.wolfgang-wiebecke-kultur.de/PaLoWi_10_08_2020.zip|Palowi|Script|Wolfgang Wiebecke|Script font|SIL OFL 1.1
https://github.com/carlstype/techna-sans/releases/download/1.003/techna-sans-1.003.zip|Techna Sans|Sans Serif|Carl Enland||SIL OFL 1.1
https://github.com/carlstype/helmet/releases/download/1.000/helmet-neue-1.000.zip|Helmet Neue|Sans Serif|Carl Enland|Grotesque font|SIL OFL 1.1
https://developer.tizen.org/sites/default/files/documentation/breeze_sans.zip|Breeze Sans|Sans Serif|Samsung||Apache
https://github.com/be5invis/Iosevka/releases/download/v16.0.2/ttf-iosevka-16.0.2.zip|Iosevka|Sans Serif|Belleve Invis||SIL OFL 1.1
https://github.com/be5invis/Iosevka/releases/download/v16.0.2/ttf-iosevka-fixed-16.0.2.zip|Iosevka Fixed|Monospace|Belleve Invis||SIL OFL 1.1
https://github.com/be5invis/Iosevka/releases/download/v16.0.2/ttf-iosevka-slab-16.0.2.zip|Iosevka Slab|Slab-Serif|Belleve Invis||SIL OFL 1.1
https://github.com/be5invis/Iosevka/releases/download/v16.0.2/ttf-iosevka-term-16.0.2.zip|Iosevka Terminal|Monospace|Belleve Invis||SIL OFL 1.1
https://github.com/aliftype/xits/releases/download/v1.302/XITS-1.302.zip|XITS|Math|Aliftype|Font with math symbols|SIL OFL 1.1
https://github.com/aliftype/reem-kufi/archive/refs/heads/main.zip|Reem Kufi|Script|Aliftype|Kufic Script|SIL OFL 1.1
https://github.com/aliftype/amiri/archive/refs/heads/main.zip|Amiri|Script|Aliftype|Arabic Naskh body text script|SIL OFL 1.1
https://github.com/aliftype/mada/archive/refs/heads/main.zip|Mada|Sans Serif|Aliftype|Arabic sans-style font|SIL OFL 1.1
https://github.com/aliftype/aref-ruqaa/archive/refs/heads/main.zip|Aref Ruqaa|Sans Serif|Aliftype|Arabic ruqaa style font|SIL OFL 1.1
https://github.com/aliftype/noname-fixed/archive/refs/heads/main.zip|Arabic Noname Fixed|Monospace|Aliftype|Arabic fixed width font|SIL OFL 1.1
https://github.com/aliftype/raqq/archive/refs/heads/main.zip|Raqq|Script|Aliftype|Arabic Kufic Script|SIL OFL 1.1
https://github.com/aliftype/qahiri/archive/refs/heads/main.zip|Qahiri|Script|Aliftype|Arabic Kufic Script|SIL OFL 1.1
https://github.com/coz-m/MPLUS_FONTS/archive/refs/heads/master.zip|M+|Sans Serif|Coji Morishita||SIL OFL 1.1
https://github.com/mozilla/zilla-slab/releases/download/v1.002/Zilla-Slab-Fonts-v1.002.zip|Zilla Slab|Slab Serif|Mozilla||SIL OFL 1.1
https://github.com/EkType/Anek/releases/download/1.000/Ek-Type-Anek-Variable-1.002.zip|Anek|Serif|EkType|Font supporting Bangla, Devanagari, Kannada, Latin, Gujarati, Gurmukhi, Malayalam, Odia, Tamil and Telugua|SIL OFL 1.1
https://github.com/EkType/Mukta/releases/download/2.538/Mukta.Font.Family.2.538.zip|Mukta|Serif|EkType|Font supporting Devanagari, Gujarati, Gurumukhi, Tamil and Latin| SIL OFL 1.1
https://github.com/EkType/Modak/releases/download/1.155/Modak.1.155.zip|Modak|Serif|EkType|Plump and curvy font supporting Devanagari and Latin| SIL OFL 1.1
https://github.com/EkType/Jaini/releases/download/1.001/Jaini.1.001.zip|Jaini|Serif|EkType|Devanagari script based on style of style of the Jain Kalpasūtra manuscripts|SIL OFL 1.1
https://github.com/EkType/Gotu/releases/download/2.32/Gotu.2.320.zip|Gotu|Sans Serif|EkType|Clean font for Devanagari and Latin|SIL OFL 1.1
https://github.com/NNBnh/bmono/releases/download/v1.2-11.2.2/bmono-ttf.zip|BMono|Monospace|NNBnh|Customized version of Iosevka|SIL OFL 1.1
https://github.com/erikdkennedy/figtree/archive/refs/tags/v1.0.1.zip|Figtree|Sans Serif|Erik Kennedy||SIL OFL 1.1
https://github.com/ronotypo/Minipax/archive/refs/heads/master.zip|Minipax|Serif|ronotypo|Serif font inspired by Orwell's 1984|SIL OFL 1.1
https://github.com/ctrlcctrlv/QuaeriteRegnumDei/raw/master/QuaeriteRegnumDei.otf|Quaerite Regnum Dei|Blackletter|ctrlcctrlv|Style of a medeival Spanish rotunda|SIL OFL 1.1
https://github.com/ctrlcctrlv/kjv1611/raw/master/KJV1611.otf|KJV1611|Blackletter|ctrlcctrlv|Restoration of the typeface found in the 1611 King James Bible|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020Base-Regular.ttf|TT2020|Serif|ctrlcctrlv|Typewriter style font|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020Base-Italic.ttf|TT2020|Italic|ctrlcctrlv|Typewriter style font|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleB-Regular.ttf|TT2020 Style B|Serif|ctrlcctrlv|Typewriter style font, heavy weight (too much ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleB-Italic.ttf|TT2020 Style B|Italic|ctrlcctrlv|Typewriter style font, heavy weight (too much ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleD-Regular.ttf|TT2020 Style D|Serif|ctrlcctrlv|Typewriter style font, light weight (too little ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleD-Italic.ttf|TT2020 Style D|Italic|ctrlcctrlv|Typewriter style font, light weight (too little ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleE-Regular.ttf|TT2020 Style E|Serif|ctrlcctrlv|Typewriter style font, heavy weight (strong ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleE-Italic.ttf|TT2020 Style E|Italic|ctrlcctrlv|Typewriter style font, heavy weight (strong ink) version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleF-Regular.ttf|TT2020 Style F|Serif|ctrlcctrlv|Typewriter style font, almost no ink version|SIL OFL 1.1
https://copypaste.wtf/TT2020/dist/TT2020StyleG-Regular.ttf|TT2020 Style G|Serif|ctrlcctrlv|Typewriter style font, almost no ink version|SIL OFL 1.1
https://github.com/ctrlcctrlv/FRBAmericanCursive/archive/refs/heads/main.zip|American Cursive|Script|ctrlcctrlv|Handwiring style font|SIL OFL 1.1
https://github.com/ctrlcctrlv/chomsky/raw/master/dist/Chomsky.otf|Chomsky|Blackletter|ctrlcctrlv|Font in the style of the New York Times masthead|SIL OFL 1.1
https://github.com/ctrlcctrlv/some-time-later/raw/master/Some%20Time%20Later.otf|Some Time Later|Comic|ctrlcctrlv|Font in the style of Spongebob squarepants|SIL OFL 1.1
https://github.com/fonsecapeter/brass_mono/releases/download/v0.0.5/BrassMono.zip|Brass Mono|Monospace|Peter Fonseca|70s engineering style font|SIL OFL 1.1
https://github.com/psb1558/Eadui-Font/archive/refs/heads/master.zip|Eadui|Blackletter|Peter Baker|Script font based on the work of the eleventh-century scribe Eadui Basan|SIL OFL 1.1
https://github.com/psb1558/Cerne-font/releases/download/v1.000/Cerne.zip|Cerne|Blackletter|Peter Baker|A font based on the script of the ninth-century Book of Cerne|SIL OFL 1.1
https://github.com/psb1558/BeowulfOT-font/archive/refs/heads/master.zip|Beowulf OT|Blackletter|Peter Baker|Font inspired by the beowulf manuscript|SIL OFL 1.1
https://github.com/psb1558/Cissanthemos-font/archive/refs/heads/master.zip|Cissanthemos|Blackletter|Peter Baker|Medieval style font|SIL OFL 1.1
https://github.com/psb1558/ParkerChronicle-font/archive/refs/heads/master.zip|Parker Chronicle|Blackletter|Peter Baker|A script font based on the first hand of Corpus Christi College, Cambridge, MS|SIL OFL 1.1
https://github.com/psb1558/Elstob-font/releases/download/v1.015/Elstob_v1_015a.zip|Elstob|Blackletter|Peter Baker|Based on 17th and 18th century Oxford University Press fonts|SIL OFL 1.1
https://github.com/MuseScoreFonts/Leland/archive/refs/heads/main.zip|Leland|Music|Muse Score Fonts|Music Notation Font|SIL OFL 1.1
https://github.com/steinbergmedia/bravura/archive/refs/heads/master.zip|Bravura|Music|Steinberg Media|Music Notation Font|SIL OFL 1.1
https://github.com/steinbergmedia/petaluma/archive/refs/heads/master.zip|Petaluma|Music|Steinberg Media|Music Notation Font|SIL OFL 1.1
https://github.com/froyotam/ferrite-core/archive/refs/heads/master.zip|Ferrite Core|Display|Froyo Tam|Dramatic futuristic font |SIL OFL 1.1
https://github.com/froyotam/Fantasma/archive/refs/heads/main.zip|Fantasma|Sans Serif|Froyo Tam|Sinister futuruistic font|CC0 1.0
https://github.com/graphicore/librebarcode/archive/refs/heads/master.zip|Libre Barcode|Barcode|Graphicore|Barcode fonts for Code38 Code128 and EAN|SIL OFL 1.1
https://optician-sans.com/font-files/v2/OpticianSans.zip|Optician Sans|Sans Serif|ANTI Hamar and Faábio Duarte Martin|Optician style font|SIL OFL 1.1
https://github.com/aminabedi68/Fandogh/archive/refs/heads/master.zip|Fandogh|Sans Serif|Amin Abedi|Persian Font|SIL OFL 1.1
https://github.com/aminabedi68/Mikhak/archive/refs/heads/master.zip|Mikhak|Sans Serif|Amin Abedi|Arabic Font|SIL OFL 1.1
https://github.com/aminabedi68/AzarMehrMonospaced/archive/refs/heads/master.zip|Azar Mehr Mono|Monospace|Amin Abedi|Arabic Font|SIL OFL 1.1
https://github.com/aminabedi68/Estedad/archive/refs/heads/master.zip|Estedad|Serif|Amin Abedi|Arabic Font|SIL OFL 1.1
https://github.com/intel/intel-one-mono/releases/download/V1.2.1/ttf.zip|Intel Mono One|Monospace|Intel|Programmers Font|SIL OFL 1.1
https://github.com/gidole/Gidole-Typefaces/raw/master/gidole.zip|Gidole|Sans Serif|Andreas Larsen|DIN style sans font|SIL OFL 1.1
https://github.com/nerdypepper/scientifica/releases/download/v2.3/scientifica.tar|Scientifica|Monospace|Nerdy Pepper|Mini Monospaced Font|SIL OFL 1.1