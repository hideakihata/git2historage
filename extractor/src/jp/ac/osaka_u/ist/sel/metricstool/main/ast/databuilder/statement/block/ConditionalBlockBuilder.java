package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.LocalVariableDeclarationStatementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;


public abstract class ConditionalBlockBuilder<TResolved extends ConditionalBlockInfo, T extends UnresolvedConditionalBlockInfo<TResolved>>
        extends InnerBlockBuilder<TResolved, T> {

    protected ConditionalBlockBuilder(final BuildDataManager targetDataManager,
            final InnerBlockStateManager blockStateManager,
            final ExpressionElementManager expressionManager,
            final LocalVariableDeclarationStatementBuilder variableBuilder) {
        super(targetDataManager, blockStateManager);

        if (null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null.");
        }

        this.expressionManager = expressionManager;

        this.conditionBuilder = new ConditionBuilder(expressionManager, variableBuilder,
                new AstToken[] { DescriptionToken.CONDITIONAL_CLAUSE });
        this.conditionBuilder.deactivate();

        this.addInnerBuilder(this.conditionBuilder);
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChanged(event);
        final StateChangeEventType type = event.getType();

        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE)) {
            this.startCondition();
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE)) {
            final AstVisitEvent trigger = event.getTrigger();
            this.endCondition(trigger.getStartLine(), trigger.getStartColumn(), trigger
                    .getEndLine(), trigger.getEndColumn());
        }
    }

    protected void startCondition() {
        this.conditionBuilder.clearBuiltData();
        this.conditionBuilder.activate();
    }

    protected void endCondition(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        final T buildingBlock = this.getBuildingBlock();
        final List<UnresolvedConditionInfo<? extends ConditionInfo>> conditionList = null != this.conditionBuilder ? this.conditionBuilder
                .getLastBuildData()
                : null;

        if (!this.buildingBlockStack.isEmpty()
                && buildingBlock == this.buildManager.getCurrentBlock() && null != conditionList) {

            final UnresolvedConditionInfo<? extends ConditionInfo> condition = conditionList
                    .size() == 0 ? null : conditionList.get(0);

            assert null != condition || buildingBlock instanceof UnresolvedForBlockInfo : "Illegal state; conditional expression is not found.";

            final UnresolvedConditionalClauseInfo conditionalCluase = new UnresolvedConditionalClauseInfo(
                    buildingBlock, condition);

                conditionalCluase.setFromLine(fromLine);
                conditionalCluase.setFromColumn(fromColumn);
                conditionalCluase.setToLine(toLine);
                conditionalCluase.setToColumn(toColumn);
              
            buildingBlock.setConditionalClause(conditionalCluase);

            //assert buildingBlock.getStatements().size() <= 1 : "Illegal state: the number of conditional statements is more than one.";

            /*if (buildingBlock.getStatements().size() >= 1) {
                final UnresolvedStatementInfo<? extends StatementInfo> statement = buildingBlock
                        .getStatements().iterator().next();
                assert statement instanceof UnresolvedVariableDeclarationStatementInfo : "Illegal state: the conditioanl statement is not a variable declaration.";
                if (statement instanceof UnresolvedVariableDeclarationStatementInfo) {
                    buildingBlock.setCondition((UnresolvedVariableDeclarationStatementInfo) statement);
                }
            }*/

        }

        this.conditionBuilder.deactivate();
    }

    protected final ConditionBuilder conditionBuilder;

    protected final ExpressionElementManager expressionManager;

}
