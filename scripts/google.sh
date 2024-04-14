GFONTURI="https://fonts.google.com/download/list?family="
# curl https://fonts.google.com/metadata/fonts| jq -r '.familyMetadataList[].family' |tr ' ' '+'
GFONTLIST="https://fonts.google.com/metadata/fonts"

execute_install()
{
  VENDOR="google"
  LIST="$XTMP/google.urls"
  XGET="wget -O "
  XFAMILY=
  # there is only one possible option: the destdir
  # --sys(tem) -sys(tem)
  # --local -local
  # --user -user
  # --prefix -prefix <path>
  # --filter -filter <rx>
  make_filter_and_dest "$@"

  if [ "$DOLIST" -gt 0 ]; then
    if [ -z "$FILTER" ]; then
      curl "$GFONTLIST" | jq -r '.familyMetadataList[].family'
    else
      curl "$GFONTLIST" | jq -r '.familyMetadataList[].family' |grep -E -i "$FILTER"
    fi
    exit 0
  fi

  if [ -z "$FILTER" ]; then
    curl "$GFONTLIST" 2>/dev/null | jq -r '.familyMetadataList[].family' |tr ' ' '+' > "$LIST.tmp"
  else
    curl "$GFONTLIST" 2>/dev/null | jq -r '.familyMetadataList[].family' |grep -E -i "$FILTER"|tr ' ' '+' > "$LIST.tmp"
  fi

  if [ ! -z "$XFAMILY" ]; then
    echo "$XFAMILY" |tr ' ' '+' > "$LIST.tmp"
  fi

  for x in $(cat "$LIST.tmp"); do
    rm -f "$LIST"
    TVENDOR="$VENDOR/$(echo $x |tr -d '+')"
    for y in $(curl "$GFONTURI$x" 2>/dev/null |tail +2|jq -r -c '.manifest.fileRefs[] | (.url+"?/"+.filename )'); do
      echo "0  $y" >> "$LIST"
    done
    if [ -f "$LIST" ]; then
      execute_install_from_archive "$TVENDOR" "$DEST" "$LIST"
    else
      echo "family /$x/ not found ... exiting."
      exit 1
    fi
  done
}