package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ASTビジターがローカル変数定義部に到達した時に状態遷移するステートマネージャ
 * 
 * @author kou-tngt
 *
 */
public class LocalVariableStateManager extends VariableDefinitionStateManager {

    /**
     * トークンがローカル変数定義部を表すかどうかを返す．
     * 判定にはtoken.isLocalVariableDefinition()メソッドを用いる
     * 
     * @param token　ローカル変数定義部どうか判定するトークン
     * @return ローカル変数定義部であればtrue.
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isLocalVariableDefinition();
    }
}
