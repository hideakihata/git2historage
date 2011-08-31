package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Collection;


/**
 * プラグイン実行部のインタフェース
 * @author kou-tngt
 *
 */
public interface PluginLauncher {
    /**
     * プラグインを実行する.
     * @param plugin 実行するプラグイン
     */
    public void launch(AbstractPlugin plugin);

    /**
     * プラグインをまとめて実行する
     * @param plugins 実行するプラグインのコレクション
     */
    public void launchAll(Collection<AbstractPlugin> plugins);

    /**
     * プラグインの実行をキャンセルする
     * @param plugin キャンセルするプラグイン
     */
    public boolean cancel(AbstractPlugin plugin);

    /**
     * プラグインの実行をまとめてキャンセルする
     * @param plugins キャンセルするプラグイン
     */
    public void cancelAll(Collection<AbstractPlugin> plugins);

    /**
     * 実行中，実行待ちのプラグインの実行を全てキャンセルする
     */
    public void cancelAll();

    /**
     * プラグイン同時実行最大数を設定するメソッド
     * @param num プラグイン同時実行最大数
     */
    public void setMaximumLaunchingNum(int num);

    /**
     * ランチャーを停止する.
     * 実行待ちのタスクは削除し，実行中のタスクは終わるまで待つ.
     */
    public void stopLaunching();

    /**
     * ランチャーを直ちに停止する.
     * 実行待ちのタスクは削除し，実行中のタスクは全てキャンセルされる.
     */
    public void stopLaunchingNow();

    /**
     * 実行待ちのタスクの数を返す.
     */
    public int getLaunchWaitingTaskNum();

    /**
     * 同時実行最大数を返すメソッド
     * @return 同時実行最大数
     */
    public int getMaximumLaunchingNum();

    /**
     * 現在実行されているプラグインの数を返す
     * @return 現在実行されているプラグインの数
     */
    public int getCurrentLaunchingNum();
}
