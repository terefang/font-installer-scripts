package fonttool.provider;

import com.github.terefang.jmelange.commons.http.HttpClientResponse;
import com.github.terefang.jmelange.commons.http.HttpOkClient;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.commons.util.PdataUtil;
import fonttool.FontMain;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class GithubProvider extends GenericUrlProvider
{
    public static GithubProvider getInstance()
    {
        if(INSTANCE==null)
        {
            INSTANCE = new GithubProvider();
        }
        return INSTANCE;
    }

    public GithubProvider()
    {
        super("GITHUB");
    }

    static GithubProvider INSTANCE;

    public static String REPOLIST = "data/github.list"; // ClasspathResourceLoader.of(
    public static String REPOURI = "https://github.com/%s/archive/refs/heads/master.zip?/%s.zip";

    @SneakyThrows
    @Override
    public void getResourceList(ResourceCallback _cb)
    {
        new Thread(()->{
            try
            {
                try (InputStream _in = ClasspathResourceLoader.of(REPOLIST, null).getInputStream();)
                {
                    List<String> _urls = new Vector();
                    List<String> _data = IOUtil.readLines(_in, StandardCharsets.UTF_8);
                    FontMain.INSTANCE.logProgress(0, "syncing List");
                    int _i = 0;
                    for(String _line : _data)
                    {
                        _line = _line.trim();
                        if(_line.startsWith("#")) continue;

                        String[] _parts = _line.split("\\s+", 3);
                        if(_parts.length>=1)
                        {
                            _cb.resourceCallback(String.format(REPOURI,_parts[0],_parts[0].replaceAll("[^A-Za-z0-9]+", "_")));
                            FontMain.INSTANCE.logProgress((_i%100), "syncing List "+_i);
                            _i++;
                        }
                    }
                    FontMain.INSTANCE.logProgress(100, "synced "+_i);
                }
                catch (Exception _xe)
                {
                    FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
                }
            }
            catch(Exception _xe)
            {
            }
        }).start();
    }
}
