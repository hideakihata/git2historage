package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.AnnotationStatementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AnnotationStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AnnotationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * アノテーションに関する情報を構築するビルダ
 * @author a-saitoh
 *
 */

public class AnnotationBuilder extends CompoundDataBuilder<ModifierInfo[]> {

    public AnnotationBuilder() {
        this.addStateManager(stateMachine);
        this.addInnerBuilder(this.identifierBuilder);

        this.annotationStatementBuilder.deactivate();
        this.addInnerBuilder(this.annotationStatementBuilder);
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType eventType = event.getType();
        if (eventType.equals(AnnotationStateManager.ANNOTATION_STATE.ENTER_ANNOTATION)) {
            this.identifierBuilder.activate();
        } else if (eventType
                .equals(AnnotationStateManager.ANNOTATION_STATE.ENTER_ANNOTATION_STRING)) {
            this.identifierBuilder.deactivate();
            this.annotationStatementBuilder.activate();
        } else if (eventType.equals(AnnotationStateManager.ANNOTATION_STATE.EXIT_ANNOTATION_STRING)) {
            this.annotationStatementBuilder.deactivate();
        } else if (event.getType().equals(AnnotationStateManager.ANNOTATION_STATE.EXIT_ANNOTATION)) {
            final String annotationName = this.identifierBuilder.popLastBuiltData()[0];
            final String annotationArgument = this.annotationStatementBuilder.getArguments();
            this.annotationStatementBuilder.clearAnnotationArguments();
            final ModifierInfo modifierInfo = new AnnotationInfo(annotationName, annotationArgument);//ModifierInfo.getModifierInfo(annotationTypeName);
            this.annotations.add(modifierInfo);
            this.annotationStatementBuilder.deactivate();
        }

        /* 真面目にアノテーションを解析するなら，下記のイベントを使って処理を記述する
        else if (eventType.equals(AnnotationStateManager.ANNOTATION_STATE.ENTER_ANNOTATION_ARRAY_INIT)
                || eventType.equals(AnnotationStateManager.ANNOTATION_STATE.ENTER_ANNOTATION_MEMBER)
                || eventType.equals(AnnotationStateManager.ANNOTATION_STATE.ENTER_ANNOTATION_MEMBER_VALUE_PAIR)){
            //とりあえずビルダを動かさない，後で実装
            this.identifierBuilder.deactivate();
        }  else if(eventType.equals(AnnotationStateManager.ANNOTATION_STATE.EXIT_ANNOTATION_ARRAY_INIT)
                || eventType.equals(AnnotationStateManager.ANNOTATION_STATE.EXIT_ANNOTATION_MEMBER)
                || eventType.equals(AnnotationStateManager.ANNOTATION_STATE.EXIT_ANNOTATION_MEMBER_VALUE_PAIR)){
            
        }
        */

    }

    public List<ModifierInfo> getAnnotations() {
        return annotations;
    }

    public void clearAnnotations() {
        annotations.clear();
    }

    private final List<ModifierInfo> annotations = new ArrayList<ModifierInfo>();

    private final AnnotationStateManager stateMachine = new AnnotationStateManager();

    private final AnnotationStatementBuilder annotationStatementBuilder = new AnnotationStatementBuilder();

    private final IdentifierBuilder identifierBuilder = new IdentifierBuilder();
}
