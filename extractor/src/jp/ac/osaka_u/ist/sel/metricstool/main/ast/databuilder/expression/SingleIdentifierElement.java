package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


public class SingleIdentifierElement extends IdentifierElement {

    public SingleIdentifierElement(final String name,
            UnresolvedExpressionInfo<? extends ExpressionInfo> ownerUsage, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(name, fromLine, fromColumn, toLine, toColumn);

        this.qualifiedName = new String[] { name };
        this.ownerUsage = ownerUsage;

    }

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return null;
    }

    private UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            final BuildDataManager buildDataManager,
            final UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> usedVariable,
            final boolean reference, final boolean assignment) {
        final UnresolvedVariableUsageInfo<?> localVariableUsage;
        if (null == usedVariable || usedVariable instanceof UnresolvedFieldInfo) {
            //変数がみつからないので多分どこかのフィールド or 見つかった変数がフィールドだった
            localVariableUsage = new UnresolvedFieldUsageInfo(
                    UnresolvedMemberImportStatementInfo.getMemberImportStatements(buildDataManager
                            .getAllAvaliableNames()), ownerUsage, name, reference, assignment,
                    buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn, this.toLine,
                    this.toColumn);
        } else if (usedVariable instanceof UnresolvedParameterInfo) {
            UnresolvedParameterInfo parameter = (UnresolvedParameterInfo) usedVariable;
            localVariableUsage = new UnresolvedParameterUsageInfo(parameter, reference, assignment,
                    buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn, this.toLine,
                    this.toColumn);
        } else {
            assert (usedVariable instanceof UnresolvedLocalVariableInfo) : "Illegal state: unexpected VariableInfo";
            UnresolvedLocalVariableInfo localVariable = (UnresolvedLocalVariableInfo) usedVariable;
            localVariableUsage = new UnresolvedLocalVariableUsageInfo(localVariable, reference,
                    assignment, buildDataManager.getCurrentUnit(), this.fromLine, this.fromColumn,
                    this.toLine, this.toColumn);
        }

        buildDataManager.addVariableUsage(localVariableUsage);

        this.usage = localVariableUsage;

        return localVariableUsage;
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            final BuildDataManager buildDataManager, final boolean reference,
            final boolean assignment) {
        UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> variable = buildDataManager
                .getCurrentScopeVariable(this.name);

        return resolveAsVariable(buildDataManager, variable, reference, assignment);
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        //特に何もしない
        return this;
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager) {
        UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> variable = buildDataManager
                .getCurrentScopeVariable(this.name);
        if (null != variable) {
            return resolveAsVariable(buildDataManager, variable, true, false);
        } else {
            return null;
        }
    }

}
