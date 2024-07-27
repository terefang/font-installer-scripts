package fonttool.provider;

import com.github.terefang.jmelange.commons.http.HttpOkClient;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

public class DaFontProvider extends GenericUrlProvider
{
    public DaFontProvider(String _vendor) {
        super(_vendor);
    }

    boolean _nonfree = false;

    public static DaFontProvider getInstance(boolean _nf)
    {
        if(INSTANCE==null)
        {
            INSTANCE = new DaFontProvider("DAFONT");
            INSTANCE._nonfree = _nf;
        }
        return INSTANCE;
    }

    static DaFontProvider INSTANCE;

    public static String DABASE="https://www.dafont.com";
    public static String DAQUERY="/alpha.php?fpp=200&l[]=10&l[]=1&";
    public static String DAQUERY_NONFREE="/alpha.php?fpp=200&";
    public static List<String> _LETTER_LIST = ListMapUtil.toList("%23", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{
            int _i = 0;
            FontMain.INSTANCE.logProgress(0, "syncing List from "+DABASE);
            for(String _l : _LETTER_LIST)
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
                            _url = String.format("%s%spage=%d&lettre=%s",DABASE,(this._nonfree ? DAQUERY_NONFREE : DAQUERY),_page,_l);
                            FontMain.INSTANCE.logPrintLn(_url);
                            _html = HttpOkClient.fetchToString(_url);
                            if(_html!=null)
                            {
                                Document _doc = Jsoup.parse(_html);
                                Elements _sel = _doc.select("div.dlbox > a.dl");
                                int _sz = _sel.size();
                                for(Element _el : _sel)
                                {
                                    String _zip = _el.attr("href");
                                    _zip = _zip+"&/"+_zip.substring(_zip.lastIndexOf('=')+1)+".zip";
                                    _cb.resourceCallback("https:"+_zip);
                                    FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i+" | "+_l);
                                    _i++;
                                }

                                Element _el = _doc.selectFirst("a[title='Keyboard shortcut: Right arrow']");
                                if(_el!=null)
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