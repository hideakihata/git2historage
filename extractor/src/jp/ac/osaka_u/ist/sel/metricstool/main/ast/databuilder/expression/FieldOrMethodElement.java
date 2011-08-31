package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author kou-tngt, t-miyake
 *
 */
public class FieldOrMethodElement extends IdentifierElement {

    public FieldOrMethodElement(UnresolvedExpressionInfo<? extends ExpressionInfo> ownerUsage,
            String name, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(name, fromLine, fromColumn, toLine, toColumn);

        this.ownerUsage = ownerUsage;
    }

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return null;
    }

    @Override
    public String[] getQualifiedName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            final BuildDataManager buildDataManager, final boolean reference,
            final boolean assignment) {
        UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(
                UnresolvedMemberImportStatementInfo.getMemberImportStatements(buildDataManager
                        .getAllAvaliableNames()), this.ownerUsage, this.name, reference,
                assignment, buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn,
                this.toLine, this.toColumn);
        buildDataManager.addVariableUsage(fieldUsage);

        this.usage = fieldUsage;

        return fieldUsage;
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        return this;
    }

    @Override
    public UnresolvedExpressionInfo<ExpressionInfo> resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager) {
        throw new UnsupportedOperationException();
    }

}
