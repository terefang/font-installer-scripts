execute_install()
{
  XURI=
  XREPO=
  # --url <http:....>
  # --repo <org>/<repo>
  make_filter_and_dest "$@"

  if [ ! -z "$XREPO" ]; then
      XURI="https://github.com/$XREPO/archive/refs/heads/master.zip"
  fi

  if [ -z "$XREPO" ]; then
      XREPO=$(echo "$XURI" | cut -d/ -f4-5)
  fi

  VENDOR="github/$XREPO"
  LIST="$XTMP/github.urls"
  XGET="wget -O "

  echo "0  $XURI" >> "$LIST"
  execute_install_from_archive "$VENDOR" "$DEST" "$LIST"
}