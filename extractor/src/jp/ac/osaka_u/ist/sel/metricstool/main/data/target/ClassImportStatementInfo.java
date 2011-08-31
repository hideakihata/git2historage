package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


/**
 * import文を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ClassImportStatementInfo extends ImportStatementInfo<ClassInfo> {

    /**
     * オブジェクトを初期化
     * @param fromLine 
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ClassImportStatementInfo(final String[] namespace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(namespace, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * インポートされたクラスのSortedSetを返す
     * 
     * @return　インポートされたクラスのSortedSet
     */
    @Override
    public Set<ClassInfo> getImportedUnits() {

        final SortedSet<ClassInfo> importedClasses = new TreeSet<ClassInfo>();
        final ClassInfoManager classManager = DataManager.getInstance().getClassInfoManager();
        final String[] importName = this.getImportName();
        if (importName[importName.length - 1].equals("*")) {
            final NamespaceInfo namespace = this.getNamespace();
            importedClasses.addAll(classManager.getClassInfos(namespace));
        }

        else {
            ClassInfo importedClass = classManager.getClassInfo(importName);
            if (null == importedClass) {
                importedClass = new ExternalClassInfo(importName);
                classManager.add(importedClass);
            }
            importedClasses.add(importedClass);
        }

        return importedClasses;
    }

    @Override
    public NamespaceInfo getNamespace() {
        final String[] importName = this.getImportName();
        final String[] namespace = Arrays.copyOf(importName, importName.length - 1);
        return new NamespaceInfo(namespace);
    }
}
