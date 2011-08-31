package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DefaultBuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedImportStatementInfo;


/**
 * @author kou-tang, t-miyake
 *
 */
public class JavaBuildManager extends DefaultBuildDataManager {

    @Override
    public void addField(UnresolvedFieldInfo field) {
        if (isInInterface()) {
            //field.setPublicVisible(true);
            //field.setInheritanceVisible(true);
            //field.setNamespaceVisible(true);
            //field.setPrivateVibible(false);
            //field.setInstanceMember(false);
        }

        super.addField(field);
    }

    @Override
    public UnresolvedClassInfo endClassDefinition() {
        //        UnresolvedClassInfo currentClass = getCurrentClass();
        //        String currentClassName = currentClass.getClassName();
        //        
        //        if (!isInInterface()){
        //            boolean hasConstructor = false;
        //            for(UnresolvedMethodInfo methodInfo : currentClass.getDefinedMethods()){
        //                if (methodInfo.isConstructor()){
        //                    hasConstructor = true;
        //                    break;
        //                }
        //            }
        //            
        //            if (!hasConstructor && ! currentClassName.contains(JavaAnonymousClassBuilder.JAVA_ANONYMOUSCLASS_NAME_MARKER)){
        //                //コンストラクタを持っていない　かつ　匿名クラスではない場合
        //                //デフォルトコンストラクタを作成して追加する.
        //                UnresolvedMethodInfo defaultCostructor = new UnresolvedMethodInfo(currentClassName,currentClass,currentClass,true);
        //                defaultCostructor.setInheritanceVisible(true);
        //                defaultCostructor.setNamespaceVisible(true);
        //                defaultCostructor.setPublicVisible(true);
        //                defaultCostructor.setPrivateVibible(false);
        //                defaultCostructor.setInstanceMember(true);
        //                startMethodDefinition(defaultCostructor);
        //                enterMethodBlock();
        //                endMethodDefinition();
        //            }
        //        }

        classOrInterfaceStack.pop();

        return super.endClassDefinition();
    }

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> endCallableUnitDefinition() {
        UnresolvedCallableUnitInfo<? extends CallableUnitInfo> method = super
                .endCallableUnitDefinition();
        if (isInInterface()) {
            //method.setPublicVisible(true);
            //method.setInheritanceVisible(true);
            //method.setNamespaceVisible(true);
            //method.setPrivateVibible(false);
        }
        return method;
    }

    /*
    @Override
    public void enterClassBlock() {
        super.enterClassBlock();

        /*
        UnresolvedClassInfo classInfo = getCurrentClass();
        if (classInfo.getSuperClasses().isEmpty()) {
            final String[] fqname = classInfo.getFullQualifiedName();
            if (3 == fqname.length) {
                if (fqname[0].equals("java") && fqname[1].equals("lang")
                        && fqname[2].equals("Object")) {
                    return;
                }
            }
            classInfo.addSuperClass(OBJECT);
        }
        */
    //}

    @Override
    public void startClassDefinition(UnresolvedClassInfo classInfo) {
        super.startClassDefinition(classInfo);
        classOrInterfaceStack.push(CLASS_OR_INTERFACE.CLASS);
    }

    public void toInterface() {
        if (!classOrInterfaceStack.isEmpty()) {
            this.getCurrentClass().setIsInterface();
            classOrInterfaceStack.pop();
            classOrInterfaceStack.push(CLASS_OR_INTERFACE.INTERFACE);
        }
    }

    public boolean isInInterface() {
        if (classOrInterfaceStack.isEmpty())
            return false;
        return classOrInterfaceStack.peek().equals(CLASS_OR_INTERFACE.INTERFACE);
    }

    @Override
    public List<UnresolvedImportStatementInfo<?>> getAvailableNameSpaceSet() {
        List<UnresolvedImportStatementInfo<?>> result = super.getAvailableNameSpaceSet();
        result.add(JAVA_LANG);
        return result;
    }

    private static final UnresolvedClassImportStatementInfo JAVA_LANG = new UnresolvedClassImportStatementInfo(
            new String[] { "java", "lang" }, true);

    private static enum CLASS_OR_INTERFACE {
        CLASS, INTERFACE
    }

    private final Stack<CLASS_OR_INTERFACE> classOrInterfaceStack = new Stack<CLASS_OR_INTERFACE>();

}
