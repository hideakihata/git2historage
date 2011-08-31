package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決return文情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedReturnStatementInfo extends
        UnresolvedSingleStatementInfo<ReturnStatementInfo> {

    /**
     * 外側のブロックを与えてオブジェクトを生成
     * 
     * @param outerLocalSpace 外側のブロック
     */
    public UnresolvedReturnStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace) {
        this(outerLocalSpace, 0, 0, 0, 0);
    }

    public UnresolvedReturnStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(outerLocalSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public ReturnStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
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

        final LocalSpaceInfo ownerSpace = this.getOuterLocalSpace().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        
        assert this.getReturnedExpression() != null: "return expression must not be null";
        
        final ExpressionInfo returnedExpression = this.returnedExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ReturnStatementInfo(ownerSpace, returnedExpression, fromLine,
                fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getReturnedExpression() {
        return this.returnedExpression;
    }

    /**
     * 返す式をセットする
     * 
     * @param returnedExpression 返す式
     */
    public void setReturnedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.returnedExpression = returnedExpression;
    }

    /**
     * return文の戻り値を表す式の未解決情報を保存する変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression;

}
