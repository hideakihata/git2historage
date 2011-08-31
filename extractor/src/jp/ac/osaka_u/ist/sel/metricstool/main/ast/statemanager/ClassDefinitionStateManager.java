package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * クラス定義部とその後のクラスブロックに対するビジターの状態を管理し，状態遷移イベントを発行するクラス．
 * 
 * @author kou-tngt
 *
 */
public class ClassDefinitionStateManager extends DeclaredBlockStateManager {

    /**
     * クラス定義部とその後のクラスブロックに関する状態遷移イベントタイプを表すEnum
     * @author kou-tngt
     *
     */
    public static enum CLASS_STATE_CHANGE implements StateChangeEventType {
        ENTER_CLASS_DEF, EXIT_CLASS_DEF, ENTER_CLASS_BLOCK, EXIT_CLASS_BLOCK;
    }

    /**
     * クラスブロックに入った時に発行する状態変化イベントタイプを返す．
     * @return クラスブロックに入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK;
    }

    /**
     * クラスブロックから出た時に発行する状態変化イベントタイプを返す．
     * @return クラスブロックから出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_BLOCK;
    }

    /**
     * クラス定義部に入った時に発行する状態変化イベントタイプを返す．
     * @return クラス定義部に入った時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_DEF;
    }

    /**
     * クラス定義部から出た時に発行する状態変化イベントタイプを返す．
     * @return クラス定義部から出た時に発行する状態変化イベントのイベントタイプ
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_DEF;
    }

    /**
     * 引数のイベントがクラス定義部を表すかどうかを返す．
     * token.isClassDefinition()メソッドを用いて判定する．
     * 
     * @param event　クラス定義部を表すかどうかを調べたいASTイベント
     * @return クラス定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isClassDefinition() || event.getToken().isEnumDefinition();
    }

    /**
     * 引数のトークンがクラスブロックを表すかどうかを返す．
     * token.isClassblock()メソッドを用いて判定する．
     * 
     * @param token　クラスブロックを表すかどうかを調べたいASTトークン
     * @return クラスブロックを表すトークンであればtrue
     */
    @Override
    protected boolean isBlockToken(final AstToken token) {
        return token.isClassBlock();
    }
}
