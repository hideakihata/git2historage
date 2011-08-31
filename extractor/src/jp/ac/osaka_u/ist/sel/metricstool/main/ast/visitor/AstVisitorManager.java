package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;




/**
 * {@link AstVisitor} への操作と設定を管理するインタフェース.
 * 
 * @author kou-tngt
 *
 * @param <T>　管理するビジターが訪問するASTのノードの型
 */
public interface AstVisitorManager<T> {

    /**
     * 引数nodeが表すノードから管理するビジターの訪問を開始する.
     * 
     * @param node　ビジターの訪問を開始するノード
     */
    public void visitStart(T node) throws ASTParseException;
    
    public void reset();

}