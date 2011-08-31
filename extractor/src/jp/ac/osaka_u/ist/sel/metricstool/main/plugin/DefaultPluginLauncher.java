package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.ProgressConnector;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ClosableLinkedBlockingQueue;


/**
 * プラグインを実行スレッドを起動するランチャー
 * ほとんどのパブリックメソッドの実行に特別権限を必要とする.
 * 全てのプラグインの実行が終わった後に，必ず {@link #stopLaunching()}または
 * {@link #stopLaunchingNow()}を呼ばなければならない.
 * @author kou-tngt
 *
 */
public final class DefaultPluginLauncher implements PluginLauncher, ExecutionEndListener {

    /**
     * プラグインの実行をキャンセルするメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugin キャンセルするプラグイン
     * @return キャンセルできた場合はtrueできなかったり，すでに終了していた場合はfalse
     * @throws NullPointerException pluginがnullの場合
     * @throws AccessControlException 特別権限を持たない場合
     */
    public boolean cancel(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        if (this.futureMap.containsKey(plugin)) {
            final Future<?> future = this.futureMap.get(plugin);
            this.futureMap.remove(plugin);
            ProgressConnector.getConnector(plugin).disconnect();
            return future.cancel(true);
        }
        return false;
    }

    /**
     * 実行をまとめてキャンセルするメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugins キャンセルするプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     * @throws AccessControlException 特別権限を持たない場合
     */
    public void cancelAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.cancel(plugin);
        }
    }

    /**
     * 実行中，実行待ちのタスクを全てキャンセルする.
     * 特別権限を持つスレッドからしか実行できない.
     * @throws AccessControlException 特別権限を持たない場合
     */
    public void cancelAll() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        for (final AbstractPlugin plugin : this.futureMap.keySet()) {
            this.cancel(plugin);
        }
    }

    /**
     * 実行終了通知を受け取るリスナ
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.ExecutionEndListener#executionEnd(jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin)
     */
    public void executionEnd(final AbstractPlugin plugin) {
        this.futureMap.remove(plugin);
    }

    /**
     * 実行待ちのタスクの数を返す.
     * @return 実行待ちのタスクの数
     */
    public int getLaunchWaitingTaskNum() {
        return this.workQueue.size();
    }

    /**
     * 現在実行中のプラグインの数を返すメソッド.
     * @return 実行中のプラグインの数.
     */
    public int getCurrentLaunchingNum() {
        return this.threadPool.getActiveCount();
    }

    /**
     * 現在の同時実行最大数を返すメソッド
     * @return 同時実行最大数
     */
    public int getMaximumLaunchingNum() {
        return this.threadPool.getMaximumPoolSize();
    }

    /**
     * プラグインを実行するメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugin 実行するプラグイン
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     * @throws NullPointerException pluginがnullの場合
     */
    public void launch(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (this.stoped) {
            throw new IllegalStateException("launcher was already stoped.");
        }
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        final PluginExecutor executor = new PluginExecutor(plugin);
        final Future<?> future = this.threadPool.submit(executor);
        this.futureMap.put(executor.getPlugin(), future);
    }

    /**
     * プラグインをまとめて実行するメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param plugins 実行するプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     */
    public void launchAll(final Collection<AbstractPlugin> plugins) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        for (final AbstractPlugin plugin : plugins) {
            this.launch(plugin);
        }
    }

    /**
     * 同時実行最大数を設定するメソッド.
     * 特別権限を持つスレッドからしか実行できない.
     * @param size 同時実行最大数
     * @throws IllegalArgumentException sizeが0以下だった場合
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     */
    public void setMaximumLaunchingNum(final int size) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (1 > size) {
            throw new IllegalArgumentException("parameter size must be natural number.");
        }
        this.threadPool.setCorePoolSize(size);
        this.threadPool.setMaximumPoolSize(size);
    }

    /**
     *  ランチャーを終了する.
     *  特別権限を持つスレッドからしか実行できない.
     *  実行待ちのタスクは削除し，実行中のタスクは終わるまで待つ.
     *  @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     */
    public void stopLaunching() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stoped = true;
        this.workQueue.close();
        this.workQueue.clear();
        this.threadPool.setCorePoolSize(0);
    }

    /**
     * ランチャーを終了する.
     * 特別権限を持つスレッドからしか実行できない.
     * 実行待ちのタスクは削除し，実行中のタスクも全てキャンセルする.
     * @throws AccessControlException 特別権限を持たないスレッドから呼び出された場合
     */
    public void stopLaunchingNow() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.stopLaunching();
        this.cancelAll();
    }

    /**
     * プラグイン実行用スレッドのファクトリクラス
     * @author kou-tngt
     *
     */
    private class PluginThreadFactory implements ThreadFactory {
        /**
         * プラグイン実行用のスレッドを作成するメソッド.
         * プラグインスレッドとして登録もする.
         * @see ThreadFactory#newThread(Runnable)
         */
        public Thread newThread(final Runnable r) {
            final Thread thread = new Thread(this.PLUGIN_THREAD_GROUP, r, "plugin_"
                    + ++this.threadNameCount);
            MetricsToolSecurityManager.getInstance().addPluginThread(thread);
            return thread;
        }

        /**
         * プラグインスレッド用のスレッドグループ
         */
        private final ThreadGroup PLUGIN_THREAD_GROUP = new ThreadGroup("PluginThreads");

        /**
         * スレッドのナンバリング用変数
         */
        private int threadNameCount = 0;
    }

    /**
     * 各 {@link RunnablePlugin} のFutureを保存するマップ
     */
    private final Map<AbstractPlugin, Future<?>> futureMap = new ConcurrentHashMap<AbstractPlugin, Future<?>>();

    /**
     * ランチャーを停止されたかどうかを表す変数
     */
    private boolean stoped = false;

    /**
     * スレッドプールに使用させるキュー
     */
    private final ClosableLinkedBlockingQueue<Runnable> workQueue = new ClosableLinkedBlockingQueue<Runnable>();

    /**
     * 内部的に実際にスレッドを実行するスレッドプール
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Integer.MAX_VALUE,
            Integer.MAX_VALUE, 0, TimeUnit.SECONDS, this.workQueue, new PluginThreadFactory());

}
