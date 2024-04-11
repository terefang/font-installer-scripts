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

  if [ "$DOLIST" -gt 0 ]; then
    XREST='curl -L -H "Accept: application/vnd.github+json" -H "X-GitHub-Api-Version: 2022-11-28"'
      if [ -z "$FILTER" ]; then
        $XREST https://api.github.com/repos/$XREPO/releases |jq -c -r '.[].assets[].browser_download_url'
      else
        $XREST https://api.github.com/repos/$XREPO/releases |jq -c -r '.[].assets[].browser_download_url' |grep -E -i "$FILTER"
      fi
      exit 0
  fi

  VENDOR="github/$XREPO"
  LIST="$XTMP/github.urls"
  XGET="wget -O "
  echo "0  $XURI" >> "$LIST"
  execute_install_from_archive "$VENDOR" "$DEST" "$LIST"
}