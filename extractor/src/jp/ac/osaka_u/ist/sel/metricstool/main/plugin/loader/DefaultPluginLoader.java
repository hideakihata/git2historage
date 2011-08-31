package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginResponseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * PluginLoaderインタフェースを実装したのデフォルトのプラグインローダ．
 * <p>
 * このクラスのインスタンスを作成した後，loadPlugin，またはloadPluginsメソッド群を用いて，
 * 任意のディレクトリ以下のプラグインをロードすることができる．
 * 単にデフォルトのpluginsディレクトリから全てのプラグインをロードする場合はloadPlugins()メソッドを使う．
 * <p>
 * また，各プラグインは個別のXMLファイルを用いて，利用するクラス群にクラスパスを指定することができるが，
 * このクラスのメソッド群を利用することで，デフォルトでクラスパスを通すファイルの設定をすることができる．
 * まず，addLibraryExtensionメソッド用いて，デフォルトでライブラリとみなすファイルの拡張子群を指定する．
 * これによって，各プラグインのディレクトリ直下にあるファイルで，設定した拡張子を持つファイル群にはXMLで指定しなくても
 * パスを通すことができる．次に，addLibraryDirectoryNameメソッドを用いて，
 * 各プラグインのルートディレクトリ以外のディレクトリをライブラリファイルの置き場とみなして，指定した拡張子を持つファイル群に
 * パスを通すことができる．
 * 例えば，
 * <pre>
 *    addLibraryExtensions("jar");
 *    addLibraryDirectoryName("lib");
 * </pre>
 * とすると，XMLで指定しなくても各プラグイン直下のjarファイルとlibディレクトリ以下のjarファイルにクラスパスを
 * 通すことができる． 
 * 
 * @author kou-tngt
 */
public class DefaultPluginLoader implements PluginLoader {

    /**
     * ライブラリファイルを置くデフォルトディレクトリ名を追加するメソッド．
     * @param libraryDir 追加するデフォルトライブラリディレクトリ名
     */
    public void addLibraryDirectoryName(final String libraryDir) {
        this.libraryDirectoryNames.add(libraryDir);
    }

    /**
     * ライブラリファイルの拡張子を追加するメソッド．
     * @param extension 追加するライブラリファイルの拡張子名．
     */
    public void addLibraryExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        this.libraryExtensions.add(extension);
    }

    /**
     * 登録されているデフォルトライブラリディレクトリ名のセットを返すメソッド
     * @return 登録されているデフォルトライブラリディレクトリ名セット
     */
    public Set<String> getLibraryDirectoryNames() {
        return Collections.unmodifiableSet(this.libraryDirectoryNames);
    }

    /**
     * 登録されているデフォルトライブラリファイルの拡張子を返すメソッド
     * @return 登録されているデフォルトライブラリファイルの拡張子
     */
    public Set<String> getLibraryExtensions() {
        return Collections.unmodifiableSet(this.libraryExtensions);
    }

    /**
     * 本ツールのクラスファイル群が置かれている場所の親ディレクトリ直下のpluginsディレクトリを返すメソッド
     * 見つからなければnullを返す．
     * @return　本ツールのクラスファイル群が置かれている場所の親ディレクトリ直下のpluginsディレクトリ．発見できなければnull．
     */
    public File getPluginsDirectory() {
        if (null != this.pluginsDirectory) {
            return this.pluginsDirectory;
        }

        try {
            return this.searchPluginsDirectory();
        } catch (final PluginLoadException e) {
            return null;
        }
    }

    /**
     * プラグイン構成設定を記述するXMLファイルのファイル名を取得するメソッド．
     * @return　プラグイン構成設定を記述するXMLファイルのファイル名
     */
    public String getPluginXmlFileName() {
        return this.pluginXmlFileName;
    }

    /**
     * デフォルトのpluginsディレクトリから、pluginDirNameで指定されたディレクトリ名を持つプラグインをロードする
     * @param pluginDirName プラグインディレクトリ名
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合に投げられる．
     * @throws IllegalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合に投げられる．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合に投げられる．
     * @throws PluginResponseException ロードしたプラグインからの応答がなかった場合.
     */
    public AbstractPlugin loadPlugin(final String pluginDirName) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException {
        return this.loadPlugin(this.searchPluginsDirectory(), pluginDirName);
    }

    /**
     * pluginsDirで指定されたディレクトリ以下から，pluginNameで指定されたディレクトリ名を持つプラグインをロードする
     * @param pluginsDir プラグインが配置されるディレクトリ
     * @param pluginDirName プラグインのルートディレクトリ
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合に投げられる．
     * @throws IllegalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合に投げられる．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合に投げられる．
     * @throws PluginResponseException ロードしたプラグインからの応答がなかった場合.
     * @throws NullPointerException pluginsDirがnullの場合
     * @throws IllegalArgumentException pluginsDirが存在しない場合，ディレクトリではない場合
     */
    public AbstractPlugin loadPlugin(final File pluginsDir, final String pluginDirName)
            throws PluginLoadException, IllegalPluginXmlFormatException,
            IllegalPluginDirectoryStructureException, PluginClassLoadException,
            PluginResponseException {
        if (null == pluginsDir || null == pluginDirName) {
            throw new NullPointerException();
        }

        if (!pluginsDir.exists()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not found.");
        }

        if (!pluginsDir.isDirectory()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not directory.");
        }

        return this.loadPlugin(new File(pluginsDir, pluginDirName));
    }

    /**
     * プラグイン自体のディレクトリを直接pluginRootDirで指定してロードするメソッド．
     * @param pluginRootDir プラグインのルートディレクトリ
     * @return ロードしたプラグインクラスのインスタンス
     * @throws PluginLoadException プラグインのロードに失敗した場合に投げられる．但し，下記の例外のいずれかにケースに該当した時はそちらが優先される．
     * @throws IllegalPluginXmlFormatException ロードするプラグインの設定情報を記述したXMLファイルの形式が正しくない場合に投げられる．
     * @throws IllegalPluginDirectoryStructureException ロードするプラグインのディレクトリ構成が正しくない場合に投げられる．
     * @throws PluginClassLoadException プラグインのクラスロードに失敗した場合に投げられる．
     * @throws PluginResponseException ロードしたプラグインからの応答がなかった場合.
     * @throws NullPointerException pluginRootDirがnullの場合
     * @throws IllegalArgumentException pluginRootDirが存在しない場合，ディレクトリではない場合
     */
    public AbstractPlugin loadPlugin(final File pluginRootDir) throws PluginLoadException,
            IllegalPluginXmlFormatException, IllegalPluginDirectoryStructureException,
            PluginClassLoadException, PluginResponseException {

        //アクセス権限をチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == pluginRootDir) {
            throw new NullPointerException();
        }
        if (!pluginRootDir.exists()) {
            throw new IllegalArgumentException(pluginRootDir.getAbsolutePath() + " is not found.");
        }
        if (!pluginRootDir.isDirectory()) {
            throw new IllegalArgumentException(pluginRootDir.getAbsolutePath()
                    + " is not directory.");
        }
        //引数チェック終了

        //デフォルトでライブラリと認識してロードするように指定されてるファイルを取得
        final File[] defaultLibraryFiles = this.detectSpecifiedLibraryFiles(pluginRootDir);

        //xmlを取得
        final File pluginXml = this.detectPluginXmlFile(pluginRootDir);
        if (null == pluginXml) {
            //xmlがなかった
            throw new IllegalPluginDirectoryStructureException(this.pluginXmlFileName
                    + " is not found in " + pluginRootDir.getName() + ".");
        }

        //xmlがあった

        String pluginClassName = null;
        String[] classpathStrings = null;
        try {
            //xmlを解析
            final PluginXmlInterpreter interpreter = new DefaultPluginXmlInterpreter(pluginXml);
            //プラグインクラス名とクラスパス群を取得
            pluginClassName = interpreter.getPluginClassName();
            classpathStrings = interpreter.getClassPathAttributeNames();
        } catch (final FileNotFoundException e) {//ありえない
            throw new IllegalPluginDirectoryStructureException(this.pluginXmlFileName
                    + " is not found in " + pluginRootDir.getName() + ".", e);
        } catch (final IOException e) {
            throw new PluginLoadException("Failed to read " + pluginXml.getAbsolutePath() + ".", e);
        } catch (final IllegalPluginXmlFormatException e) {
            throw e;
        }

        if (null == pluginClassName || 0 == pluginClassName.length()) {
            //pluginクラスが指定されなかった
            throw new IllegalPluginXmlFormatException("Plugin entry class is not specifed in "
                    + pluginXml.getAbsolutePath());
        }

        //pluginクラスが指定された

        //クラスパスを通す場所のURLを作る
        final Set<URL> libraryClassPathSet = new LinkedHashSet<URL>();
        for (final File defaultLibrary : defaultLibraryFiles) {
            try {
                libraryClassPathSet.add(defaultLibrary.toURL());
            } catch (final MalformedURLException e) {
                //自動的にロードするライブラリとして，ディレクトリを探して見つけたファイルのURLが作れなかった．
                //多分このケースは有り得ないし，万が一起こっても無視する．
            }
        }

        if (null != classpathStrings) {
            for (final String classpath : classpathStrings) {
                try {
                    libraryClassPathSet.add((new File(pluginRootDir, classpath)).toURL());
                } catch (final MalformedURLException e) {
                    //ライブラリとしてXMLで指定されたファイルのURLが作れなかった．多分XMLのパス指定がおかしい
                    throw new IllegalPluginXmlFormatException("Failed to allocate classpath value "
                            + classpath + " specifed in " + pluginXml.getAbsolutePath());
                }
            }
        }

        final URL[] libraryClassPathArray = new URL[libraryClassPathSet.size()];
        libraryClassPathSet.toArray(libraryClassPathArray);

        try {
            //このプラグイン専用のURLクラスローダを作成
            final URLClassLoader loader = new URLClassLoader(libraryClassPathArray);
            //それを使ってプラグインクラスをロードしてインスタンス化
            final Class<?> pluginClass = loader.loadClass(pluginClassName);
            final AbstractPlugin plugin = (AbstractPlugin) pluginClass.newInstance();

            assert (null != plugin) : "Illeagal state: Plugin class's instance is null.";

            //プラグインディレクトリをセット
            plugin.setPluginRootdir(pluginRootDir);
            
            //プラグインディレクトリ以下へのアクセスパーミッションをセット
            try{
                String filePath = pluginRootDir.getAbsolutePath() + File.separator+ "-";
                plugin.addPermission(new FilePermission(filePath, "read"));
                plugin.addPermission(new FilePermission(filePath, "write"));
                plugin.addPermission(new FilePermission(filePath, "delete"));
            } catch (SecurityException e){
                //パーミッションが得られなかったけど、問題ないかもしれないので続ける.
                assert (false) : "Illegal state: Plugin directory's access permission can not created.";
            }

            //プラグイン情報の構築を試みる
            if (!this.createPluginInfo(plugin)) {
                throw new PluginResponseException("Failed to create plugin information about "
                        + pluginClassName + ". Plugin's information methods must return within "
                        + PLUGIN_METHODS_RESPONSE_TIME + " milli seconds.");
            }

            //ロード->キャスト->インスタンス化->ディレクトリのセット->プラグイン情報の構築が全て成功したので返す.
            return plugin;
        } catch (final SecurityException e) {
            throw new PluginClassLoadException("Failed to load " + pluginClassName + ".", e);
        } catch (final ClassNotFoundException e) {
            throw new PluginClassLoadException("Failed to load " + pluginClassName + ".", e);
        } catch (final InstantiationException e) {
            throw new PluginClassLoadException("Failed to instanciate " + pluginClassName + ".", e);
        } catch (final IllegalAccessException e) {
            throw new PluginClassLoadException("Failed to access to " + pluginClassName + ".", e);
        } catch (final IllegalStateException e) {
            throw new PluginLoadException("Failed to set plugin root direcotyr.", e);
        }
    }

    /**
     * デフォルトのpluginsディレクトリから全てのプラグインをロードするメソッド
     * 個別のプラグインのロード失敗によって発生した例外は返さない．
     * @return ロードできた各プラグインのプラグインクラスを格納するリスト
     * @throws PluginLoadException デフォルトのpluginsディレクトリの検出に失敗した場合．
     */
    public List<AbstractPlugin> loadPlugins() throws PluginLoadException {
        return this.loadPlugins(this.searchPluginsDirectory());
    }

    /**
     * 指定したディレクトリ以下にある全てのプラグインをロードするメソッド．
     * 個別のプラグインのロード失敗によって発生した例外は返さない．
     * @param pluginsDir プラグインが配置されているディレクトリ
     * @return　ロードできた各プラグインのプラグインクラスを格納するリスト
     * @throws NullPointerException pluginsDirがnullの場合
     * @throws IllegalArgumentException pluginsDirが存在しない場合，ディレクトリではない場合
     */
    public List<AbstractPlugin> loadPlugins(final File pluginsDir) {
        if (null == pluginsDir) {
            throw new NullPointerException();
        }

        if (!pluginsDir.exists()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not found.");
        }

        if (!pluginsDir.isDirectory()) {
            throw new IllegalArgumentException(pluginsDir.getAbsolutePath() + " is not directory.");
        }

        final List<AbstractPlugin> result = new ArrayList<AbstractPlugin>(100);
        final File[] pluginDirs = pluginsDir.listFiles();

        final MessagePrinter errorPrinter = new DefaultMessagePrinter(new MessageSource() {
            public String getMessageSourceName() {
                return "PluginLoader#loadPlugins(File)";
            }
        }, MESSAGE_TYPE.ERROR);

        for (final File pluginDir : pluginDirs) {
            if (pluginDir.isDirectory()) {
                try {
                    final AbstractPlugin plugin = this.loadPlugin(pluginDir);
                    result.add(plugin);
                } catch (final PluginLoadException e) {
                    errorPrinter.println("Failed to load plugin : " + pluginDir.getName());
                } catch (final PluginResponseException e) {
                    errorPrinter.println(e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * デフォルトライブラリクラスディレクトリを削除するメソッド．
     * @param libraryDirName 削除するライブラリクラスディレクトリ
     */
    public void removeLibraryDirectoryName(final String libraryDirName) {
        this.libraryDirectoryNames.remove(libraryDirName);
    }

    /**
     * デフォルトライブラリファイルの拡張子を削除するメソッド．
     * @param exntension 削除するデフォルトライブラリファイルの拡張子
     */
    public void removeLibraryExtension(final String exntension) {
        this.libraryExtensions.remove(exntension);
    }

    /**
     * プラグイン構成情報を記述するXMLファイル名を置き換えるメソッド．
     * デフォルトはplugin.xml
     * @param xmlFileName 置き換えるファイル名
     */
    public void setPluginXmlFileName(final String xmlFileName) {
        this.pluginXmlFileName = xmlFileName;
    }

    /**
     * デフォルトライブラリディレクトリかどうかを判定するメソッド．
     * このメソッドをオーバーライドすることによって，どのディレクトリをライブラリ置き場と見なすかの判断を
     * 自由に拡張できる．
     * @param dir 判定対象のディレクトリ
     * @return ライブラリ置き場と見なす場合はtrue
     */
    protected boolean isLibraryDirectory(final File dir) {
        if (null == dir || !dir.exists() || !dir.isDirectory()) {
            return false;
        }

        final String directoryName = dir.getName();
        for (final String libDir : this.libraryDirectoryNames) {
            if (directoryName.equals(libDir)) {
                return true;
            }
        }

        return false;
    }

    /**
     * デフォルトライブラリファイルかどうかを判定するメソッド．
     * このメソッドをオーバーライドすることによって，どのファイルをライブラリと見なすかの判断を
     * 自由に拡張できる．
     * @param file 判定対象ファイル
     * @return ライブラリと見なす場合はtrue
     */
    protected boolean isLibraryFile(final File file) {
        if (null == file || !file.exists() || !file.isFile()) {
            return false;
        }

        final String fileName = file.getName();
        for (final String extension : this.libraryExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * デフォルトのpluginsディレクトリを探索するメソッド．
     * @return デフォルトのpluginsディレクトリ
     * @throws PluginLoadException pluginsディレクトリの探索がセキュリティ上できなかった場合，最終的に見つからなかった場合
     * @throws IllegalPluginDirectoryStructureException 探索結果のディレクトリが不正な場所であった場合
     */
    protected synchronized File searchPluginsDirectory() throws PluginLoadException,
            IllegalPluginDirectoryStructureException {
        if (null != this.pluginsDirectory) {
            return this.pluginsDirectory;
        }

        File result = null;

        CodeSource source = null;
        try {
            source = this.getClass().getProtectionDomain().getCodeSource();
        } catch (final SecurityException e) {
            throw new PluginLoadException("Could not search plugins directory.", e);
        }

        if (null != source) {
            URI sourceUri = null;
            final URL sourceUrl = source.getLocation();
            try {
                sourceUri = sourceUrl.toURI();
            } catch (final URISyntaxException e) {
                throw new IllegalPluginDirectoryStructureException(
                        "Could not allocate plugins directory " + sourceUrl, e);
            }

            if (null != sourceUri) {
                File sourceRootDir = new File(sourceUri).getParentFile();

                assert (sourceRootDir.exists()) : "Illeagal state: "
                        + sourceRootDir.getAbsolutePath() + " is not found.";
                assert (sourceRootDir.isDirectory()) : "Illeagal state: "
                        + sourceRootDir.getAbsolutePath() + " is not direcotry.";

                final File[] directoryEntries = sourceRootDir.listFiles();
                for (final File directoryEntry : directoryEntries) {
                    if (directoryEntry.isDirectory()
                            && directoryEntry.getName().equals(PLUGINS_DIRECTORY_NAME)) {
                        result = directoryEntry;
                        break;
                    }
                }

            }
        }

        if (null != result) {
            assert (result.exists()) : result + " is not found.";
            assert (result.isDirectory()) : result + " is not plugins directory.";

            this.pluginsDirectory = result;
            return result;
        }

        throw new PluginLoadException("Plugins directory is not found.");
    }

    /**
     * プラグイン情報を別スレッドで構築する.
     * 指定時間以内に構築できなかった場合は諦める.
     * @param plugin 情報を構築するプラグイン.
     * @return プラグイン情報を指定時間以内に構築できたらtrue，できなかったらfalse.
     */
    private boolean createPluginInfo(final AbstractPlugin plugin) {
        final Thread creationThread = new Thread() {
            @Override
            public void run() {
                plugin.getPluginInfo();
            }
        };

        creationThread.start();
        try {
            creationThread.join(PLUGIN_METHODS_RESPONSE_TIME);//構築まで指定時間待つ
        } catch (final InterruptedException e) {
            //諦める
        }
        return plugin.isPluginInfoCreated();
    }

    /**
     * 指定されたディレクトリからプラグイン設定XMLファイルを探すメソッド
     * @param pluginRootDir 探すディレクトリ
     * @return XMLファイル．見つからなければnull．
     */
    private File detectPluginXmlFile(final File pluginRootDir) {
        final File[] directoryEntries = pluginRootDir.listFiles();

        for (final File directoryEntry : directoryEntries) {
            if (directoryEntry.isFile()) {
                if (directoryEntry.getName().equals(this.pluginXmlFileName)) {
                    return directoryEntry;
                }
            }
        }

        return null;
    }

    /**
     * 指定されたディレクトリから以下から，デフォルトのライブラリファイル群を検索するメソッド
     * @param pluginRootDir 指定されたディレクトリ
     * @return 発見したライブラリファイル群
     */
    private File[] detectSpecifiedLibraryFiles(final File pluginRootDir) {
        final File[] directoryEntries = pluginRootDir.listFiles();

        final List<File> libraryDirectries = new ArrayList<File>();
        final List<File> libraryFiles = new ArrayList<File>();

        for (final File directoryEntry : directoryEntries) {
            if (directoryEntry.isFile()) {
                if (this.isLibraryFile(directoryEntry)) {
                    libraryFiles.add(directoryEntry);
                }
            } else if (directoryEntry.isDirectory()) {
                if (this.isLibraryDirectory(directoryEntry)) {
                    libraryDirectries.add(directoryEntry);
                }
            } else {
                assert (false) : "Unknown directory entry.";
            }
        }

        for (final File libraryDirectory : libraryDirectries) {
            assert (libraryDirectory.exists() && libraryDirectory.isDirectory()) : "Illeagal state: local variable libraryDirectories has unexpected File.";
            final File[] libraryDirEntries = libraryDirectory.listFiles();
            for (final File libraryDirEntry : libraryDirEntries) {
                if (libraryDirEntry.isFile()) {
                    if (this.isLibraryFile(libraryDirEntry)) {
                        libraryFiles.add(libraryDirEntry);
                    }
                }
            }
        }

        final File[] result = new File[libraryFiles.size()];
        return libraryFiles.toArray(result);
    }

    /**
     * デフォルトライブラリファイルの拡張子のSet
     */
    private final Set<String> libraryExtensions = new LinkedHashSet<String>();

    /**
     * デフォルトライブラリディレクトりん名前のSet
     */
    private final Set<String> libraryDirectoryNames = new LinkedHashSet<String>();

    /**
     * プラグインの設定情報を記述するXMLファイル名
     */
    private String pluginXmlFileName = DEFAULT_PLUGIN_XML_NAME;

    /**
     * 検出したデフォルトpluginsディレクトリ
     */
    private File pluginsDirectory = null;

    /**
     * デフォルトのプラグイン設定情報XMLのファイル名．
     */
    private static final String DEFAULT_PLUGIN_XML_NAME = "plugin.xml";

    /**
     * デフォルトpluginsディレクトリ名．
     */
    private static final String PLUGINS_DIRECTORY_NAME = "plugins";

    /**
     * プラグイン情報の構築時に待つ最大時間.
     */
    private static final int PLUGIN_METHODS_RESPONSE_TIME = 5000;
}
