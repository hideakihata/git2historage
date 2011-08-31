package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.LocalVariableStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager.VARIABLE_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class LocalVariableBuilder
        extends
        InitializableVariableBuilder<UnresolvedLocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    public LocalVariableBuilder(BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder) {
        super(buildDataManager, expressionManager, new LocalVariableStateManager(),
                modifiersBuilder, typeBuilder, nameBuilder);
        this.declarationUsageTriggerStack = new Stack<AstVisitEvent>();
        this.declarationUsageStack = new Stack<UnresolvedLocalVariableUsageInfo>();
    }

    public LocalVariableBuilder(final BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager) {
        super(buildDataManager, expressionManager, new LocalVariableStateManager());

        this.declarationUsageTriggerStack = new Stack<AstVisitEvent>();
        this.declarationUsageStack = new Stack<UnresolvedLocalVariableUsageInfo>();
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChanged(event);

        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(VARIABLE_STATE.EXIT_VARIABLE_DEF)) {

            assert !this.declarationUsageTriggerStack.isEmpty() : "Illegal state: ";

            if (!this.declarationUsageTriggerStack.isEmpty() && null != this.getLastBuildData()) {
                final AstVisitEvent trigger = this.declarationUsageTriggerStack.pop();
                final UnresolvedLocalVariableUsageInfo declarationUsage = this
                        .buildDeclarationUsage(this.getLastBuildData(), trigger.getStartLine(),
                                trigger.getStartColumn(), trigger.getEndLine(),
                                trigger.getEndColumn());

                this.declarationUsageStack.push(declarationUsage);

            }

        } else if (this.variableStateManager.isInDefinition()) {
            if (eventType.equals(NameStateManager.NAME_STATE.ENTER_NAME)) {
                this.declarationUsageTriggerStack.push(event.getTrigger());
            }
        }
    }

    @Override
    protected UnresolvedLocalVariableInfo buildVariable(final String[] name,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final ModifierInfo[] modifiers,
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> definitionSpace,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression = this
                .getLastBuiltExpression();

        final UnresolvedLocalVariableInfo var = new UnresolvedLocalVariableInfo(varName, type,
                definitionSpace, initializationExpression, startLine, startColumn, endLine,
                endColumn);
        for (ModifierInfo modifier : modifiers) {
            var.addModifier(modifier);
        }

        this.buildDataManager.addLocalVariable(var);

        return var;
    }

    private UnresolvedLocalVariableUsageInfo buildDeclarationUsage(
            final UnresolvedLocalVariableInfo declaredVariable, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedLocalVariableUsageInfo declarationUsage = new UnresolvedLocalVariableUsageInfo(
                declaredVariable, false, true, this.buildDataManager.getCurrentUnit(), fromLine,
                fromColumn, toLine, toColumn);

        this.buildDataManager.addVariableUsage(declarationUsage);

        return declarationUsage;
    }

    @Override
    public void reset() {
        super.reset();
        this.declarationUsageStack.clear();
        this.declarationUsageTriggerStack.clear();
    }

    @Override
    protected UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> validateDefinitionSpace(
            UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        return definitionUnit instanceof UnresolvedLocalSpaceInfo ? (UnresolvedLocalSpaceInfo<?>) definitionUnit
                : null;
    }

    public UnresolvedLocalVariableUsageInfo getLastDeclarationUsage() {
        return this.declarationUsageStack.isEmpty() ? null : this.declarationUsageStack.peek();
    }

    private final Stack<AstVisitEvent> declarationUsageTriggerStack;

    private final Stack<UnresolvedLocalVariableUsageInfo> declarationUsageStack;

}
