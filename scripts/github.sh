execute_install()
{
  XURI=
  XREPO=
  VENDOR=
  # --url <http:....>
  # --repo <org>/<repo>
  make_filter_and_dest "$@"

  if [ "$DOLIST" -gt 0 ]; then
      XREST='curl -s -L -H "Accept: application/vnd.github+json" -H "X-GitHub-Api-Version: 2022-11-28"'
      if [ -z "$XREPO" ]; then
        XREPO=$(grep -v "^#" < "$XDIR/data/github.list")
      fi
      for xrepo in $XREPO; do
        echo "https://github.com/$xrepo/archive/refs/heads/master.zip"
        if [ -z "$FILTER" ]; then
          $XREST "https://api.github.com/repos/$xrepo/releases" |jq -c -r '.[].assets[].browser_download_url'
        else
          $XREST "https://api.github.com/repos/$xrepo/releases" |jq -c -r '.[].assets[].browser_download_url' |grep -E -i "$FILTER"
        fi
      done
      exit 0
  fi

  if [ -z "$XREPO" -a -z "$XURI" ]; then
    echo -e "\n$XSEP\nKnown Repositories include:\n"
    grep -v "^#" < "$XDIR/data/github.list"
    echo -e "\n$XSEP"
    exit 1
  fi

  if [ ! -z "$XREPO" ]; then
      XURI="https://github.com/$XREPO/archive/refs/heads/master.zip"
  fi

  if [ -z "$XREPO" ]; then
      XREPO=$(echo "$XURI" | cut -d/ -f4-5)
  fi

  if [ $VENDOR = '.' ]; then
    VENDOR=
  else
    VENDOR="github/$XREPO"
  fi
  LIST="$XTMP/github.urls"
  XGET="wget -O "
  echo "0  $XURI" >> "$LIST"
  execute_install_from_archive "$VENDOR" "$DEST" "$LIST"
}