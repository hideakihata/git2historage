package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


public abstract class IdentifierElement extends ExpressionElement {

    public IdentifierElement(final String name, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == name) {
            throw new IllegalArgumentException("name is null.");
        }

        this.name = name;

        this.ownerUsage = null;

    }

    public String getName() {
        return this.name;
    }

    public String[] getQualifiedName() {
        return this.qualifiedName;
    }

    public UnresolvedExpressionInfo<? extends ExpressionInfo> getOwnerUsage() {
        return this.ownerUsage;
    }

    public abstract UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            BuildDataManager buildDataManager, final boolean reference, final boolean assignment);

    public abstract IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager);

    public abstract UnresolvedExpressionInfo<? extends ExpressionInfo> resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager);

    protected final String name;

    protected String[] qualifiedName;

    protected UnresolvedExpressionInfo<? extends ExpressionInfo> ownerUsage;

}
