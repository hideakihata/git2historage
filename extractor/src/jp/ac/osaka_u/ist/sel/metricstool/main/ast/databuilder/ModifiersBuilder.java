package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


public class ModifiersBuilder extends CompoundDataBuilder<ModifierInfo[]> { //StateDrivenDataBuilder<ModifierInfo[]>{
    public ModifiersBuilder() {
        addStateManager(this.stateMachine);

        //アノテーションを識別するために追加
        this.addInnerBuilder(this.annotationBuilder);
    }

    @Override
    public void entered(AstVisitEvent event) {
        super.entered(event);

        if (isActive() && stateMachine.isEntered()) {
            AstToken token = event.getToken();
            if (token.isModifier()) {
                modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(token.toString()));
            }
        }
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        if (isActive()) {
            if (event.getType().equals(
                    ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                this.annotationBuilder.activate();
            } else if (event.getType().equals(
                    ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                registBuiltData(buildModifiers());
                this.annotationBuilder.deactivate();
            }
        }
    }

    private ModifierInfo[] buildModifiers() {
        //アノテーションをModifierとして追加
        modifiers.addAll(this.annotationBuilder.getAnnotations());
        this.annotationBuilder.clearAnnotations(); 
        ModifierInfo[] result = new ModifierInfo[modifiers.size()];
        modifiers.toArray(result);
        modifiers.clear();
        return result;
    }

    private final List<ModifierInfo> modifiers = new ArrayList<ModifierInfo>();

    private final ModifiersDefinitionStateManager stateMachine = new ModifiersDefinitionStateManager();

    private final AnnotationBuilder annotationBuilder = new AnnotationBuilder();
}
