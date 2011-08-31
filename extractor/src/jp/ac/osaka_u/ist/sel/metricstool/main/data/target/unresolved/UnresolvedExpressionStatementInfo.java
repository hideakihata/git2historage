package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 式文の未解決情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedExpressionStatementInfo extends
        UnresolvedSingleStatementInfo<ExpressionStatementInfo> {

    /**
     * 式文を構成する式の未解決情報を与えて初期化
     * @param outerLocalSpace 式分を直接所有する空間
     * @param expression 式文を構成する式の未解決情報
     */
    public UnresolvedExpressionStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> expression) {
        super(outerLocalSpace);

        if (null == expression) {
            throw new IllegalArgumentException("expression is null");
        }

        this.expression = expression;
    }

    @Override
    public ExpressionStatementInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo ownerSpace = this.getOuterLocalSpace().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final ExpressionInfo expression = this.expression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ExpressionStatementInfo(ownerSpace, expression, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getExpression() {
        return this.expression;
    }

    /**
     * 式文を構成する式の未解決情報を保存する変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> expression;

}
