package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ASTビジターがメソッド内部のパラメータ定義部（for文中の変数定義などのように，定義された場所から次のブロックの終わりまで有効な変数定義部）
 * に到達した時に状態遷移するステートマネージャ
 * 
 * @author kou-tngt
 *
 */
public class LocalParameterStateManager extends VariableDefinitionStateManager {

    /**
     * トークンがローカルパラメータ定義部かどうかを返す．
     * 判定にはtoken.isLocalParameterDefinition()メソッドを用いる
     * 
     * @param token　ローカルパラメータ定義部どうか判定するトークン
     * @return ローカルパラメータ定義部であればtrue.
     */
    @Override
    protected boolean isDefinitionToken(AstToken token) {
        return token.isLocalParameterDefinition();
    }
}