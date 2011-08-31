package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnknownUsageInfo;


/**
 * 
 * @author kou-tngt, t-miyake
 * 
 */
public class CompoundIdentifierElement extends IdentifierElement {

    public CompoundIdentifierElement(final IdentifierElement owner, final String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, fromLine, fromColumn, toLine, toColumn);

        if (null == owner) {
            throw new NullPointerException("owner is null.");
        }

        this.owner = owner;

        final String[] ownerName = owner.getQualifiedName();
        final String[] thisName = new String[ownerName.length + 1];
        System.arraycopy(ownerName, 0, thisName, 0, ownerName.length);
        thisName[thisName.length - 1] = name;
        this.qualifiedName = thisName;
    }

    public ExpressionElement getOwner() {
        return this.owner;
    }

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return null;
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(final BuildDataManager buildDataManager) {
        this.ownerUsage = this.resolveOwner(buildDataManager);
        return this;
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            final BuildDataManager buildDataManager, final boolean reference,
            final boolean assignment) {
        this.ownerUsage = this.resolveOwner(buildDataManager);
        final UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(
                UnresolvedMemberImportStatementInfo.getMemberImportStatements(buildDataManager
                        .getAllAvaliableNames()), this.ownerUsage, this.name, reference,
                assignment, buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn,
                this.toLine, this.toColumn);
        buildDataManager.addVariableUsage(fieldUsage);

        this.usage = fieldUsage;

        return fieldUsage;
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveReferencedEntityIfPossible(
            final BuildDataManager buildDataManager) {
        this.ownerUsage = this.owner.resolveReferencedEntityIfPossible(buildDataManager);

        if (this.ownerUsage != null) {
            final UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(
                    UnresolvedMemberImportStatementInfo.getMemberImportStatements(buildDataManager
                            .getAllAvaliableNames()), this.ownerUsage, this.name, true, false,
                    buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn, this.toLine,
                    this.toColumn);
            buildDataManager.addVariableUsage(fieldUsage);

            this.usage = fieldUsage;

            return fieldUsage;
        }

        return null;
    }

    protected UnresolvedExpressionInfo<? extends ExpressionInfo> resolveOwner(
            final BuildDataManager buildDataManager) {
        this.ownerUsage = this.owner.resolveReferencedEntityIfPossible(buildDataManager);

        return null != this.ownerUsage ? this.ownerUsage : new UnresolvedUnknownUsageInfo(
                buildDataManager.getAllAvaliableNames(), this.owner.getQualifiedName(),
                buildDataManager.getCurrentUnit(), this.owner.fromLine, this.owner.fromColumn,
                this.owner.toLine, this.owner.toColumn);
    }

    private final IdentifierElement owner;

}
