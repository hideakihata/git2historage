package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;


/**
 * 送信されたメッセージをリスナーに送り届けるクラス
 * 
 * メッセージタイプ毎にインスタンスを作成する.
 * 
 * @author kou-tngt
 *
 */
public class MessagePool {

    /**
     * タイプごとのインスタンスを返すメソッド
     * @param type 取得するインスタンスのタイプ
     * @return typeに対応するインスタンス
     * @throws IllegalArgumentException type用のインスタンスが見つからなかった場合
     */
    public static MessagePool getInstance(final MESSAGE_TYPE type) {
        for (final MessagePool instance : INSTANCES) {
            if (type == instance.getMessageType()) {
                return instance;
            }
        }
        //メッセージタイプ毎にインスタンスが用意してあるはずなので，ここに来るのはありえない
        assert (false) : "Illegal state : unknown message type " + type.name() + " is found.";

        throw new IllegalArgumentException("unknown message type " + type.name());
    }

    /**
     * リスナーを追加する
     * @param listener 追加したいリスナー
     * @throws NullPointerException listenerがnullの場合
     */
    public void addMessageListener(final MessageListener listener) {
        if (null == listener) {
            throw new NullPointerException("listner is null.");
        }
        synchronized (this) {
            this.listeners.add(listener);
        }
    }

    /**
     * このインスタンスが対応するメッセージタイプを返す
     * @return メッセージタイプ
     */
    public MESSAGE_TYPE getMessageType() {
        return this.messageType;
    }

    /**
     * リスナーを削除する
     * @param listener 削除するリスナー
     */
    public void removeMessageListener(final MessageListener listener) {
        if (null != listener) {
            synchronized (this) {
                this.listeners.remove(listener);
            }
        }
    }

    /**
     * メッセージを送信するメソッド
     * @param source メッセージ送信者
     * @param message メッセージ
     * @throws NullPointerException sourceまたはmessageがnullの場合
     */
    public void sendMessage(final MessageSource source, final String message) {
        if (null == message) {
            throw new NullPointerException("message is null.");
        }
        if (null == source) {
            throw new NullPointerException("source is null.");
        }

        this.fireMessage(new MessageEvent(source, this.messageType, message));
    }

    /**
     * メッセージイベントをリスナーに送信するメソッド
     * @param event 送信するイベント
     * @throws NullPointerException eventがnullの場合
     */
    private void fireMessage(final MessageEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null");
        }

        synchronized (this) {
            for (final MessageListener listener : this.listeners) {
                listener.messageReceived(event);
            }
        }
    }

    /**
     * メッセージタイプに対応するインスタンスを作成するprivateコンストラクタ
     * @param type
     */
    private MessagePool(final MESSAGE_TYPE type) {
        this.messageType = type;
    }

    /**
     * このインスタンスのメッセージタイプ
     */
    private final MESSAGE_TYPE messageType;

    /**
     * 登録されているメッセージリスナ
     */
    private final Set<MessageListener> listeners = new ConcurrentHashSet<MessageListener>();

    /**
     * インスタンス群
     */
    private static final MessagePool[] INSTANCES;

    static {
        //メッセージタイプ毎にインスタンスを作成する
        final MESSAGE_TYPE[] types = MESSAGE_TYPE.values();
        final int size = types.length;
        INSTANCES = new MessagePool[size];
        for (int i = 0; i < size; i++) {
            INSTANCES[i] = new MessagePool(types[i]);
        }
    }

}
