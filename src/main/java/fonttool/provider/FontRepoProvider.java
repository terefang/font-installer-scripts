package fonttool.provider;

import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

public class FontRepoProvider extends GenericUrlProvider
{
    public FontRepoProvider(String _vendor) {
        super(_vendor);
    }

    boolean _nonfree = false;

    public static FontRepoProvider getInstance(boolean _nf)
    {
        if(INSTANCE==null)
        {
            INSTANCE = new FontRepoProvider("FONTREPO");
            INSTANCE._nonfree = _nf;
        }
        return INSTANCE;
    }

    static FontRepoProvider INSTANCE;

    public static String FRBASE="https://www.fontrepo.com";
    public static String FRSTART="/fonts/public-domain-/-gpl-/-ofl/1";
    public static String FRSTARTNONFREE="/page/1";




    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{
            // curl 2>/dev/null "$FRBASE/$XQ"|xq -q '#nc > div.node > div.title > div > a:nth-child(2)' -a href
            //
            // XQ=$(curl 2>/dev/null "$FRBASE/$XQ" | xq -q '#nc > div.pagingCarrier > div > a:nth-child(3)' -a href)

            String _url = FRBASE+"/"+(this._nonfree ? FRSTARTNONFREE : FRSTART);
            try
            {
                int _i = 0;
                while(_url!=null)
                {
                    Document _doc = Jsoup.parse(new URL(_url), 10000);
                    _url = null;
                    FontMain.INSTANCE.logProgress(0, "syncing List from "+FRBASE);
                    Elements _sel = _doc.select("#nc > div.node > div.title > div > a:nth-child(2)");
                    int _sz = _sel.size();
                    for(Element _el : _sel)
                    {
                        _cb.resourceCallback(FRBASE+"/"+_el.attr("href"));
                        FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i);
                        _i++;
                    }
                    Element _el = _doc.selectFirst("#nc > div.pagingCarrier > div > a:nth-child(3)");
                    if(_el!=null)
                    {
                        _url = FRBASE+"/"+_el.attr("href");
                        FontMain.INSTANCE.logPrintLn(_url);
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


}