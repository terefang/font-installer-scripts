# curl 'https://fontesk.com/license/free-for-commercial-use/?s=Cane'|xq -q 'a[itemprop="mainEntityOfPage"]' -n
# <a itemprop="mainEntityOfPage" href="https://fontesk.com/arcane-font-2/">Arcane Font</a>
# curl https://fontesk.com/dinofiles-font/|xq -q 'div.fw-col-inner script' -n|fgrep location.href |cut -f2 -d';' |cut -f1 -d'?'
# <script>
#function downloads(){
#window.location.href=&#39;https://fontesk.com/download/133794/?tmstv=1712644291&#39;
#}
#</script>

FELIST="https://fontesk.com/license/free-for-commercial-use/"

fontesk_query()
{
  XQ="$FELIST"

  XFILE="$XTMP/.fontesk"

  if [ ! -z "$XQUERY" ]; then
    XQ="$XQ?s=$(echo $XQUERY|tr ' ' '+')"
  fi

  if [ -z "$FILTER" ]; then
    curl 2>/dev/null "$XQ"|xq -q 'a[itemprop="mainEntityOfPage"]' -n |cut -f4- -d'"'
  else
    curl 2>/dev/null "$XQ"|xq -q 'a[itemprop="mainEntityOfPage"]' -n |cut -f4- -d'"'|grep -E -i "$FILTER"
  fi
}

execute_install()
{
  VENDOR="fontesk"
  LIST="$XTMP/fontesk.urls"
  XGET="wget -O "
  XQUERY=
  # there is only one possible option: the destdir
  # --sys(tem) -sys(tem)
  # --local -local
  # --user -user
  # --prefix -prefix <path>
  # --filter -filter <rx>
  make_filter_and_dest "$@"

  XQ="$FELIST"
  if [ ! -z "$XQUERY" ]; then
    XQ="$XQ?s=$XQUERY"
  fi
  if [ "$DOLIST" -gt 0 ]; then
    fontesk_query
    exit 0
  fi

  if [ -z "$FILTER" ]; then
      XLIST=$(fontesk_query |cut -f1 -d'"')
  else
      XLIST=$(fontesk_query |cut -f1 -d'"')
  fi

  for x in $XLIST; do
    XDL=$(curl "$x"|xq -q 'div.fw-col-inner script' -n|fgrep location.href |cut -f2 -d';' |cut -f1 -d'?')
    echo "0  $XDL/font.zip" > "$LIST"
    execute_install_from_archive "$VENDOR" "$DEST" "$LIST"
  done
}