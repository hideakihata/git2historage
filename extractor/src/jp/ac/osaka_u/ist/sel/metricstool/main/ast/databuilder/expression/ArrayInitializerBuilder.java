package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ArrayInitializerStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ArrayInitializerStateManager.ARRAY_INITILIZER_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


public class ArrayInitializerBuilder extends ExpressionBuilder {

    public ArrayInitializerBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {

        super(expressionManager, buildDataManager);
        if (null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null");
        }

        this.buildingInitilizerStack = new Stack<UnresolvedArrayInitializerInfo>();

        this.stateManager = new ArrayInitializerStateManager();
        this.addStateManager(this.stateManager);
    }

    @Override
    protected boolean isRelated(AstToken token) {
        return this.isActive() && this.isTriggerToken(token);
    }

    @Override
    protected void afterExited(AstVisitEvent event) throws ASTParseException {
        //
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isArrayInitilizer();
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (type.equals(ARRAY_INITILIZER_STATE.ENTER_ARRAY_INIT)) {
            final AstVisitEvent trigger = event.getTrigger();
            final UnresolvedArrayInitializerInfo initializer = new UnresolvedArrayInitializerInfo(
                    this.buildDataManager.getCurrentUnit(),
                    trigger.getStartLine(), trigger.getStartColumn(), trigger.getEndLine(),
                    trigger.getEndColumn());
            this.buildingInitilizerStack.push(initializer);

        } else if (type.equals(ARRAY_INITILIZER_STATE.EXIT_ARRAY_INIT)) {

            final UnresolvedArrayInitializerInfo builtInitilizer = this.buildingInitilizerStack
                    .pop();
            this.pushElement(new UsageElement(builtInitilizer));

            if (this.stateManager.isInInitilizer()) {
                this.getBuildingInitilizer().addElement(builtInitilizer);
            }

        } else if (type.equals(ARRAY_INITILIZER_STATE.EXIT_EXPR)) {
            final UnresolvedExpressionInfo<? extends ExpressionInfo> elementExpression = this.expressionManager
                    .getPeekExpressionElement().getUsage();
            this.getBuildingInitilizer().addElement(elementExpression);
        }
    }

    private final UnresolvedArrayInitializerInfo getBuildingInitilizer() {
        return this.buildingInitilizerStack.peek();
    }

    @Override
    public void clearBuiltData() {
        super.clearBuiltData();

        this.buildingInitilizerStack.clear();
    }

    private final Stack<UnresolvedArrayInitializerInfo> buildingInitilizerStack;

    private final ArrayInitializerStateManager stateManager;
}
