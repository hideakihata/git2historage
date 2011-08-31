package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * 実際にプラグインを実行するクラス.
 * インスタンス化には特別権限が必要.
 * @author kou-tngt
 *
 */
public class PluginExecutor implements Runnable {
    /**
     * 引数のプラグインを実行するインスタンスを生成する.
     * 特別権限を持つクラスからのみ呼び出すことができる.
     * @param plugin 実行するプラグイン
     * @throws AccessControlException 特別権限を持たないスレッドから呼ばれた場合
     */
    public PluginExecutor(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.plugin = plugin;
    }

    /**
     * 実行が終了した時に，実行スレッドから呼び出されるリスナを登録するメソッド
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addExecutionEndListener(final ExecutionEndListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }

        this.listeners.add(listener);
    }

    /**
     * 実行メソッド
     */
    public void execute() {
        this.plugin.executionWrapper();
        this.fireExecutionEnd();
    }

    /**
     * プラグインを取得する
     * @return プラグインを取得する
     */
    public AbstractPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * 別スレッドとして起動される場合，のエントリメソッド.
     * {@link #execute()} を呼び出すのみである.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        this.execute();
    }

    /**
     * 実行が終了した時に，実行スレッドから呼び出されるリスナを削除するメソッド
     * @param listener 削除したいリスナ
     */
    public void removeExectionEndListener(final ExecutionEndListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * 実行終了をリスナに通知する.
     */
    private void fireExecutionEnd() {
        for (final ExecutionEndListener listener : this.listeners) {
            listener.executionEnd(this.plugin);
        }
    }

    /**
     * 実行するプラグイン
     */
    private final AbstractPlugin plugin;

    /**
     * リスナのSet
     */
    private final Set<ExecutionEndListener> listeners = new WeakHashSet<ExecutionEndListener>();

}
