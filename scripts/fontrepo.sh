#curl 'https://www.fontrepo.com/fonts/public-domain-/-gpl-/-ofl/30' | xq -q '#nc > div.pagingCarrier > div > a:nth-child(3)' -a href

FRBASE="https://www.fontrepo.com"
FRSTART="/fonts/public-domain-/-gpl-/-ofl/1"

fontrepo_query()
{
  while [ ! -z "$XQ" ]; do
    if [ -z "$FILTER" ]; then
      curl 2>/dev/null "$FRBASE/$XQ"|xq -q '#nc > div.node > div.title > div > a:nth-child(2)' -a href
    else
      curl 2>/dev/null "$FRBASE/$XQ"|xq -q '#nc > div.node > div.title > div > a:nth-child(2)' -a href|grep -E -i "$FILTER"
    fi
    XQ=$(curl 2>/dev/null "$FRBASE/$XQ" | xq -q '#nc > div.pagingCarrier > div > a:nth-child(3)' -a href)
  done
}

execute_install()
{
  VENDOR="fontrepo"
  LIST="$XTMP/fontrepo.urls"
  XGET="wget -O "
  XQUERY=
  # there is only one possible option: the destdir
  # --sys(tem) -sys(tem)
  # --local -local
  # --user -user
  # --prefix -prefix <path>
  # --filter -filter <rx>
  make_filter_and_dest "$@"

  XQ="$FRSTART"
  if [ ! -z "$XQUERY" ]; then
    XQ="/fonts/$XQUERY/"
  fi
  if [ "$DOLIST" -gt 0 ]; then
    fontrepo_query
    exit 0
  fi

  XLIST=$(fontrepo_query)

  for x in $XLIST; do
    echo "0  $FRBASE/$x" > "$LIST"
    XFN=$(cut -f2- -d/ <<<"${x%.*}"|tr '/' '-')
    execute_install_from_archive "$VENDOR/$XFN" "$DEST" "$LIST"
  done
}