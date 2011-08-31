package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager.VARIABLE_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableLengthParameterInfo;


public class MethodParameterBuilder
        extends
        VariableBuilder<UnresolvedParameterInfo, UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> {

    public MethodParameterBuilder(BuildDataManager buildDataManager) {
        this(buildDataManager, new ModifiersBuilder(), new TypeBuilder(buildDataManager),
                new NameBuilder());
    }

    public MethodParameterBuilder(BuildDataManager buildDataManager,
            ModifiersBuilder modifiersBuilder, TypeBuilder typeBuilder, NameBuilder nameBuilder) {
        super(buildDataManager, new MethodParameterStateManager(), modifiersBuilder, typeBuilder,
                nameBuilder);
        this.isVariableParameterStack = new Stack<Boolean>();
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(VARIABLE_STATE.ENTER_VARIABLE_DEF)) {
            if (event.getTrigger().getToken().isVariableParameterDefinition()) {
                this.isVariableParameterStack.push(true);
            } else {
                this.isVariableParameterStack.push(false);
            }
        } else if (eventType.equals(VARIABLE_STATE.EXIT_VARIABLE_DEF)) {
            //            this.isVariableParameterStack.pop();
        }

        super.stateChanged(event);
    }

    @Override
    protected UnresolvedParameterInfo buildVariable(String[] name,
            UnresolvedTypeInfo<? extends TypeInfo> type, ModifierInfo[] modifiers,
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> definitionMehtod,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        final int index = buildDataManager.getCurrentParameterCount();
        final UnresolvedParameterInfo parameter;
        //パラメータが可変長引数であるかチェック
        if (this.isAnalyzingVariableParameter()) {
            parameter = new UnresolvedVariableLengthParameterInfo(varName, type, index,
                    definitionMehtod, startLine, startColumn, endLine, endColumn);
        } else {
            parameter = new UnresolvedParameterInfo(varName, type, index, definitionMehtod,
                    startLine, startColumn, endLine, endColumn);
        }
        for (ModifierInfo modifier : modifiers) {
            parameter.addModifier(modifier);
        }

        if (null != buildDataManager) {
            buildDataManager.addMethodParameter(parameter);
        }

        return parameter;
    }

    @Override
    protected UnresolvedCallableUnitInfo<? extends CallableUnitInfo> validateDefinitionSpace(
            UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        if (definitionUnit instanceof UnresolvedCallableUnitInfo) {
            return (UnresolvedCallableUnitInfo<?>) definitionUnit;
        } else {
            return null;
        }
    }

    private final boolean isAnalyzingVariableParameter() {
        return this.isVariableParameterStack.pop();
    }

    private final Stack<Boolean> isVariableParameterStack;
}
