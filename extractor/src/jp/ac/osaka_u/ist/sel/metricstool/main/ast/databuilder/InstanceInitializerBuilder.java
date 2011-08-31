package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InstanceInitializerStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedInstanceInitializerInfo;


public class InstanceInitializerBuilder extends
        InitializerBuilder<UnresolvedInstanceInitializerInfo> {

    public InstanceInitializerBuilder(BuildDataManager buildDataManager) {
        super(buildDataManager, new InstanceInitializerStateManager());
    }

    @Override
    protected void registToOwnerClass(UnresolvedInstanceInitializerInfo initializer) {
        initializer.getOwnerClass().addInstanceInitializer(initializer);
    }

    @Override
    protected UnresolvedInstanceInitializerInfo createUnresolvedCallableUnitInfo(int fromLine,
            int fromColumn, int toLine, int toColumn) {
        return new UnresolvedInstanceInitializerInfo(this.buildManager.getCurrentClass(), fromLine,
                fromColumn, toLine, toColumn);
    }
}
