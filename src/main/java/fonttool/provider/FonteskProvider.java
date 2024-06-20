package fonttool.provider;

import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;


public class FonteskProvider extends GenericUrlProvider
{
    public FonteskProvider(String _vendor) {
        super(_vendor);
    }

    boolean _nonfree = false;

    public static FonteskProvider getInstance(boolean _nf)
    {
        if(INSTANCE==null)
        {
            INSTANCE = new FonteskProvider("FONTESK");
            INSTANCE._nonfree = _nf;
        }
        return INSTANCE;
    }

    static FonteskProvider INSTANCE;
    public static String FELIST="https://fontesk.com/license/free-for-commercial-use/";
    public static String FELIST_NONFREE="https://fontesk.com/fonts/";

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        this._syncThread = new Thread(()->{
            // # curl 'https://fontesk.com/license/free-for-commercial-use/?s=Cane'|xq -q 'a[itemprop="mainEntityOfPage"]' -n
            //# <a itemprop="mainEntityOfPage" href="https://fontesk.com/arcane-font-2/">Arcane Font</a>
            //# curl https://fontesk.com/dinofiles-font/|xq -q 'div.fw-col-inner script' -n|fgrep location.href |cut -f2 -d';' |cut -f1 -d'?'
            //# <script>
            //#function downloads(){
            //#window.location.href=&#39;https://fontesk.com/download/133794/?tmstv=1712644291&#39;
            //#}
            //#</script>
            String _url = (this._nonfree ? FELIST_NONFREE : FELIST);
            try
            {
                int _i = 0;
                while(_url!=null)
                {
                    Document _doc = Jsoup.parse(new URL(_url), 10000);
                    _url = null;
                    FontMain.INSTANCE.logProgress(0, "syncing List from "+FELIST);
                    Elements _sel = _doc.select("a[itemprop=\"mainEntityOfPage\"]");
                    int _sz = _sel.size();
                    for(Element _el : _sel)
                    {
                        _cb.resourceCallback(_el.attr("href"));
                        FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i);
                        _i++;
                    }
                    Element _el = _doc.selectFirst("a.next");
                    if(_el!=null)
                    {
                        _url = _el.attr("href");
                        if(_url.startsWith("javascript:"))
                        {
                            _url = null;
                        }
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
    public void installResource(String _res, File _target) {
        //    XDL=$(curl "$x"|xq -q 'div.fw-col-inner script' -n|fgrep location.href |cut -f2 -d';' |cut -f1 -d'?')
        //    echo "0  $XDL/font.zip" > "$LIST"
        Document _doc = Jsoup.parse(new URL(_res), 10000);
        Element _el = _doc.selectFirst("div.fw-col-inner > script");
        String _text = _el.toString();
        _text = _text.substring(_text.indexOf('\'')+1, _text.lastIndexOf('\''))+"&/font.zip";
        super.installResource(_text, _target);
    }

}