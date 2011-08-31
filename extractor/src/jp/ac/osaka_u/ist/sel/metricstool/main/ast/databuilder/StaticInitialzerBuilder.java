package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.BlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;


public class StaticInitialzerBuilder extends
        StateDrivenDataBuilder<UnresolvedStaticInitializerInfo> {

    public StaticInitialzerBuilder(final BuildDataManager buildManager) {
        if (null == buildManager) {
            throw new NullPointerException("buildManager is null.");
        }

        this.buildManager = buildManager;
        addStateManager(new BlockStateManager());
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        AstToken token = event.getTrigger().getToken();
    }

    private final BuildDataManager buildManager;

}
