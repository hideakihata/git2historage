package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * assert文の未解決情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedAssertStatementInfo extends
        UnresolvedSingleStatementInfo<AssertStatementInfo> {

    /**
     * 未解決アサート文を生成
     * 
     * @param outerLocalSpace 外側のブロック
     */
    public UnresolvedAssertStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace) {
        super(outerLocalSpace);
    }

    @Override
    public AssertStatementInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ローカルスペースを解決
        final UnresolvedLocalSpaceInfo<?> unresolvedOuterLocalSpace = this.getOuterLocalSpace();
        final LocalSpaceInfo outerLocalSpace = unresolvedOuterLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedAssertedExpression = this
                .getAssertedExpression();
        final ExpressionInfo assertedExpression = unresolvedAssertedExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final UnresolvedExpressionInfo<?> unresolvedMessageExpression = this.getMessageExpression();
        final ExpressionInfo messageExpression = null == unresolvedMessageExpression ? new EmptyExpressionInfo(
                usingMethod, toLine, toColumn, toLine, toColumn)
                : unresolvedMessageExpression.resolve(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new AssertStatementInfo(outerLocalSpace, assertedExpression,
                messageExpression, fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * 検証の結果がfalseであったときに出力されるメッセージを表す式の未解決情報を設定する
     * @param messageExpression
     */
    public final void setMessageExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == messageExpression) {
            throw new IllegalArgumentException();
        }
        this.messageExpression = messageExpression;
    }

    /**
     * メッセージを返す
     * 
     * @return　メッセージ
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getMessageExpression() {
        return this.messageExpression;
    }

    /**
     * 検証式の未解決情報を設定する
     * @param assertedExpression 検証式の未解決情報
     */
    public final void setAsserttedExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assertedExpression) {
            throw new IllegalArgumentException();
        }

        this.assertedExpression = assertedExpression;
    }

    /**
     * 検証式を返す
     * 
     * @return　検証式
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getAssertedExpression() {
        return this.assertedExpression;
    }

    /**
     * 検証式の未解決情報を保存する変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> assertedExpression;

    /**
     * 検証式がfalseを返すときに出力されるメッセージを表す式の未解決情報を保存するための変数
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> messageExpression;

}
