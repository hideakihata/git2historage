package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


@SuppressWarnings("serial")
public class MemberImportStatementInfo extends ImportStatementInfo<Member> {

    /**
     * オブジェクトを初期化
     *
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public MemberImportStatementInfo(final String[] importName, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(importName, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    @Override
    public Set<Member> getImportedUnits() {

        final String[] importName = this.getImportName();
        final String[] fullQualifiedName = Arrays.copyOf(importName, importName.length - 1);
        final ClassInfoManager classManager = DataManager.getInstance().getClassInfoManager();
        ClassInfo ownerClass = classManager.getClassInfo(fullQualifiedName);
        if (null == ownerClass) {
            ownerClass = new ExternalClassInfo(fullQualifiedName);
            classManager.add(ownerClass);
        }

        final Set<Member> importedMembers = new TreeSet<Member>();
        if (importName[importName.length - 1].equals("*")) {
            final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();
            final SortedSet<FieldInfo> staticFields = StaticOrInstanceProcessing
                    .getStaticMembers(fields);
            importedMembers.addAll(staticFields);
            final SortedSet<MethodInfo> methods = ownerClass.getDefinedMethods();
            final SortedSet<MethodInfo> staticMethods = StaticOrInstanceProcessing
                    .getStaticMembers(methods);
            importedMembers.addAll(staticMethods);
        }

        else {
            final String memberName = importName[importName.length - 1];
            final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();
            for (final FieldInfo field : fields) {
                if (memberName.equals(field.getName())
                        && field.getModifiers().contains(JavaPredefinedModifierInfo.STATIC)) {
                    importedMembers.add(field);
                }
            }
            final SortedSet<MethodInfo> methods = ownerClass.getDefinedMethods();
            for (final MethodInfo method : methods) {
                if (memberName.equals(method.getMethodName())
                        && method.getModifiers().contains(JavaPredefinedModifierInfo.STATIC)) {
                    importedMembers.add(method);
                }
            }
        }

        return importedMembers;
    }

    @Override
    public NamespaceInfo getNamespace() {
        final String[] importName = this.getImportName();
        final String[] namespace = Arrays.copyOf(importName, importName.length - 2);
        return new NamespaceInfo(namespace);
    }
}
