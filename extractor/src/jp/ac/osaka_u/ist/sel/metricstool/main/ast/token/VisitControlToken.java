package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;


/**
 * ビジターの動作を制御するために予め用意された定数トークンを持つクラス.
 * 
 * @author kou-tngt
 *
 */
public class VisitControlToken extends AstTokenAdapter {

    /**
     * このトークンで表されるノードの中にはビジターは入らない.
     */
    public static final VisitControlToken SKIP = new VisitControlToken("SKIP");

    /**
     * このトークンで表されるノード自体には特別な処理はしないが，ビジターはそのノードの子ノードを訪問しに行く.
     */
    public static final VisitControlToken ENTER = new VisitControlToken("ENTER");

    /**
     * 未定義のノードを表現するトークン.
     * このトークンで表されるノードに対してどのような処理を行うかは {@link AstVisitStrategy}の実装に任される.
     */
    public static final VisitControlToken UNKNOWN = new VisitControlToken("UNKNOWN");

    /**
     * インスタンスの種類を表現する文字列を与えるコンストラクタ.
     * @param text
     */
    private VisitControlToken(final String text) {
        super(text);
    }
}
