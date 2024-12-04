package fonttool.provider;

import com.github.terefang.jmelange.commons.http.HttpOkClient;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class FontDownloadProvider extends GenericUrlProvider
{
    public FontDownloadProvider(String _vendor) {
        super(_vendor);
    }

    boolean _nonfree = false;

    public static FontDownloadProvider getInstance(boolean _nf)
    {
        if(INSTANCE==null)
        {
            INSTANCE = new FontDownloadProvider("FONTDL");
            INSTANCE._nonfree = _nf;
        }
        return INSTANCE;
    }

    static FontDownloadProvider INSTANCE;

    public static String BASE="https://font.download";
    public static String QUERY="/category/%s/%d";

    public static List<String> _TYPE_LIST = ListMapUtil.toList("monospaced","serif","sans-serif","handwritten","display");

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{
            int _i = 0;
            FontMain.INSTANCE.logProgress(0, "syncing List from "+BASE);
            for(String _l : _TYPE_LIST)
            {
                String _url = null;
                int _page = 1;
                try
                {
                    while(_page>0)
                    {
                        String _html = null;
                        try
                        {
                            _url = BASE+String.format(QUERY,_l,_page);
                            FontMain.INSTANCE.logPrintLn(_url);
                            _html = HttpOkClient.fetchToString(_url);
                            if(_html!=null)
                            {
                                Document _doc = Jsoup.parse(_html);
                                Elements _sel = _doc.select("div.font-list-item > div.row > div > div.buttons > a.btn");
                                int _sz = _sel.size();
                                for(Element _el : _sel)
                                {
                                    String _zip = _el.attr("href");
                                    _cb.resourceCallback(_zip);
                                    FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i+" | "+_l);
                                    _i++;
                                }

                                _sel = _doc.select("li.page-item.disabled > span.page-link");
                                boolean _disable = false;
                                for(Element _el : _sel)
                                {
                                    String _text = _el.text();
                                    if("»".equalsIgnoreCase(_text)) _disable = true;
                                }
                                if(!_disable)
                                {
                                    _page++;
                                }
                                else
                                {
                                    Thread.sleep(1000L);
                                    _page=-1;
                                }
                            }
                            else
                            {
                                break;
                            }
                        }
                        catch(InterruptedException _ie)
                        {
                            FontMain.INSTANCE.logProgressError(_ie.getMessage(), _ie);
                            FontMain.INSTANCE.logPrintLn(_html);
                            //return;
                            throw _ie;
                        }
                        catch(Exception _xe)
                        {
                            FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
                            FontMain.INSTANCE.logPrintLn(_html);
                            return;
                        }
                    }
                }
                catch(InterruptedException _ie)
                {
                    FontMain.INSTANCE.logProgressError(_ie.getMessage(), _ie);
                    return;
                }
                catch(Exception _xe)
                {
                    FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
                    return;
                }
            }
            FontMain.INSTANCE.logProgress(100, "synced "+_i);
        });
        this._syncThread.start();
    }

}