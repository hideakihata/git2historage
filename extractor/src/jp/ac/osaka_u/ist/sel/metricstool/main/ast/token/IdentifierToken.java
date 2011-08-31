package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 識別子を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class IdentifierToken extends AstTokenAdapter {

    /**
     * 指定された文字列の識別子を表すインスタンスを作成する.
     * @param text 識別子の名前
     */
    public IdentifierToken(final String text) {
        super(text);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isIdentifier()
     */
    @Override
    public boolean isIdentifier() {
        return true;
    }
}
