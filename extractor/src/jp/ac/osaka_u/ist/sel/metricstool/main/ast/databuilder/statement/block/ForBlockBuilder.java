package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.LocalVariableDeclarationStatementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForBlockStateManager.FOR_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class ForBlockBuilder extends ConditionalBlockBuilder<ForBlockInfo, UnresolvedForBlockInfo> {

    public ForBlockBuilder(final BuildDataManager targetDataManager,
            final ExpressionElementManager expressionManager,
            final LocalVariableDeclarationStatementBuilder variableBuilder) {
        super(targetDataManager, new ForBlockStateManager(), expressionManager, variableBuilder);

        this.conditionBuilder.addTriggerToken(DescriptionToken.FOR_INIT);
        this.conditionBuilder.addTriggerToken(DescriptionToken.FOR_ITERATOR);

    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChanged(event);
        final StateChangeEventType type = event.getType();

        if (type.equals(FOR_BLOCK_STATE_CHANGE.ENTER_FOR_INIT)) {
            this.conditionBuilder.clearBuiltData();
            this.conditionBuilder.activate();
        } else if (type.equals(FOR_BLOCK_STATE_CHANGE.EXIT_FOR_INIT)) {

            for (final UnresolvedConditionInfo<? extends ConditionInfo> condition : this.conditionBuilder
                    .getLastBuildData()) {
                this.getBuildingBlock().addInitializerExpression(condition);
            }

            this.conditionBuilder.deactivate();
        } else if (type.equals(FOR_BLOCK_STATE_CHANGE.ENTER_FOR_ITERATOR)) {
            this.conditionBuilder.clearBuiltData();
            this.conditionBuilder.activate();
        } else if (type.equals(FOR_BLOCK_STATE_CHANGE.EXIT_FOR_ITERATOR)) {

            for (final UnresolvedConditionInfo<? extends ConditionInfo> condition : this.conditionBuilder
                    .getLastBuildData()) {
                assert condition instanceof UnresolvedExpressionInfo : "Illegal state: iterator expression was not ExpressionInfo";
                if (condition instanceof UnresolvedExpressionInfo) {
                    this.getBuildingBlock().addIteratorExpression(
                            (UnresolvedExpressionInfo<?>) condition);
                }
            }

            this.conditionBuilder.deactivate();

            this.getBuildingBlock().initBody();
        }
    }

    @Override
    protected UnresolvedForBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedForBlockInfo(outerSpace);
    }

}
