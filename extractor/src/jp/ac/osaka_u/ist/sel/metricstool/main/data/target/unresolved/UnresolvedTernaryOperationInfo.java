package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 三項演算使用の未解決情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedTernaryOperationInfo extends UnresolvedExpressionInfo<TernaryOperationInfo> {

    /**
     * オブジェクトを初期化
     * 
     * @param condition 条件式
     * @param trueExpression trueの場合の式
     * @param falseExpression falseの場合の式
     */
    public UnresolvedTernaryOperationInfo(
            final UnresolvedConditionInfo<? extends ConditionInfo> condition,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> trueExpression,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> falseExpression) {
        super();
        if (null == condition || null == trueExpression || null == falseExpression) {
            throw new IllegalArgumentException();
        }

        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public TernaryOperationInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
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

        final ConditionInfo conditionalExpression = this.condition.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo trueExpression = this.trueExpression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo falseExpression = this.falseExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        /*// 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new TernaryOperationInfo(conditionalExpression, trueExpression,
                falseExpression, usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    public UnresolvedConditionInfo<? extends ConditionInfo> getCondition() {
        return this.condition;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getTrueExpression() {
        return this.trueExpression;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getFalseExpression() {
        return this.falseExpression;
    }

    /**
     * 三項演算の条件式(第一項)のみ解決情報を保存する変数
     */
    private final UnresolvedConditionInfo<? extends ConditionInfo> condition;

    /**
     * 三項演算の条件式がtrueのときに返される式(第二項)の未解決情報を保存する変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> trueExpression;

    /**
     * 三項演算の条件式がfalseのときに返される式(第三項)の未解決情報を保存する変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> falseExpression;

}
