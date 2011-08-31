package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 何からの要素が記述されている箇所を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class DescriptionToken extends AstTokenAdapter {

    /**
     * 条件文の条件節を表す定数インスタンス
     */
    public static final DescriptionToken CONDITIONAL_CLAUSE = new DescriptionToken(
            "CONDITIONAL_CLAUSE") {
        @Override
        public boolean isConditionalClause() {
            return true;
        }
    };

    /**
     * for分の初期化節を表す定数インスタンス
     */
    public static final DescriptionToken FOR_INIT = new DescriptionToken("FOR_INIT") {
        @Override
        public boolean isForInit() {
            return true;
        }
    };

    /**
     * for文の繰り返し節を表すインスタンス
     */
    public static final DescriptionToken FOR_ITERATOR = new DescriptionToken("FOR_ITERATOR") {
        @Override
        public boolean isForIterator() {
            return true;
        };
    };

    /**
     * foreach文の節を表すインスタンス
     */
    public static final DescriptionToken FOREACH_CLAUSE = new DescriptionToken("FOREACH_CLAUSE") {
        @Override
        public boolean isForeachClause() {
            return true;
        }
    };

    /**
     * foreach文の変数を表すインスタンス
     */
    public static final DescriptionToken FOREACH_VARIABLE = new DescriptionToken("FOREACH_VARIABLE") {
        @Override
        public boolean isForeachVariable() {
            return true;
        }
    };

    /**
     * foreach文の式を表すインスタンス
     */
    public static final DescriptionToken FOREACH_EXPRESSION = new DescriptionToken(
            "FOREACH_VARIABLE") {
        @Override
        public boolean isForeachExpression() {
            return true;
        }
    };

    /**
     * 式記述部を表す定数インスタンス.
     */
    public static final DescriptionToken EXPRESSION = new DescriptionToken("EXPRESSION") {
        @Override
        public boolean isExpression() {
            return true;
        }
    };

    /**
     * 括弧式を表す定数インスタンス
     */
    public static final DescriptionToken PAREN_EXPR = new DescriptionToken("PAREN_EXPR") {
        @Override
        public boolean isParenthesesExpression() {
            return true;
        }
    };

    /**
     * 式文記述部を表す定数インスタンス.
     */
    public static final DescriptionToken EXPRESSION_STATEMENT = new DescriptionToken(
            "EXPRESSION_STATEMENT") {
        @Override
        public boolean isExpressionStatement() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * ラベル付き文記述部を表す定数インスタンス.
     */
    public static final DescriptionToken LABELED_STATEMENT = new DescriptionToken(
            "LABELED_STATEMENT") {
        @Override
        public boolean isLabeledStatement() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * 親クラス記述部を表す定数インスタンス.
     */
    public static final DescriptionToken INHERITANCE = new DescriptionToken("INHERITANCE") {
        @Override
        public boolean isInheritanceDescription() {
            return true;
        }
    };

    /**
     * 親クラス記述部を表す定数インスタンス.
     */
    public static final DescriptionToken IMPLEMENTS = new DescriptionToken("IMPLEMENTS") {
        @Override
        public boolean isImplementsDescription() {
            return true;
        }
    };

    /**
     * 修飾子記述部を表す定数インスタンス.
     */
    public static final DescriptionToken MODIFIER = new DescriptionToken("MODIFIER") {
        @Override
        public boolean isModifiersDefinition() {
            return true;
        }
    };

    /**
     * 名前記述部を表す定数インスタンス.
     */
    public static final DescriptionToken NAME = new DescriptionToken("NAME") {
        @Override
        public boolean isNameDescription() {
            return true;
        }
    };

    /**
     * 型記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE = new DescriptionToken("TYPE") {
        @Override
        public boolean isTypeDescription() {
            return true;
        }
    };

    /**
     * 型下限記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE_LOWER_BOUNDS = new DescriptionToken(
            "TYPE_LOWER_BOUNDS") {
        @Override
        public boolean isTypeLowerBoundsDescription() {
            return true;
        }
    };

    /**
     * 型上限記述部を表す定数インスタンス.
     */
    public static final DescriptionToken TYPE_UPPER_BOUNDS = new DescriptionToken(
            "TYPE_UPPER_BOUNDS") {
        @Override
        public boolean isTypeUpperBoundsDescription() {
            return true;
        }
    };
    
    public static final DescriptionToken TYPE_ADDITIONAL_BOUNDS = new DescriptionToken("TYPE_ADDITIONAL_BOUNDS"){
        @Override
        public boolean isTypeAdditionalBoundsDescription(){
            return true;
        }
    };

    /**
     * 型引数記述部を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_ARGUMENT = new DescriptionToken("TYPE_ARGUMENT") {
        @Override
        public boolean isTypeArgument() {
            return true;
        }
    };

    /**
     * 型引数記述部の列を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_ARGUMENTS = new DescriptionToken("TYPE_ARGUMENTS") {
        @Override
        public boolean isTypeArguments() {
            return true;
        }
    };

    /**
     * ワイルドカード型引数を表す定数インスタンス
     */
    public static final DescriptionToken TYPE_WILDCARD = new DescriptionToken("TYPE_WILDCARD") {
        @Override
        public boolean isTypeWildcard() {
            return true;
        }
    };

    /**
     * 名前空間利用宣言記述部を表す定数インスタンス.
     */
    public static final DescriptionToken USING = new DescriptionToken("USING") {
        @Override
        public boolean isNameUsingDefinition() {
            return true;
        }
    };

    public static final DescriptionToken SLIST = new DescriptionToken("SLIST") {
        @Override
        public boolean isSList() {
            return true;
        }
    };

    /**
     * 指定された文字列で初期化するコンストラクタ.
     * @param text このトークンを表す文字列.
     */
    public DescriptionToken(final String text) {
        super(text);
    }
}
