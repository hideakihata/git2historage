package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE;


/**
 * 演算子を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class OperatorToken extends AstTokenAdapter {

    /**
     * キャストは演算子を表す定数インスタンス
     */
    public static final OperatorToken CAST = new OperatorToken(null, "CAST", 2, false, false,
            new int[] { 0 });

    /**
     * インクリメント演算子とデクリメント演算子を表す定数インスタンス
     */
    public static final OperatorToken INCL_AND_DECL = new OperatorToken(null, "INCLEMENT", 1, true,
            true, new int[] { 0 });

    /**
     * 代入演算子を表す定数インスタンス
     */
    public static final OperatorToken ASSIGNMENT = new OperatorToken(OPERATOR_TYPE.ASSIGNMENT,
            "ASSIMENT", 2, true, false, new int[] { 0 });

    /**
     * 複合代入演算子を表す定数インスタンス
     */
    public static final OperatorToken COMPOUND_ASSIGNMENT = new OperatorToken(
            OPERATOR_TYPE.ASSIGNMENT, "COMPOUND_ASSIGNMENT", 2, true, true, new int[] { 0 });

    /**
     * 単項算術演算子を表す定数インスタンス
     */
    public static final OperatorToken ARITHMETHIC_UNARY = new OperatorToken(
            OPERATOR_TYPE.ARITHMETIC, "ARITHMETIC_UNARY", 1, false, true, new int[] { 0 });

    /**
     * 二項算術演算子を表す定数インスタンス
     */
    public static final OperatorToken ARITHMETICH_BINOMIAL = new OperatorToken(
            OPERATOR_TYPE.ARITHMETIC, "ARITHMETIC_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * 単項論理演算子を表す定数インスタンス
     */
    public static final OperatorToken LOGICAL_UNARY = new OperatorToken(OPERATOR_TYPE.LOGICAL,
            "NOT_UNARY", 1, false, true, new int[] { 0 });

    /**
     * 二項論理演算子を表す定数インスタンス
     */
    public static final OperatorToken LOGICAL_BINOMIAL = new OperatorToken(OPERATOR_TYPE.LOGICAL,
            "LOGICAL_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * 単項ビット演算子を表す定数インスタンス
     */
    public static final OperatorToken BIT_UNARY = new OperatorToken(OPERATOR_TYPE.BITS,
            "BIT_UNARY", 1, false, true, new int[] { 0 });

    /**
     * 二項ビット演算子を表す定数インスタンス
     */
    public static final OperatorToken BIT_BINOMIAL = new OperatorToken(OPERATOR_TYPE.BITS,
            "BIT_BINOMIAL", 2, false, true, new int[] { 0, 1 });

    /**
     * 二項シフト演算子を表す定数インスタンス
     */
    public static final OperatorToken SHIFT = new OperatorToken(OPERATOR_TYPE.SHIFT, "SHIFT", 2,
            false, true, new int[] { 0, 1 });

    /**
     * 二項比較演算子を表す定数インスタンス
     */
    public static final OperatorToken COMPARATIVE = new OperatorToken(OPERATOR_TYPE.COMPARATIVE,
            "COMPARATIVE", 2, false, true, new int[] {});

    /**
     * 三項演算子を表す定数インスタンス
     */
    public static final OperatorToken TERNARY = new OperatorToken(null, "TERNARY", 3, false, true,
            new int[] { 1, 2 });

    /**
     * 配列記述子を表す定数インスタンス
     */
    public static final OperatorToken ARRAY = new OperatorToken(null, "ARRAY", 2, false, true,
            new int[] {});

    /**
     * 演算子の文字列，扱う項の数，左辺値への参照と代入を行うかどうか，演算結果の型を指定するコンストラクタ.
     * 
     * @param text 演算子の文字列
     * @param termCount 扱う項の数
     * @param leftIsAssignmentee 左辺値への代入がある場合はtrue
     * @param leftIsReferencee 左辺値へのがある場合はtrue
     * @param specifiedResultType 演算結果の型が決まっている場合はその型を，決まっていない場合はnullを指定する
     * @throws IllegalArgumentException termCountが0以下の場合
     */
    public OperatorToken(final OPERATOR_TYPE operator, final String text, final int termCount,
            final boolean leftIsAssignmentee, final boolean leftIsReferencee,
            final int[] typeSpecifiedTermIndexes) {
        super(text);

        if (termCount <= 0) {
            throw new IllegalArgumentException("Operator must treat one or more terms.");
        }

        this.operator = operator;
        this.leftIsAssignmentee = leftIsAssignmentee;
        this.leftIsReferencee = leftIsReferencee;
        this.termCount = termCount;
        this.typeSpecifiedTermIndexes = typeSpecifiedTermIndexes;
    }

    /**
     * この演算子が取り扱う項の数を返す.
     * @return この演算子が取り扱う項の数
     */
    public int getTermCount() {
        return this.termCount;
    }

    /**
     * 左辺値への代入があるかどうかを返す.
     * @return　左辺値への代入がある場合はtrue
     */
    @Override
    public boolean isAssignmentOperator() {
        return this.leftIsAssignmentee;
    }

    /**
     * 演算子を表すトークンかどうかを返す.
     * 
     * @return　true
     */
    @Override
    public boolean isOperator() {
        return true;
    }

    /**
     * 左辺値が参照として利用されるかどうかを返す.
     * @return　左辺値が参照として利用される場合はtrue
     */
    public boolean isLeftTermIsReferencee() {
        return this.leftIsReferencee;
    }

    /**
     * 名前解決部が利用するEnum OPERATORの要素を返す．
     * 名前解決部に型解決を委譲しない種類の演算子の場合はnullを返す．
     * @return 名前解決部が利用するEnum OPERATORの要素，名前解決部に型解決を委譲しない種類の演算子の場合はnull
     */
    public OPERATOR_TYPE getOperator() {
        return this.operator;
    }

    /**
     * 演算結果の型を決定される際に考慮される項のインデックスの配列を返す.
     * 項の型とは関係なく型が決定される場合は空の配列を返す.
     * @return 演算結果の型を決定される際に考慮される項のインデックスの配列
     */
    public int[] getTypeSpecifiedTermIndexes() {
        return this.typeSpecifiedTermIndexes;
    }

    /**
     * 左辺値への代入があるかどうかを表す
     */
    private final boolean leftIsAssignmentee;

    /**
     * 左辺値が参照として利用されるかどうかを表す
     */
    private final boolean leftIsReferencee;

    /**
     * この演算子が取り扱う項の数
     */
    private final int termCount;

    /**
     * 演算子
     */
    private final OPERATOR_TYPE operator;

    /**
     * 演算結果の型を決定される際に考慮される項のインデックスの配列
     */
    private final int[] typeSpecifiedTermIndexes;
}
