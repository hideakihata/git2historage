package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * プラグインインスタンスを管理するクラス．
 * シングルトンパターンで実装されている．
 * 
 * @author kou-tngt
 */
public class PluginManager {

    /**
     * プラグインを登録する
     * このメソッドを呼び出すには特別権限が必要である
     * @param plugin 登録するプラグイン
     * @throws AccessControlException 特別権限スレッドでない場合
     * @throws NullPointerException pluginがnullの場合
     */
    public void addPlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.plugins.add(plugin);
        final PluginInfo info = plugin.getPluginInfo();
        this.pluginInfos.add(info);
        this.info2pluginMap.put(info, plugin);

        METRIC_TYPE type = plugin.getMetricType();
        switch (type) {
        case FILE_METRIC:
            this.filePlugins.add(plugin);
            break;
        case CLASS_METRIC:
            this.classPlugins.add(plugin);
            break;
        case METHOD_METRIC:
            this.methodPlugins.add(plugin);
            break;
        case FIELD_METRIC:
            this.fieldPlugins.add(plugin);
            break;
        }
    }

    /**
     * プラグインを登録する
     * このメソッドを呼び出すには特別権限が必要である
     * @param collection 登録するプラグイン群をもつコレクション
     * @throws AccessControlException 特別権限スレッドでない場合
     * @throws NullPointerException collectionがnullの場合
     */
    public void addPlugins(final Collection<AbstractPlugin> collection) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == collection) {
            throw new NullPointerException("collection is null.");
        }

        for (final AbstractPlugin plugin : collection) {
            this.addPlugin(plugin);
        }
    }

    /**
     * プラグイン情報をキーにして，対応するプラグインインスタンスを返す.
     * 特別権限を持つスレッド以外からは呼び出せない
     * @param info キーとなるプラグイン情報
     * @return 対応するプラグイン
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public AbstractPlugin getPlugin(final PluginInfo info) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return this.info2pluginMap.get(info);
    }

    /**
     * 登録されているプラグインの数を返す.
     * @return 登録されているプラグインの数.
     */
    public int getPluginCount() {
        return this.plugins.size();
    }

    /**
     * 全てのプラグインを含んだ編集不可なSetを返す
     * 特別権限を持つスレッド以外からは呼び出せない
     * @return プラグインのSet
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.plugins);
    }

    /**
     * ファイル単位のメトリクスを計測するプラグインの編集不可なSetを返す
     * 特別権限を持つスレッド以外からは呼び出せない
     * @return ファイル単位のメトリクスを計測するプラグインのSet
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getFileMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.filePlugins);
    }

    /**
     * クラス単位のメトリクスを計測するプラグインの編集不可なSetを返す
     * 特別権限を持つスレッド以外からは呼び出せない
     * @return クラス単位のメトリクスを計測するプラグインのSet
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getClassMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.classPlugins);
    }

    /**
     * メソッド単位のメトリクスを計測するプラグインの編集不可なSetを返す
     * 特別権限を持つスレッド以外からは呼び出せない
     * @return メソッド単位のメトリクスを計測するプラグインのSet
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getMethodMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.methodPlugins);
    }

    /**
     * フィールド単位のメトリクスを計測するプラグインの編集不可なSetを返す
     * 特別権限を持つスレッド以外からは呼び出せない
     * @return フィールド単位のメトリクスを計測するプラグインのSet
     * @throws AccessControlException 特別権限を持っていないスレッドからの呼び出しの場合
     */
    public Set<AbstractPlugin> getFieldMetricPlugins() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        return Collections.unmodifiableSet(this.fieldPlugins);
    }

    /**
     * プラグイン情報の編集不可なSetを返す
     * @return プラグイン情報のSet
     */
    public Set<PluginInfo> getPluginInfos() {
        return Collections.unmodifiableSet(this.pluginInfos);
    }

    /**
     * プラグインを削除する
     * 特別権限スレッドのみから呼び出せる.
     * @param plugin 削除するプラグイン
     * @throws AccessControlException 特別権限を持っていない場合
     */
    public void removePlugin(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null != plugin) {
            this.plugins.remove(plugin);
            final PluginInfo info = plugin.getPluginInfo();
            this.pluginInfos.remove(info);
            this.info2pluginMap.remove(info);

            switch (plugin.getMetricType()) {
            case FILE_METRIC:
                this.filePlugins.remove(plugin);
                break;
            case CLASS_METRIC:
                this.classPlugins.remove(plugin);
                break;
            case METHOD_METRIC:
                this.methodPlugins.remove(plugin);
                break;
            case FIELD_METRIC:
                this.fieldPlugins.remove(plugin);
            }
        }
    }

    /**
     * プラグインを削除する
     * 特別権限スレッドのみから呼び出せる.
     * @param plugins 削除するプラグインのCollection
     * @throws AccessControlException 特別権限を持っていない場合
     */
    public void removePlugins(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (plugins != null) {
            for (final AbstractPlugin plugin : plugins) {
                this.removePlugin(plugin);
            }
        }
    }

    /**
     * シングルトン用，空のprivateコンストラクタ
     */
    public PluginManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.plugins = new ConcurrentHashSet<AbstractPlugin>();
        this.filePlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.classPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.methodPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.fieldPlugins = new ConcurrentHashSet<AbstractPlugin>();
        this.pluginInfos = new ConcurrentHashSet<PluginInfo>();
        this.info2pluginMap = new ConcurrentHashMap<PluginInfo, AbstractPlugin>();
    };

    /**
     * 全てのプラグインのSet
     */
    private final Set<AbstractPlugin> plugins;

    /**
     * ファイル単位のメトリクスを計測するプラグインのセット
     */
    private final Set<AbstractPlugin> filePlugins;

    /**
     * クラス単位のメトリクスを計測するプラグインのセット
     */
    private final Set<AbstractPlugin> classPlugins;

    /**
     * メソッド単位のメトリクスを計測するプラグインのセット
     */
    private final Set<AbstractPlugin> methodPlugins;

    /**
     * フィールド単位のメトリクスを計測するプラグインのセット
     */
    private final Set<AbstractPlugin> fieldPlugins;

    /**
     * 全てのプラグイン情報のSet
     */
    private final Set<PluginInfo> pluginInfos;

    /**
     * プラグイン情報からプラグインインスタンスへのマッピング
     */
    private final Map<PluginInfo, AbstractPlugin> info2pluginMap;
}
