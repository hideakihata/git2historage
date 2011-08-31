package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * 文法情報を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class SyntaxToken extends AstTokenAdapter {

    /**
     * 名前区切りを表す定数インスタンス
     */
    public static final SyntaxToken NAME_SEPARATOR = new SyntaxToken("NAME_SEPARATOR") {
        @Override
        public boolean isNameSeparator() {
            return true;
        }
    };

    /**
     * クラスブロック以外のブロックの開始を表す定数インスタンス
     */
    public static final SyntaxToken BLOCK_START = new SyntaxToken("BLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }
    };

    /**
     * クラスブロックの開始を表す定数インスタンス
     */
    public static final SyntaxToken CLASSBLOCK_START = new SyntaxToken("CLASSBLOCK_START") {
        @Override
        public boolean isBlock() {
            return true;
        }

        @Override
        public boolean isClassBlock() {
            return true;
        }
    };

    /**
     * メソッド呼び出し文を表す定数インスタンス
     */
    public static final SyntaxToken METHOD_CALL = new SyntaxToken("METHOD_CALL") {
        @Override
        public boolean isMethodCall() {
            return true;
        }
    };

    /**
     * new文を表す定数インスタンス
     */
    public static final SyntaxToken NEW = new SyntaxToken("NEW") {
        @Override
        public boolean isInstantiation() {
            return true;
        }
    };

    /**
     * 配列宣言を表す定数インスタンス
     */
    public static final SyntaxToken ARRAY = new SyntaxToken("ARRAY") {
        @Override
        public boolean isArrayDeclarator() {
            return true;
        }
    };

    /**
     * break文を表す定数インスタンス
     */
    public static final SyntaxToken BREAK = new SyntaxToken("BREAK") {
        @Override
        public boolean isJump() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    public static final SyntaxToken CONTINUE = new SyntaxToken("CONTINUE") {
        @Override
        public boolean isJump() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * return文を表す定数インスタンス
     */
    public static final SyntaxToken RETURN = new SyntaxToken("RETURN") {
        @Override
        public boolean isReturn() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * throw文を表す定数インスタンス
     */
    public static final SyntaxToken THROW = new SyntaxToken("THROW") {
        @Override
        public boolean isThrow() {
            return true;
        }

        @Override
        public boolean isStatement() {
            return true;
        }
    };

    /**
     * assert文を表す定数インスタンス
     */
    public static final SyntaxToken ASSERT = new SyntaxToken("ASSERT") {

        @Override
        public boolean isAssertStatement() {
            return true;
        }
        
        @Override
        public boolean isStatement() {
            return true;
        }

    };

    /**
     * プロパティのget部を表す定数インスタンス
     */
    public static final SyntaxToken PROPERTY_GET_BODY = new SyntaxToken("PROPERTY_GET_BODY") {
        @Override
        public boolean isPropertyGetBody() {
            return true;
        };

        @Override
        public boolean isBlock() {
            return true;
        }

    };

    /**
     * プロパティのset部を表す定数インスタンス
     */
    public static final SyntaxToken PROPERTY_SET_BODY = new SyntaxToken("PROPERTY_SET_BODY") {
        @Override
        public boolean isPropertySetBody() {
            return true;
        };

        @Override
        public boolean isBlock() {
            return true;
        }
    };

    /**
     * 指定された文字列で表されるトークンを作成するコンストラクタ
     * @param text トークンを表す文字列
     */
    public SyntaxToken(final String text) {
        super(text);
    }
}
