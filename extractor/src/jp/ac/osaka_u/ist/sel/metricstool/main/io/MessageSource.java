package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * テキスト表示部へのメッセージ送信者を表すインタフェース
 * 
 * @author kou-tngt
 *
 */
public interface MessageSource {

    /**
     * 送信者の名前を返す
     * @return 送信者の名前
     */
    public String getMessageSourceName();
}
