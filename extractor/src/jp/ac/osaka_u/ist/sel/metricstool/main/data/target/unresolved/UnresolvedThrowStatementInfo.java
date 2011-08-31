package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決throw文情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedThrowStatementInfo extends UnresolvedSingleStatementInfo<ThrowStatementInfo> {

    /**
     * 外側のスコープを与えてオブジェクトを初期化
     * 
     * @param outerLocalSpace 外側のスコープ
     */
    public UnresolvedThrowStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace) {
        super(outerLocalSpace);
    }

    @Override
    public ThrowStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

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

        final LocalSpaceInfo outerLocalSpace = this.getOuterLocalSpace().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final ExpressionInfo thrownExpression = this.thrownExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ThrowStatementInfo(outerLocalSpace, thrownExpression, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getThrownExpression() {
        return this.thrownExpression;
    }

    /**
     * throw文によって投げられる例外の未解決情報を保存する
     * @param thrownExpression throw文によって投げられる例外の未解決情報
     */
    public final void setThrownExpresasion(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> thrownExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == thrownExpression) {
            throw new IllegalArgumentException("thronExpression is null");
        }

        this.thrownExpression = thrownExpression;
    }

    /**
     * throw文によって投げられる例外の未解決情報を保存する変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> thrownExpression;

}
