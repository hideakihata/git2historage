package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * アクセス修飾子を表すトークンクラス.
 * 
 * @author kou-tngt
 *
 */
public class AccessModifierToken extends ModifierToken {

    /**
     * 引数で指定された文字列と可視性を持ったアクセス修飾子トークンを作成するコンストラクタ
     * 
     * @param text 修飾子の文字列
     * @param publicVisibility この修飾子を付けられた要素がパブリックな可視性を持つかどうか
     * @param nameSpaceVisibility この修飾子を付けられた要素が同じ名前空間からの可視性を持つかどうか
     * @param inheritanceVisibility この修飾子を付けられた要素が継承関係にあるクラスからの可視性をもつかどうか
     */
    public AccessModifierToken(final String text, final boolean publicVisibility, final boolean nameSpaceVisibility,
            final boolean inheritanceVisibility) {
        super(text);
        this.publicVisibility = publicVisibility;
        this.nameSpaceVisibility = nameSpaceVisibility;
        this.inheritanceVisibility = inheritanceVisibility;
    }

    @Override
    public boolean isAccessModifier() {
        return true;
    }

    /**
     * この修飾子を付けられた要素がパブリックな可視性を持つかどうかを返す
     * @return　パブリックな可視性を持つ場合はtrue
     */
    public boolean isPublicVisibility() {
        return this.publicVisibility;
    }

    /**
     * この修飾子を付けられた要素が同じ名前空間からの可視性を持つかどうかを返す
     * @return 同じ名前空間からの可視性を持つ場合はtrue
     */
    public boolean isNameSpaceVisibility() {
        return this.nameSpaceVisibility;
    }

    /**
     * この修飾子を付けられた要素が継承関係にあるクラスからの可視性を持つかどうかを返す
     * @return 継承関係にあるクラスからの可視性を持つ場合はtrue
     */
    public boolean isInheritanceVisibility() {
        return this.inheritanceVisibility;
    }

    /**
     * パブリックな可視性を持つかどうかを表す
     */
    private final boolean publicVisibility;

    /**
     * 同じ名前空間からの可視性を持つかどうかを表す
     */
    private final boolean nameSpaceVisibility;

    /**
     * 継承関係からの可視性を持つかどうかを表す
     */
    private final boolean inheritanceVisibility;
}
