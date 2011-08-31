package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ASTノードの種類を表現するクラス.
 * MASUのAST解析部で汎用的に利用される.
 * 
 * @author kou-tngt
 *
 */
public interface AstToken {

    /**
     * トークンがアクセス修飾子を表すかどうかを返す.
     * @return アクセス修飾子を表すトークンならtrue
     */
    public boolean isAccessModifier();

    /**
     * トークンが代入演算子を表すかどうかを返す.
     * @return 代入演算子を表すトークンならtrue
     */
    public boolean isAssignmentOperator();

    /**
     * 配列記述子（[]）を表すかどうかを返す.
     * @return 配列記述子を表すトークンならtrue
     */
    public boolean isArrayDeclarator();

    /**
     * 配列の初期化部を表すかどうかを返す．
     * @return 配列の初期化部を表すトークンならtrue
     */
    public boolean isArrayInitilizer();

    /**
     * トークンがブロックを表すかどうかを返す.
     * @return ブロックを表すトークンならtrue
     */
    public boolean isBlock();

    /**
     * トークンがブロックの宣言を表すかどうかを返す．
     * @return ブロックの宣言を表すトークンならtrue
     */
    public boolean isBlockDefinition();

    /**
     * トークンが特殊なブロックを表すかどうか返す．
     * @return 特殊なブロックを表すトークンならtrue
     */
    public boolean isBlockName();

    /**
     * トークンが組み込み型であるかどうかを返す．
     * @return 組み込み型ならtrue
     */
    public boolean isBuiltinType();

    /**
     * トークンがクラス定義部を表すかどうかを返す.
     * @return クラス定義部を表すトークンならtrue
     */
    public boolean isClassDefinition();

    /**
     * トークンがenum定義部を表すかどうかを返す.
     * @return enum定義部を表すトークンならtrue
     */
    public boolean isEnumDefinition();

    /**
     * expression列の開始部ならtrue
     * Enumのコンストラクタを処理するために仕様
     * @return
     */
    public boolean isExpressionList();

    /**
     * トークンがクラスブロックを表すかどうかを返す.
     * クラスブロックは通常のブロックとは区別されなければならない.
     * @return クラスブロックを表すトークンならtrue
     */
    public boolean isClassBlock();

    /**
     * トークンがクラスのインポートを表すかどうかを返す．
     * @return クラスのインポートを表すトークンならtrue
     */
    public boolean isClassImport();

    /**
     * トークンが条件文の条件節を表すかどうか返す．
     * @return 条件節定義部を表すトークンならtrue
     */
    public boolean isConditionalClause();

    /**
     * トークンが定数を表すかどうかを返す．
     * @return 定数を表すトークンならtrue
     */
    public boolean isConstant();

    /**
     * トークンがコンストラクタ定義部を表すかどうかを返す.
     * @return コンストラクタ定義部を表すトークンならtrue
     */
    public boolean isConstructorDefinition();

    /**
     * トークンが継承記述部を表すかどうかを返す.
     * @return 継承記述部を表すトークンならtrue
     */
    public boolean isInheritanceDescription();

    /**
     * トークンがインターフェース実装記述部を表すかどうかを返す.
     * @return インターフェース実装記述部を表すトークンならtrue
     */
    public boolean isImplementsDescription();

    public boolean isEnumConstant();

    /**
     * トークンが式を表すかどうかを返す.
     * @return 式を表すトークンならtrue
     */
    public boolean isExpression();

    /**
     * トークンが括弧式を表すかどうかを返す
     * @return 括弧式を表すならtrue
     */
    public boolean isParenthesesExpression();

    /**
     * トークンが式文を表すかどうかを返す．
     * @return 式文を表すトークンならtrue
     */
    public boolean isExpressionStatement();

    /**
     * トークンがasset文を表すかどうか返す．
     * @return assert文を表すトークンならtrue
     */
    public boolean isAssertStatement();

    /**
     * トークンがラベル付き文を表すどうかを返す.
     * @return ラベル付き文を表すトークンならtrue
     */
    public boolean isLabeledStatement();

    /**
     * トークンが文のリストを表すかどうか返す
     * @return 文のリストを表すトークンならtrue
     */
    public boolean isSList();

    /**
     * トークンがフィールド定義部を表すかどうかを返す.
     * @return フィールド定義部を表すトークンならtrue
     */
    public boolean isFieldDefinition();

    /**
     * トークンが識別子を表すかどうかを返す.
     * @return フィールド識別子を表すトークンならtrue
     */
    public boolean isIdentifier();

    /**
     * トークンがnew文を表すかどうかを返す.
     * @return new文を表すトークンならtrue
     */
    public boolean isInstantiation();

    /**
     * トークンがfor文やcatch節などの冒頭で定義される変数の定義部を表すかどうかを返す.
     * @return for文やcatch節などの冒頭で定義される変数の定義部であればtrue
     */
    public boolean isLocalParameterDefinition();

    /**
     * トークンがローカル変数定義部を表すかどうかを返す.
     * @return ローカル変数定義部であればtrue
     */
    public boolean isLocalVariableDefinition();

    /**
     * トークンがメソッド定義部を表すかどうかを返す.
     * @return メソッド定義部であればtrue
     */
    public boolean isMethodDefinition();

    /**
     * トークンが static initializer の定義部を表すかどうかを返す．
     * @return static initializer の定義部であればtrue
     */
    public boolean isStaticInitializerDefinition();

    /**
     * トークンが instance initializer の定義部を表すかどうかを返す
     * @return instance initializer の定義部であれば true
     */
    public boolean isInstanceInitializerDefinition();

    /**
     * トークンがthis()による自クラスのコンストラクタ呼び出しを表すかどうかを返す.
     * @return メソッド呼び出しを表すトークンならtrue
     */
    public boolean isThisConstructorCall();

    /**
     * トークンがsuper()による親クラスのコンストラクタ呼び出しを表すかどうかを返す.
     * @return メソッド呼び出しを表すトークンならtrue
     */
    public boolean isSuperConstructorCall();

    /**
     * トークンがメソッド呼び出しを表すかどうかを返す.
     * @return メソッド呼び出しを表すトークンならtrue
     */
    public boolean isMethodCall();

    /**
     * トークンがメソッドパラメータ定義を表すかどうかを返す.
     * @return メソッドパラメータの定義を表すトークンならtrue
     */
    public boolean isMethodParameterDefinition();

    /**
     * トークンが可変長パラメータ定義を表すかどうかを返す．
     * @return 可変長パラメータ定義を表すトークンならtrue
     */
    public boolean isVariableParameterDefinition();

    /**
     * トークンがメンバーのインポートを表すかどうかを返す．
     * @return メンバーのインポートを表すトークンならtrue
     */
    public boolean isMemberImport();

    /**
     * トークンが修飾子を表すかどうかを返す.
     * @return 修飾子を表すトークンならtrue
     */
    public boolean isModifier();

    /**
     * トークンが修飾子定義部を表すかどうかを返す.
     * @return 修飾子定義部を表すトークンならtrue
     */
    public boolean isModifiersDefinition();

    /**
     * トークンが何らかの定義部内の名前記述部を表すかどうかを返す.
     * @return 名前記述部を表すトークンならtrue
     */
    public boolean isNameDescription();

    /**
     * トークンが名前空間定義部を表すかどうかを返す.
     * @return 名前空間定義部を表すトークンならtrue
     */
    public boolean isNameSpaceDefinition();

    /**
     * トークンが名前空間の使用宣言部を表すかどうかを返す.
     * @return 名前空間の使用宣言部を表すトークンならtrue
     */
    public boolean isNameUsingDefinition();

    /**
     * トークンが識別子の区切りに使われるものかどうかを返す.
     * @return 識別子の区切りに使われるトークンならtrue
     */
    public boolean isNameSeparator();

    /**
     * トークンが演算子を表すかどうかを返す.
     * @return 演算子を表すトークンならtrue
     */
    public boolean isOperator();

    /**
     * トークンが基本型を表すかどうかを返す.
     * @return 基本型を表すトークンならtrue
     */
    public boolean isPrimitiveType();

    /**
     * トークンがプロパティの定義部を表すかどうか返す．
     * @return プロパティを表すトークンならtrue
     */
    public boolean isPropertyDefinition();

    /**
     * トークンがプロパティのget部の表すかどうか返す．
     * @return プロパティのget部を表すトークンならtrue
     */
    public boolean isPropertyGetBody();

    /**
     * トークンがプロパティのset部を表すかどうか返す．
     * @return プロパティがset部を表すトークンならtrue
     */
    public boolean isPropertySetBody();

    /**
     * トークンが型引数記述部を表すかどうかを返す．
     * @return 型引数記述部を表すならtrue
     */
    public boolean isTypeArgument();

    /**
     * トークンが型引数記述部の列を表すかどうかを返す．
     * @return 型引数記述部の列を表すならtrue
     */
    public boolean isTypeArguments();

    /**
     * トークンが型記述部を表すかどうかを返す.
     * @return 型記述部を表すトークンならtrue
     */
    public boolean isTypeDescription();

    /**
     * トークンが型上限の制約記述部であるかどうかを返す．
     * @return 型上限の制約記述部であればtrue
     */
    public boolean isTypeUpperBoundsDescription();

    /**
     * トークンが型パラメータを表すかどうかを返す．
     * @return 型パラメータを表すトークンならtrue
     */
    public boolean isTypeParameterDefinition();

    /**
     * トークンが型下限の制約記述部であるかどうかを返す．
     * @return 型下限の制約記述部であればtrue
     */
    public boolean isTypeLowerBoundsDescription();

    /**
     * トークンが型変数宣言時の複数上限記述部であるかどうかを返す
     * @return 複数上限記述部であればtrue
     */
    public boolean isTypeAdditionalBoundsDescription();

    /**
     * トークンがワイルドカード型引数を表すかどうかを返す．
     * @return ワイルドカード型引数を表すならtrue
     */
    public boolean isTypeWildcard();

    /**
     * トークンがvoid型を表すかどうかを返す.
     * @return void型を表すトークンならtrue
     */
    public boolean isVoidType();

    /**
     * トークンがifを表すかどうかを返す
     * @return ifを表すトークンならtrue
     */
    public boolean isIf();

    /**
     * トークンがelseを表すかどうかを返す
     * @return elseを表すトークンならtrue
     */
    public boolean isElse();

    /**
     * トークンがwhileを表すかどうかを返す
     * @return whileを表すトークンならtrue
     */
    public boolean isWhile();

    /**
     * トークンがdoを表すかどうかを返す
     * @return doを表すトークンならtrue
     */
    public boolean isDo();

    /**
     * トークンがforを表すかどうかを返す
     * @return forを表すトークンならtrue
     */
    public boolean isFor();

    /**
     * トークンがforeachを表すかどうかを返す
     * @return foreachを表すトークンならtrue
     */
    public boolean isForeach();

    /**
     * トークンがfor文の初期節を表すかどうか返す
     * @return for文の初期節ならtrue
     */
    public boolean isForInit();

    /**
     * トークンがfor文の繰り返し節を表すかどうか返す
     * @return for文の繰り返し節ならtrue
     */
    public boolean isForIterator();

    /**
     * トークンがforeach文の節を表すかどうか返す
     * @return foreach文の節ならtrue
     */
    public boolean isForeachClause();

    /**
     * トークンがforeach文の変数を表すかどうか返す
     * @return foreach文の変数ならtrue
     */
    public boolean isForeachVariable();

    /**
     * トークンがforeach文の式を表すかどうか返す
     * @return foreach文の式ならtrue
     */
    public boolean isForeachExpression();

    /**
     * トークンがtryを表すかどうかを返す
     * @return tryを表すトークンならtrue
     */
    public boolean isTry();

    /**
     * トークンがcatchを表すかどうかを返す
     * @return catchを表すトークンならtrue
     */
    public boolean isCatch();

    /**
     * トークンがfinallyを表すかどうかを返す
     * @return finallyを表すトークンならtrue
     */
    public boolean isFinally();

    /**
     * トークンがsynchronizedを表すかどうかを返す
     * @return synchronizedを表すトークンならtrue
     */
    public boolean isSynchronized();

    /**
     * トークンがswitchを表すかどうかを返す
     * @return switchを表すトークンならtrue
     */
    public boolean isSwitch();

    /**
     * このトークンがcaseグループ(caseやdefaultエントリ)の定義を表すかどうか返す
     * @return caseグループの定義を表すならtrue
     */
    public boolean isCaseGroupDefinition();

    /**
     * トークンがSwitch文のエントリの宣言を表すかどうか返す．
     * @return Switch文のエントリの宣言をならtrue
     */
    public boolean isEntryDefinition();

    /**
     * トークンがcaseを表すかどうかを返す
     * @return caseを表すトークンならtrue
     */
    public boolean isCase();

    /**
     * トークンがdefaultを表すかどうかを返す
     * @return defaultを表すトークンならtrue
     */
    public boolean isDefault();

    /**
     * トークンがbreak文をあらわすかどうか返す
     * @return break文を表すトークンならtrue
     */
    public boolean isJump();

    /**
     * トークンがreturn文を表すかどうか返す
     * @return return文を表すトークンならtrue
     */
    public boolean isReturn();

    /**トークンがthurows文を表すかどうか返す
     * @return throws文を表すトークンならtrue
     */
    public boolean isThrows();

    /**
     * トークンがthrow文を表すかどうか返す
     * @return throw文を表すトークンならtrue
     */
    public boolean isThrow();

    /**
     * トークンが文を表すかどうか返す
     * @return 文を表すトークンならtrue
     */
    public boolean isStatement();

    /**
     * トークンがアノテーション列を表すかどうか返す
     * @return アノテーション列を表すトークンならtrue
     */
    public boolean isAnnotations();

    /**
     * トークンがアノテーションを表すかどうか返す
     * @return アノテーションを表すトークンならtrue
     */
    public boolean isAnnotation();

    /**
     * トークンがアノテーションに渡す引数(省略形式)かどうか返す
     * @return アノテーションに渡す引数(省略形式)トークンならtrue
     */
    public boolean isAnnotationMember();

    /**
     * トークンがアノテーションに渡す引数列かどうか返す
     * @return アノテーションに渡す引数列トークンならtrue
     */
    public boolean isAnnotationMemberValuePair();

    /**
     * トークンがアノテーションに渡す配列かどうか返す
     * @return アノテーションに渡す配列トークンならtrue
     */
    public boolean isAnnotationArrayInit();

    /**
     * トークンがアノテーション引数(文字列として認識)かどうか返す
     */
    public boolean isAnnotationString();

}
