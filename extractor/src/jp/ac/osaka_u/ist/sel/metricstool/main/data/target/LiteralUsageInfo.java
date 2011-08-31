package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * リテラルの使用を表すクラス
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class LiteralUsageInfo extends ExpressionInfo {

    /**
     * リテラル、リテラルの型、出現位置を与えて初期化
     * 
     * @param ownerMethod オーナーメソッド
     * @param literal リテラル
     * @param type リテラルの型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public LiteralUsageInfo(final String literal, final TypeInfo type,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.literal = literal;
        this.type = type;

    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo#getType()
     */
    @Override
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * リテラルの文字列を返す
     * 
     * @return リテラルの文字列
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * リテラルは変数参照ではないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public final Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * このリテラル使用のテキスト表現（String型）を返す
     * 
     * @return このリテラル使用のテキスト表現（String型）
     */
    @Override
    public String getText() {
        return this.getLiteral();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final String literal = this.getLiteral();
        final TypeInfo type = this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LiteralUsageInfo newLiteralUsage = new LiteralUsageInfo(literal, type, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newLiteralUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newLiteralUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newLiteralUsage;
    }

    /**
     * リテラルを保存するための変数
     */
    private final String literal;

    /**
     * リテラルの型を保存するための変数
     */
    private final TypeInfo type;
}