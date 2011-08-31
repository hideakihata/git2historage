package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedCaseLabelInfo extends UnresolvedExpressionInfo<ExpressionInfo> {

    public UnresolvedCaseLabelInfo(final UnresolvedExpressionInfo<?> label) {
        this.label = label;
        this.ownerCaseEntry = null;
        this.setFromLine(label.getFromLine());
        this.setFromColumn(label.getFromColumn());
        this.setToLine(label.getToLine());
        this.setToColumn(label.getToColumn());
    }

    public UnresolvedExpressionInfo<?> getLabel() {
        return this.label;
    }

    public UnresolvedCaseEntryInfo getOwnerCaseEntry() {
        return this.ownerCaseEntry;
    }

    public void setOwnerCaseEntry(final UnresolvedCaseEntryInfo ownerCaseEntry) {

        if (null == ownerCaseEntry) {
            throw new IllegalArgumentException();
        }

        this.ownerCaseEntry = ownerCaseEntry;
    }

    @Override
    public ExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        if (null == this.getOwnerCaseEntry()) {
            throw new IllegalStateException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // このラベルオブジェクトの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // switch文の式の型を調べる 
        final UnresolvedCaseEntryInfo unresolvedOwnerCaseEntry = this.getOwnerCaseEntry();
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = unresolvedOwnerCaseEntry
                .getOwnerSwitchBlock();
        final UnresolvedExpressionInfo<?> unresolvedExpression = (UnresolvedExpressionInfo<?>) unresolvedOwnerSwitchBlock
                .getConditionalClause().getCondition();
        final ExpressionInfo expression = unresolvedExpression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final TypeInfo type = expression.getType();

        final UnresolvedExpressionInfo<?> unresolvedLabel = this.getLabel();

        // プリミティブ型かそのラッパークラスのとき
        if ((type instanceof PrimitiveTypeInfo) || PrimitiveTypeInfo.isJavaWrapperType(type)) {

            this.resolvedInfo = unresolvedLabel.resolve(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
            return this.resolvedInfo;
        }

        //　型が不明なとき
        else if (type instanceof UnknownTypeInfo) {

            // TODO とりあえずプリミティブ型と同じように解析する．問題ありの可能性．
            this.resolvedInfo = unresolvedLabel.resolve(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
            return this.resolvedInfo;
        }

        //　それ以外のとき
        else {

            if (!(unresolvedLabel instanceof UnresolvedVariableUsageInfo<?>)) {
                throw new IllegalStateException();
            }

            if (!(type instanceof ClassTypeInfo)) {
                throw new IllegalStateException();
            }

            final String name = ((UnresolvedVariableUsageInfo<?>) unresolvedLabel).getUsedVariableName();

            final CallableUnitInfo ownerMethod = expression.getOwnerMethod();

            final ClassInfo referencedClass = ((ClassTypeInfo) type).getReferencedClass();
            // TODO 本来はenumで列挙されているものはサブクラスとして解析されているべき
            //for (final ClassInfo subClass : referencedClass.getSubClasses()) {
            //    if (subClass.getClassName().equals(name)) {
            //        this.resolvedInfo = new CaseLabelInfo(new ClassReferenceInfo(new ClassTypeInfo(
            //                subClass), ownerMethod, fromLine, fromColumn, toLine, toColumn),
            //                fromLine, fromColumn, toLine, toColumn);
            //        return this.resolvedInfo; 
            //    }
            //}
            for (final FieldInfo field : referencedClass.getDefinedFields()) {
                if (field.getName().equals(name)) {

                    final String[] referencedClassFQName = referencedClass.getFullQualifiedName();
                    final String[] fqName = new String[referencedClassFQName.length + 1];
                    System.arraycopy(referencedClassFQName, 0, fqName, 0,
                            referencedClassFQName.length);
                    fqName[fqName.length - 1] = name;
                    ClassInfo innerClass = classInfoManager.getClassInfo(fqName);
                    if (null == innerClass) {
                        innerClass = new ExternalInnerClassInfo(fqName, referencedClass);
                        classInfoManager.add(innerClass);
                    }
                    this.resolvedInfo = new ClassReferenceInfo(new ClassTypeInfo(innerClass),
                            ownerMethod, fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }
            }

            // 外部クラスの場合は，サブクラスがあるものとする
            if (referencedClass instanceof ExternalClassInfo) {
                final String[] referencedClassFQName = referencedClass.getFullQualifiedName();
                final String[] fqName = new String[referencedClassFQName.length + 1];
                System.arraycopy(referencedClassFQName, 0, fqName, 0, referencedClassFQName.length);
                referencedClassFQName[referencedClassFQName.length - 1] = name;
                ClassInfo innerClass = classInfoManager.getClassInfo(fqName);
                if (null == innerClass) {
                    innerClass = new ExternalInnerClassInfo(fqName, referencedClass);
                    classInfoManager.add(innerClass);
                }
                this.resolvedInfo = new ClassReferenceInfo(new ClassTypeInfo(innerClass),
                        ownerMethod, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            assert false : "Here shouldn't be reached.";
        }

        return null;
    }

    final private UnresolvedExpressionInfo<?> label;

    private UnresolvedCaseEntryInfo ownerCaseEntry;
}
