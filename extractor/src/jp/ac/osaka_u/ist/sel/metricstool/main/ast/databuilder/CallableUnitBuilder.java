package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ThrownExceptionsDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager.CALLABLE_UNIT_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;


/**
 * 呼び出し可能な単位(メソッドやコンストラクタ)を表すクラスのビルダ
 * @author t-miyake
 *
 * @param <T> 呼び出し可能な単位の種類
 */

public abstract class CallableUnitBuilder<T extends UnresolvedCallableUnitInfo<? extends CallableUnitInfo>>
        extends CompoundDataBuilder<T> {

    protected CallableUnitBuilder(BuildDataManager buildDataManager,
            CallableUnitStateManager stateManager) {
        this(buildDataManager, stateManager, new ModifiersBuilder(), new TypeBuilder(
                buildDataManager), new NameBuilder(), new MethodParameterBuilder(buildDataManager));
    }

    protected CallableUnitBuilder(BuildDataManager targetDataManager,
            CallableUnitStateManager stateManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder,
            MethodParameterBuilder parameterbuilder) {

        if (null == targetDataManager || null == stateManager || null == modifiersBuilder
                || null == typeBuilder || null == nameBuilder || null == parameterbuilder) {
            throw new IllegalArgumentException();
        }

        this.buildManager = targetDataManager;
        this.stateManager = stateManager;
        this.modifierBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;
        this.methodParameterBuilder = parameterbuilder;

        addInnerBuilder(modifiersBuilder);
        addInnerBuilder(typeBuilder);
        addInnerBuilder(nameBuilder);
        addInnerBuilder(parameterbuilder);

        modifiersBuilder.deactivate();
        typeBuilder.deactivate();
        nameBuilder.deactivate();
        parameterbuilder.deactivate();

        addStateManager(this.stateManager);
        addStateManager(this.parameterStateManager);
        addStateManager(this.typeStateManager);
        addStateManager(this.typeParameterStateManager);
        addStateManager(this.throwsStateManager);
        addStateManager(new ModifiersDefinitionStateManager());
        addStateManager(new NameStateManager());
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();

        if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_DEF)) {
            final AstVisitEvent trigger = event.getTrigger();
            startUnitDefinition(trigger.getStartLine(), trigger.getStartColumn(), trigger
                    .getEndLine(), trigger.getEndColumn());
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.EXIT_DEF)) {
            endUnitDefinition();
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_BLOCK)) {
            if (null != buildManager) {
                buildManager.enterMethodBlock();
            }
        } else if (isActive() && stateManager.isInPreDeclaration()) {
            if (!parameterStateManager.isInDefinition()) {
                if (type
                        .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                    if (null != modifierBuilder) {
                        modifierBuilder.activate();
                    }
                } else if (type
                        .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                    if (null != modifierBuilder) {
                        modifierBuilder.deactivate();
                        registModifiers();
                        modifierBuilder.clearBuiltData();
                    }
                } else if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME)) {
                    if (null != nameBuilder) {
                        nameBuilder.activate();
                    }
                } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME)) {
                    if (null != nameBuilder) {
                        nameBuilder.deactivate();
                        registName();
                        nameBuilder.clearBuiltData();
                    }
                } else if (!typeParameterStateManager.isEnterParameterDefinition()
                        && !throwsStateManager.isEntered()) {
                    // 返り値の型
                    if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                        if (null != typeBuilder) {
                            typeBuilder.activate();
                        }
                    } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                        if (null != typeBuilder && !typeStateManager.isEntered()) {
                            typeBuilder.deactivate();
                            registType();
                            typeBuilder.clearBuiltData();
                        }
                    }
                }
            }

            if (type.equals(VariableDefinitionStateManager.VARIABLE_STATE.ENTER_VARIABLE_DEF)) {
                if (null != methodParameterBuilder) {
                    methodParameterBuilder.activate();
                }
            } else if (type.equals(VariableDefinitionStateManager.VARIABLE_STATE.EXIT_VARIABLE_DEF)) {
                if (null != methodParameterBuilder) {
                    methodParameterBuilder.deactivate();
                }
            }
        }
    }

    protected void endUnitDefinition() {
        T builtMethod = buildingUnitStack.pop();
        registBuiltData(builtMethod);

        buildManager.endCallableUnitDefinition();
    }

    private void registModifiers() {
        if (!buildingUnitStack.isEmpty()) {
            T buildingMethod = buildingUnitStack.peek();
            ModifierInfo[] modifiers = modifierBuilder.popLastBuiltData();
            for (ModifierInfo modifier : modifiers) {
                buildingMethod.addModifier(modifier);
            }

        }
    }

    protected abstract void registType();

    protected abstract void registName();

    protected T startUnitDefinition(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        T callableUnit = createUnresolvedCallableUnitInfo(fromLine, fromColumn, toLine, toColumn);

        this.buildingUnitStack.push(callableUnit);

        buildManager.startCallableUnitDefinition(callableUnit);

        return callableUnit;
    }

    protected abstract T createUnresolvedCallableUnitInfo(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn);

    protected Stack<T> buildingUnitStack = new Stack<T>();

    protected final BuildDataManager buildManager;

    protected final TypeBuilder typeBuilder;

    protected final ModifiersBuilder modifierBuilder;

    protected final NameBuilder nameBuilder;

    protected final MethodParameterBuilder methodParameterBuilder;

    protected final CallableUnitStateManager stateManager;

    protected final MethodParameterStateManager parameterStateManager = new MethodParameterStateManager();

    protected final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    protected final TypeParameterStateManager typeParameterStateManager = new TypeParameterStateManager();

    protected final ThrownExceptionsDefinitionStateManager throwsStateManager = new ThrownExceptionsDefinitionStateManager();
}
