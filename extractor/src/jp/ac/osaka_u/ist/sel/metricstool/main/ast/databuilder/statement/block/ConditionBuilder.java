package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.LocalVariableDeclarationStatementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ConditionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ConditionStateManager.CONDITION_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionInfo;


public class ConditionBuilder extends
        StateDrivenDataBuilder<List<UnresolvedConditionInfo<? extends ConditionInfo>>> {

    public ConditionBuilder(final ExpressionElementManager expressionManager,
            final LocalVariableDeclarationStatementBuilder variableDeclarationStatementBuilder,
            final AstToken[] triggerTokens) {
        this.expressionManager = expressionManager;
        this.declarationStatementBuilder = variableDeclarationStatementBuilder;

        this.stateManager = new ConditionStateManager(triggerTokens);
        this.addStateManager(this.stateManager);
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (type.equals(CONDITION_STATE_CHANGE.ENTER_CONDITION)) {
            final List<UnresolvedConditionInfo<? extends ConditionInfo>> conditionList = new LinkedList<UnresolvedConditionInfo<? extends ConditionInfo>>();
            this.registBuiltData(conditionList);
        } else if (type.equals(CONDITION_STATE_CHANGE.EXIT_CONDITION)) {

        } else if (type.equals(CONDITION_STATE_CHANGE.ENTER_DECLARATION)) {

        } else if (type.equals(CONDITION_STATE_CHANGE.EXIT_DECLARATION)) {
            if (null != this.declarationStatementBuilder
                    && null != this.declarationStatementBuilder.getLastBuildData()) {
                this.getLastBuildData().add(this.declarationStatementBuilder.getLastBuildData());
            }
        } else if (type.equals(CONDITION_STATE_CHANGE.ENTER_EXPRESSION)) {

        } else if (type.equals(CONDITION_STATE_CHANGE.EXIT_EXPRESSION)) {
            if (null != this.expressionManager
                    && null != this.expressionManager.getPeekExpressionElement()) {
                this.getLastBuildData().add(
                        this.expressionManager.getPeekExpressionElement().getUsage());
            }
        }
    }

    public void addTriggerToken(final AstToken triggerToken) {
        this.stateManager.addTriggerToken(triggerToken);
    }

    private final ConditionStateManager stateManager;

    private final ExpressionElementManager expressionManager;

    private final LocalVariableDeclarationStatementBuilder declarationStatementBuilder;

}
