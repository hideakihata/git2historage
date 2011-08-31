package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.io.File;
import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Enumeration;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultClassMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFieldMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFileInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultFileMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultMethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.DefaultMethodMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FieldMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FileInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.FileMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodMetricsRegister;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.AlreadyConnectedException;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressReporter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * メトリクス計測プラグイン実装用の抽象クラス
 * <p>
 * 各プラグインはこのクラスを継承したクラスを1つもたなければならない． また，そのクラス名をplugin.xmlファイルに指定の形式で記述しなければならない．
 * <p>
 * mainモジュールは各プラグインディレクトリからplugin.xmlファイルを探索し、 そこに記述されている，このクラスを継承したクラスをインスタンス化し、
 * 各メソッドを通じて情報を取得した後、executeメソッドを呼び出してメトリクス値を計測する
 * 
 * @author kou-tngt
 */
public abstract class AbstractPlugin implements MessageSource, ProgressSource {

    /**
     * プラグインの情報を保存する内部不変クラス． AbstractPluginからのみインスタンス化できる．
     * <p>
     * プラグインの情報を動的に変更されると困るので、この内部クラスのインスタンスを用いて 情報をやりとすることでプラグイン情報の不変性を実現する．
     * 各プラグインの情報を保存するPluginInfoインスタンスの取得には {@link AbstractPlugin#getPluginInfo()}を用いる．
     * 
     * @author kou-tngt
     * 
     */
    public class PluginInfo {

        /**
         * デフォルトのコンストラクタ
         */
        private PluginInfo() {
            final LANGUAGE[] languages = AbstractPlugin.this.getMeasurableLanguages();
            this.measurableLanguages = new LANGUAGE[languages.length];
            System.arraycopy(languages, 0, this.measurableLanguages, 0, languages.length);
            this.metricName = AbstractPlugin.this.getMetricName();
            this.metricType = AbstractPlugin.this.getMetricType();
            this.useClassInfo = AbstractPlugin.this.useClassInfo();
            this.useMethodInfo = AbstractPlugin.this.useMethodInfo();
            this.useFieldInfo = AbstractPlugin.this.useFieldInfo();
            this.useFileInfo = AbstractPlugin.this.useFileInfo();
            this.useMethodLocalInfo = AbstractPlugin.this.useMethodLocalInfo();
            this.description = AbstractPlugin.this.getDescription();
            this.detailDescription = AbstractPlugin.this.getDetailDescription();
        }

        /**
         * このプラグインの簡易説明を１行で返す（できれば英語で）. デフォルトの実装では "Measure メトリクス名 metrics." と返す
         * 各プラグインはこのメソッドを任意にオーバーライドする.
         * 
         * @return 簡易説明文字列
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * このプラグインの詳細説明を返す（できれば英語で）. デフォルトの実装では空文字列を返す 各プラグインはこのメソッドを任意にオーバーライドする.
         * 
         * @return 詳細説明文字列
         */
        public String getDetailDescription() {
            return this.detailDescription;
        }

        /**
         * このプラグインがメトリクスを計測できる言語を返す．
         * 
         * @return 計測可能な言語を全て含む配列．
         */
        public LANGUAGE[] getMeasurableLanguages() {
            return this.measurableLanguages;
        }

        /**
         * このプラグインが引数で指定された言語で利用可能であるかを返す．
         * 
         * @param language 利用可能であるかを調べたい言語
         * @return 利用可能である場合は true，利用できない場合は false．
         */
        public boolean isMeasurable(final LANGUAGE language) {
            final LANGUAGE[] measurableLanguages = this.getMeasurableLanguages();
            for (int i = 0; i < measurableLanguages.length; i++) {
                if (language.equals(measurableLanguages[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * このプラグインが計測するメトリクスの名前を返す．
         * 
         * @return メトリクス名
         */
        public String getMetricName() {
            return this.metricName;
        }

        /**
         * このプラグインが計測するメトリクスのタイプを返す．
         * 
         * @return メトリクスタイプ
         * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
         */
        public METRIC_TYPE getMetricType() {
            return this.metricType;
        }

        /**
         * このプラグインがクラスに関する情報を利用するかどうかを返す．
         * 
         * @return クラスに関する情報を利用する場合はtrue．
         */
        public boolean isUseClassInfo() {
            return this.useClassInfo;
        }

        /**
         * このプラグインがフィールドに関する情報を利用するかどうかを返す．
         * 
         * @return フィールドに関する情報を利用する場合はtrue．
         */
        public boolean isUseFieldInfo() {
            return this.useFieldInfo;
        }

        /**
         * このプラグインがファイルに関する情報を利用するかどうかを返す．
         * 
         * @return ファイルに関する情報を利用する場合はtrue．
         */
        public boolean isUseFileInfo() {
            return this.useFileInfo;
        }

        /**
         * このプラグインがメソッドに関する情報を利用するかどうかを返す．
         * 
         * @return メソッドに関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodInfo() {
            return this.useMethodInfo;
        }

        /**
         * このプラグインがメソッド内部に関する情報を利用するかどうかを返す．
         * 
         * @return メソッド内部に関する情報を利用する場合はtrue．
         */
        public boolean isUseMethodLocalInfo() {
            return this.useMethodLocalInfo;
        }

        private final LANGUAGE[] measurableLanguages;

        private final String metricName;

        private final METRIC_TYPE metricType;

        private final String description;

        private final String detailDescription;

        private final boolean useClassInfo;

        private final boolean useFieldInfo;

        private final boolean useFileInfo;

        private final boolean useMethodInfo;

        private final boolean useMethodLocalInfo;
    }

    /**
     * プラグインの実行時に許可されるパーミッションを追加する. 特別権限を持つスレッドからしか呼び出せない.
     * 
     * @param permission 許可するパーミッション
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出した場合
     */
    public final void addPermission(final Permission permission) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.permissions.add(permission);
    }

    /**
     * プラグインインスタンス同士を比較する. クラスの標準名が取れるならそれを用いて比較する. 取れない場合は， {@link Class}インスタンスのを比較する.
     * ただし，通常の機能を用いてロードされるプラグインが匿名クラスであることはありえない.
     * よって，同一プラグインクラスのインスタンスは別のクラスローダからロードされても同一であると判定される.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @see #hashCode()
     */
    @Override
    public final boolean equals(final Object o) {
        if (o instanceof AbstractPlugin) {
            final String myClassName = this.getClass().getCanonicalName();
            final String otherClassName = o.getClass().getCanonicalName();
            if (null != myClassName && null != otherClassName) {
                // どちらも匿名クラスじゃない場合
                return myClassName.equals(otherClassName);
            } else if (null != myClassName || null != otherClassName) {
                // どっちかは匿名クラスだけど，どっちかは違う
                return false;
            } else {
                // 両方とも匿名クラス
                return this.getClass().equals(o.getClass());
            }
        }

        return false;
    }

    /**
     * プラグインインスタンスのハッシュコードを返す. クラスの標準名が取れるならそのハッシュコードを使う. 取れない場合は， {@link Class}インスタンスのハッシュコードを使う.
     * ただし，通常の機能を用いていロードされるプラグインが匿名クラスであることはありえない.
     * よって，同一プラグインクラスのインスタンスは別のクラスローダからロードされても同一のハッシュコードを返す.
     * 
     * @see java.lang.Object#hashCode()(java.lang.Object)
     * @see #equals(Object)
     */
    @Override
    public final int hashCode() {
        final Class<?> myClass = this.getClass();
        final String myClassName = myClass.getCanonicalName();
        return myClassName != null ? myClassName.hashCode() : myClass.hashCode();
    }

    /**
     * プラグインのルートディレクトリをセットする 一度セットされた値を変更することは出来ない.
     * 
     * @param rootDir ルートディレクトリ
     * @throws NullPointerException rootDirがnullの場合
     * @throws IllegalStateException rootDirが既にセットされている場合
     */
    public final synchronized void setPluginRootdir(final File rootDir) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == rootDir) {
            throw new NullPointerException("rootdir is null.");
        }
        if (null != this.pluginRootDir) {
            throw new IllegalStateException("rootdir was already set.");
        }

        this.pluginRootDir = rootDir;
    }

    /**
     * メッセージ送信者としての名前を返す
     * 
     * @return 送信者としての名前
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.MessageSource#getMessageSourceName()
     */
    public String getMessageSourceName() {
        return this.sourceName;
    }

    /**
     * このプラグインに許可されているパーミッションの不変な集合を返す.
     * 
     * @return このプラグインに許可されているパーミッションの集合.
     */
    public final Permissions getPermissions() {
        final Permissions permissions = new Permissions();

        for (final Enumeration<Permission> enumeration = this.permissions.elements(); enumeration
                .hasMoreElements();) {
            permissions.add(enumeration.nextElement());
        }
        permissions.setReadOnly();
        return permissions;
    }

    /**
     * プラグイン情報を保存している{@link PluginInfo}クラスのインスタンスを返す．
     * 同一のAbstractPluginインスタンスに対するこのメソッドは必ず同一のインスタンスを返し， その内部に保存されている情報は不変である．
     * 
     * @return プラグイン情報を保存している{@link PluginInfo}クラスのインスタンス
     */
    public final PluginInfo getPluginInfo() {
        if (null == this.pluginInfo) {
            synchronized (this) {
                if (null == this.pluginInfo) {
                    this.pluginInfo = new PluginInfo();
                    this.sourceName = this.pluginInfo.getMetricName();
                }
            }
        }
        return this.pluginInfo;
    }

    /**
     * プラグインのルートディレクトリを返す
     * 
     * @return プラグインのルートディレクトリ
     */
    public final File getPluginRootDir() {
        return this.pluginRootDir;
    }

    /**
     * 進捗情報送信者としての名前を返す
     * 
     * @return 進捗情報送信者としての名前
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.connection.ProgressSource#getProgressSourceName()
     */
    public String getProgressSourceName() {
        return this.sourceName;
    }

    /**
     * プラグイン情報が既に構築済みかどうかを返す
     * 
     * @return プラグイン情報が既に構築済みならtrue,そうでなければfalse
     */
    public final boolean isPluginInfoCreated() {
        return null != this.pluginInfo;
    }

    /**
     * メトリクス解析をスタートする抽象メソッド．
     */
    protected abstract void execute();

    /**
     * ファイル情報にアクセスするデフォルトのアクセサを取得する.
     * 
     * @return ファイル情報にアクセスするデフォルトのアクセサ.
     */
    protected final FileInfoAccessor getFileInfoAccessor() {
        return this.fileInfoAccessor;
    }

    /**
     * クラス情報にアクセスするデフォルトのアクセサを取得する.
     * 
     * @return クラス情報にアクセスするデフォルトのアクセサ.
     */
    protected final ClassInfoAccessor getClassInfoAccessor() {
        return this.classInfoAccessor;
    }

    /**
     * メソッド情報にアクセスするデフォルトのアクセサを取得する.
     * 
     * @return メソッド情報にアクセスするデフォルトのアクセサ.
     */
    protected final MethodInfoAccessor getMethodInfoAccessor() {
        return this.methodInfoAccessor;
    }

    /**
     * このプラグインの簡易説明を１行で返す（できれば英語で） デフォルトの実装では "Measuring the メトリクス名 metric." と返す
     * 各プラグインはこのメソッドを任意にオーバーライドする.
     * 
     * @return 簡易説明文字列
     */
    protected String getDescription() {
        return "Measuring the " + this.getMetricName() + " metric.";
    }

    /**
     * このプラグインの詳細説明を返す（できれば英語で） デフォルト実装では空文字列を返す. 各プラグインはこのメソッドを任意にオーバーライドする.
     * 
     * @return
     */
    protected String getDetailDescription() {
        return "";
    }

    /**
     * このプラグインがメトリクスを計測できる言語を返す 利用できる言語に制限のあるプラグインは、このメソッドをオーバーライドする必要がある．
     * 
     * @return 計測可能な言語を全て含む配列
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    protected LANGUAGE[] getMeasurableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す抽象メソッド．
     * 
     * @return メトリクス名
     */
    protected abstract String getMetricName();

    /**
     * このプラグインが計測するメトリクスのタイプを返す抽象メソッド．
     * 
     * @return メトリクスタイプ
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
     */
    protected abstract METRIC_TYPE getMetricType();

    /**
     * ファイル単位のメトリクス値を登録するメソッド.
     * 
     * @param fileInfo メトリクス値を登録するファイル
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 既にこのプラグインからこのファイルに関するメトリクス値の報告がされている場合.
     */
    protected final void registMetric(final FileInfo fileInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fileInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.fileMetricsRegister) {
            synchronized (this) {
                if (null == this.fileMetricsRegister) {
                    this.fileMetricsRegister = new DefaultFileMetricsRegister(this);
                }
            }
        }
        this.fileMetricsRegister.registMetric(fileInfo, value);
    }

    /**
     * クラス単位のメトリクス値を登録するメソッド.
     * 
     * @param classInfo メトリクス値を登録するクラス
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 既にこのプラグインからこのクラスに関するメトリクス値の報告がされている場合.
     */
    protected final void registMetric(final TargetClassInfo classInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == classInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.classMetricsRegister) {
            synchronized (this) {
                if (null == this.classMetricsRegister) {
                    this.classMetricsRegister = new DefaultClassMetricsRegister(this);
                }
            }
        }
        this.classMetricsRegister.registMetric(classInfo, value);
    }

    /**
     * メソッド単位のメトリクス値を登録するメソッド.
     * 
     * @param methodInfo メトリクス値を登録するメソッド
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 既にこのプラグインからこのメソッドに関するメトリクス値の報告がされている場合.
     */
    protected final void registMetric(final TargetMethodInfo methodInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == methodInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.methodMetricsRegister) {
            synchronized (this) {
                if (null == this.methodMetricsRegister) {
                    this.methodMetricsRegister = new DefaultMethodMetricsRegister(this);
                }
            }
        }
        this.methodMetricsRegister.registMetric(methodInfo, value);
    }

    /**
     * フィールド単位のメトリクス値を登録するメソッド.
     * 
     * @param fieldInfo メトリクス値を登録するメソッド
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 既にこのプラグインからこのメソッドに関するメトリクス値の報告がされている場合.
     */
    protected final void registMetric(final TargetFieldInfo fieldInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fieldInfo) || (null == value)) {
            throw new NullPointerException();
        }

        if (null == this.fieldMetricsRegister) {
            synchronized (this) {
                if (null == this.fieldMetricsRegister) {
                    this.fieldMetricsRegister = new DefaultFieldMetricsRegister(this);
                }
            }
        }
        this.fieldMetricsRegister.registMetric(fieldInfo, value);
    }

    /**
     * このプラグインからの進捗情報を送るメソッド
     * 
     * @param percentage 進捗情報値
     */
    protected final void reportProgress(final int percentage) {
        if (this.reporter != null) {
            this.reporter.reportProgress(percentage);
        }
    }

    /**
     * このプラグインがクラスに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * クラスに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return クラスに関する情報を利用する場合はtrue．
     */
    protected boolean useClassInfo() {
        return false;
    }

    /**
     * このプラグインがフィールドに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * フィールドに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return フィールドに関する情報を利用する場合はtrue．
     */
    protected boolean useFieldInfo() {
        return false;
    }

    /**
     * このプラグインがファイルに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * ファイルに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return ファイルに関する情報を利用する場合はtrue．
     */
    protected boolean useFileInfo() {
        return false;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * メソッドに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return メソッドに関する情報を利用する場合はtrue．
     */
    protected boolean useMethodInfo() {
        return false;
    }

    /**
     * このプラグインがメソッド内部に関する情報を利用するかどうかを返すメソッド． デフォルト実装ではfalseを返す．
     * メソッド内部に関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * 
     * @return メソッド内部に関する情報を利用する場合はtrue．
     */
    protected boolean useMethodLocalInfo() {
        return false;
    }

    /**
     * 実行前後の共通処理をしてから， {@link #execute()}を呼び出す.
     */
    final synchronized void executionWrapper() {
        assert (null == this.reporter) : "Illegal state : previous reporter was not removed.";
        try {
            this.reporter = new DefaultProgressReporter(this);
        } catch (final AlreadyConnectedException e1) {
            assert (null == this.reporter) : "Illegal state : previous reporter was still connected.";
        }

        // このスレッドにパーミッションを許可するように要請
        MetricsToolSecurityManager.getInstance().requestPluginPermission(this);

        try {
            this.execute();
        } catch (final Throwable e) {
            this.err.println(e);
        }

        if (null != this.reporter) {
            // 進捗報告の終了イベントを送る
            // プラグイン側で既に送られていたら何もせずに返ってくる
            this.reporter.reportProgressEnd();
            this.reporter = null;
        }

        // このスレッドからパーミッションを解除するように要請
        MetricsToolSecurityManager.getInstance().removePluginPermission(this);
    }

    /**
     * メッセージ出力用のプリンター
     */
    protected final MessagePrinter out = new DefaultMessagePrinter(this, MESSAGE_TYPE.OUT);

    /**
     * エラーメッセージ出力用のプリンター
     */
    protected final MessagePrinter err = new DefaultMessagePrinter(this, MESSAGE_TYPE.ERROR);

    /**
     * 登録されているファイル情報にアクセスするデフォルトのアクセサ.
     */
    private final FileInfoAccessor fileInfoAccessor = new DefaultFileInfoAccessor();

    /**
     * 登録されているクラス情報にアクセスするデフォルトのアクセサ.
     */
    private final ClassInfoAccessor classInfoAccessor = new DefaultClassInfoAccessor();

    /**
     * 登録されているメソッド情報にアクセスするデフォルトのアクセサ.
     */
    private final MethodInfoAccessor methodInfoAccessor = new DefaultMethodInfoAccessor();

    /**
     * ファイル単位のメトリクス値を登録するレジスタ.
     */
    private FileMetricsRegister fileMetricsRegister;

    /**
     * クラス単位のメトリクス値を登録するレジスタ.
     */
    private ClassMetricsRegister classMetricsRegister;

    /**
     * メソッド単位のメトリクス値を登録するレジスタ.
     */
    private MethodMetricsRegister methodMetricsRegister;
    
    /**
     * 
     */
    private FieldMetricsRegister fieldMetricsRegister; 

    /**
     * 進捗情報送信用のレポーター
     */
    private ProgressReporter reporter;

    /**
     * このプラグインの実行時の許可されるパーミッション
     */
    private final Permissions permissions = new Permissions();

    /**
     * プラグインの情報を保存する{@link PluginInfo}クラスのインスタンス getPluginInfoメソッドの初回の呼び出しによって作成され．
     * それ以降、このフィールドは常に同じインスタンスを参照する．
     */
    private PluginInfo pluginInfo;

    /**
     * プラグインのルートディレクトリ
     */
    private File pluginRootDir;

    /**
     * {@link MessageSource}と {@link ProgressSource}用の名前
     */
    private String sourceName = "";
}
