package jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ModifiersBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager.CALLABLE_UNIT_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * C#やVBのプロパティのASTを解析して，それと等価なメソッドの情報を構築するビルダ
 * @author t-miyake
 *
 */
public class MethodBuilderFromPropertyAST extends CompoundDataBuilder<UnresolvedMethodInfo> {

    public MethodBuilderFromPropertyAST(final BuildDataManager targetDataManager,
            final ModifiersBuilder modifiersBuilder, final TypeBuilder typeBuilder,
            final NameBuilder nameBuilder) {

        if (null == targetDataManager || null == modifiersBuilder || null == typeBuilder
                || null == nameBuilder) {
            throw new IllegalArgumentException();
        }

        this.buildManager = targetDataManager;
        this.stateManager = new PropertyStateManager();
        this.modifierBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;

        addInnerBuilder(modifiersBuilder);
        addInnerBuilder(typeBuilder);
        addInnerBuilder(nameBuilder);

        modifiersBuilder.deactivate();
        typeBuilder.deactivate();
        nameBuilder.deactivate();

        addStateManager(this.stateManager);
        addStateManager(this.typeStateManager);
        addStateManager(new ModifiersDefinitionStateManager());
        addStateManager(new NameStateManager());
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();

        if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_DEF)) {

        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.EXIT_DEF)) {
            this.endPropertyDefinition();
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_BLOCK)) {
            final AstVisitEvent trigger = event.getTrigger();

            final ModifierInfo[] modifiers = this.propertyModifierStack.peek();
            final UnresolvedTypeInfo<? extends TypeInfo> propertyType = this.propertyTypeStack
                    .peek();
            final String name = this.propertyNameStack.peek();
            final int fromLine = trigger.getStartLine();
            final int fromColumn = trigger.getStartColumn();
            final int toLine = trigger.getEndLine();
            final int toColumn = trigger.getEndColumn();

            UnresolvedMethodInfo propertyBody = null;
            final AstToken token = event.getTrigger().getToken();
            if (token.isPropertyGetBody()) {
                propertyBody = this.createGetterMethod(modifiers, propertyType, name, fromLine,
                        fromColumn, toLine, toColumn);
            } else if (token.isPropertySetBody()) {
                propertyBody = this.createSetterMethod(modifiers, propertyType, name, fromLine,
                        fromColumn, toLine, toColumn);
            }

            this.startPropertyBody(propertyBody);
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.EXIT_BLOCK)) {
            this.endPropertyBody();
        } else if (isActive() && stateManager.isInPreDeclaration()) {
            if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
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
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
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

    protected void endPropertyBody() {
        UnresolvedMethodInfo builtMethod = buildingUnitStack.pop();
        registBuiltData(builtMethod);

        buildManager.endCallableUnitDefinition();
    }

    private void registModifiers() {
        this.propertyModifierStack.push(this.modifierBuilder.popLastBuiltData());
    }

    private void registType() {
        this.propertyTypeStack.push(this.typeBuilder.popLastBuiltData());
    }

    private void registName() {
        String[] name = this.nameBuilder.popLastBuiltData();
        assert name.length > 0 : "Illeagal state : property name is not found";
        if (0 < name.length) {
            this.propertyNameStack.push(name[0]);
        }
    }

    private UnresolvedMethodInfo createSetterMethod(final ModifierInfo[] modifiers,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedMethodInfo setterMethod = createMethodInfo(modifiers, VoidTypeInfo
                .getInstance(), name, fromLine, fromColumn, toLine, toColumn);

        final UnresolvedParameterInfo defaultParameter = new UnresolvedParameterInfo("value", type,
                -1, setterMethod, 0, 0, 0, 0);
        setterMethod.addParameter(defaultParameter);

        return setterMethod;
    }

    private UnresolvedMethodInfo createGetterMethod(final ModifierInfo[] modifiers,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        return createMethodInfo(modifiers, type, name, fromLine, fromColumn, toLine, toColumn);
    }

    private UnresolvedMethodInfo createMethodInfo(final ModifierInfo[] modifiers,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedMethodInfo method = new UnresolvedMethodInfo(this.buildManager
                .getCurrentClass(), fromLine, fromColumn, toLine, toColumn);

        for (ModifierInfo modifier : modifiers) {
            method.addModifier(modifier);
        }
        method.setReturnType(type);

        method.setMethodName(name);

        return method;
    }

    protected UnresolvedMethodInfo startPropertyBody(final UnresolvedMethodInfo propetyBody) {

        this.buildingUnitStack.push(propetyBody);

        this.buildManager.startCallableUnitDefinition(propetyBody);

        this.buildManager.enterMethodBlock();

        return propetyBody;
    }

    private void endPropertyDefinition() {
        this.propertyModifierStack.pop();
        this.propertyTypeStack.pop();
        this.propertyNameStack.pop();
    }

    protected final Stack<ModifierInfo[]> propertyModifierStack = new Stack<ModifierInfo[]>();

    protected final Stack<UnresolvedTypeInfo<? extends TypeInfo>> propertyTypeStack = new Stack<UnresolvedTypeInfo<? extends TypeInfo>>();

    protected final Stack<String> propertyNameStack = new Stack<String>();

    protected final Stack<UnresolvedMethodInfo> buildingUnitStack = new Stack<UnresolvedMethodInfo>();

    protected final BuildDataManager buildManager;

    protected final TypeBuilder typeBuilder;

    protected final ModifiersBuilder modifierBuilder;

    protected final NameBuilder nameBuilder;

    protected final CallableUnitStateManager stateManager;

    protected final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

}
