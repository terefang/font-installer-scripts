# curl 'https://www.nerdfonts.com/font-downloads' | xq -n -x '//*[@id="downloads"]/div[2]/div[1]/div/a'|cut -f2 -d'"'
NFONTLIST="https://www.nerdfonts.com/font-downloads"

execute_install()
{
  VENDOR="Nerd"
  LIST="$XTMP/nerd.urls"
  XGET="wget -O "
  # there is only one possible option: the destdir
  # --sys(tem) -sys(tem)
  # --local -local
  # --user -user
  # --prefix -prefix <path>
  # --filter -filter <rx>
  # --listonly
  make_filter_and_dest "$@"

  if [ -z "$FILTER" ]; then
    curl "$NFONTLIST" | xq -n -x '//*[@id="downloads"]/div[2]/div[1]/div/a'|cut -f2 -d'"' > "$LIST.tmp"
  else
    curl "$NFONTLIST" | xq -n -x '//*[@id="downloads"]/div[2]/div[1]/div/a'|cut -f2 -d'"'|grep -E -i "$FILTER" > "$LIST.tmp"
  fi

  if [ "$DOLIST" -gt 0 ]; then
    if [ -z "$FILTER" ]; then
      cat "$LIST.tmp"| cut -f9 -d/ |sed 's/.zip$//'
    else
      cat "$LIST.tmp"| cut -f9 -d/ |sed 's/.zip$//' |grep -E -i "$FILTER"
    fi
    exit 0
  fi
  for x in $(cat "$LIST.tmp"); do
    TVENDOR="$VENDOR/$(basename $x .zip)"
    echo "0  $x" > "$LIST"
    execute_install_from_archive "$TVENDOR" "$DEST" "$LIST"
  done
}