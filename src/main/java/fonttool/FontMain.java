package fonttool;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import com.github.terefang.jmelange.commons.util.CfgDataUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.swing.SwingHelper;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideComboBox;
import com.jidesoft.swing.JideTabbedPane;
import fonttool.provider.FontProvider;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;

import com.github.terefang.jmelange.commons.util.OsUtil;
import com.jidesoft.plaf.LookAndFeelFactory;
import lombok.SneakyThrows;
public class FontMain extends JXFrame
{
    public static FontMain INSTANCE;

    private JPanel panel;

    private JTextArea _textArea;

    private JScrollPane _scroll;
    private DefaultComboBoxModel provider;
    private JideComboBox _prov_box;
    private JideTabbedPane _tabbedPane;
    private DefaultListModel<String> _listmodel;
    private List<String> _listlist;
    private JTextField _filter;
    private JXList _list;
    private JTextField _target;
    private JCheckBox _confirm;
    private FontProvider _prov;
    private JCheckBox _subfolder;

    public FontMain() throws HeadlessException
    {
        super("FontInstaller "+Version._VERSION);
    }

    @SneakyThrows
    public void init()
    {
        this.setStartPosition(JXFrame.StartPosition.CenterInScreen);
        this.setMinimumSize(new Dimension(1024,768));

        createStatusBar();

        this.panel = new JXPanel();
        this.panel.setLayout(new JideBoxLayout(this.panel, JideBoxLayout.Y_AXIS, 6));
        this.add(this.panel);

        createProviderBar();
        createTabPane();
        createListPane();
        createTargetBar();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
    }

    private void createTabPane()
    {
        this._textArea = new JTextArea(40, 40);
        this._textArea.setEditable(false);

        DefaultCaret caret = (DefaultCaret) this._textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this._textArea.setForeground(Color.WHITE);
        this._textArea.setBackground(Color.BLACK);

        this._scroll = new JScrollPane(this._textArea);
        this._scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this._tabbedPane = new JideTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this._tabbedPane.setTabShape(JideTabbedPane.SHAPE_DEFAULT);
        this._tabbedPane.setTabTrailingComponent(new JXPanel());
        this._tabbedPane.add("OUTPUT", this._scroll);

        this.panel.add(this._tabbedPane);
    }

    Thread _thread;
    private void createListPane()
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));

        {
            JPanel _pane2 = new JPanel();
            _pane2.setLayout(new BoxLayout(_pane2, BoxLayout.X_AXIS));

            _pane2.add(this._filter = new JTextField(30));
            this._filter.setMinimumSize(new Dimension(200, 25));
            this._filter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            _pane2.add(new JButton(new AbstractAction("Filter") {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    SwingUtilities.invokeLater(()->{
                        FontMain.this._listmodel.clear();
                        String _flt = FontMain.this._filter.getText();
                        for(String _item : FontMain.this._listlist)
                        {
                            if(_flt.startsWith("!~") && !_item.matches(_flt.substring(2)))
                            {
                                FontMain.this._listmodel.addElement(_item);
                            }
                            else
                            if(_flt.startsWith("~") && _item.matches(_flt.substring(1)))
                            {
                                FontMain.this._listmodel.addElement(_item);
                            }
                            else
                            if(_flt.startsWith("!") && !_item.toLowerCase().contains(_flt.toLowerCase()))
                            {
                                FontMain.this._listmodel.addElement(_item);
                            }
                            else
                            if(_item.toLowerCase().contains(_flt.toLowerCase()))
                            {
                                FontMain.this._listmodel.addElement(_item);
                            }
                        }
                    });
                }
            }));
            _panel.add(_pane2);
        }
        {
            this._listmodel = new DefaultListModel<String>();
            this._list = new JXList(_listmodel);
            this._list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            this._listlist = ListMapUtil.toList("test1", "test2");
            _listmodel.addAll(this._listlist);

            JScrollPane _scroll = new JScrollPane(this._list);

            _panel.add(_scroll);
        }

        this._tabbedPane.add("LIST", _panel);
    }

    private void createProviderBar() {
        Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
        TitledBorder _title = BorderFactory.createTitledBorder(_etch, "Provider");

        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.X_AXIS));
        _panel.setBorder(_title);

        this.provider = new DefaultComboBoxModel();
        for(FontProvider.Providers _fp : FontProvider.Providers.values())
        {
            this.provider.addElement(_fp.name());
        }

        this._prov_box =  new JideComboBox(this.provider);
        this._prov_box.setMinimumSize(new Dimension(200, 25));
        this._prov_box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        _panel.add(this._prov_box);

        _panel.add(new JButton(new AbstractAction("List/Sync") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String _item = FontMain.this._prov_box.getSelectedItem().toString();
                FontMain.this._prov = FontProvider.Providers.valueOf(_item).getProvider();

                SwingUtilities.invokeLater(()-> {
                    FontMain.this._tabbedPane.setSelectedIndex(1);
                    FontMain.this._listmodel.clear();

                    FontMain.this._listlist = new Vector<>();
                });


                _prov.getResourceList((_res)->{
                    SwingUtilities.invokeLater(()-> {
                        FontMain.this._listlist.add(_res);
                        FontMain.this._listmodel.addElement(_res);
                    });
                });

            }
        }));

        {
            JPanel _pane2 = new JPanel();
            _pane2.setLayout(new BoxLayout(_pane2, BoxLayout.X_AXIS));
            _pane2.add(new JButton(new AbstractAction("Install") {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String _item = FontMain.this._prov_box.getSelectedItem().toString();
                    final FontProvider _prov = FontProvider.Providers.valueOf(_item).getProvider();
                    final File _tgt = new File(FontMain.INSTANCE._target.getText());

                    if(!_tgt.exists()) return;
                    if(!_tgt.isDirectory()) return;

                    SwingUtilities.invokeLater(()->{
                        FontMain.this._tabbedPane.setSelectedIndex(0);
                    });
                    FontMain.this.logToStamped("started.");
                    FontMain.this._thread = new Thread(() -> {
                        try
                        {
                            Object[] _vals = FontMain.this._list.getSelectedValues();
                            int _i = 0;

                            for(Object _sel : _vals)
                            {
                                if(FontMain.INSTANCE._confirm.isSelected())
                                {
                                    _prov.installResource(_sel.toString(), _tgt, FontMain.this._subfolder.isSelected());
                                }
                                else
                                {
                                    FontMain.INSTANCE.logPrintLn("install not confirmed. "+_sel.toString());
                                }
                                FontMain.INSTANCE.logProgress((100*_i/_vals.length),_sel.toString());
                                _i++;
                            }
                            FontMain.INSTANCE.logProgress(100,"FINIS.");
                        }
                        catch (Exception _xe)
                        {
                            return;
                        }
                        finally {
                            FontMain.this.logToStamped("stopped.");
                        }
                    });
                    FontMain.this._thread.start();
                }
            }));
            _pane2.add(new JButton(new AbstractAction("Stop") {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(FontMain.this._prov!=null)
                    {
                        FontMain.this._prov.interruptSync();
                    }

                    if(FontMain.this._thread!=null)
                    {
                        FontMain.this._thread.interrupt();
                    }

                    FontMain.this.logToStamped("interrupt.");
                }
            }));
            _panel.add(_pane2);
        }

        _panel.add(new JButton(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));
        this.panel.add(_panel);
    }

    private void createTargetBar() {
        Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
        TitledBorder _title = BorderFactory.createTitledBorder(_etch, "Target");

        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.X_AXIS));
        _panel.setBorder(_title);

        _panel.add(this._target = SwingHelper.createTextField(CfgDataUtil.getRecentFromConfig(OsUtil.getApplicationName(),OsUtil.getUserFontDirectory()).get(0) ));
        
        _panel.add(new JButton(new AbstractAction("...") {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _dir = CfgDataUtil.getRecentFromConfig(OsUtil.getApplicationName(),OsUtil.getUserFontDirectory()).get(0);
                SwingUtilities.invokeLater(()->{
                    SwingHelper.executeDirectoryChooser(FontMain.INSTANCE,
                    "Choose Font Installation Directory ...", "Select",
                    new File(_dir),
                    (_d)->{
                        CfgDataUtil.addRecentToConfig(_d.getAbsolutePath());
                        FontMain.INSTANCE._target.setText(_d.getAbsolutePath());
                    },()->{
                        FontMain.INSTANCE._target.setText(_dir);
                    });
                });
            }
        }));

        _panel.add(this._confirm = new JCheckBox("Confirm"));
        _panel.add(this._subfolder = new JCheckBox("Subfolder"));

        this.panel.add(_panel);
    }

    private JProgressBar progressBar;
    private JLabel progressLabel;


    private void createStatusBar()
    {
        this.setStatusBar(new JXStatusBar());
        this.getStatusBar().add(this.progressBar = new JProgressBar(0, 100));
        this.progressBar.setMinimumSize(new Dimension(200, 15));
        this.progressBar.setMaximumSize(new Dimension(200, 15));
        this.progressBar.setPreferredSize(new Dimension(200, 15));
        this.progressBar.setSize(new Dimension(200, 15));
        this.getStatusBar().add(this.progressLabel = new JLabel());
        this.progressLabel.setMinimumSize(new Dimension(600, 15));
        this.progressLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
    }

    public synchronized void logTo(String message)
    {
        SwingUtilities.invokeLater( () -> {
            this._textArea.append(message);
            this._textArea.append("\n");
            this._textArea.setCaretPosition(this._textArea.getDocument().getLength());
            this._textArea.repaint();
        });
    }

    public synchronized void logToStamped(String message)
    {
        SwingUtilities.invokeLater( () -> {
            this._textArea.append((_sdf.format(new Date()))+" - "+message);
            this._textArea.append("\n");
            this._textArea.setCaretPosition(this._textArea.getDocument().getLength());
            this._textArea.repaint();
        });
    }

    public synchronized void logPrint(String message)
    {
        SwingUtilities.invokeLater( () -> {
            this._textArea.append(message);
            this._textArea.setCaretPosition(this._textArea.getDocument().getLength());
            this._textArea.repaint();
        });
    }

    public synchronized void logPrintLn(String message)
    {
        this.logPrint(message+"\n");
    }

    public synchronized void logPrint(char _c)
    {
        SwingUtilities.invokeLater( () -> {
            this._textArea.append(Character.toString(_c));
            if(_c=='\n')
            {
                this._textArea.setCaretPosition(this._textArea.getDocument().getLength());
                this._textArea.repaint();
            }
        });
    }

    DateFormat _sdf = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    public synchronized void logProgress(int _pct, String _message)
    {
        SwingUtilities.invokeLater( () -> {
            this.progressBar.setMaximum(100);
            this.progressBar.setValue(_pct);
            this.progressLabel.setOpaque(true);
            this.progressLabel.setForeground(Color.WHITE);
            this.progressLabel.setBackground(Color.BLACK);
            this.progressLabel.setText((_sdf.format(new Date()))+" - "+_message);
        });
    }

    public synchronized void logProgress(String _message)
    {
        SwingUtilities.invokeLater( () -> {
            this.progressBar.setValue(this.progressBar.getValue()+1);
            this.progressLabel.setOpaque(true);
            this.progressLabel.setForeground(Color.WHITE);
            this.progressLabel.setBackground(Color.BLACK);
            this.progressLabel.setText((_sdf.format(new Date()))+" - "+_message);
        });
    }

    public synchronized void logProgressError(String _message)
    {
        SwingUtilities.invokeLater( () -> {
            this.progressBar.setMaximum(100);
            this.progressBar.setValue(0);
            this.progressLabel.setOpaque(true);
            this.progressLabel.setForeground(Color.WHITE);
            this.progressLabel.setBackground(Color.RED);
            this.progressLabel.setText((_sdf.format(new Date()))+" - "+_message);
        });
    }

    public synchronized void logProgressError(String _message, Throwable _th)
    {
        logProgressError(_message);
        StringWriter _sw = new StringWriter();
        PrintWriter _pw = new PrintWriter(_sw);
        _th.printStackTrace(_pw);
        this.logTo(_sw.getBuffer().toString());
    }

    public synchronized void logProgress(int _n, int _max, String _message)
    {
        SwingUtilities.invokeLater( () -> {
            this.progressBar.setMaximum(_max);
            this.progressBar.setValue(_n);
            this.progressLabel.setOpaque(true);
            this.progressLabel.setForeground(Color.WHITE);
            this.progressLabel.setBackground(Color.BLACK);
            this.progressLabel.setText((_sdf.format(new Date()))+" - "+_message);
        });
    }

    public synchronized void logClear()
    {
        SwingUtilities.invokeLater( () -> {
            this._textArea.setText("");
            this._textArea.repaint();
        });
    }
    @SneakyThrows
    public static void main(String[] args)
    {
        OsUtil.setApplicationName("font-installer");
        if (OsUtil.isMac) {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            System.setProperty("swing.defaultlaf", UIManager.getCrossPlatformLookAndFeelClassName());
            LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        } else if (OsUtil.isWindows) {
            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.EXTENSION_STYLE_VSNET);
        }

        INSTANCE = new FontMain();
        INSTANCE.init();
    }
}
