package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;


/**
 * メソッド定義部とその後のブロックに対するビジターの状態を管理し，状態遷移イベントを発行するクラス．
 * 
 * @author kou-tngt
 *
 */
public abstract class CallableUnitStateManager extends DeclaredBlockStateManager {

    /**
     * 発行する状態遷移イベントの種類を表すEnum
     * 
     * @author kou-tngt
     *
     */
    public static enum CALLABLE_UNIT_STATE_CHANGE implements StateChangeEventType {
        ENTER_DEF, EXIT_DEF,

        ENTER_BLOCK, EXIT_BLOCK, ;
    }

    /**
     * メソッド定義部に続くブロックに入った時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部に続くブロックに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.ENTER_BLOCK;
    }

    /**
     * メソッド定義部に続くブロックから出た時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部に続くブロックから出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.EXIT_BLOCK;
    }

    /**
     * メソッド定義部に入った時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部にに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.ENTER_DEF;
    }

    /**
     * メソッド定義部から出た時に発行する状態変化イベントタイプを返す．
     * @return メソッド定義部から出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.EXIT_DEF;
    }



}
