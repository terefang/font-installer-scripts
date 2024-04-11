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

### adf, nerd, tex, urw, google

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

* standalone Fira?, Noto?, Roboto?, Droid?
* maybe change cp to install

