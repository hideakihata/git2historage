package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


/**
 * 文字列，マジックナンバーなどの定数値トークンを表すクラス
 * 
 * @author kou-tngt
 *
 */
public class ConstantToken extends AstTokenAdapter {

    /**
     * 引数textで指定された文字列で記述され，引数typeで指定された型の定数を表すトークンを作成する
     * 
     * @param text　定数として記述された文字列
     * @param type　定数の型
     * @throws typeがnullの場合
     */
    public ConstantToken(final String text, final TypeInfo type) {
        super(text);

        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;
    }

    /**
     * このトークンが表す定数の型を返す.
     * @return このトークンが表す定数の型
     */
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * 定数を表すトークンかどうかを返す．
     * @return true
     */
    @Override
    public boolean isConstant() {
        return true;
    }

    /**
     * このトークンが表す定数の型
     */
    private final TypeInfo type;

}
