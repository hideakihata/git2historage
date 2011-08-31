package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} がどのようにASTのノードを訪問するかを制御するインタフェース.
 * 
 * @author kou-tngt
 *
 * @param <T> ビジターが訪問するASTの各ノードの型
 */
public interface AstVisitStrategy<T> {

    /**
     * ビジターが現在のノードの子ノードを訪問する必要があるかどうかを返す.
     * 
     * @param node ビジターが現在到達しているノード
     * @param token ビジターが現在到達しているノードの種類を表すトークン
     */
    public boolean needToVisitChildren(T node, AstToken token);

}
