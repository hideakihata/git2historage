package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターのブロックへの出入りを管理するステートマネージャ．
 * 
 * ビジターがブロックを表すASTトークンの中に出入りする際に，種類BLOCK_STATE_CHANGEの状態変化イベントを通知する．
 * 
 * @author kou-tngt
 *
 */
public class BlockStateManager extends EnterExitStateManager {

    /**
     * ブロックへ出入りする際に発行される状態変化イベントのイベントタイプ
     * @author kou-tngt
     *
     */
    public static enum BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER, EXIT
    };

    /**
     * ブロックの中に入った時に通知される状態変化イベントの種類を返す．
     * @return　ブロックの中に入った時に通知される状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return BLOCK_STATE_CHANGE.ENTER;
    }

    /**
     * ブロックから出た時に通知される状態変化イベントの種類を返す．
     * @return　ブロックから出た時に通知される状態変化イベントの種類
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return BLOCK_STATE_CHANGE.EXIT;
    }

    /**
     * 引数で与えられたイベントがブロックを表すかどうかを返す.
     * token.isBlock()がtrueの場合はtrue,falseの場合はfalseを返す．
     * 
     * @param event ブロックを表すかどうかを調べるイベント
     * @return token.isBlock()がtrueの場合はtrue,falseの場合はfalse
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isBlock();
    }

}
