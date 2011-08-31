package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.EventObject;


/**
 * 状態変化を表すイベント
 * 
 * @author kou-tngt
 *
 * @param <T>　状態変化のトリガとなる要素の型
 */
public class StateChangeEvent<T> extends EventObject {

    /**
     * 状態変化イベントの種類を表すマーカーインタフェース
     * 
     * @author kou-tngt
     *
     */
    public interface StateChangeEventType {
    }

    /**
     * 状態変化イベントを発行したソース，状態変化の種類，状態変化のトリガとなった要素を指定するコンストラクタ.
     * 
     * @param source 状態変化イベントを発行したソース
     * @param stateChangeType 状態変化の種類
     * @param trigger 状態変化のトリガとなった要素
     * @throws NullPointerException source, stateChangeType, trigger のいずれかがnullだった場合
     */
    public StateChangeEvent(final StateManager source, final StateChangeEventType stateChangeType, final T trigger) {
        super(source);

        if (null == source) {
            throw new NullPointerException("source is null.");
        }
        if (null == stateChangeType) {
            throw new NullPointerException("stateChangeType is null.");
        }
        if (null == trigger) {
            throw new NullPointerException("trigger is null.");
        }

        this.source = source;
        this.stateChangeType = stateChangeType;
        this.trigger = trigger;
    }

    /**
     * イベントを発行したソースを取得する
     * @return イベントを発行したソース
     * @see java.util.EventObject#getSource()
     */
    @Override
    public StateManager getSource() {
        return this.source;
    }

    /**
     * イベントの種類を取得する
     * @return　イベントの種類
     */
    public StateChangeEventType getType() {
        return this.stateChangeType;
    }

    /**
     * イベントのトリガとなった要素を取得する
     * @return　イベントのトリガとなった要素
     */
    public T getTrigger() {
        return this.trigger;
    }

    /**
     * イベントを発行したソース
     */
    private final StateManager source;

    /**
     * イベントの種類
     */
    private final StateChangeEventType stateChangeType;

    /**
     * イベントのトリガとなった要素
     */
    private final T trigger;

}
