package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * メッセージプリンタのデフォルト実装
 * 
 * @author kou-tngt
 *
 */
public class DefaultMessagePrinter implements MessagePrinter {

    /**
     * コンストラクタ
     * @param source メッセージ送信者
     * @param type メッセージタイプ
     */
    public DefaultMessagePrinter(final MessageSource source, final MESSAGE_TYPE type) {
        this.pool = MessagePool.getInstance(type);
        this.source = source;
    }

    /**
     * メッセージをそのまま出力する
     * @param o 出力するメッセージ
     */
    public void print(final Object o) {
        this.pool.sendMessage(this.source, String.valueOf(o));
    }

    /**
     * 改行する
     */
    public void println() {
        this.print(LINE_SEPARATOR);
    }

    /**
     * メッセージを出力して改行する
     * @param o 出力するメッセージ
     */
    public void println(final Object o) {
        this.print(String.valueOf(o) + LINE_SEPARATOR);
    }

    /**
     * 複数行のメッセージの間に，他のメッセージの割り込みがないように出力する.
     * @param objects 出力するメッセージの配列
     */
    public void println(final Object[] objects) {
        final StringBuilder builder = new StringBuilder();
        for (final Object o : objects) {
            builder.append(String.valueOf(o));
            builder.append(LINE_SEPARATOR);
        }
        this.print(builder.toString());
    }

    /**
     * メッセージ送信者
     */
    private final MessageSource source;

    /**
     * 対応するメッセージプール
     */
    private final MessagePool pool;

    /**
     * システム依存の改行記号
     */
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

}
