package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ASTビジターがメソッドパラメータ定義部に到達した時に状態遷移するステートマネージャ
 * 
 * @author kou-tngt
 *
 */
public class MethodParameterStateManager extends VariableDefinitionStateManager {

    /**
     * トークンがメソッドパラメータ定義部を表すかどうかを返す．
     * 判定にはtoken.isMethodParameterDefinitionメソッドを用いる
     * 
     * @param token　メソッドパラメータ定義部どうか判定するトークン
     * @return メソッドパラメータ定義部であればtrue.
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isMethodParameterDefinition();
    }

}
