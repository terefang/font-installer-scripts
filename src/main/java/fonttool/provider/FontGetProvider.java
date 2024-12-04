package fonttool.provider;

import com.github.terefang.jmelange.commons.CommonUtil;
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


public class FontGetProvider
        extends GenericUrlProvider
{
    public FontGetProvider(String _vendor) {
        super(_vendor);
    }

    boolean _nonfree = false;

    public static FontGetProvider getInstance(boolean _nf)
    {
        if(INSTANCE==null)
        {
            INSTANCE = new FontGetProvider("FONTGET"+(_nf?"":"_NONFREE"));
        }
        INSTANCE._nonfree = _nf;
        return INSTANCE;
    }

    static FontGetProvider INSTANCE;
    public static String FGLIST="https://srv.fontget.com/api/index?page=%d&license=commercial";
    public static String FGLIST_NONFREE="https://srv.fontget.com/api/index?page=%d&license=all";
    // https://srv.fontget.com/api/search?q=letter&page=1&license=commercial&sort=name

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{
            HttpOkClient _cl = new HttpOkClient();
            String _url = this._nonfree ? FGLIST_NONFREE : FGLIST;
            try
            {
                FontMain.INSTANCE.logProgress(0, "syncing List from "+FGLIST);
                int _i = 1;
                int _p = 1;
                int _last= 1024;
                while(_last>=_p)
                {
                    HttpClientResponse _resp = _cl.getRequest(String.format(_url, _p), "application/json");
                    
                    Map<String, Object> _data = PdataUtil.loadFrom(new StringReader(_resp.getAsString()));
                    for(Map<String, Object> _fam :(List<Map<String, Object>>)_data.get("data"))
                    {
                        _cb.resourceCallback(_fam.get("download_url").toString()+"/"+_fam.get("slug").toString()+".zip");
                        _i++;
                        FontMain.INSTANCE.logProgress((_i)%100, "synced "+_i);
                    }
                    _last = CommonUtil.checkInt(_data.get("last_page"));
                    FontMain.INSTANCE.logProgress(100, "synced page "+_p+" / "+_last);
                    _p++;
                    Thread.sleep(200L);
                }
                FontMain.INSTANCE.logProgress(100, "synced "+_i);
            }
            catch(Exception _xe)
            {
                FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
            }
        });
        this._syncThread.start();
    }

    @SneakyThrows
    @Override
    public void installResource(String _res, File _target, boolean _sf)
    {
        super.installResource(_res, _target, _sf);
    }

}