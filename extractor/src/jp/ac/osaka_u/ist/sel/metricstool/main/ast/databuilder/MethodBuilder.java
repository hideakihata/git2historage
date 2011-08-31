package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;


public class MethodBuilder extends CallableUnitBuilder<UnresolvedMethodInfo> {

    public MethodBuilder(BuildDataManager targetDataManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder,
            MethodParameterBuilder parameterbuilder) {

        super(targetDataManager, new MethodStateManager(), modifiersBuilder, typeBuilder,
                nameBuilder, parameterbuilder);

    }

    @Override
    protected void registType() {
        if (!buildingUnitStack.isEmpty()) {
            UnresolvedMethodInfo buildingMethod = buildingUnitStack.peek();
            buildingMethod.setReturnType(typeBuilder.popLastBuiltData());
        }
    }

    @Override
    protected void registName() {
        if (!buildingUnitStack.isEmpty()) {
            UnresolvedMethodInfo buildingMethod = buildingUnitStack.peek();
            String[] name = nameBuilder.popLastBuiltData();
            if (name.length > 0) {
                buildingMethod.setMethodName(name[0]);
            }
        }
    }

    @Override
    protected UnresolvedMethodInfo startUnitDefinition(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        UnresolvedMethodInfo method = super.startUnitDefinition(fromLine, fromColumn, toLine,
                toColumn);
        method.getOwnerClass().addDefinedMethod(method);
        return method;
    }

    @Override
    protected UnresolvedMethodInfo createUnresolvedCallableUnitInfo(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        return new UnresolvedMethodInfo(this.buildManager.getCurrentClass(), fromLine, fromColumn,
                toLine, toColumn);
    }

}
