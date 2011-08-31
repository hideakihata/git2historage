package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * メッセージを出力部に送るためのインタフェース
 * 
 * @author kou-tngt
 *
 */
public interface MessagePrinter {
    /**
     * メッセージの種類
     * @author kou-tngt
     */
    public static enum MESSAGE_TYPE {
        /**
         * 標準出力用
         */
        OUT,

        /**
         * 情報出力用
         */
        INFO,

        /**
         * 警告出力用
         */
        WARNING,

        /**
         * エラー出力用
         */
        ERROR
    };

    /**
     * メッセージをそのまま出力する
     * @param o 出力するメッセージ
     */
    public void print(Object o);

    /**
     * 改行する
     */
    public void println();

    /**
     * メッセージを出力して改行する
     * @param o 出力するメッセージ
     */
    public void println(Object o);

    /**
     * 複数行のメッセージの間に，他のメッセージの割り込みがないように出力する.
     * @param o 出力するメッセージの配列
     */
    public void println(Object[] o);
}
