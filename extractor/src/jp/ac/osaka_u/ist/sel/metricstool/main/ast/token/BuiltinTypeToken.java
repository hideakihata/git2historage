package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * 組み込み型を表すトークンクラス.
 * 
 * @author kou-tngt
 *
 */
public class BuiltinTypeToken extends AstTokenAdapter {

    /**
     * bool型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken BOOLEAN = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BOOLEAN), PrimitiveTypeInfo.BOOLEAN_STRING);

    /**
     * byte型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken BYTE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BYTE), PrimitiveTypeInfo.BYTE_STRING);

    /**
     * char型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken CHAR = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.CHAR), PrimitiveTypeInfo.CHAR_STRING);

    /**
     * short型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken SHORT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.SHORT), PrimitiveTypeInfo.SHORT_STRING);

    /**
     * int型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken INT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.INT), PrimitiveTypeInfo.INT_STRING);

    /**
     * long型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken LONG = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.LONG), PrimitiveTypeInfo.LONG_STRING);

    /**
     * float型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken FLOAT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.FLOAT), PrimitiveTypeInfo.FLOAT_STRING);

    /**
     * double型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken DOUBLE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.DOUBLE), PrimitiveTypeInfo.DOUBLE_STRING);

    /**
     * void型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken VOID = new BuiltinTypeToken(VoidTypeInfo.getInstance(),
            VoidTypeInfo.VOID_STRING);

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return this.type;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isPrimitiveType()
     */
    @Override
    public boolean isPrimitiveType() {
        return !this.isVoidType();
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isVoidType()
     */
    @Override
    public boolean isVoidType() {
        return this.equals(VOID);
    }

    /**
     * 指定された文字列で表される基本型を表すトークンを作成するコンストラクタ.
     * 
     * @param text　この組み込み型を表す文字列
     */
    protected BuiltinTypeToken(final UnresolvedTypeInfo<? extends TypeInfo> type, final String name) {
        super(name);

        this.type = type;
    }

    /**
     * このトークンが表す基本型
     */
    private final UnresolvedTypeInfo<? extends TypeInfo> type;
}
