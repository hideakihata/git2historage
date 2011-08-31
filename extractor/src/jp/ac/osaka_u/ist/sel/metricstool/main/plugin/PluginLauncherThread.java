package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ClosableLinkedBlockingQueue;


/**
 * プラグインランチャーのラッパークラス.
 * 特別権限を持つスレッド以外はインスタンス化できない.
 * このスレッドのインスタンスは必ず特別権限を持つ.
 * このラッパークラスを通すことで，本来特別権限が必要なプラグイン実行メソッドへのアクセスを特別権限無しで実行することができる.
 * ゆえに，このクラスをインスタンス化した特別権限を持つスレッドは，そのインスタンスがプラグインに取得されないように注意しなければならない.
 * <p>
 * 全てのプラグインの実行が終わった後に，必ず {@link #stopLaunching()}または
 * {@link #stopLaunchingNow()}を呼ばなければならない.
 * @author kou-tngt
 *
 */
public class PluginLauncherThread extends Thread implements PluginLauncher {
    /**
     * 唯一のコンストラクタ
     * 特別権限以外のスレッドから呼び出された場合は {@link AccessControlException}をスローする
     * このコンストラクタによって生成されるスレッドインスタンスは特別権限を付与される
     * @throws AccessControlException 呼び出したスレッドが特別権限を持っていなかった場合
     */
    public PluginLauncherThread(final PluginLauncher launcher) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        MetricsToolSecurityManager.getInstance().addPrivilegeThread(this);
        this.launcher = launcher;
    }

    /**
     * 実行のキャンセル要求をするメソッド
     * @param plugin キャンセルしたいプラグイン
     * @throws NullPointerException pluginがnullの場合
     */
    public synchronized boolean cancel(final AbstractPlugin plugin) {
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }
        this.cancelQueue.offer(plugin);
        this.notify();
        return true;

    }

    /**
     * キャンセル要求まとめて登録するメソッド
     * @param plugins キャンセルするプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     */
    public void cancelAll(final Collection<AbstractPlugin> plugins) {
        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        if (!this.stopFlag) {
            for (final AbstractPlugin plugin : plugins) {
                this.cancel(plugin);
            }
        }
    }

    /**
     * 実行中，実行待ちのプラグインの実行を全てキャンセルする
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher#cancelAll()
     */
    public void cancelAll() {
        this.requestCancelAll = true;
    }
    
    /**
     * 実行待ちのタスクの数を返す.
     * @return 実行待ちのタスクの数
     */
    public int getLaunchWaitingTaskNum(){
        return this.launcher.getLaunchWaitingTaskNum() + this.launchQueue.size();
    }

    /**
     * 現在実行中のプラグインの数を返すメソッド.
     * @return 実行中のプラグインの数.
     */
    public int getCurrentLaunchingNum() {
        return this.launcher.getCurrentLaunchingNum();
    }

    /**
     * プラグインの同時実行最大数を返すメソッド
     * @return 同時実行最大数
     */
    public int getMaximumLaunchingNum() {
        return this.launcher.getMaximumLaunchingNum();
    }

    /**
     * 実行要求を登録するメソッド
     * @param plugin 実行するプラグイン
     * @throws NullPointerException pluginがnullの場合
     */
    public synchronized void launch(final AbstractPlugin plugin) {
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.launchQueue.offer(plugin);
        this.notify();
    }

    /**
     * 実行要求をまとめて登録するメソッド
     * @param plugins 実行するプラグイン群を含むコレクション
     * @throws NullPointerException pluginsがnullの場合
     */
    public void launchAll(final Collection<AbstractPlugin> plugins) {
        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        if (!this.stopFlag) {
            for (final AbstractPlugin plugin : plugins) {
                this.launch(plugin);
            }
        }
    }

    /**
     * このスレッドの実行メソッド
     * 停止信号が来るまで，登録された実行要求やキャンセル要求をキューから取り出して処理する.
     * 
     */
    @Override
    public void run() {
        while (!this.stopNowFlag
                && (!this.stopFlag || !this.launchQueue.isEmpty() || !this.cancelQueue.isEmpty())) {
            //即時停止信号が来ていない　かつ　（停止信号が来てない or 仕事が残ってる）
            synchronized (this) {
                //実行リクエストが来ているプラグインを実行する
                while (!this.launchQueue.isEmpty()) {
                    this.launcher.launch(this.launchQueue.poll());
                }

                //キャンセルリクエストが来ているプラグインをキャンセルする
                //launcherでキャンセルに失敗したら，こっちのキューにいる可能性があるので，探して削除する
                while (!this.cancelQueue.isEmpty()) {
                    final AbstractPlugin plugin = this.cancelQueue.poll();
                    if (!this.launcher.cancel(plugin)) {
                        for (final Iterator<AbstractPlugin> it = this.launchQueue.iterator(); it.hasNext();) {
                            if (it.next() == plugin) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
                //全キャンセルのリクエストが来たので，全キャンセルする
                if (this.requestCancelAll) {
                    this.requestCancelAll = false;
                    this.launcher.cancelAll();
                    this.launchQueue.clear();
                }
                
                //最大同時実行数の変更要求が来たので変更する
                if (this.maximumLaunchingNumRequest > 0){
                    this.launcher.setMaximumLaunchingNum(this.maximumLaunchingNumRequest);
                    this.maximumLaunchingNumRequest = 0;
                }

                if (!this.stopNowFlag && !this.stopFlag) {
                    //まだ停止信号が来てないので，誰かが起こしに来るまで寝る
                    try {
                        this.wait();
                    } catch (final InterruptedException e) {
                        //割り込まれても気にしない
                    }
                }
            }
        }
        //停止信号を内部ランチャーに送る
        if (this.stopNowFlag) {
            this.launcher.stopLaunchingNow();
        } else if (this.stopFlag) {
            this.launcher.stopLaunching();
        }
    }

    /**
     * プラグインの同時実行最大数をセットするメソッド
     * @param size 最大数を指定する，1以上の整数
     * @throws IllegalArgumentException sizeが0以下だった場合
     */
    public void setMaximumLaunchingNum(final int size) {
        if (1 > size) {
            throw new IllegalArgumentException("size must be a natural number.");
        }

        this.maximumLaunchingNumRequest = size;
    }

    /**
     * このスレッドに停止信号を送るメソッド.
     * 実行待ちのタスクは削除し，実行中のプラグインは終了するまで待つ.
     */
    public synchronized void stopLaunching() {
        this.stopFlag = true;
        this.launchQueue.close();
        this.launchQueue.clear();
        this.notify();
    }

    /**
     * ランチャーを直ちに停止する. 
     * 実行待ちのタスクは削除し，実行中のタスクは全てキャンセルされる. 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher#stopLaunchingNow()
     */
    public void stopLaunchingNow() {
        this.stopNowFlag = true;
        this.cancelQueue.clear();
        this.stopLaunching();
    }

    /**
     * 実行要求キュー
     */
    private final ClosableLinkedBlockingQueue<AbstractPlugin> launchQueue = new ClosableLinkedBlockingQueue<AbstractPlugin>();

    /**
     * キャンセル要求キュー
     */
    private final BlockingQueue<AbstractPlugin> cancelQueue = new LinkedBlockingQueue<AbstractPlugin>();

    /**
     * 内部で利用するランチャー
     */
    private final PluginLauncher launcher;

    /**
     * 停止信号
     */
    private boolean stopFlag = false;

    /**
     * 即時停止信号
     */
    private boolean stopNowFlag = false;

    /**
     * タスクの全キャンセル要求
     */
    private boolean requestCancelAll = false;
    
    /**
     * タスク最大実行数の変更要求をする変数
     */
    private int maximumLaunchingNumRequest = 0;

}
