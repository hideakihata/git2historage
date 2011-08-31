package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public abstract class JumpStatementInfo extends SingleStatementInfo {

    public JumpStatementInfo(final LocalSpaceInfo ownerSpace, final LabelInfo destinationLabel,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        this.destinationLabel = destinationLabel;
    }

    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
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

    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder(this.getReservedKeyword());
        if (null != this.getDestinationLabel()) {
            text.append(" ").append(this.getDestinationLabel().getName());
        }
        text.append(";");
        return text.toString();
    }

    protected abstract String getReservedKeyword();

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    public BlockInfo getCorrespondingBlock() {

        if (null != this.getDestinationLabel()) {
            return (BlockInfo) this.getDestinationLabel().getLabeledStatement();
        } else {

            for (BlockInfo ownerBlock = (BlockInfo) this.getOwnerSpace();; ownerBlock = (BlockInfo) ownerBlock
                    .getOwnerSpace()) {

                if (ownerBlock instanceof SwitchBlockInfo
                        || ownerBlock instanceof SynchronizedBlockInfo
                        || ownerBlock.isLoopStatement()) {
                    return ownerBlock;
                }

                if (!(ownerBlock.getOwnerSpace() instanceof BlockInfo)) {
                    break;
                }
            }

            assert false : "Here shouldn't be reached!";
            return null;
        }
    }

    //public abstract StatementInfo getFollowingStatement();

    public LabelInfo getDestinationLabel() {
        return this.destinationLabel;
    }

    private final LabelInfo destinationLabel;

}
