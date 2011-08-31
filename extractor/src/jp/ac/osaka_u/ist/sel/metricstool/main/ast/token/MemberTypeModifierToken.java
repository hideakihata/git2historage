package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * クラスメンバの種類を指定する修飾子を表すトークン
 * 
 * @author kou-tngt
 *
 */
public class MemberTypeModifierToken extends ModifierToken{

    /**
     * staticメンバを表す定数インスタンス
     */
    public static final MemberTypeModifierToken STATIC = new MemberTypeModifierToken("static");
    
    /**
     * @param text
     */
    public MemberTypeModifierToken(String text) {
        super(text);
    }
    
    /**
     * staticメンバかどうかを返す
     * @return staticメンバであればtrue,そうでないならfalse
     */
    public boolean isStaticMember(){
        return this.equals(STATIC);
    }
}
