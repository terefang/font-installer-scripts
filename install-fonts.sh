#!/usr/bin/env bash

XDIR=$(cd $(dirname $0) && pwd)
SEPR="# ------------------------------"
XSYS=$(uname)
XTMP=$(mktemp -d /tmp/fontinstall-XXXXXXXXXXXXXXXXXXXX)
XCURL="curl --connect-timeout 2 --max-time 10 -o "
XARIA="aria2c --connect-timeout 2 --timeout 10 -o "
XGET="$XARIA"
XTTC="fontforge -lang=ff -script $XDIR/scripts/ttc_to_ttf.ff"
cleanup()
{
  rm -rf "$XTMP"
}

trap cleanup INT TERM EXIT HUP

case "$XSYS" in
  Darwin*)
    # is readonly , may fail
    FSYSDIR="/System/Library/Fonts"
    FLCLDIR="/Library/Fonts"
    FUSRDIR="$HOME/Library/Fonts"
    ;;
  Linux*)
    FSYSDIR="/usr/share/fonts"
    FLCLDIR="/usr/local/share/fonts"
    FUSRDIR="$HOME/.local/share/fonts"
    ;;
  *)
    echo "Error unknown System Type /$XSYS/ ... exiting."
    exit 1
    ;;
esac

usage ()
{
  cat <<EOT
$SEPR
Usage:
$0 <command> [... options ...]

You may want to try:

install-fonts -h
install-fonts -help
install-fonts --help
install-fonts help
install-fonts -list
$SEPR

EOT
}

source $XDIR/scripts/functions

while [ $# -gt 0 ]; do
  case "$1" in
    -h|-help|--help|help)
      usage
      exit 0
      ;;
    -list)
      list_commands
      exit 0
      ;;
    *)
      execute_command "$@"
      exit 0
  esac
done

usage
exit 1