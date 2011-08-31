package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * 進捗情報送信者用インタフェース
 * @author kou-tngt
 *
 */
public interface ProgressSource {
    /**
     * 送信者の名前を返す
     * @return 送信者の名前
     */
    public String getProgressSourceName();
}
