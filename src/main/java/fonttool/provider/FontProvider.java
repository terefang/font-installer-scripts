package fonttool.provider;

import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;

import java.io.File;
import java.util.List;
import java.util.Vector;

public interface FontProvider {
    public static enum Providers
    {
        ADF{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("ADF",ClasspathResourceLoader.of("data/adf.urls", null));
            }
        },
        ADOBE{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("ADOBE",ClasspathResourceLoader.of("data/adobe.urls", null));
            }
        },
        CDNFONTS{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("CDNFONTS",ClasspathResourceLoader.of("data/cdnfonts.urls", null));
            }
        },
        DAFONT{
            @Override
            public FontProvider getProvider() {
                return DaFontProvider.getInstance(false);
            }
        },
        DAFONT_NONFREE{
            @Override
            public FontProvider getProvider() {
                return DaFontProvider.getInstance(true);
            }
        },
        FONTDOWNLOAD{
            @Override
            public FontProvider getProvider() {
                return FontDownloadProvider.getInstance(false);
            }
        },
        FONTGET{
            @Override
            public FontProvider getProvider() {
                return FontGetProvider.getInstance(false);
            }
        },
        FONTGET_NONFREE{
            @Override
            public FontProvider getProvider() {
                return FontGetProvider.getInstance(true);
            }
        },
        FONTESK{
            @Override
            public FontProvider getProvider() {
                return FonteskProvider.getInstance(false);
            }
        },
        FONTESK_NONFREE{
            @Override
            public FontProvider getProvider() {
                return FonteskProvider.getInstance(true);
            }
        },
        FONTREPO{
            @Override
            public FontProvider getProvider() {
                return FontRepoProvider.getInstance(false);
            }
        },
        FONTREPO_NONFREE{
            @Override
            public FontProvider getProvider() {
                return FontRepoProvider.getInstance(true);
            }
        },
        FONNTs_NONFREE{
            @Override
            public FontProvider getProvider() {
                return FonntProvider.getInstance();
            }
        },
        GITHUB{
            @Override
            public FontProvider getProvider() {
                return new GithubProvider();
            }
        },
        GOOGLE{
            @Override
            public FontProvider getProvider() {
                return GoogleProvider.getInstance();
            }
        },
        LIBRE{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("LIBRE",ClasspathResourceLoader.of("data/libre.urls", null));
            }
        },
        NERDFONTS{
            @Override
            public FontProvider getProvider() {
                return NerdProvider.getInstance();
            }
        },
        OPENSANS{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("OPENSANS",ClasspathResourceLoader.of("data/opensans.urls", null));
            }
        },
        REDHAT{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("REDHAT",ClasspathResourceLoader.of("data/redhat.urls", null));
            }
        },
        TEX{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("TEX",ClasspathResourceLoader.of("data/tex.urls", null));
            }
        },
        URW{
            @Override
            public FontProvider getProvider() {
                return GenericUrlProvider.from("URW",ClasspathResourceLoader.of("data/urw.urls", null));
            }
        },
        ;
        public FontProvider getProvider() { return (FontProvider)null; }
    }

    default List<String> getResourceList()
    {
        final List<String> _ret = new Vector<>();
        this.getResourceList((_res)-> { _ret.add(_res); });
        return _ret;
    }
    public void getResourceList(ResourceCallback _cb);

    public void installResource(String _res, File _target, boolean _sf);
    public void installResources(List<String> _res, File _target, boolean _sf);

    public void interruptSync();

    public static interface ResourceCallback {
        public void resourceCallback(String _res);
    }
}
