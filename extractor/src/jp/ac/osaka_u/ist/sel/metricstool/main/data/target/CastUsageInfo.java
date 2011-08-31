package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * キャストの使用を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class CastUsageInfo extends ExpressionInfo {

    /**
     * 必要な情報を与えてオブジェクトを初期化
     * 
     * @param castType キャストの型
     * @param castedUsage キャストされる要素
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CastUsageInfo(final TypeInfo castType, final ExpressionInfo castedUsage,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
        this.castedUsage = castedUsage;

        this.castedUsage.setOwnerExecutableElement(this);
    }

    /**
     * このキャストの型を返す
     * 
     * @return このキャストの型
     */
    @Override
    public TypeInfo getType() {
        return this.castType;
    }

    /**
     * キャストされる要素を返す
     * 
     * @return キャストされる要素
     */
    public ExpressionInfo getCastedUsage() {
        return this.castedUsage;
    }

    /**
     * この式（キャスト使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getCastedUsage().getVariableUsages();
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getCastedUsage().getCalls();
    }

    /**
     * このキャスト使用のテキスト表現（String型）を返す
     * 
     * @return このキャスト使用のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("(");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append(")");

        final ExpressionInfo expression = this.getCastedUsage();
        sb.append(expression.getText());

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getCastedUsage().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {

        final TypeInfo castType = this.getType();
        final ExpressionInfo castedUsage = (ExpressionInfo) this.getCastedUsage().copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CastUsageInfo newCastUsage = new CastUsageInfo(castType, castedUsage, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCastUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCastUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCastUsage;
    }

    private final TypeInfo castType;

    private final ExpressionInfo castedUsage;
}
