package fonttool.provider;

import fonttool.FontMain;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.List;
import java.util.Vector;

public class NerdProvider extends GenericUrlProvider
{
    public NerdProvider(String _vendor) {
        super(_vendor);
    }

    public static NerdProvider getInstance()
    {
        if(INSTANCE==null)
        {
            INSTANCE = new NerdProvider("NERDFONTS");
        }
        return INSTANCE;
    }

    static NerdProvider INSTANCE;
    public static String NFONTLIST="https://www.nerdfonts.com/font-downloads";
    @SneakyThrows
    @Override
    public List<String> getResourceList()
    {
        List<String> _ret = new Vector<>();
        // # curl 'https://www.nerdfonts.com/font-downloads' | xq -n -x '//*[@id="downloads"]/div[2]/div[1]/div/a'|cut -f2 -d'"'
        Document _doc = Jsoup.parse(new URL(NFONTLIST), 10000);
        FontMain.INSTANCE.logProgress(0, "syncing List from "+NFONTLIST);
        Elements _sel = _doc.select("#downloads > div > div > div > a");
        int _sz = _sel.size();
        int _i = 0;
        for(Element _el : _sel)
        {
            _ret.add(_el.attr("href"));
            FontMain.INSTANCE.logProgress((_i*100/_sz), "syncing List "+_i);
            _i++;
        }
        FontMain.INSTANCE.logProgress(100, "synced "+_i);
        return _ret;
    }
}
