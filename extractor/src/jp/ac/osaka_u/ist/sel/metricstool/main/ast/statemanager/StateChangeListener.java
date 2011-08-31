package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.EventListener;


/**
 * 状態変化の通知を受け取るリスナ
 * 
 * @author kou-tngt
 *
 * @param <T>　状態変化のトリガとなる要素の型
 */
public interface StateChangeListener<T> extends EventListener {

    /**
     * 状態変化を通知するメソッド
     * @param event 状態変化を表すイベント
     */
    public void stateChanged(StateChangeEvent<T> event);
}
