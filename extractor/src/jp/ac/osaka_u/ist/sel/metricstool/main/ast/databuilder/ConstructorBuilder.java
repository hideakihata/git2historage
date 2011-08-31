package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ConstructorStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;


public class ConstructorBuilder extends CallableUnitBuilder<UnresolvedConstructorInfo> {

    public ConstructorBuilder(BuildDataManager targetDataManager,
            ModifiersBuilder modifiersBuilder, TypeBuilder typeBuilder, NameBuilder nameBuilder,
            MethodParameterBuilder parameterbuilder) {

        super(targetDataManager, new ConstructorStateManager(), modifiersBuilder, typeBuilder,
                nameBuilder, parameterbuilder);

    }

    @Override
    protected void registType() {

    }

    @Override
    protected void registName() {

    }

    @Override
    protected UnresolvedConstructorInfo startUnitDefinition(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        UnresolvedConstructorInfo constructor = super.startUnitDefinition(fromLine, fromColumn,
                toLine, toColumn);
        constructor.getOwnerClass().addDefinedConstructor(constructor);
        return constructor;
    }

    @Override
    protected UnresolvedConstructorInfo createUnresolvedCallableUnitInfo(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        return new UnresolvedConstructorInfo(buildManager.getCurrentClass(), fromLine, fromColumn,
                toLine, toColumn);
    }

}
