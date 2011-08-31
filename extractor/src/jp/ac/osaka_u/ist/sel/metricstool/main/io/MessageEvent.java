package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


/**
 * メッセージイベントクラス
 * 
 * @author kou-tngt
 *
 */
public class MessageEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = -4711363868655969016L;

    /**
     * コンストラクタ
     * @param source メッセージ送信者
     * @param messageType メッセージの種類
     * @param message メッセージ
     */
    public MessageEvent(final MessageSource source, final MESSAGE_TYPE messageType,
            final String message) {
        super(source);
        this.source = source;
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * メッセージを取得するメソッド
     * @return メッセージ
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * メッセージの種類を取得するメソッド
     * @return メッセージの種類
     */
    public MESSAGE_TYPE getMessageType() {
        return this.messageType;
    }

    /**
     * メッセージ送信者を取得するメソッド
     * @return メッセージ送信者
     * @see java.util.EventObject#getSource()
     */
    @Override
    public MessageSource getSource() {
        return this.source;
    }

    /**
     * メッセージ送信者
     */
    private final MessageSource source;

    /**
     * メッセージの種類
     */
    private final MESSAGE_TYPE messageType;

    /**
     * メッセージ
     */
    private final String message;

}
