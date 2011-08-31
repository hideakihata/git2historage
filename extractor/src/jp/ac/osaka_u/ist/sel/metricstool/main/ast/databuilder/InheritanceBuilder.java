package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InheritanceDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InheritanceDefinitionStateManager.INHERITANCE_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author kou-tngt, t-miyake
 *
 */
public class InheritanceBuilder extends CompoundDataBuilder<UnresolvedClassTypeInfo> {

    public InheritanceBuilder(BuildDataManager buildManager) {
        this(buildManager, new TypeBuilder(buildManager));
    }

    public InheritanceBuilder(final BuildDataManager buildDataManager, TypeBuilder typebuilder) {

        this.buildDataManager = buildDataManager;

        this.typeBuilder = typebuilder;

        addInnerBuilder(typebuilder);
        typebuilder.deactivate();

        this.addStateManager(inheritanceStateManager);
        this.addStateManager(typeStateManager);

    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (isActive() && inheritanceStateManager.isEntered()) {
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)) {
                typeBuilder.activate();
            } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) {
                if (!typeStateManager.isEntered()) {
                    typeBuilder.deactivate();
                    buildInheritance();
                }
            }
        } else if (isActive()
                && type
                        .equals(InheritanceDefinitionStateManager.INHERITANCE_STATE.EXIT_INHERITANCE_DEF)) {

            //Superクラスが空(extendsしてるクラスが存在しない)ならObjectクラスを親クラスとして追加
            UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
            if (classInfo.isEnum()) {
                //String[] enumFullQualifiedName = {"java", "lang", "enum"};
                UnresolvedClassTypeInfo enumTypeInfo = new UnresolvedClassTypeInfo(
                        new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] {
                                "java", "lang", "Enum" });
                classInfo.addSuperClass(enumTypeInfo);
            } else if (!classInfo.isInterface() && classInfo.getSuperClasses().isEmpty()) {
                final String[] fqname = classInfo.getFullQualifiedName();
                //Objectクラスそのものには追加しない
                if (3 == fqname.length) {
                    if (fqname[0].equals("java") && fqname[1].equals("lang")
                            && fqname[2].equals("Object")) {
                        return;
                    }
                }
                classInfo.addSuperClass(OBJECT);
            }

        }
    }

    private void buildInheritance() {
        UnresolvedTypeInfo<? extends TypeInfo> type = typeBuilder.popLastBuiltData();

        if (type instanceof UnresolvedClassTypeInfo) {
            UnresolvedClassTypeInfo classType = (UnresolvedClassTypeInfo) type;
            registBuiltData(classType);

            if (null != buildDataManager) {
                UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
                if (null != classInfo) {
                    classInfo.addSuperClass(classType);
                }
            }
        }
    }

    private final TypeBuilder typeBuilder;

    private final InheritanceDefinitionStateManager inheritanceStateManager = new InheritanceDefinitionStateManager();

    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    private final BuildDataManager buildDataManager;

    private static final UnresolvedClassTypeInfo OBJECT = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "Object" });

}
