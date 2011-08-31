package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 未解決エンティティ利用を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class UnknownEntityUsageInfo extends ExpressionInfo {

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    /**
     * 位置情報を与えて，オブジェクトを初期化
     * 
     * @param referencedName 参照されている名前
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnknownEntityUsageInfo(final String[] referencedName,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == referencedName) {
            throw new IllegalArgumentException();
        }

        this.referencedName = referencedName;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * この未解決エンティティ使用のテキスト表現を返す
     * 
     * @return この未解決エンティティ使用のテキスト表現
     */
    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder();
        for (final String name : this.getReferencedName()) {
            text.append(name);
            text.append(".");
        }
        text.deleteCharAt(text.length() - 1);
        return text.toString();
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

    /**
     * 参照されている文字列を返す
     * 
     * @return 参照されている文字列
     */
    public String[] getReferencedName() {
        return this.referencedName;
    }

    @Override
    public ExecutableElementInfo copy() {
        final String[] referencedName = this.getReferencedName();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnknownEntityUsageInfo newEntityUsage = new UnknownEntityUsageInfo(referencedName,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newEntityUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newEntityUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newEntityUsage;
    }

    private final String[] referencedName;
}
