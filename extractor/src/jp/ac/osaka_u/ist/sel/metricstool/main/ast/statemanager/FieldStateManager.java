package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ASTビジターがフィールド定義部に到達した時に状態遷移するステートマネージャ
 * 
 * @author kou-tngt
 *
 */
public class FieldStateManager extends VariableDefinitionStateManager {

    /**
     * 引数のトークンがフィールド定義部かどうかを返す．
     * token.isFieldDefinition()メソッドを用いて判定する．
     * 
     * @param token フィールド定義部かどうかを調べるトークン
     * @return tokenがフィールド定義部ならtrue
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isFieldDefinition();
    }

}
