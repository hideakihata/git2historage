package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 一項演算の内容を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedMonominalOperationInfo extends
        UnresolvedExpressionInfo<MonominalOperationInfo> {

    /**
     * 項と一項演算の結果の型を与えて初期化
     * 
     * @param operand 項
     * @param operator 一項演算の演算子
     */
    public UnresolvedMonominalOperationInfo(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> operand,
            final OPERATOR operator) {

        if (null == operand || null == operator) {
            throw new IllegalArgumentException("term or type is null");
        }

        this.operand = operand;
        this.operator = operator;
    }

    @Override
    public MonominalOperationInfo resolve(final TargetClassInfo usingClass,
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

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedExpressionInfo<?> unresolvedTerm = this.getOperand();
        final ExpressionInfo term = unresolvedTerm.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean isPreposed = this.isPreposed();

        this.resolvedInfo = new MonominalOperationInfo(term, this.operator, isPreposed,
                usingMethod, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * 一項演算の項を返す
     * 
     * @return 一項演算の項
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getOperand() {
        return this.operand;
    }

    public OPERATOR getOperator() {
        return this.operator;
    }

    public boolean isPreposed() {
        return this.getFromColumn() < this.operand.getFromColumn() ? true : false;
    }

    /**
     * 一項演算の項
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> operand;

    /**
     * 一項演算の演算子
     */
    private final OPERATOR operator;

}
