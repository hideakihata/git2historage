package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import static jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE.*;


/**
 * 演算子を表す列挙型
 * 
 * @author t-miyake
 *
 */
public enum OPERATOR {

    /**
     * 算術演算子"+"
     */
    PLUS(ARITHMETIC, "+", null, true, false),

    /**
     * 算術演算子"-"
     */
    MINUS(ARITHMETIC, "-", null, true, false),

    /**
     * 算術演算子"*"
     */
    STAR(ARITHMETIC, "*", null, true, false),

    /**
     * 算術演算子"/"
     */
    DIV(ARITHMETIC, "/", null, true, false),

    /**
     * 算術演算子"%"
     */
    MOD(ARITHMETIC, "%", null, true, false),

    /**
     * 比較演算子"=="
     */
    EQUAL(COMPARATIVE, "==", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 比較演算子"!="
     */
    NOT_EQUAL(COMPARATIVE, "!=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 比較演算子"<"
     */
    LT(COMPARATIVE, "<", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 比較演算子">"
     */
    GT(COMPARATIVE, ">", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 比較演算子"<="
     */
    LE(COMPARATIVE, "<=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 比較演算子">="
     */
    GE(COMPARATIVE, ">=", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * instanceof演算子
     */
    INSTANCEOF(COMPARATIVE, "instanceof", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 論理演算子"&&"
     */
    LAND(LOGICAL, "&&", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 論理演算子"||"
     */
    LOR(LOGICAL, "||", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * 論理演算子"!"
     */
    LNOT(LOGICAL, "!", PrimitiveTypeInfo.BOOLEAN, true, false),

    /**
     * ビット演算子"&"
     */
    BAND(BITS, "&", null, true, false),

    /**
     * ビット演算子"|"
     */
    BOR(BITS, "|", null, true, false),

    /**
     * ビット演算子"^"
     */
    BXOR(BITS, "^", null, true, false),

    /**
     * ビット演算子"~"
     */
    BNOT(BITS, "~", null, true, false),

    /**
     * シフト演算子"<<"
     */
    SL(SHIFT, "<<", null, true, false),

    /**
     * シフト演算子">>"
     */
    SR(SHIFT, ">>", null, true, false),

    /**
     * シフト演算子">>>"
     */
    BSR(SHIFT, ">>>", null, true, false),

    /**
     * 代入演算子"="
     */
    ASSIGN(ASSIGNMENT, "=", null, false, true),

    /**
     * 代入演算子"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+=", null, true, true),

    /**
     * 代入演算子"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-=", null, true, true),

    /**
     * 代入演算子"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*=", null, true, true),

    /**
     * 代入演算子"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/=", null, true, true),

    /**
     * 代入演算子"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%=", null, true, true),

    /**
     * 代入演算子"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&=", null, true, true),

    /**
     * 代入演算子"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|=", null, true, true),

    /**
     * 代入演算子"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^=", null, true, true),

    /**
     * 代入演算子"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<=", null, true, true),

    /**
     * 代入演算子">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>=", null, true, true),

    /**
     * 代入演算子">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>=", null, true, true),

    /**
     * 算術一項演算子"++"
     */
    INC(ARITHMETIC, "++", PrimitiveTypeInfo.INT, true, true),

    /**
     * 算術一項演算子"--"
     */
    DEC(ARITHMETIC, "--", PrimitiveTypeInfo.INT, true, true), ;

    /**
     * 演算子のタイプとトークンを与えて初期化
     * 
     * @param operatorType 演算子のタイプ
     * @param token 演算子のトークン
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token,
            final PrimitiveTypeInfo specifiedResultType, final boolean firstIsReferencee,
            final boolean firstIsAssignmentee) {
        this.operatorType = operatorType;
        this.token = token;
        this.specifiedResultType = specifiedResultType;
        
        this.firstIsReferencee = firstIsReferencee;
        this.firstIsAssignmentee = firstIsAssignmentee;
    }

    /**
     * 演算子のタイプを返す
     * 
     * @return 演算子のタイプ
     */
    public OPERATOR_TYPE getOperatorType() {
        return this.operatorType;
    }

    /**
     * 演算子のトークンを返す
     * 
     * @return 演算子のトークン
     */
    public String getToken() {
        return this.token;
    }

    public static OPERATOR getOperator(final String token) {
        for (final OPERATOR operator : OPERATOR.values()) {
            if (operator.getToken().equals(token)) {
                return operator;
            }
        }
        return null;
    }
    
    public boolean isFirstIsAssignmentee() {
        return this.firstIsAssignmentee;
    }
    
    public boolean isFirstIsReferencee() {
        return this.firstIsReferencee;
    }

    /**
     * 演算結果の型が決まっている場合はその型を返す.
     * 決まっていない場合はnullを返す.
     * @return 演算結果の型が決まっている場合はその型，決まっていない場合はnull
     */
    public PrimitiveTypeInfo getSpecifiedResultType() {
        return this.specifiedResultType;
    }

    final private PrimitiveTypeInfo specifiedResultType;

    /**
     * 演算子のタイプを表す変数
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * 演算子のトークンを表す変数
     */
    final private String token;

    /**
     * 一項への代入があるかどうかを表す
     */
    private final boolean firstIsAssignmentee;

    /**
     * 一項が参照として利用されるかどうかを表す
     */
    private final boolean firstIsReferencee;
}
