package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決変数宣言文を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedVariableDeclarationStatementInfo extends
        UnresolvedSingleStatementInfo<VariableDeclarationStatementInfo> implements
        UnresolvedConditionInfo<VariableDeclarationStatementInfo> {

    /**
     * 宣言されている変数，（もしあれば）初期化の式を与えて，オブジェクトを初期化
     * 
     * @param variableDeclaration 宣言されている変数
     * @param initializationExpression （もしあれば）初期化の式
     */
    public UnresolvedVariableDeclarationStatementInfo(
            final UnresolvedLocalVariableUsageInfo variableDeclaration,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression) {

        super(variableDeclaration.getUsedVariable().getDefinitionUnit());

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }
        this.variableDeclaration = variableDeclaration;
        this.initializationExpression = initializationExpression;
        this.setOuterUnit(variableDeclaration.getOuterUnit());
    }

    @Override
    public VariableDeclarationStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedUnitInfo<? extends UnitInfo> unresolvedOuterUnit = this.getOuterUnit();
        final LocalSpaceInfo outerLocalSpace = (LocalSpaceInfo) unresolvedOuterUnit.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final LocalVariableUsageInfo variableDeclaration = this.variableDeclaration.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo initializationExpression = null != this.initializationExpression ? this.initializationExpression
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager)
                : null;
        this.resolvedInfo = new VariableDeclarationStatementInfo(outerLocalSpace, variableDeclaration,
                initializationExpression, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * 定義されている変数を返す
     * 
     * @return 定義されている変数
     */
    public final UnresolvedLocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * 宣言されている変数の初期化式を返す
     * 
     * @return 宣言されている変数の初期化式．初期化されてい場合はnull
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * 宣言されている変数が初期化されているかどうかを返す
     * 
     * @return 宣言されている変数が初期化されていればtrue
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {
        final UnresolvedLocalSpaceInfo<?> outerUnit = (UnresolvedLocalSpaceInfo<?>) this
                .getOuterUnit();
        return outerUnit instanceof UnresolvedCallableUnitInfo<?> ? (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outerUnit
                : outerUnit.getOuterCallableUnit();
    }

    @Override
    public UnresolvedClassInfo getOuterClass() {
        return this.getOuterCallableUnit().getOuterClass();
    }

    /**
     * 宣言されている変数を表すフィールド
     */
    private final UnresolvedLocalVariableUsageInfo variableDeclaration;

    /**
     * 宣言されている変数の初期化式を表すフィールド
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression;
}
