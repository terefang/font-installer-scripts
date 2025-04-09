package fonttool.provider;

import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jfree.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;


public class FonntProvider
        extends GenericUrlProvider
{
    public FonntProvider(String _vendor) {
        super(_vendor);
    }

    public static FonntProvider getInstance()
    {
        if(INSTANCE==null)
        {
            INSTANCE = new FonntProvider("FONNT_NONFREE");
        }
        return INSTANCE;
    }

    static FonntProvider INSTANCE;
    public static String FNLIST="https://fonnts.com";

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{

            String _url = FNLIST+"/page/1/";
            try
            {
                int _i = 0;
                int _p = 1;
                while(_url!=null)
                {
                    FontMain.INSTANCE.logProgress((_i%100), "syncing page "+_p);
                    Document _doc = Jsoup.parse(new URL(_url), 10000);
                    _url = null;
                    FontMain.INSTANCE.logProgress(0, "syncing List from "+FNLIST);
                    Elements _sel = _doc.select("div.font-item");
                    int _sz = _sel.size();
                    for(Element _el : _sel)
                    {
                        String _id = _el.attr("post_id");
                        String _name = _id;
                        _el = _el.selectFirst("div > div > div > a");
                        if(_el!=null)
                        {
                            Node _t = _el.firstChild();
                            if(_t!=null)
                            {
                                _name = _t.toString().replace(' ', '-');
                            }
                        }
                        _cb.resourceCallback(FNLIST+"/downloads/fonnts.com-"+_id+".zip?/"+_name+".zip");
                        _i++;
                    }
                    _p++;
                    _url = null;
                    Element _el = _doc.selectFirst("div.next-link").nextElementSibling();
                    if(_el!=null)
                    {
                        _el = _el.selectFirst("a");
                        _url = _el.attr("href");
                    }
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
    public void installResource(String _res, File _target, boolean _sf) {
        super.installResource(_res, _target, _sf);
    }

}