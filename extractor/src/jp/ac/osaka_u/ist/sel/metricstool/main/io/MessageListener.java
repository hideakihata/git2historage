package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventListener;


/**
 * メッセージイベントを受け取るリスナ
 * @author kou-tngt
 *
 */
public interface MessageListener extends EventListener {

    /**
     * メッセージ受信メソッド
     * @param event メッセージイベント
     */
    public void messageReceived(MessageEvent event);
}
