package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 特定のインスタンスを指定するトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class InstanceToken extends AstTokenAdapter {

    /**
     * 自分自身を指定する定数.
     */
    public static final InstanceToken THIS = new InstanceToken("this");

    /**
     * 空のインスタンスを指定する定数.
     */
    public static final InstanceToken NULL = new InstanceToken("null");

    /**
     * 
     * @param text
     */
    protected InstanceToken(final String text) {
        super(text);
    }
}
