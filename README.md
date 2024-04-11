# Font Installer Scripts

## Abstract

This package allows for easy installation of Fonts from the Web including:

* ADF Fonts
* Google Fonts
* Libertinus Fonts
* MS Core Fonts For Web
* Nerd Fonts
* URW Base 35 Fonts

You will need an Internet connection during execution.

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

### List available providers

```
install-fonts -list
```

### List available fonts

```
install-fonts provider -list
```

### install to system directories

```
install-fonts provider -system
```

### install to local directories

```
install-fonts provider -local
```

### install to user directories

```
install-fonts provider -user
```

### install to specific directories

```
install-fonts provider -prefix /path/to/prefix/
```

## PROVIDERS

* adf -- slow and incomplete if tuxfamily.org is offline
* google -- working again with new google api (1600+ fonts!)
* mswin -- broken, todo
* nerd
* tex -- only has tex-gyre
* urw

## TODO

* standalone Fira?, Noto?, Roboto?, Droid?
* fix the mswin provider
* maybe change cp to install

