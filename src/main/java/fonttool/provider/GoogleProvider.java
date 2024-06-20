package fonttool.provider;

import com.github.terefang.jmelange.commons.http.HttpClientResponse;
import com.github.terefang.jmelange.commons.http.HttpOkClient;
import com.github.terefang.jmelange.commons.util.PdataUtil;
import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class GoogleProvider extends GenericUrlProvider
{
    public GoogleProvider(String _vendor) {
        super(_vendor);
    }

    public static GoogleProvider getInstance()
    {
        if(INSTANCE==null)
        {
            INSTANCE = new GoogleProvider("GOOGLE");
        }
        return INSTANCE;
    }

    static GoogleProvider INSTANCE;
    public static String GFONTURI="https://fonts.google.com/download/list?family=";
    public static String GFONTLIST="https://fonts.google.com/metadata/fonts";
    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        new Thread(()->{
            //# curl https://fonts.google.com/metadata/fonts| jq -r '.familyMetadataList[].family' |tr ' ' '+'
            try
            {
                HttpOkClient _cl = new HttpOkClient();
                int _i = 0;
                FontMain.INSTANCE.logProgress(0, "syncing List from "+GFONTLIST);
                HttpClientResponse _resp = _cl.getRequest(GFONTLIST, "application/json");

                Map<String, Object> _data = PdataUtil.loadFrom(new StringReader(_resp.getAsString()));
                for(Map<String, Object> _fam :(List<Map<String, Object>>)_data.get("familyMetadataList"))
                {
                    _cb.resourceCallback(_fam.get("family").toString());
                    FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i);
                    _i++;
                }
                FontMain.INSTANCE.logProgress(100, "synced "+_i);
            }
            catch(Exception _xe)
            {
                FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
            }
        }).start();
    }

    @SneakyThrows
    @Override
    public void installResource(String _res, File _target) {
        //    for y in $(curl "$GFONTURI$x" 2>/dev/null |tail +2|jq -r -c '.manifest.fileRefs[] | (.url+"?/"+.filename )'); do
        //      echo "0  $y" >> "$LIST"
        //    done
        HttpOkClient _cl = new HttpOkClient();
        HttpClientResponse _resp = _cl.getRequest(GFONTURI+_res, "application/json");
        String _text = _resp.getAsString();
        _text = _text.substring(_text.indexOf('\n'));
        List<Map<String,String>> _list = (List<Map<String,String>>)((Map<String,List<Map<String,String>>>)PdataUtil.loadFrom(new StringReader(_text)).get("manifest")).get("fileRefs");
        for(Map<String,String> _entry : _list)
        {
            super.installResource(_entry.get("url")+"?/"+_entry.get("filename"), _target);
        }
    }

}

//
//execute_install()
//{
//  VENDOR="google"
//  LIST="$XTMP/google.urls"
//  XGET="wget -O "
//  XFAMILY=
//  # there is only one possible option: the destdir
//  # --sys(tem) -sys(tem)
//  # --local -local
//  # --user -user
//  # --prefix -prefix <path>
//  # --filter -filter <rx>
//  make_filter_and_dest "$@"
//
//  if [ "$DOLIST" -gt 0 ]; then
//    if [ -z "$FILTER" ]; then
//      curl "$GFONTLIST" | jq -r '.familyMetadataList[].family'
//    else
//      curl "$GFONTLIST" | jq -r '.familyMetadataList[].family' |grep -E -i "$FILTER"
//    fi
//    exit 0
//  fi
//
//  if [ -z "$FILTER" ]; then
//    curl "$GFONTLIST" 2>/dev/null | jq -r '.familyMetadataList[].family' |tr ' ' '+' > "$LIST.tmp"
//  else
//    curl "$GFONTLIST" 2>/dev/null | jq -r '.familyMetadataList[].family' |grep -E -i "$FILTER"|tr ' ' '+' > "$LIST.tmp"
//  fi
//
//  if [ ! -z "$XFAMILY" ]; then
//    echo "$XFAMILY" |tr ' ' '+' > "$LIST.tmp"
//  fi
//
//  for x in $(cat "$LIST.tmp"); do
//    rm -f "$LIST"
//    if [ -f "$LIST" ]; then
//      execute_install_from_archive "$TVENDOR" "$DEST" "$LIST"
//    else
//      echo "family /$x/ not found ... exiting."
//      exit 1
//    fi
//  done
//}