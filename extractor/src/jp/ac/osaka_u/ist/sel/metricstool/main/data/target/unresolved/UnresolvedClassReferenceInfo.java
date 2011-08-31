package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決クラス参照を表すクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedClassReferenceInfo extends UnresolvedExpressionInfo<ExpressionInfo> {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedClassReferenceInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.fullReferenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.qualifierUsage = null;
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<?>>();
    }

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     * @param ownerUsage 親参照
     */
    public UnresolvedClassReferenceInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName, final UnresolvedClassReferenceInfo ownerUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerUsage)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        String[] ownerReferenceName = ownerUsage.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length + ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length,
                referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.qualifierUsage = ownerUsage;
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<?>>();
    }

    @Override
    public ExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        /*// 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        final String[] referenceName = this.getReferenceName();

        if (this.hasOwnerReference()) {

            final UnresolvedClassReferenceInfo unresolvedClassReference = this.getQualifierUsage();
            ExpressionInfo classReference = unresolvedClassReference.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert null != classReference : "null is returned!";

            NEXT_NAME: for (int i = 0; i < referenceName.length; i++) {

                // 親が UnknownTypeInfo だったら，どうしようもない
                if (classReference.getType() instanceof UnknownTypeInfo) {

                    this.resolvedInfo = new UnknownEntityUsageInfo(referenceName, usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                    return this.resolvedInfo;

                    // 親が対象クラス(TargetClassInfo)の場合
                } else if (classReference.getType() instanceof ClassTypeInfo) {

                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference.getType())
                            .getReferencedClass();

                    // インナークラスから探すので一覧を取得
                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                            .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                    .getType()).getReferencedClass());
                    for (final InnerClassInfo innerClass : innerClasses) {

                        final ClassInfo innerClassInfo = (ClassInfo) innerClass;

                        // 一致するクラス名が見つかった場合
                        if (referenceName[i].equals(innerClassInfo.getClassName())) {
                            // TODO 利用関係を構築するコードが必要？

                            // TODO 型パラメータ情報を追記する処理が必要
                            final ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                            classReference = new ClassReferenceInfo(reference, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                            continue NEXT_NAME;
                        }
                    }

                    // 見つからなくても外部クラスの場合はしょうがない
                    if (ownerClass instanceof ExternalClassInfo) {
                        classReference = new UnknownEntityUsageInfo(referenceName, usingMethod,
                                fromLine, fromColumn, toLine, toColumn);
                        /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                        continue NEXT_NAME;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                assert false : "Here shouldn't be reached!";
            }

            this.resolvedInfo = classReference;
            return this.resolvedInfo;

        } else {

            // 未解決参照型が UnresolvedFullQualifiedNameReferenceTypeInfo ならば，完全限定名参照であると判断できる
            if (this instanceof UnresolvedFullQualifiedNameClassReferenceInfo) {

                ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null == classInfo) {
                    classInfo = new ExternalClassInfo(referenceName);
                    classInfoManager.add(classInfo);
                }

                // TODO 型パラメータ情報を追記する処理が必要
                final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                this.resolvedInfo = new ClassReferenceInfo(reference, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                return this.resolvedInfo;
            }

            // 参照名が完全限定名であるとして検索
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {

                    // TODO　型パラメータ情報を追記する処理が必要
                    final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                    this.resolvedInfo = new ClassReferenceInfo(reference, usingMethod, fromLine,
                            fromColumn, toLine, toColumn);
                    /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                    return this.resolvedInfo;
                }
            }

            // 利用可能なインナークラス名から探す
            {
                final ClassInfo outestClass;
                if (usingClass instanceof InnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final ClassInfo innerClassInfo : ClassInfo.convert(NameResolver
                        .getAvailableInnerClasses(outestClass))) {

                    if (innerClassInfo.getClassName().equals(referenceName[0])) {

                        // availableField.getType() から次のword(name[i])を名前解決
                        // TODO 型パラメータ情報を格納する処理が必要
                        ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                        ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                        /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                        NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (classReference.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(referenceName,
                                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                                return this.resolvedInfo;

                                // 親がクラス型の場合
                            } else if (classReference.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass();

                                // インナークラスから探すので一覧を取得
                                final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                        .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass());
                                for (final ClassInfo innerClass : ClassInfo.convert(innerClasses)) {

                                    // 一致するクラス名が見つかった場合
                                    if (referenceName[i].equals(innerClass.getClassName())) {
                                        // TODO 利用関係を構築するコードが必要？

                                        // TODO　型パラメータ情報を格納する処理が必要
                                        reference = new ClassTypeInfo(innerClass);
                                        classReference = new ClassReferenceInfo(reference,
                                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                                        /*classReference
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        continue NEXT_NAME;
                                    }
                                }

                                // 親が外部クラス(ExternalClassInfo)の場合
                                if (ownerClass instanceof ExternalClassInfo) {

                                    classReference = new UnknownEntityUsageInfo(referenceName,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                    /*classReference
                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                    continue NEXT_NAME;
                                }
                            }

                            assert false : "Here shouldn't be reached!";
                        }

                        this.resolvedInfo = classReference;
                        return this.resolvedInfo;
                    }
                }
            }

            // 利用可能な名前空間から型名を探す
            {
                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // 名前空間名.* となっている場合
                    if (availableNamespace.isAll()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // 名前空間の下にある各クラスに対して
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {

                            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                            final String className = classInfo.getClassName();
                            if (className.equals(referenceName[0])) {

                                // availableField.getType() から次のword(name[i])を名前解決
                                // TODO 型パラメータ情報を格納する処理が必要
                                ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                                ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                                /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                                NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                    // 親が UnknownTypeInfo だったら，どうしようもない
                                    if (classReference.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = new UnknownEntityUsageInfo(
                                                referenceName, usingMethod, fromLine, fromColumn,
                                                toLine, toColumn);
                                        /*this.resolvedInfo
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        return this.resolvedInfo;

                                        // 親がクラス型の場合
                                    } else if (classReference.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass();

                                        // インナークラスから探すので一覧を取得
                                        final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                        .getType()).getReferencedClass());
                                        for (final ClassInfo innerClass : ClassInfo
                                                .convert(innerClasses)) {

                                            // 一致するクラス名が見つかった場合
                                            if (referenceName[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                // TODO 型パラメータ情報を格納する処理が必要
                                                reference = new ClassTypeInfo(innerClass);
                                                classReference = new ClassReferenceInfo(reference,
                                                        usingMethod, fromLine, fromColumn, toLine,
                                                        toColumn);
                                                /*classReference
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                                continue NEXT_NAME;
                                            }
                                        }

                                        // 親が外部クラス(ExternalClassInfo)の場合
                                        if (ownerClass instanceof ExternalClassInfo) {

                                            classReference = new UnknownEntityUsageInfo(
                                                    referenceName, usingMethod, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            /*classReference
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";
                                }

                                this.resolvedInfo = classReference;
                                return this.resolvedInfo;
                            }
                        }

                        // 名前空間.クラス名 となっている場合
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (importName[importName.length - 1].equals(referenceName[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add(specifiedClassInfo);
                            }

                            // TODO 型パラメータ情報を格納する処理が必要
                            ClassTypeInfo reference = new ClassTypeInfo(specifiedClassInfo);
                            ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                    usingMethod, fromLine, fromColumn, toLine, toColumn);
                            /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                            NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                // 親が UnknownTypeInfo だったら，どうしようもない
                                if (classReference.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = new UnknownEntityUsageInfo(referenceName,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                    /*this.resolvedInfo
                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                    return this.resolvedInfo;

                                    // 親がクラス型の場合
                                } else if (classReference.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                            .getType()).getReferencedClass();

                                    // インナークラス一覧を取得
                                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                    .getType()).getReferencedClass());
                                    for (final ClassInfo innerClass : ClassInfo
                                            .convert(innerClasses)) {

                                        // 一致するクラス名が見つかった場合
                                        if (referenceName[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            // TODO 型パラメータ情報を格納する処理が必要
                                            reference = new ClassTypeInfo(innerClass);
                                            classReference = new ClassReferenceInfo(reference,
                                                    usingMethod, fromLine, fromColumn, toLine,
                                                    toColumn);
                                            /*classReference
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            continue NEXT_NAME;
                                        }
                                    }

                                    // 親が外部クラス(ExternalClassInfo)の場合
                                    if (ownerClass instanceof ExternalClassInfo) {

                                        classReference = new UnknownEntityUsageInfo(referenceName,
                                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                                        /*classReference
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        continue NEXT_NAME;
                                    }
                                }

                                assert false : "Here shouldn't be reached!";
                            }

                            this.resolvedInfo = classReference;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        /*
         * if (null == usingMethod) { err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter())); } else {
         * err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter()) + "#" +
         * usingMethod.getMethodName() + "\"."); }
         */

        // 見つからなかった場合は，UknownTypeInfo を返す
        this.resolvedInfo = new UnknownEntityUsageInfo(referenceName, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeArgument 追加する型パラメータ使用
     */
    public final void addTypeArgument(final UnresolvedTypeInfo<?> typeArgument) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * このクラス参照で使用されている型パラメータの List を返す
     * 
     * @return このクラス参照で使用されている型パラメータの List
     */
    public final List<UnresolvedTypeInfo<?>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型のownerも含めた参照名を返す
     * 
     * @return この参照型のownerも含めた参照名を返す
     */
    public final String[] getFullReferenceName() {
        return Arrays.<String> copyOf(this.fullReferenceName, this.fullReferenceName.length);
    }

    /**
     * この参照型の参照名を返す
     * 
     * @return この参照型の参照名を返す
     */
    public final String[] getReferenceName() {
        return Arrays.<String> copyOf(this.referenceName, this.referenceName.length);
    }

    /**
     * この参照型がくっついている未解決参照型を返す
     * 
     * @return この参照型がくっついている未解決参照型
     */
    public final UnresolvedClassReferenceInfo getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * この参照型が，他の参照型にくっついているかどうかを返す
     * 
     * @return くっついている場合は true，くっついていない場合は false
     */
    public final boolean hasOwnerReference() {
        return null != this.qualifierUsage;
    }

    /**
     * この参照型の参照名を引数で与えられた文字で結合して返す
     * 
     * @param delimiter 結合に用いる文字
     * @return この参照型の参照名を引数で与えられた文字で結合した文字列
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * この参照型の完全限定名として可能性のある名前空間名の一覧を返す
     * 
     * @return この参照型の完全限定名として可能性のある名前空間名の一覧
     */
    public final List<UnresolvedClassImportStatementInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * 引数で与えられた未解決型情報を表す解決済み型情報クラスを生成する． ここで引数として与えられるのは，ソースコードがパースされていない型であるので，生成する解決済み型情報クラスは
     * ExternalClassInfo となる．
     * 
     * @param unresolvedReferenceType 未解決型情報
     * @return 解決済み型情報
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedClassReferenceInfo unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        // 未解決クラス情報の参照名を取得
        final String[] referenceName = unresolvedReferenceType.getReferenceName();

        // 利用可能な名前空間を検索し，未解決クラス情報の完全限定名を決定
        for (final UnresolvedClassImportStatementInfo availableNamespace : unresolvedReferenceType
                .getAvailableNamespaces()) {

            // 名前空間名.* となっている場合は，見つけることができない
            if (availableNamespace.isAll()) {
                continue;
            }

            // 名前空間.クラス名 となっている場合
            final String[] importName = availableNamespace.getImportName();

            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
            if (importName[importName.length - 1].equals(referenceName[0])) {

                final String[] namespace = availableNamespace.getNamespace();
                final String[] fullQualifiedName = new String[namespace.length
                        + referenceName.length];
                System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                        referenceName.length);

                final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
                return classInfo;
            }
        }

        // 見つからない場合は，名前空間が UNKNOWN な 外部クラス情報を作成
        final ExternalClassInfo unknownClassInfo = new ExternalClassInfo(
                referenceName[referenceName.length - 1]);
        return unknownClassInfo;
    }

    /**
     * 未解決参照型を与えると，その未解決クラス参照を返す
     * 
     * @param referenceType 未解決参照型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return 未解決クラス参照
     */
    public final static UnresolvedClassReferenceInfo createClassReference(
            final UnresolvedClassTypeInfo referenceType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedClassReferenceInfo reference = new UnresolvedClassReferenceInfo(
                referenceType.getAvailableNamespaces(), referenceType.getReferenceName());
        reference.setOuterUnit(outerUnit);
        reference.setFromLine(fromLine);
        reference.setFromColumn(fromColumn);
        reference.setToLine(toLine);
        reference.setToColumn(toColumn);

        return reference;
    }

    /**
     * 利用可能な名前空間名を保存するための変数，名前解決処理の際に用いる
     */
    private final List<UnresolvedClassImportStatementInfo> availableNamespaces;

    /**
     * 参照名を保存する変数
     */
    private final String[] referenceName;

    /**
     * ownerも含めた参照名を保存する変数
     */
    private final String[] fullReferenceName;

    /**
     * この参照がくっついている未解決参照型を保存する変数
     */
    private final UnresolvedClassReferenceInfo qualifierUsage;

    /**
     * 未解決型パラメータ使用を保存するための変数
     */
    private final List<UnresolvedTypeInfo<?>> typeArguments;

}
