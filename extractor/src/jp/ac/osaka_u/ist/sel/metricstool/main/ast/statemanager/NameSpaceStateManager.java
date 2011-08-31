package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * 名前空間定義部とその後のブロックに対するビジターの状態を管理し，状態遷移イベントを発行するクラス．
 * 
 * @author kou-tngt
 *
 */
public class NameSpaceStateManager extends DeclaredBlockStateManager {

    /**
     * 発行する状態遷移イベントの種類を表すEnum
     * 
     * @author kou-tngt
     *
     */
    public static enum NAMESPACE_STATE_CHANGE implements StateChangeEventType {
        ENTER_NAMESPACE_DEF, EXIT_NAMESPACE_DEF,

        ENTER_NAMESPACE_BLOCK, EXIT_NAMESPACE_BLOCK, ;
    }

    /**
     * 名前空間定義部に続くブロックに入った時に発行する状態変化イベントタイプを返す．
     * @return 名前空間定義部に続くブロックに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_BLOCK;
    }

    /**
     * 名前空間定義部に続くブロックから出た時に発行する状態変化イベントタイプを返す．
     * @return 名前空間定義部に続くブロックから出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_BLOCK;
    }

    /**
     * 名前空間定義部に入った時に発行する状態変化イベントタイプを返す．
     * @return 名前空間定義部にに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_DEF;
    }

    /**
     * 名前空間定義部から出た時に発行する状態変化イベントタイプを返す．
     * @return 名前空間定義部から出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_DEF;
    }

    /**
     * 引数のイベントが名前空間定義部を表すかどうかを返す．
     * token.isNameSpaceDefinition()メソッドを用いて判定する．
     * 
     * @param token　名前空間定義部を表すかどうかを調べたいASTイベント
     * @return 名前空間定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isNameSpaceDefinition();
    }
}
