package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



/**
 * Œ^T‚Å•\Œ»‚³‚ê‚éAST‚Ìƒm[ƒh‚ğC {@link AstToken}‚É–|–ó‚·‚é.
 * 
 * @author kou-tngt
 *
 * @param <T> –|–ó‚³‚ê‚éASTƒm[ƒh‚ÌŒ^
 */
public interface AstTokenTranslator<T> {

    /**
     * ˆø”node‚ª•\‚·ASTƒm[ƒh‚ğ{@link AstToken}‚É–|–ó‚·‚é.
     * 
     * @param node –|–ó‚·‚éASTƒm[ƒh
     * @return –|–óŒ‹‰Ê‚Ìƒg[ƒNƒ“
     */
    public AstToken translate(T node);

}
