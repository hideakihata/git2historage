package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import java.lang.Enum;


/**
 * Java特有の要素を表すトークンを定義するクラス
 * 
 * @author kou-tngt
 *
 */
public class JavaAstToken extends AstTokenAdapter {

    /**
     * import記述部を表す定数インスタンス
     */
    public static final JavaAstToken CLASS_IMPORT = new JavaAstToken("CLASS_IMPORT") {
        @Override
        public boolean isClassImport() {
            return true;
        }
    };

    /**
     * import記述部を表す定数インスタンス
     */
    public static final JavaAstToken MEMBER_IMPORT = new JavaAstToken("MEMBER_IMPORT") {
        @Override
        public boolean isMemberImport() {
            return true;
        }
    };

    /**
     * throw記述部を表す定数インスタンス
     */
    //TODO: 場所ここでいいのかな?
    public static final JavaAstToken THROWS = new JavaAstToken("THROWS") {
        @Override
        public boolean isThrows() {
            return true;
        }
    };

    /**
     * enum要素の記述部を表す定数インスタンス
     */
    public static final JavaAstToken ENUM_CONSTANT = new JavaAstToken("ENUM_CONST") {
        @Override
        public boolean isEnumConstant() {
            return true;
        }
    };

    public static final JavaAstToken EXPR_LIST = new JavaAstToken("ELIST") {
        @Override
        public boolean isExpressionList() {
            return true;
        }
    };

    /**
     * super記述しを表す定数インスタンス
     */
    public static final JavaAstToken SUPER = new JavaAstToken("SUPER");

    /**
     * 配列初期化部を表す定数インスタンス
     */
    public static final JavaAstToken ARRAY_INIT = new JavaAstToken("ARRAY_INIT") {
        @Override
        public boolean isArrayInitilizer() {
            return true;
        }
    };

    /**
     * new文による配列型指定（[]）を表す定数インスタンス
     */
    public static final JavaAstToken ARRAY_INSTANTIATION = new JavaAstToken("ARRAY_INSTANTIATION");

    /**
     * this()のような自コンストラクタの呼び出しを表す定数インスタンス
     */
    public static final JavaAstToken CONSTRUCTOR_CALL = new JavaAstToken("CONSTRUCTOR_CALL") {
        @Override
        public boolean isThisConstructorCall() {
            return true;
        }
    };

    /**
     * super()のような親コンストラクタの呼び出しを表す定数インスタンス
     */
    public static final JavaAstToken SUPER_CONSTRUCTOR_CALL = new JavaAstToken(
            "SUPER_CONSTRUCTOR_CALL") {
        @Override
        public boolean isSuperConstructorCall() {
            return true;
        }
    };

    /**
     * .classを記述を表す定数インスタンス
     */
    public static final JavaAstToken CLASS = new JavaAstToken("CLASS");

    /**
     * インタフェース定義部を表す定数インスタンス
     */
    public static final JavaAstToken INTERFACE_DEFINITION = new JavaAstToken("INTERFACE_DEFINITION") {
        @Override
        public boolean isClassDefinition() {
            return true;
        }

        @Override
        public boolean isBlockDefinition() {
            return true;
        }
    };

    /**
     * アノテーション列を表す定数インスタンス
     */
    public static final JavaAstToken ANNOTATIONS = new JavaAstToken("ANNOTATIONS") {
        @Override
        public boolean isAnnotations() {
            return true;
        }
    };

    /**
     * アノテーションを表す定数インスタンス
     */
    public static final JavaAstToken ANNOTATION = new JavaAstToken("ANNOTATION") {
        @Override
        public boolean isAnnotation() {
            return true;
        }
    };

    /**
     * アノテーションに渡す配列インスタンス
     */
    public static final JavaAstToken ANNOTATION_MEMBER = new JavaAstToken("ANNOTATION_MEMBER") {
        @Override
        public boolean isAnnotationMember() {
            return true;
        }
    };

    /**
     * アノテーションに渡す引数列インスタンス
     */
    public static final JavaAstToken ANNOTATION_MEMBER_VALUE_PAIR = new JavaAstToken(
            "ANNOTATION_MEMBER_VALUE_PAIR") {
        @Override
        public boolean isAnnotationMemberValuePair() {
            return true;
        }
    };

    /**
     * アノテーションに渡す配列インスタンス
     */
    public static final JavaAstToken ANNOTATION_ARRAY_INIT = new JavaAstToken(
            "ANNOTATION_ARRAY_INIT") {
        @Override
        public boolean isAnnotationArrayInit() {
            return true;
        }
    };

    /**
     * アノテーション引数(文字列形式)インスタンス
     */
    public static final JavaAstToken ANNOTATION_STRING = new JavaAstToken("ANNOTATION_STRING") {
        @Override
        public boolean isAnnotationString() {
            return true;
        }
    };

    /**
     * 指定された文字列を表すトークンを作成する.
     * @param text トークンの文字列
     */
    public JavaAstToken(final String text) {
        super(text);
    }

}
