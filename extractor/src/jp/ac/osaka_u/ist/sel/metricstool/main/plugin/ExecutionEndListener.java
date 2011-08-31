package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.EventListener;


/**
 * プラグインの実行終了時に呼び出されるリスナインタフェース
 * @author kou-tngt
 *
 */
public interface ExecutionEndListener extends EventListener {
    /**
     * プラグインの実行終了時に呼び出されるリスナ
     * @param plugin
     */
    public void executionEnd(AbstractPlugin plugin);
}
