package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターの名前記述部への出入りを管理するステートマネージャ．
 * 
 * 
 * @author kou-tngt
 *
 */
public class NameStateManager extends EnterExitStateManager {
    public static enum NAME_STATE implements StateChangeEventType {
        ENTER_NAME, EXIT_NAME
    }

    /**
     * 名前記述部の中に入った時に通知されるための状態変化イベントの種類を返す．
     * 
     * @return　名前記述部の中に入った時に通知されるための状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return NAME_STATE.ENTER_NAME;
    }

    /**
     * 名前記述部から出た時に通知されるための状態変化イベントの種類を返す．
     * 
     * @return　名前記述部から出た時に通知されるための状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return NAME_STATE.EXIT_NAME;
    }

    /**
     * 引数で与えられたイベントが名前記述部を表すかどうかを返す.
     * token.isNameDescription()がtrueであればtrueを返す．
     * 
     * @param event 名前記述部を表すかどうかを調べるイベント
     * @return token.isNameDescription()がtrueであればtrue
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isNameDescription();
    }

}
