package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventListener;


/**
 * 進捗報告を受け取るためのインタフェース
 * 
 * @author kou-tngt
 *
 */
public interface ProgressListener extends EventListener {
    /**
     * 進捗報告があると呼び出されるメソッド
     * 
     * @param event 進捗報告内容を表すイベント
     */
    public void updataProgress(ProgressEvent event);

    /**
     * 進捗報告が途切れた時に呼び出されるメソッド
     * @param event 進捗報告が途切れたことを表すイベント
     */
    public void disconnected(ProgressEvent event);

    /**
     * 進捗報告が終了すると呼び出されるメソッド
     * @param event 進捗報告が終了したことを表すイベント
     */
    public void progressEnd(ProgressEvent event);
}
