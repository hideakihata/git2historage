package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SynchronizedBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SynchronizedBlockStateManager.SYNCHRONIZED_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSynchronizedBlockInfo;


public class SynchronizedBlockBuilder extends
        InnerBlockBuilder<SynchronizedBlockInfo, UnresolvedSynchronizedBlockInfo> {

    public SynchronizedBlockBuilder(final BuildDataManager targetDataManager,
            final ExpressionElementManager expressionManager) {
        super(targetDataManager, new SynchronizedBlockStateManager());

        if (null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null");
        }

        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChanged(event);

        final StateChangeEventType type = event.getType();
        if (type.equals(SYNCHRONIZED_BLOCK_STATE_CHANGE.EXIT_SYNCHRONIZED_EXPRESSION)) {
            final UnresolvedExpressionInfo<? extends ExpressionInfo> synchronizedExpression = null == this.expressionManager
                    .getPeekExpressionElement() ? null : this.expressionManager
                    .getPeekExpressionElement().getUsage();
            assert null != synchronizedExpression;
            if(null != synchronizedExpression) {
                this.getBuildingBlock().setSynchronizedExpression(synchronizedExpression);
            }
        }
    }

    @Override
    protected UnresolvedSynchronizedBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedSynchronizedBlockInfo(outerSpace);
    }

    private final ExpressionElementManager expressionManager;
}
