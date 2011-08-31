package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedForeachBlockInfo extends UnresolvedConditionalBlockInfo<ForeachBlockInfo> {

    /**
     * 外側のブロック情報を与えて，foreach ブロック情報を初期化
     * 
     * @param outerSpace 外側のブロック
     */
    public UnresolvedForeachBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
        this.iteratorExpression = null;
        this.iteratorVariable = null;
    }

    /**
     * この未解決 for ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public ForeachBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この foreach文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ForeachBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // 外側の空間を取得
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
    * 変数定義を設定する
    * 
    * @param iteraotorVariableDeclaration 変数定義
    */
    public void setIteratorVariable(
            final UnresolvedVariableDeclarationStatementInfo iteratorVariable) {

        if (null == iteratorVariable) {
            throw new IllegalArgumentException();
        }

        this.iteratorVariable = iteratorVariable;

        if (null != this.iteratorExpression) {

            final int fromLine = this.iteratorVariable.getFromLine();
            final int fromColumn = this.iteratorVariable.getFromColumn();
            final int toLine = this.iteratorExpression.getToLine();
            final int toColumn = this.iteratorExpression.getToColumn();
            final UnresolvedForeachConditionInfo condition = new UnresolvedForeachConditionInfo(
                    this, fromLine, fromColumn, toLine, toColumn);
            condition.setIteratorVariable(this.iteratorVariable);
            condition.setIteratorExpression(this.iteratorExpression);

            final UnresolvedConditionalClauseInfo conditionalClause = new UnresolvedConditionalClauseInfo(
                    this, condition);
            this.setConditionalClause(conditionalClause);
        }
    }

    /**
     * 繰り返し用の式を設定する
     * 
     * @param iteratorExpression 繰り返し用の式
     */
    public void setIteratorExpression(final UnresolvedExpressionInfo<?> iteratorExpression) {

        if (null == iteratorExpression) {
            throw new IllegalArgumentException();
        }

        this.iteratorExpression = iteratorExpression;

        if (null != this.iteratorVariable) {
            final int fromLine = this.iteratorVariable.getFromLine();
            final int fromColumn = this.iteratorVariable.getFromColumn();
            final int toLine = this.iteratorExpression.getToLine();
            final int toColumn = this.iteratorExpression.getToColumn();
            final UnresolvedForeachConditionInfo condition = new UnresolvedForeachConditionInfo(
                    this, fromLine, fromColumn, toLine, toColumn);
            condition.setIteratorVariable(this.iteratorVariable);
            condition.setIteratorExpression(this.iteratorExpression);

            final UnresolvedConditionalClauseInfo conditionalClause = new UnresolvedConditionalClauseInfo(
                    this, condition);
            this.setConditionalClause(conditionalClause);
        }
    }

    /**
     * 変数定義を返す
     * 
     * @return 変数定義
     */
    public UnresolvedVariableDeclarationStatementInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    /**
     * 繰り返し用の式を返す
     * 
     * @return 繰り返し用の式
     */
    public UnresolvedExpressionInfo<?> getIteratorExpression() {
        return this.iteratorExpression;
    }

    private UnresolvedVariableDeclarationStatementInfo iteratorVariable;

    private UnresolvedExpressionInfo<?> iteratorExpression;

}
