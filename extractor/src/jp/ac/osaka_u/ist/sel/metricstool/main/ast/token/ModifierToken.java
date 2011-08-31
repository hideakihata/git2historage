package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 修飾子を表すトークン
 * 
 * @author kou-tngt
 *
 */
public class ModifierToken extends AstTokenAdapter {
    
    
    /**
     * 指定された文字列の修飾子を表すトークンを作成する
     * @param text 修飾子の文字列
     */
    public ModifierToken(final String text) {
        super(text);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isModifier()
     */
    @Override
    public boolean isModifier() {
        return true;
    }
}
