package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public abstract class CommentInfo extends UnitInfo {

    final String content;

    CommentInfo(final String content, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        this.content = content;
    }

    @Override
    final public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return Collections.unmodifiableSet(new HashSet<CallInfo<? extends CallableUnitInfo>>());
    }

    @Override
    final public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return Collections.unmodifiableSet(new HashSet<VariableInfo<? extends UnitInfo>>());
    }

    @Override
    final public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return Collections
                .unmodifiableSet(new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>());
    }

    @Override
    public final int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return content;
    }
}
