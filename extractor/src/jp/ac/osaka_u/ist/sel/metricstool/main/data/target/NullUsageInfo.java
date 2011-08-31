package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * null使用を表すクラス．
 * 
 * @author higo, t-miyake
 * 
 */
@SuppressWarnings("serial")
public final class NullUsageInfo extends ExpressionInfo {

    /**
     * 位置情報を与えて，オブジェクトを初期化
     * 
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public NullUsageInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * null使用の型は不明
     * 
     * @return 不明型を返す
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    /**
     * nullの使用に変数使用が含まれることはないので空のセットを返す
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
     * null使用のテキスト表現（型）を返す
     * 
     * @return null使用のテキスト表現（型）
     */
    @Override
    public String getText() {
        return NULLSTRING;
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
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final NullUsageInfo newNullUsage = new NullUsageInfo(ownerMethod, fromLine, fromColumn,
                toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newNullUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newNullUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newNullUsage;
    }

    /**
     * null使用の型を保存するための定数
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();

    private static final String NULLSTRING = "null";
}
