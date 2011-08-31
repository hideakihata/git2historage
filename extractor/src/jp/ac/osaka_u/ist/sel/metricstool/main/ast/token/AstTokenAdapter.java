package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * {@link AstToken}のアダプタクラス.
 * AstTokenで宣言されている全てのメソッドについて，falseを返すだけのデフォルト実装を持つ.
 * 
 * @author kou-tngt
 *
 */
public class AstTokenAdapter implements AstToken {

    /**
     * 指定された文字列情報を持つトークンを作成するコンストラクタ.
     * @param text
     * @throws NullPointerException textがnullの場合
     * @throws IllegalArgumentException textが空文字列の場合
     */
    public AstTokenAdapter(final String text) {
        if (null == text) {
            throw new NullPointerException("text is null");
        }

        if (text.length() == 0) {
            throw new IllegalArgumentException("text must not be empty string.");
        }
        this.text = text;
    }

    public boolean isAccessModifier() {
        return false;
    }

    public boolean isArrayDeclarator() {
        return false;
    }

    public boolean isArrayInitilizer() {
        return false;
    }

    public boolean isAssignmentOperator() {
        return false;
    }

    public boolean isBlock() {
        return false;
    }

    public boolean isBlockDefinition() {
        return false;
    }

    public boolean isBlockName() {
        return false;
    }

    public boolean isBuiltinType() {
        return isPrimitiveType() || isVoidType();
    }

    public boolean isConditionalClause() {
        return false;
    }

    public boolean isClassBlock() {
        return false;
    }

    public boolean isClassDefinition() {
        return false;
    }

    public boolean isEnumDefinition() {
        return false;
    }

    @Override
    public boolean isExpressionList() {
        return false;
    }

    public boolean isClassImport() {
        return false;
    }

    public boolean isConstant() {
        return false;
    }

    public boolean isConstructorDefinition() {
        return false;
    }

    public boolean isExpression() {
        return false;
    }

    public boolean isParenthesesExpression() {
        return false;
    }

    public boolean isEnumConstant() {
        return false;
    }

    public boolean isExpressionStatement() {
        return false;
    }

    @Override
    public boolean isAssertStatement() {
        return false;
    }

    public boolean isLabeledStatement() {
        return false;
    }

    public boolean isSList() {
        return false;
    }

    public boolean isInheritanceDescription() {
        return false;
    }

    public boolean isImplementsDescription() {
        return false;
    }

    public boolean isFieldDefinition() {
        return false;
    }

    public boolean isIdentifier() {
        return false;
    }

    public boolean isInstantiation() {
        return false;
    }

    public boolean isLocalParameterDefinition() {
        return false;
    }

    public boolean isLocalVariableDefinition() {
        return false;
    }

    public boolean isMemberImport() {
        return false;
    }

    public boolean isMethodCall() {
        return false;
    }

    public boolean isThisConstructorCall() {
        return false;
    }

    public boolean isSuperConstructorCall() {
        return false;

    }

    public boolean isMethodDefinition() {
        return false;
    }

    public boolean isStaticInitializerDefinition() {
        return false;
    }

    public boolean isInstanceInitializerDefinition() {
        return false;
    }

    public boolean isMethodParameterDefinition() {
        return false;
    }

    public boolean isVariableParameterDefinition() {
        return false;
    }

    public boolean isModifier() {
        return this.isAccessModifier();
    }

    public boolean isModifiersDefinition() {
        return false;
    }

    public boolean isNameDescription() {
        return false;
    }

    public boolean isNameSeparator() {
        return false;
    }

    public boolean isNameSpaceDefinition() {
        return false;
    }

    public boolean isNameUsingDefinition() {
        return false;
    }

    public boolean isOperator() {
        return false;
    }

    public boolean isPrimitiveType() {
        return false;
    }

    public boolean isPropertyDefinition() {
        return false;
    }

    public boolean isPropertyGetBody() {
        return false;
    }

    public boolean isPropertySetBody() {
        return false;
    }

    public boolean isTypeArguments() {
        return false;
    }

    public boolean isTypeArgument() {
        return false;
    }

    public boolean isTypeDescription() {
        return false;
    }

    public boolean isTypeLowerBoundsDescription() {
        return false;
    }

    public boolean isTypeParameterDefinition() {
        return false;
    }

    public boolean isTypeUpperBoundsDescription() {
        return false;
    }

    public boolean isTypeAdditionalBoundsDescription() {
        return false;
    }

    public boolean isTypeWildcard() {
        return false;
    }

    public boolean isVoidType() {
        return false;
    }

    public boolean isCaseGroupDefinition() {
        return false;
    }

    public boolean isEntryDefinition() {
        return false;
    }

    public boolean isCase() {
        return false;
    }

    public boolean isCatch() {
        return false;
    }

    public boolean isDefault() {
        return false;
    }

    public boolean isDo() {
        return false;
    }

    public boolean isElse() {
        return false;
    }

    public boolean isFinally() {
        return false;
    }

    public boolean isFor() {
        return false;
    }

    public boolean isForeach() {
        return false;
    }

    public boolean isForInit() {
        return false;
    }

    public boolean isForIterator() {
        return false;
    }

    public boolean isForeachClause() {
        return false;
    }

    public boolean isForeachVariable() {
        return false;
    }

    public boolean isForeachExpression() {
        return false;
    }

    public boolean isIf() {
        return false;
    }

    public boolean isSwitch() {
        return false;
    }

    public boolean isTry() {
        return false;
    }

    public boolean isWhile() {
        return false;
    }

    public boolean isSynchronized() {
        return false;
    }

    public boolean isJump() {
        return false;
    }

    public boolean isReturn() {
        return false;
    }

    @Override
    public boolean isThrows() {
        return false;
    }

    @Override
    public boolean isThrow() {
        return false;
    }

    @Override
    public boolean isStatement() {
        return false;
    }

    @Override
    public boolean isAnnotations() {
        return false;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public boolean isAnnotationArrayInit() {
        return false;
    }

    @Override
    public boolean isAnnotationMemberValuePair() {
        return false;
    }

    @Override
    public boolean isAnnotationMember() {
        return false;
    }

    @Override
    public boolean isAnnotationString() {
        return false;
    }

    @Override
    public String toString() {
        return this.text;
    }

    /**
     * このトークンの文字列
     */
    private final String text;

}
