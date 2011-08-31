package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstanceProcessing;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決エンティティ使用を保存するためのクラス． 未解決エンティティ使用とは，パッケージ名やクラス名の参照 を表す．
 * 
 * @author higo
 * 
 */
public final class UnresolvedUnknownUsageInfo extends UnresolvedExpressionInfo<ExpressionInfo> {

    /**
     * 未解決エンティティ使用オブジェクトを作成する．
     * 
     * @param availableNamespaces 利用可能な名前空間
     * @param name 未解決エンティティ使用名
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedUnknownUsageInfo(
            final List<UnresolvedImportStatementInfo<?>> availableNamespaces, final String[] name,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this.availableNamespaces = availableNamespaces;
        this.name = Arrays.<String> copyOf(name, name.length);

        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);

    }

    @Override
    public ExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // エンティティ参照名を取得
        final String[] name = this.getName();

        // 位置情報を取得
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

        // 利用可能なインスタンスフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なインスタンスフィールド一覧を取得
            final List<FieldInfo> availableFieldsOfThisClass = StaticOrInstanceProcessing
                    .<FieldInfo> getInstanceMembers(NameResolver.getAvailableFields(usingClass,
                            usingClass));

            for (final FieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    // usingMethod.addReferencee(availableFieldOfThisClass);
                    // availableFieldOfThisClass.addReferencer(usingMethod);

                    // 親の型を生成
                    final ClassTypeInfo usingClassType = new ClassTypeInfo(usingClass);

                    // 暗黙的なクラス参照なので位置情報はすべて0
                    final ClassReferenceInfo classReference = new ClassReferenceInfo(
                            usingClassType, usingMethod, 0, 0, 0, 0);
                    /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                    // availableField.getType() から次のword(name[i])を名前解決
                    ExpressionInfo entityUsage = FieldUsageInfo.getInstance(classReference,
                            usingClassType, availableFieldOfThisClass, true, false, usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    /*entityUsage.setOwnerExecutableElement(ownerExecutableElement);*/

                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                            return this.resolvedInfo;

                            // 親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なインスタンスフィールド一覧を取得
                                final List<FieldInfo> availableFields = StaticOrInstanceProcessing
                                        .getInstanceMembers(NameResolver.getAvailableFields(
                                                ownerClass, usingClass));

                                for (final FieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        // usingMethod.addReferencee(availableField);
                                        // availableField.addReferencer(usingMethod);

                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        availableField, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();
                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass(referencedClass);
                                    if (!(referencedClass instanceof InnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        // usingMethod.addReferencee(fieldInfo);
                                        // fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                        found = true;
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                    }
                                }
                            }

                            // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                            {
                                if (!found && (ownerClass instanceof ExternalClassInfo)) {
                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], (ExternalClassInfo) ownerClass);
                                    entityUsage = FieldUsageInfo.getInstance(classReference,
                                            entityUsage.getType(), fieldInfo, true, false,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                }
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedInfo = entityUsage;
                    return this.resolvedInfo;
                }
            }
        }

        // 利用可能なスタティックフィールド名からエンティティ名を検索
        {
            // このクラスで利用可能なスタティックフィールド一覧を取得
            final List<FieldInfo> availableFieldsOfThisClass = StaticOrInstanceProcessing
                    .<FieldInfo> getStaticMembers(NameResolver.getAvailableFields(usingClass,
                            usingClass));

            for (final FieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // 一致するフィールド名が見つかった場合
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    // usingMethod.addReferencee(availableFieldOfThisClass);
                    // availableFieldOfThisClass.addReferencer(usingMethod);

                    // 親の型を生成
                    final ClassTypeInfo usingClassType = new ClassTypeInfo(usingClass);

                    // 暗黙的なクラス参照なので位置情報はすべて0
                    final ClassReferenceInfo classReference = new ClassReferenceInfo(
                            usingClassType, usingMethod, 0, 0, 0, 0);
                    /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                    // availableField.getType() から次のword(name[i])を名前解決
                    ExpressionInfo entityUsage = FieldUsageInfo.getInstance(classReference,
                            usingClassType, availableFieldOfThisClass, true, false, usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    /*entityUsage.setOwnerExecutableElement(ownerExecutableElement);*/

                    for (int i = 1; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                            return this.resolvedInfo;

                            // 親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なスタティックフィールド一覧を取得
                                final List<FieldInfo> availableFields = StaticOrInstanceProcessing
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                ownerClass, usingClass));

                                for (final FieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        // usingMethod.addReferencee(availableField);
                                        // availableField.addReferencer(usingMethod);

                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        availableField, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/

                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                            {
                                if (!found) {
                                    // インナークラス一覧を取得
                                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses(ownerClass);
                                    for (final ClassInfo innerClass : ClassInfo
                                            .convert(innerClasses)) {

                                        // 一致するクラス名が見つかった場合
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                    innerClass);
                                            entityUsage = new ClassReferenceInfo(referenceType,
                                                    usingMethod, fromLine, fromColumn, toLine,
                                                    toColumn);
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();
                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass(referencedClass);
                                    if (!(referencedClass instanceof InnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        // usingMethod.addReferencee(fieldInfo);
                                        // fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                        found = true;
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                    }
                                }

                                // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                                {
                                    if (!found && (ownerClass instanceof ExternalClassInfo)) {
                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], (ExternalClassInfo) ownerClass);
                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                    }
                                }
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedInfo = entityUsage;
                    return this.resolvedInfo;
                }
            }
        }

        // エンティティ名が完全限定名である場合を検索
        {

            for (int length = 1; length <= name.length; length++) {

                // 検索する名前(String[])を作成
                final String[] searchingName = new String[length];
                System.arraycopy(name, 0, searchingName, 0, length);

                final ClassInfo searchingClass = classInfoManager.getClassInfo(searchingName);
                if (null != searchingClass) {

                    final ClassReferenceInfo searchedClassReference = new ClassReferenceInfo(
                            new ClassTypeInfo(searchingClass), usingMethod, fromLine, fromColumn,
                            toLine, toColumn);
                    /*searchedClassReference.setOwnerExecutableElement(ownerExecutableElement);*/
                    ExpressionInfo entityUsage = searchedClassReference;

                    for (int i = length; i < name.length; i++) {

                        // 親が UnknownTypeInfo だったら，どうしようもない
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                            return this.resolvedInfo;

                            // 親がクラス型の場合
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // まずは利用可能なフィールド一覧を取得
                            boolean found = false;
                            {
                                // 利用可能なフィールド一覧を取得
                                final List<FieldInfo> availableFields = StaticOrInstanceProcessing
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                ownerClass, usingClass));

                                for (final FieldInfo availableField : availableFields) {

                                    // 一致するフィールド名が見つかった場合
                                    if (name[i].equals(availableField.getName())) {
                                        // usingMethod.addReferencee(availableField);
                                        // availableField.addReferencer(usingMethod);

                                        entityUsage = FieldUsageInfo.getInstance(
                                                searchedClassReference, entityUsage.getType(),
                                                availableField, true, false, usingMethod, fromLine,
                                                fromColumn, toLine, toColumn);
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                            {
                                if (!found) {

                                    // インナークラス一覧を取得
                                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses(ownerClass);
                                    for (final ClassInfo innerClass : ClassInfo
                                            .convert(innerClasses)) {

                                        // 一致するクラス名が見つかった場合
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO 利用関係を構築するコードが必要？

                                            final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                    innerClass);
                                            entityUsage = new ClassReferenceInfo(referenceType,
                                                    usingMethod, fromLine, fromColumn, toLine,
                                                    toColumn);
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                            // そのクラスのフィールドを使用しているとみなす
                            {
                                if (!found) {

                                    final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();
                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass(referencedClass);
                                    if (!(referencedClass instanceof InnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        // usingMethod.addReferencee(fieldInfo);
                                        // fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        entityUsage = FieldUsageInfo.getInstance(
                                                searchedClassReference, entityUsage.getType(),
                                                fieldInfo, true, false, usingMethod, fromLine,
                                                fromColumn, toLine, toColumn);
                                        found = true;
                                        /*entityUsage
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                    }
                                }
                            }

                            // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                            {
                                if (!found && (ownerClass instanceof ExternalClassInfo)) {
                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], (ExternalClassInfo) ownerClass);
                                    entityUsage = FieldUsageInfo.getInstance(
                                            searchedClassReference, entityUsage.getType(),
                                            fieldInfo, true, false, usingMethod, fromLine,
                                            fromColumn, toLine, toColumn);
                                }
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedInfo = entityUsage;
                    return this.resolvedInfo;
                }
            }
        }

        // 利用可能なクラス名からエンティティ名を検索
        {

            // 利用可能な名前空間から検索
            {
                for (final UnresolvedClassImportStatementInfo availableNamespace : UnresolvedClassImportStatementInfo
                        .getClassImportStatements(this.getAvailableNamespaces())) {

                    // 名前空間名.* となっている場合
                    if (availableNamespace.isAll()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // 名前空間の下にある各クラスに対して
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                            final String className = classInfo.getClassName();

                            // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                            if (className.equals(name[0])) {

                                final ClassReferenceInfo classReference = new ClassReferenceInfo(
                                        new ClassTypeInfo(classInfo), usingMethod, fromLine,
                                        fromColumn, toLine, toColumn);
                                /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                                ExpressionInfo entityUsage = classReference;

                                for (int i = 1; i < name.length; i++) {

                                    // 親が UnknownTypeInfo だったら，どうしようもない
                                    if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = new UnknownEntityUsageInfo(name,
                                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                                        /*this.resolvedInfo
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        return this.resolvedInfo;

                                        // 親がクラス型の場合
                                    } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();

                                        // まずは利用可能なフィールド一覧を取得
                                        boolean found = false;
                                        {
                                            // 利用可能なフィールド一覧を取得
                                            final List<FieldInfo> availableFields = NameResolver
                                                    .getAvailableFields(ownerClass, usingClass);

                                            for (FieldInfo availableField : availableFields) {

                                                // 一致するフィールド名が見つかった場合
                                                if (name[i].equals(availableField.getName())) {
                                                    // usingMethod.addReferencee(availableField);
                                                    // availableField.addReferencer(usingMethod);

                                                    entityUsage = FieldUsageInfo.getInstance(
                                                            classReference, entityUsage.getType(),
                                                            availableField, true, false,
                                                            usingMethod, fromLine, fromColumn,
                                                            toLine, toColumn);
                                                    /*entityUsage
                                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }

                                        // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                        {
                                            if (!found) {
                                                // インナークラス一覧を取得
                                                final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                        .getAvailableDirectInnerClasses(ownerClass);
                                                for (final ClassInfo innerClass : ClassInfo
                                                        .convert(innerClasses)) {

                                                    // 一致するクラス名が見つかった場合
                                                    if (name[i].equals(innerClass.getClassName())) {
                                                        // TODO 利用関係を構築するコードが必要？

                                                        final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                                innerClass);
                                                        entityUsage = new ClassReferenceInfo(
                                                                referenceType, usingMethod,
                                                                fromLine, fromColumn, toLine,
                                                                toColumn);
                                                        /*entityUsage
                                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                        // そのクラスのフィールドを使用しているとみなす
                                        {
                                            if (!found) {

                                                final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                        .getType()).getReferencedClass();
                                                final ExternalClassInfo externalSuperClass = NameResolver
                                                        .getExternalSuperClass(referencedClass);
                                                if (!(referencedClass instanceof InnerClassInfo)
                                                        && (null != externalSuperClass)) {

                                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                            name[i], externalSuperClass);

                                                    // usingMethod.addReferencee(fieldInfo);
                                                    // fieldInfo.addReferencer(usingMethod);
                                                    fieldInfoManager.add(fieldInfo);

                                                    entityUsage = FieldUsageInfo.getInstance(
                                                            classReference, entityUsage.getType(),
                                                            fieldInfo, true, false, usingMethod,
                                                            fromLine, fromColumn, toLine, toColumn);
                                                    found = true;
                                                    /*entityUsage
                                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                                }
                                            }
                                        }

                                        // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                                        {
                                            if (!found && (ownerClass instanceof ExternalClassInfo)) {
                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], (ExternalClassInfo) ownerClass);
                                                entityUsage = FieldUsageInfo.getInstance(
                                                        classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                            }
                                        }

                                    } else {
                                        assert false : "Here shouldn't be reached!";
                                    }
                                }

                                this.resolvedInfo = entityUsage;
                                return this.resolvedInfo;
                            }
                        }

                        // 名前空間.クラス名 となっている場合
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                        if (importName[importName.length - 1].equals(name[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add(specifiedClassInfo);
                            }

                            final ClassReferenceInfo classReference = new ClassReferenceInfo(
                                    new ClassTypeInfo(specifiedClassInfo), usingMethod, fromLine,
                                    fromColumn, toLine, toColumn);
                            /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                            ExpressionInfo entityUsage = classReference;

                            for (int i = 1; i < name.length; i++) {

                                // 親が UnknownTypeInfo だったら，どうしようもない
                                if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = new UnknownEntityUsageInfo(name,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                    /*this.resolvedInfo
                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                    return this.resolvedInfo;

                                    // 親がクラス型の場合
                                } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();

                                    // まずは利用可能なフィールド一覧を取得
                                    boolean found = false;
                                    {
                                        // 利用可能なフィールド一覧を取得
                                        final List<FieldInfo> availableFields = NameResolver
                                                .getAvailableFields(ownerClass, usingClass);

                                        for (final FieldInfo availableField : availableFields) {

                                            // 一致するフィールド名が見つかった場合
                                            if (name[i].equals(availableField.getName())) {
                                                // usingMethod.addReferencee(availableField);
                                                // availableField.addReferencer(usingMethod);

                                                entityUsage = FieldUsageInfo.getInstance(
                                                        classReference, entityUsage.getType(),
                                                        availableField, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                /*entityUsage
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                    // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                    {
                                        if (!found) {
                                            // インナークラス一覧を取得
                                            final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                    .getAvailableDirectInnerClasses(ownerClass);
                                            for (final ClassInfo innerClass : ClassInfo
                                                    .convert(innerClasses)) {

                                                // 一致するクラス名が見つかった場合
                                                if (name[i].equals(innerClass.getClassName())) {
                                                    // TODO 利用関係を構築するコードが必要？

                                                    final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                            innerClass);
                                                    entityUsage = new ClassReferenceInfo(
                                                            referenceType, usingMethod, fromLine,
                                                            fromColumn, toLine, toColumn);
                                                    /*entityUsage
                                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                    // そのクラスのフィールドを使用しているとみなす
                                    {
                                        if (!found) {

                                            final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                    .getType()).getReferencedClass();
                                            final ExternalClassInfo externalSuperClass = NameResolver
                                                    .getExternalSuperClass(referencedClass);
                                            if (!(referencedClass instanceof InnerClassInfo)
                                                    && (null != externalSuperClass)) {

                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], externalSuperClass);

                                                // usingMethod.addReferencee(fieldInfo);
                                                // fieldInfo.addReferencer(usingMethod);
                                                fieldInfoManager.add(fieldInfo);

                                                entityUsage = FieldUsageInfo.getInstance(
                                                        classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                /*entityUsage
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                            }
                                        }
                                    }

                                    // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                                    {
                                        if (ownerClass instanceof ExternalClassInfo) {
                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], (ExternalClassInfo) ownerClass);
                                            entityUsage = FieldUsageInfo.getInstance(
                                                    classReference, entityUsage.getType(),
                                                    fieldInfo, true, false, usingMethod, fromLine,
                                                    fromColumn, toLine, toColumn);
                                        }
                                    }

                                } else {
                                    assert false : "Here shouldn't be reached!";
                                }
                            }

                            this.resolvedInfo = entityUsage;
                            return this.resolvedInfo;
                        }
                    }
                }
            }

            // 内部クラス名から検索
            {
                final ClassInfo outestClass;
                if (usingClass instanceof InnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final ClassInfo innerClassInfo : ClassInfo.convert(NameResolver
                        .getAvailableInnerClasses(outestClass))) {

                    // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                    final String innerClassName = innerClassInfo.getClassName();
                    if (innerClassName.equals(name[0])) {

                        final ClassReferenceInfo innerClassReference = new ClassReferenceInfo(
                                new ClassTypeInfo(innerClassInfo), usingMethod, fromLine,
                                fromColumn, toLine, toColumn);
                        /*innerClassReference.setOwnerExecutableElement(ownerExecutableElement);*/
                        ExpressionInfo entityUsage = innerClassReference;
                        for (int i = 1; i < name.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                                return this.resolvedInfo;

                                // 親がクラス型の場合
                            } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                        .getReferencedClass();

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なフィールド一覧を取得
                                    final List<FieldInfo> availableFields = NameResolver
                                            .getAvailableFields(ownerClass, usingClass);

                                    for (final FieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            // usingMethod.addReferencee(availableField);
                                            // availableField.addReferencer(usingMethod);

                                            entityUsage = FieldUsageInfo.getInstance(
                                                    innerClassReference, entityUsage.getType(),
                                                    availableField, true, false, usingMethod,
                                                    fromLine, fromColumn, toLine, toColumn);
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                {
                                    if (!found) {
                                        // インナークラス一覧を取得
                                        final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses(ownerClass);
                                        for (final ClassInfo innerClass : ClassInfo
                                                .convert(innerClasses)) {

                                            // 一致するクラス名が見つかった場合
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClassInfo);
                                                entityUsage = new ClassReferenceInfo(referenceType,
                                                        usingMethod, fromLine, fromColumn, toLine,
                                                        toColumn);
                                                /*entityUsage
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                // そのクラスのフィールドを使用しているとみなす
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass(referencedClass);
                                        if (!(referencedClass instanceof InnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = FieldUsageInfo.getInstance(
                                                    innerClassReference, entityUsage.getType(),
                                                    fieldInfo, true, false, usingMethod, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/

                                        } else {
                                            // 見つからなかった処理を行う
                                            // assert false : "Can't resolve entity usage3.5 : " + this.toString();
                                            usingMethod.addUnresolvedUsage(this);
                                            this.resolvedInfo = new UnknownEntityUsageInfo(name,
                                                    usingMethod, fromLine, fromColumn, toLine,
                                                    toColumn);
                                            /*this.resolvedInfo
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            return this.resolvedInfo;

                                        }
                                    }
                                }

                            } else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        this.resolvedInfo = entityUsage;
                        return this.resolvedInfo;
                    }
                }
            }

            // import文を用いない範囲で利用可能なクラスから検索
            for (final ClassInfo availableClass : NameResolver.getAvailableClasses(usingClass)) {
                if (availableClass.isSuffixMatch(name)) {
                    this.resolvedInfo = new ClassReferenceInfo(new ClassTypeInfo(availableClass),
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }
            }

            //利用可能な名前空間から検索
            // 利用可能な名前空間から検索
            {
                for (final UnresolvedMemberImportStatementInfo availableNamespace : UnresolvedMemberImportStatementInfo
                        .getMemberImportStatements(this.getAvailableNamespaces())) {

                    final String[] importedFullQualifiedName = availableNamespace
                            .getFullQualifiedName();
                    if (importedFullQualifiedName[importedFullQualifiedName.length - 1]
                            .equals(name[0])) {
                        final String[] ownerClassFullQualifiedName = Arrays.<String> copyOf(
                                importedFullQualifiedName, importedFullQualifiedName.length - 1);
                        final ClassInfo ownerClass = classInfoManager
                                .getClassInfo(ownerClassFullQualifiedName);

                        for (final FieldInfo field : ownerClass.getDefinedFields()) {
                            if (field.getName().equals(name[0])) {
                                final FieldUsageInfo fieldUsage = FieldUsageInfo.getInstance(null,
                                        new ClassTypeInfo(ownerClass), field, true, false,
                                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                                return fieldUsage;
                            }
                        }

                        for (final MethodInfo method : ownerClass.getDefinedMethods()) {
                            if (method.getMethodName().equals(name[0])) {
                                final MethodCallInfo methodCall = new MethodCallInfo(
                                        new ClassTypeInfo(ownerClass), null, method, method
                                                .getReturnType(), usingMethod, fromLine,
                                        fromColumn, toLine, toColumn);
                                return methodCall;
                            }
                        }
                    }
                }
            }

            //　親クラスから検索
            {
                // 利用可能な各親クラスに対して
                for (final ClassInfo classInfo : NameResolver.getAvailableClasses(usingClass)) {
                    final String className = classInfo.getClassName();

                    // クラス名と参照名の先頭が等しい場合は，そのクラス名が参照先であると決定する
                    if (className.equals(name[0])) {

                        final ClassReferenceInfo classReference = new ClassReferenceInfo(
                                new ClassTypeInfo(classInfo), usingMethod, fromLine, fromColumn,
                                toLine, toColumn);
                        /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                        ExpressionInfo entityUsage = classReference;

                        for (int i = 1; i < name.length; i++) {

                            // 親が UnknownTypeInfo だったら，どうしようもない
                            if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                                return this.resolvedInfo;

                                // 親がクラス型の場合
                            } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                        .getReferencedClass();

                                // まずは利用可能なフィールド一覧を取得
                                boolean found = false;
                                {
                                    // 利用可能なフィールド一覧を取得
                                    final List<FieldInfo> availableFields = NameResolver
                                            .getAvailableFields(ownerClass, usingClass);

                                    for (final FieldInfo availableField : availableFields) {

                                        // 一致するフィールド名が見つかった場合
                                        if (name[i].equals(availableField.getName())) {
                                            // usingMethod.addReferencee(availableField);
                                            // availableField.addReferencer(usingMethod);

                                            entityUsage = FieldUsageInfo.getInstance(
                                                    classReference, entityUsage.getType(),
                                                    availableField, true, false, usingMethod,
                                                    fromLine, fromColumn, toLine, toColumn);
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // スタティックフィールドで見つからなかった場合は，インナークラスから探す
                                {
                                    if (!found) {
                                        // インナークラス一覧を取得
                                        final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses(ownerClass);
                                        for (final ClassInfo innerClass : ClassInfo
                                                .convert(innerClasses)) {

                                            // 一致するクラス名が見つかった場合
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO 利用関係を構築するコードが必要？

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClass);
                                                entityUsage = new ClassReferenceInfo(referenceType,
                                                        usingMethod, fromLine, fromColumn, toLine,
                                                        toColumn);
                                                /*entityUsage
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず．
                                // そのクラスのフィールドを使用しているとみなす
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass(referencedClass);
                                        if (!(referencedClass instanceof InnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = FieldUsageInfo.getInstance(
                                                    classReference, entityUsage.getType(),
                                                    fieldInfo, true, false, usingMethod, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            found = true;
                                            /*entityUsage
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                        }
                                    }
                                }

                                // ownerClassがExternalClassであれば，フィールドを作成し，そのフィールドを利用していることにする
                                {
                                    if (!found && (ownerClass instanceof ExternalClassInfo)) {
                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], (ExternalClassInfo) ownerClass);
                                        entityUsage = FieldUsageInfo
                                                .getInstance(classReference, entityUsage.getType(),
                                                        fieldInfo, true, false, usingMethod,
                                                        fromLine, fromColumn, toLine, toColumn);
                                    }
                                }

                            } else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        this.resolvedInfo = entityUsage;
                        return this.resolvedInfo;
                    }
                }
            }
        }

        // java言語の場合は，javaかjavaxで始まり，長さが3以上のUnknownEntityUsageInfoはJDK内のクラスとみなす
        final Settings settings = Settings.getInstance();
        if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                || settings.getLanguage().equals(LANGUAGE.JAVA14)
                || settings.getLanguage().equals(LANGUAGE.JAVA13)) {

            if ((name[0].equals("java") || name[0].equals("javax")) && (3 <= name.length)) {
                final ExternalClassInfo externalClass = new ExternalClassInfo(name);
                final ClassTypeInfo externalClassType = new ClassTypeInfo(externalClass);
                this.resolvedInfo = new ClassReferenceInfo(externalClassType, usingMethod,
                        fromLine, fromColumn, toLine, toColumn);
                classInfoManager.add(externalClass);
                return this.resolvedInfo;
            }
        }

        err.println("Remain unresolved \"" + this.toString() + "\"" + " line:" + this.getFromLine()
                + " column:" + this.getFromColumn() + " on \""
                + usingClass.getOwnerFile().getName() + "\"");

        // 見つからなかった処理を行う
        usingMethod.addUnresolvedUsage(this);

        this.resolvedInfo = new UnknownEntityUsageInfo(name, usingMethod, fromLine, fromColumn,
                toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    /**
     * 未解決エンティティ使用名を返す．
     * 
     * @return 未解決エンティティ使用名
     */
    public String[] getName() {
        return Arrays.<String> copyOf(this.name, this.name.length);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.name[0]);
        for (int i = 1; i < this.name.length; i++) {
            sb.append(".");
            sb.append(this.name[i]);
        }
        return sb.toString();
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を返す．
     * 
     * @return この未解決エンティティ使用が利用することのできる名前空間
     */
    public List<UnresolvedImportStatementInfo<?>> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * この未解決エンティティ使用が利用することのできる名前空間を保存するための変数
     */
    private final List<UnresolvedImportStatementInfo<?>> availableNamespaces;

    /**
     * この未解決エンティティ使用名を保存するための変数
     */
    private final String[] name;

    /**
     * エラーメッセージ出力用のプリンタ
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedUnknownUsage";
        }
    }, MESSAGE_TYPE.ERROR);
}
