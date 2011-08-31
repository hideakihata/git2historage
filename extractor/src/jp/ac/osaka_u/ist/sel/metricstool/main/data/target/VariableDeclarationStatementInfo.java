package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 変数宣言文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class VariableDeclarationStatementInfo extends SingleStatementInfo implements ConditionInfo {

    /**
     * 宣言されている変数，初期化式，位置情報を与えて初期化
     * 宣言されている変数が初期化されている場合，このコンストラクタを使用する
     * 
     * @param variableDeclaration 宣言されているローカル変数
     * @param initializationExpression 初期化式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public VariableDeclarationStatementInfo(final LocalSpaceInfo ownerSpace,
            final LocalVariableUsageInfo variableDeclaration,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.variableDeclaration = variableDeclaration;
        this.variableDeclaration.setOwnerExecutableElement(this);
        this.variableDeclaration.getUsedVariable().setDeclarationStatement(this);

        if (null != initializationExpression) {
            this.initializationExpression = initializationExpression;
        } else {

            // ownerSpaceInfoがメソッドまたはコンストラクタの時
            if (ownerSpace instanceof CallableUnitInfo) {
                this.initializationExpression = new EmptyExpressionInfo(
                        (CallableUnitInfo) ownerSpace, toLine, toColumn - 1, toLine, toColumn - 1);
            }

            // ownerSpaceInfoがブロック文の時
            else if (ownerSpace instanceof BlockInfo) {
                final CallableUnitInfo ownerMethod = ((BlockInfo) ownerSpace).getOwnerMethod();
                this.initializationExpression = new EmptyExpressionInfo(ownerMethod, toLine,
                        toColumn - 1, toLine, toColumn - 1);
            }

            // それ以外の時はエラー
            else {
                throw new IllegalStateException();
            }
        }

        this.initializationExpression.setOwnerExecutableElement(this);
        this.ownerConditionalBlock = null;
    }

    /**
     * この宣言文で宣言されている変数を返す
     * 
     * @return この宣言文で宣言されている変数
     */
    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * 宣言時の変数使用を返す
     * @return 宣言時の変数使用
     */
    public final LocalVariableUsageInfo getDeclaration() {
        return this.variableDeclaration;
    }

    /**
     * 宣言されている変数の初期化式を返す
     * 
     * @return 宣言されている変数の初期化式．初期化されてい場合はnull
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * 宣言されている変数が初期化されているかどうかを返す
     * 
     * @return 宣言されている変数が初期化されていればtrue
     */
    public boolean isInitialized() {
        return !(this.initializationExpression instanceof EmptyExpressionInfo);
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        usages.add(this.variableDeclaration);
        if (this.isInitialized()) {
            usages.addAll(this.getInitializationExpression().getVariableUsages());
        }

        return Collections.unmodifiableSet(usages);
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this.getDeclaredLocalVariable());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return this.isInitialized() ? this.getInitializationExpression().getCalls()
                : CallInfo.EmptySet;
    }

    /**
     * この変数宣言文のテキスト表現（String型）を返す
     * 
     * @return この変数宣言文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final LocalVariableInfo variable = this.getDeclaredLocalVariable();
        final TypeInfo type = variable.getType();
        sb.append(type.getTypeName());

        sb.append(" ");

        sb.append(variable.getName());

        if (this.isInitialized()) {

            sb.append(" = ");
            final ExpressionInfo expression = this.getInitializationExpression();
            sb.append(expression.getText());
        }

        sb.append(";");

        return sb.toString();
    }

    @Override
    public String toString() {
        return this.getText() + "// (" + this.getFromLine() + ": " + this.getFromColumn() + ", "
                + this.getToLine() + ": " + this.getToColumn() + ")";
    }

    /**
     * 宣言されている変数の型を返す
     * @return 宣言されている変数の型
     */
    public TypeInfo getType() {
        return this.variableDeclaration.getType();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        if (this.isInitialized()) {
            thrownExpressions.addAll(this.getInitializationExpression().getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    /**
     * この式を条件として持つConditionalBlockInfo返す
     */
    @Override
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerConditionalBlock;
    }

    /**
     * この式を条件として持つConditionalBlockInfoを設定する
     * 引数がnullであることを許容する.
     */
    @Override
    public void setOwnerConditionalBlock(final ConditionalBlockInfo ownerConditionalBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        assert null == this.ownerConditionalBlock : "this.ownerConditionalBlock must be null!";
        this.ownerConditionalBlock = ownerConditionalBlock;
    }

    @Override
    public ExecutableElementInfo copy() {
        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final LocalVariableUsageInfo variableDeclaration = (LocalVariableUsageInfo) this
                .getDeclaration().copy();
        final ExpressionInfo initializerExpression = null != this.getInitializationExpression() ? (ExpressionInfo) this
                .getInitializationExpression().copy()
                : null;
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final VariableDeclarationStatementInfo newVariableDeclaration = new VariableDeclarationStatementInfo(
                outerUnit, variableDeclaration, initializerExpression, fromLine, fromColumn,
                toLine, toColumn);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        newVariableDeclaration.setOwnerConditionalBlock(ownerConditionalBlock);

        return newVariableDeclaration;
    }

    /**
     * 宣言されている変数を表すフィールド
     */
    private final LocalVariableUsageInfo variableDeclaration;

    /**
     * 宣言されている変数の初期化式を表すフィールド
     */
    private final ExpressionInfo initializationExpression;

    /**
     * この変数宣言文を条件として持つConditionalBlockInfoを保存するためのフィールド
     */
    private ConditionalBlockInfo ownerConditionalBlock;
}
