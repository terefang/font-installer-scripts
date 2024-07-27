package fonttool.provider;

import com.github.terefang.jmelange.commons.http.HttpOkClient;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.util.FileUtil;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.commons.util.OsUtil;
import fonttool.FontMain;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GenericUrlProvider implements FontProvider
{
    Thread _syncThread = null;

    List<String> urls;
    String vendor;
    public GenericUrlProvider(String _vendor, List<String> _urls)
    {
        this.vendor = _vendor;
        this.urls = _urls;
    }

    public GenericUrlProvider(String _vendor)
    {
        this.vendor = _vendor;
        this.urls = null;
    }

    public static GenericUrlProvider from(String _vendor, List<String> _urls)
    {
        return new GenericUrlProvider(_vendor, _urls);
    }

    public static GenericUrlProvider from(String _vendor, ResourceLoader _loader)
    {
        try (InputStream _in = _loader.getInputStream();)
        {
            List<String> _urls = new Vector();
            List<String> _data = IOUtil.readLines(_in, StandardCharsets.UTF_8);
            for(String _line : _data)
            {
                _line = _line.trim();
                if(_line.startsWith("#")) continue;

                String[] _parts = _line.split("\\s+", 3);
                if(_parts.length>=2)
                {
                    _urls.add(_parts[1]);
                }
            }
            return from(_vendor, _urls);
        }
        catch (Exception _xe)
        {
            //IGNORE
        }
        return null;
    }


    @Override
    public List<String> getResourceList() {
        return Collections.unmodifiableList(this.urls);
    }

    @Override
    public void getResourceList(ResourceCallback _cb) {
        for(String _res : GenericUrlProvider.this.getResourceList())
        {
            _cb.resourceCallback(_res);
        }
    }

    @SneakyThrows
    @Override
    public void installResource(String _res, File _target, boolean _sf) {
        File _tmp = new File(OsUtil.getUnixyUserConfigDirectory(UUID.randomUUID().toString()));
        _tmp.mkdirs();
        FontMain.INSTANCE.logToStamped("installing "+_res);
        File _tmpfile = new File(_tmp, _res.substring(_res.lastIndexOf('/')+1));
        try
        {
            HttpOkClient.fetchToFile(_res, _tmpfile);
            if(_tmpfile.getName().toLowerCase().endsWith(".zip"))
            {
                String _sd = _tmpfile.getName();
                _sd = _sd.substring(0, _sd.length()-4);
                ZipFile _zf = new ZipFile(_tmpfile);
                Enumeration<? extends ZipEntry> _en = _zf.entries();
                while(_en.hasMoreElements())
                {
                    ZipEntry _ze = _en.nextElement();
                    if(_ze.getName().toLowerCase().endsWith(".ttf") || _ze.getName().toLowerCase().endsWith(".otf"))
                    {
                        String _name = _ze.getName();
                        if(_name.contains("/"))
                        {
                            _name = _name.substring(_name.lastIndexOf('/')+1);
                        }
                        _name = _name.replace(' ', '_');
                        File _dest = new File(_target, (_sf?_sd+"/":"")+_name);
                        if(_sf) _dest.getParentFile().mkdirs();
                        IOUtil.copyToFile(_zf.getInputStream(_ze),_dest);
                        FontMain.INSTANCE.logToStamped("to "+_dest.getAbsolutePath());
                    }
                }
                _zf.close();
            }
            else
            if(_tmpfile.getName().toLowerCase().endsWith(".tar.gz"))
            {
                String _sd = _tmpfile.getName();
                _sd = _sd.substring(0, _sd.length()-7);
                try (InputStream _fi = Files.newInputStream(_tmpfile.toPath());
                     InputStream _bi = new BufferedInputStream(_fi);
                     InputStream _gzi = new GzipCompressorInputStream(_bi);
                     TarArchiveInputStream _i = new TarArchiveInputStream(_gzi)) {

                    ArchiveEntry _entry = null;
                    while ((_entry = _i.getNextEntry())!=null)
                    {
                        if(_entry.getName().toLowerCase().endsWith(".ttf") || _entry.getName().toLowerCase().endsWith(".otf"))
                        {
                            String _name = _entry.getName();
                            if(_name.contains("/"))
                            {
                                _name = _name.substring(_name.lastIndexOf('/')+1);
                            }
                            _name = _name.replace(' ', '_');
                            File _dest = new File(_target, (_sf?_sd+"/":"")+_name);
                            if(_sf) _dest.getParentFile().mkdirs();
                            IOUtil.copyToFile(_i,_dest);
                            FontMain.INSTANCE.logToStamped("to "+_dest.getAbsolutePath());
                        }
                    }
                }
            }
            else
            if(_tmpfile.getName().toLowerCase().endsWith(".ttf") || _tmpfile.getName().toLowerCase().endsWith(".otf"))
            {
                String _sd = _tmpfile.getName();
                _sd = _sd.substring(0, _sd.length()-4);

                File _dest = new File(_target, (_sf?_sd+"/":"")+_tmpfile.getName());
                if(_sf) _dest.getParentFile().mkdirs();
                FileUtil.copyFile(_tmpfile, _dest);
                FontMain.INSTANCE.logToStamped("to "+_dest.getAbsolutePath());
            }
        }
        catch (Exception _xe)
        {
            FontMain.INSTANCE.logProgressError(_xe.getMessage(), _xe);
        }
        finally
        {
            FileUtil.deleteDirectory(_tmp);
        }
    }

    @Override
    public void installResources(List<String> _res, File _target, boolean _sf)
    {
        for(String _r : _res)
        {
            this.installResource(_r, _target, _sf);
        }
    }

    @Override
    public void interruptSync() {
        if(this._syncThread!=null)
        {
            try {this._syncThread.interrupt();} catch(Exception _xe) {}
        }
    }
}
