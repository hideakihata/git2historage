package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.ArrayList;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;


/**
 * @author kou-tngt, t-miyake
 *
 */
public class JavaEnumElementBuilder extends CompoundDataBuilder<UnresolvedFieldInfo> {

    public JavaEnumElementBuilder(BuildDataManager buildDataManager,
            ExpressionElementManager expressionManager) {

        if (null == buildDataManager) {
            throw new NullPointerException("builderManager is null.");
        }
        if (null == expressionManager) {
            throw new NullPointerException("expressionElementManager is null.");
        }

        this.buildManager = buildDataManager;
        this.expressionManager = expressionManager;

        addStateManager(this.stateManager);
        addInnerBuilder(this.nameBuilder);
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        if (isActive()) {
            if (type.equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.ENTER_ENUM_ELEMENT)) {
                nameBuilder.clearBuiltData();
                enumClassStack.push(buildManager.getCurrentClass());
                nameBuilder.activate();

                AstVisitEvent trigger = event.getTrigger();
                buildAnonymousClass(trigger.getStartLine(), trigger.getStartColumn(), trigger
                        .getEndLine(), trigger.getEndColumn());

            } else if (type
                    .equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.EXIT_ENUM_ELEMENT)) {
                nameBuilder.deactivate();
                endAnonymousClass();
                AstVisitEvent trigger = event.getTrigger();
                buildEnumElement(trigger.getStartLine(), trigger.getStartColumn(), trigger
                        .getEndLine(), trigger.getEndColumn());
                this.expressionElementList.clear();

            } else if (type
                    .equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.ENTER_ENUM_ANONYMOUS_CLASS)) {

            } else if (type
                    .equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.EXIT_ENUM_ANONYMOUS_CLASS)) {

            } else if (type
                    .equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.ENTER_ENUM_ARGUMENT)) {

            } else if (type
                    .equals(JavaEnumElementStateManager.ENUM_ELEMENT_STATE.EXIT_ENUM_ARGUMENT)) {
                // this.expressionManager.getLaterExpressionElements();
                this.getExpressionListFromExpressionElementManager();
            }
        }
    }

    protected void buildAnonymousClass(int startLine, int startColumn, int endLine, int endColumn) {
        if (null != buildManager && !enumClassStack.isEmpty()) {
            UnresolvedClassInfo enumClass = enumClassStack.peek();

            final FileInfo currentFile = DataManager.getInstance().getFileInfoManager()
                    .getCurrentFile(Thread.currentThread());
            assert null != currentFile : "Illegal state: the file information was not registered to FileInfoManager";

            UnresolvedClassInfo enumAnonymous = new UnresolvedClassInfo(currentFile,
                    this.buildManager.getCurrentUnit());
            int count = buildManager.getAnonymousClassCount(enumClass);
            enumAnonymous.setClassName(enumClass.getClassName()
                    + JavaAnonymousClassBuilder.JAVA_ANONYMOUSCLASS_NAME_MARKER + count);

            UnresolvedClassTypeInfo superClassReference = new UnresolvedClassTypeInfo(
                    UnresolvedClassImportStatementInfo.getClassImportStatements(buildManager
                            .getAllAvaliableNames()), enumClass.getFullQualifiedName());

            enumAnonymous.addSuperClass(superClassReference);

            enumAnonymous.setFromLine(startLine);
            enumAnonymous.setFromColumn(startColumn);
            enumAnonymous.setToLine(endLine);
            enumAnonymous.setToColumn(endColumn);

            buildManager.startClassDefinition(enumAnonymous);
            buildManager.enterClassBlock();
        }
    }

    protected void buildEnumElement(int startLine, int startColumn, int endLine, int endColumn) {
        String[] name = nameBuilder.getFirstBuiltData();
        if (null != name && name.length > 0 && !enumClassStack.isEmpty() && null != buildManager) {
            String elementName = name[0];
            UnresolvedClassInfo enumClass = enumClassStack.peek();
            //Enumは列挙子と同じ名前のフィールドを持つをみなす
            UnresolvedClassConstructorCallInfo _initializer = new UnresolvedClassConstructorCallInfo(
                    enumClass.getClassType());

            //列挙子に含まれる引数を追加
            for (final ExpressionElement expressionElement : this.expressionElementList) {
                _initializer.addArgument(expressionElement.getUsage());
            }

            //a.addArguments(null);
            //UnresolvedExpressionInfo<? extends ExpressionInfo> initializer = new UnresolvedClassConstructorCallInfo(enumClass.getClassType());

            UnresolvedFieldInfo element = new UnresolvedFieldInfo(elementName,
                    UnresolvedClassTypeInfo.getInstance(enumClass), enumClass, _initializer,
                    startLine, startColumn, endLine, endColumn);
            //Enumのフィールドはpublic final static扱い
            element.addModifier(JavaPredefinedModifierInfo.STATIC);
            element.addModifier(JavaPredefinedModifierInfo.PUBLIC);
            element.addModifier(JavaPredefinedModifierInfo.FINAL);
            
            //modifierInterpriter.interprit(defaultModifiers, element, element);

            buildManager.addField(element);
            enumClass.addDefinedField(element);
        }
    }

    protected void endAnonymousClass() {
        buildManager.endClassDefinition();
    }

    /**
     * 列挙子に引数が入っている場合expressionElementListに格納する
     * 直接ExpressionManagerを触っているのであんまりスマートでない??
     */
    private void getExpressionListFromExpressionElementManager() {
        final int count = this.expressionManager.getExpressionStackSize();
        for (int i = 0; i < count; i++) {
            this.expressionElementList.add(this.expressionManager.popExpressionElement());
        }
    }

    private final ExpressionElementManager expressionManager;

    private final JavaEnumElementStateManager stateManager = new JavaEnumElementStateManager();

    private final BuildDataManager buildManager;

    private final NameBuilder nameBuilder = new NameBuilder();

    private final Stack<UnresolvedClassInfo> enumClassStack = new Stack<UnresolvedClassInfo>();

    // private final JavaModifiersInterpriter modifierInterpriter = new JavaModifiersInterpriter();

    private ArrayList<ExpressionElement> expressionElementList = new ArrayList<ExpressionElement>();
    /*
    private static final ModifierInfo[] defaultModifiers = new ModifierInfo[] {
            JavaPredefinedModifierInfo.getModifierInfo("public"),
            JavaPredefinedModifierInfo.getModifierInfo("static"),
            JavaPredefinedModifierInfo.getModifierInfo("final") };
     */
}
