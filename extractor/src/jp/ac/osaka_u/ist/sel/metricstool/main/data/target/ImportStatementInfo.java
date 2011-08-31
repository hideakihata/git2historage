package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public abstract class ImportStatementInfo<T> extends UnitInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    ImportStatementInfo(final String[] importName, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        final int length = importName.length;
        final String[] tmp = new String[length];
        System.arraycopy(importName, 0, tmp, 0, length);
        this.importName = tmp;
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    abstract Set<T> getImportedUnits();

    public final String[] getImportName() {
        return this.importName;
    }

    abstract NamespaceInfo getNamespace();

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
    final public int hashCode() {
        return this.getImportedUnits().hashCode() + this.getFromLine() + this.getFromColumn()
                + this.getToLine() + this.getToColumn();
    }

    @Override
    final public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String name : this.getImportName()) {
            sb.append(name);
            sb.append(".");
        }
        sb.setCharAt(sb.length() - 1, ';');
        return sb.toString();
    }

    private final String[] importName;
}
