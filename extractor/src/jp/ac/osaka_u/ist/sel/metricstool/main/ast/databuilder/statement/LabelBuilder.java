package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LabelStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LabelStateManager.LABEL_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;


public class LabelBuilder extends StateDrivenDataBuilder<UnresolvedLabelInfo> {

    public LabelBuilder(final BuildDataManager buildDataManager) {
        if (null == buildDataManager) {
            throw new IllegalArgumentException("buildDataManager is null");
        }

        this.buildDataManager = buildDataManager;
        this.buildingDataStack = new Stack<UnresolvedLabelInfo>();
        this.previousStatementsStack = new Stack<Set<UnresolvedStatementInfo<? extends StatementInfo>>>();

        this.addStateManager(new LabelStateManager());
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (type.equals(LABEL_STATE.ENTER_LABEL_DEF)) {
            final UnresolvedLabelInfo label = new UnresolvedLabelInfo();
            this.buildingDataStack.push(label);
            this.buildDataManager.addLabel(label);
            label.setOuterUnit(this.buildDataManager.getCurrentUnit());
            this.pushCurrentBuiltStatement();
            
        } else if (type.equals(LABEL_STATE.EXIT_LABEL_DEF)) {

            final Set<UnresolvedStatementInfo<? extends StatementInfo>> previousStatements = this.previousStatementsStack
                    .pop();
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                    .getCurrentLocalSpace();
            assert null != currentLocal;
            if (null != currentLocal) {
                final Set<UnresolvedStatementInfo<? extends StatementInfo>> currentStatements = new TreeSet<UnresolvedStatementInfo<? extends StatementInfo>>(
                        currentLocal.getStatements());
                currentStatements.removeAll(previousStatements);
                if (!currentStatements.isEmpty()) {
                    this.getBuildingLabel()
                            .setLabeledStatement(currentStatements.iterator().next());
                }
            }

            this.buildingDataStack.pop();
            //this.buildDataManager.addStatement(this.buildingDataStack.pop());
        } else if (type.equals(LABEL_STATE.ENTER_LABEL_NAME)) {
            final AstVisitEvent trigger = event.getTrigger();
            final UnresolvedLabelInfo buildingLabel = this.getBuildingLabel();
            buildingLabel.setName(event.getTrigger().getToken().toString());
            buildingLabel.setFromLine(trigger.getStartLine());
            buildingLabel.setFromColumn(trigger.getStartColumn());
            buildingLabel.setToLine(trigger.getEndLine());
            buildingLabel.setToColumn(trigger.getEndColumn());
        } else if (type.equals(LABEL_STATE.ENTER_LABELED_STATEMENT)) {
            //this.pushCurrentBuiltStatement();
        } else if (type.equals(LABEL_STATE.EXIT_LABELED_STATEMENT)) {

        }

    }

    private void pushCurrentBuiltStatement() {
        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                .getCurrentLocalSpace();
        assert null != currentLocal;
        if (null != currentLocal) {
            this.previousStatementsStack
                    .push(new TreeSet<UnresolvedStatementInfo<? extends StatementInfo>>(
                            currentLocal.getStatements()));
        }
    }

    @Override
    public void clearBuiltData() {
        super.clearBuiltData();
        this.buildingDataStack.clear();
        this.previousStatementsStack.clear();
    }

    private UnresolvedLabelInfo getBuildingLabel() {
        return this.buildingDataStack.peek();
    }

    private final Stack<Set<UnresolvedStatementInfo<? extends StatementInfo>>> previousStatementsStack;

    private final Stack<UnresolvedLabelInfo> buildingDataStack;

    private final BuildDataManager buildDataManager;
}
