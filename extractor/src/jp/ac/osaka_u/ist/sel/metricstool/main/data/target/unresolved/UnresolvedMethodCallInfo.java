package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決メソッド呼び出しを保存するためのクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodCallInfo extends UnresolvedCallInfo<MethodCallInfo> {

    /**
     * メソッド呼び出しが実行される変数の型，メソッド名を与えてオブジェクトを初期化
     * 
     * @param memberImportStatements このメソッド呼び出しが方解決のために利用できるインポート文
     * @param qualifierUsage メソッド呼び出しが実行される変数の型
     * @param methodName メソッド名
     */
    public UnresolvedMethodCallInfo(
            final List<UnresolvedMemberImportStatementInfo> memberImportStatements,
            final UnresolvedExpressionInfo<?> qualifierUsage, final String methodName) {

        if ((null == memberImportStatements) && (null == qualifierUsage) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.memberImportStatements = memberImportStatements;
        this.qualifierUsage = qualifierUsage;
        this.methodName = methodName;
    }

    @Override
    public MethodCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // メソッドのシグネチャを取得
        final String name = this.getName();
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // メソッド呼び出しがくっついている型("."の前のやつ)を解決
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifier();
        ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "resolveEntityUsage returned null!";

        if (qualifierUsage instanceof UnknownEntityUsageInfo) {
            if (unresolvedQualifierUsage instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo externalClassInfo = UnresolvedClassReferenceInfo
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedQualifierUsage);
                classInfoManager.add(externalClassInfo);
                final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedQualifierUsage)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    referenceType.addTypeArgument(typeArgument);
                }
                qualifierUsage = new ClassReferenceInfo(referenceType, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
            }
        }

        final TypeInfo qualifierType = qualifierUsage.getType();
        this.resolvedInfo = this.resolve(usingClass, usingMethod, qualifierUsage, qualifierType,
                name, actualParameters, typeArguments, fromLine, fromColumn, toLine, toColumn,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != this.resolvedInfo : "resolvedInfo must not be null!";
        return this.resolvedInfo;
    }

    private MethodCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ExpressionInfo qualifierUsage,
            final TypeInfo qualifierType, final String methodName,
            final List<ExpressionInfo> actualParameters,
            final List<ReferenceTypeInfo> typeArguments, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 型パラメータの場合はその継承型を求める
        if (qualifierType instanceof TypeParameterTypeInfo) {

            final TypeParameterInfo qualifierParameterType = ((TypeParameterTypeInfo) qualifierType)
                    .getReferncedTypeParameter();

            // extends がある場合
            if (qualifierParameterType.hasExtendsType()) {
                for (final TypeInfo extendsType : qualifierParameterType.getExtendsTypes()) {
                    final MethodCallInfo resolve = this.resolve(usingClass, usingMethod,
                            qualifierUsage, extendsType, methodName, actualParameters,
                            typeArguments, fromLine, fromColumn, toLine, toColumn,
                            classInfoManager, fieldInfoManager, methodInfoManager);
                    if (null != resolve) {
                        return resolve;
                    }
                }
            }

            // extends がない場合
            else {
                final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final MethodCallInfo resolve = this.resolve(usingClass, usingMethod,
                        qualifierUsage, new ClassTypeInfo(objectClass), methodName,
                        actualParameters, typeArguments, fromLine, fromColumn, toLine, toColumn,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                return resolve;
            }
        }

        // <?>や<? super A>のカッコ内の型の時
        else if (qualifierType instanceof ArbitraryTypeInfo
                || qualifierType instanceof SuperTypeInfo) {

            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "Object" });
            final MethodCallInfo resolve = this.resolve(usingClass, usingMethod, qualifierUsage,
                    new ClassTypeInfo(objectClass), methodName, actualParameters, typeArguments,
                    fromLine, fromColumn, toLine, toColumn, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            return resolve;
        }

        // <? extends B> のカッコ内の型の時
        else if (qualifierType instanceof ExtendsTypeInfo) {

            final TypeInfo extendsType = ((ExtendsTypeInfo) qualifierType).getExtendsType();
            final MethodCallInfo resolve = this.resolve(usingClass, usingMethod, qualifierUsage,
                    extendsType, methodName, actualParameters, typeArguments, fromLine, fromColumn,
                    toLine, toColumn, classInfoManager, fieldInfoManager, methodInfoManager);
            return resolve;
        }

        // 親が解決できなかった場合はどうしようもない
        else if (qualifierType instanceof UnknownTypeInfo) {

            final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(methodName);
            final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                    unknownMethod, UnknownTypeInfo.getInstance(), usingMethod, fromLine,
                    fromColumn, toLine, toColumn);
            resolved.addArguments(actualParameters);
            resolved.addTypeArguments(typeArguments);
            return resolved;

            // 親がクラス型だった場合
        } else if (qualifierType instanceof ClassTypeInfo
                || qualifierType instanceof PrimitiveTypeInfo) {

            final ClassInfo ownerClass;
            if (qualifierType instanceof PrimitiveTypeInfo) {
                final Settings settings = Settings.getInstance();
                ownerClass = TypeConverter.getTypeConverter(settings.getLanguage())
                        .getWrapperClass((PrimitiveTypeInfo) qualifierType);
            } else {
                ownerClass = ((ClassTypeInfo) qualifierType).getReferencedClass();
            }

            // まずは利用可能なメソッドから検索
            {
                // 利用可能なメソッド一覧を取得
                final List<MethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        ownerClass, usingClass);

                // 利用可能なメソッドから，未解決メソッドと一致するものを検索
                // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
                for (final MethodInfo availableMethod : availableMethods) {

                    // 呼び出し可能なメソッドが見つかった場合
                    if (availableMethod.canCalledWith(methodName, actualParameters)) {

                        final TypeInfo returnType = availableMethod.getReturnType();

                        // 返り値が型パラメータの場合
                        if (returnType instanceof TypeParameterTypeInfo) {
                            final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                    .getReferncedTypeParameter();
                            final TypeInfo typeArgument;

                            // メソッドの型パラメータから検索
                            if (availableMethod.isDefined(referencedTypeParameter)) {
                                final int index = referencedTypeParameter.getIndex();
                                if (index < typeArguments.size()) {
                                    typeArgument = typeArguments.get(index);
                                } else {
                                    final ClassInfo objectClass = classInfoManager
                                            .getClassInfo(new String[] { "java", "lang", "Object" });
                                    typeArgument = new ClassTypeInfo(objectClass);
                                }
                            }

                            // クラスの型パラメータから検索
                            else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                    .isDefined(referencedTypeParameter)) {
                                typeArgument = ((ClassTypeInfo) qualifierType)
                                        .getTypeArgument(referencedTypeParameter);
                            }

                            // 本来はここはエラーを出すべき
                            else {
                                final ClassInfo objectClass = classInfoManager
                                        .getClassInfo(new String[] { "java", "lang", "Object" });
                                typeArgument = new ClassTypeInfo(objectClass);
                            }

                            final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                    qualifierUsage, availableMethod, typeArgument, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            resolved.addArguments(actualParameters);
                            resolved.addTypeArguments(typeArguments);
                            return resolved;
                        }

                        // 返り値が型パラメータでない場合
                        else {
                            final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                    qualifierUsage, availableMethod, returnType, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            resolved.addArguments(actualParameters);
                            resolved.addTypeArguments(typeArguments);
                            return resolved;
                        }
                    }
                }
            }

            // スタティックインポートされているメソッドを探す
            {
                for (final UnresolvedMemberImportStatementInfo unresolvedMemberImportStatement : this
                        .getImportStatements()) {
                    final MemberImportStatementInfo memberImportStatement = unresolvedMemberImportStatement
                            .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                    for (final Member importedMember : memberImportStatement.getImportedUnits()) {
                        if (importedMember instanceof MethodInfo) {
                            final MethodInfo importedMethod = (MethodInfo) importedMember;

                            // 呼び出し可能なメソッドが見つかった場合
                            if (importedMethod.canCalledWith(methodName, actualParameters)) {

                                final TypeInfo returnType = importedMethod.getReturnType();

                                // 返り値が型パラメータの場合
                                if (returnType instanceof TypeParameterTypeInfo) {
                                    final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                            .getReferncedTypeParameter();
                                    final TypeInfo typeArgument;
                                    if (importedMethod.isDefined(referencedTypeParameter)) {
                                        final int index = referencedTypeParameter.getIndex();
                                        if (index < typeArguments.size()) {
                                            typeArgument = typeArguments.get(index);
                                        } else {
                                            final ClassInfo objectClass = classInfoManager
                                                    .getClassInfo(new String[] { "java", "lang",
                                                            "Object" });
                                            typeArgument = new ClassTypeInfo(objectClass);
                                        }
                                    }

                                    // クラスの型パラメータから検索
                                    else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                            .isDefined(referencedTypeParameter)) {
                                        typeArgument = ((ClassTypeInfo) qualifierType)
                                                .getTypeArgument(referencedTypeParameter);
                                    }

                                    // 本来はここはエラーを出すべき
                                    else {
                                        final ClassInfo objectClass = classInfoManager
                                                .getClassInfo(new String[] { "java", "lang",
                                                        "Object" });
                                        typeArgument = new ClassTypeInfo(objectClass);
                                    }

                                    final MethodCallInfo resolved = new MethodCallInfo(
                                            qualifierType, qualifierUsage, importedMethod,
                                            typeArgument, usingMethod, fromLine, fromColumn,
                                            toLine, toColumn);
                                    resolved.addArguments(actualParameters);
                                    resolved.addTypeArguments(typeArguments);
                                    return resolved;
                                }

                                // 返り値が型パラメータでない場合
                                else {
                                    final MethodCallInfo resolved = new MethodCallInfo(
                                            qualifierType, qualifierUsage, importedMethod,
                                            returnType, usingMethod, fromLine, fromColumn, toLine,
                                            toColumn);
                                    resolved.addArguments(actualParameters);
                                    resolved.addTypeArguments(typeArguments);
                                    return resolved;
                                }
                            }
                        }
                    }
                }
            }

            // 利用可能なメソッドが見つからなかった場合は，外部クラスである親クラスがあるはず．
            // そのクラスのメソッドを使用しているとみなす
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass(ownerClass);
                if (null != externalSuperClass) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                    methodInfo.setOuterUnit(externalSuperClass);
                    final List<ParameterInfo> dummyParameters = ExternalParameterInfo
                            .createParameters(actualParameters, methodInfo);
                    methodInfo.addParameters(dummyParameters);
                    methodInfoManager.add(methodInfo);

                    // 外部クラスに新規で外部メソッド変数（ExternalMethodInfo）を追加したので型は不明
                    final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                            qualifierUsage, methodInfo, UnknownTypeInfo.getInstance(), usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    resolved.addArguments(actualParameters);
                    resolved.addTypeArguments(typeArguments);
                    return resolved;
                }
            }

            // 親が外部クラス（ExternalClassInfo）だった場合
            if (ownerClass instanceof ExternalClassInfo) {

                err.println("Resolved as an external element, \"" + this.getName() + "\""
                        + " line:" + this.getFromLine() + " column:" + this.getFromColumn()
                        + " on \"" + usingClass.getOwnerFile().getName() + "\"");

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                methodInfo.setOuterUnit(ownerClass);
                final List<ParameterInfo> parameters = ExternalParameterInfo.createParameters(
                        actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // 外部クラスに新規で外部メソッド(ExternalMethodInfo)を追加したので型は不明．
                final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                        methodInfo, UnknownTypeInfo.getInstance(), usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                resolved.addArguments(actualParameters);
                resolved.addTypeArguments(typeArguments);
                return resolved;
            }

            // 親が配列だった場合
        } else if (qualifierType instanceof ArrayTypeInfo) {

            // XXX Java言語であれば， java.lang.Object に対する呼び出し
            final Settings settings = Settings.getInstance();
            if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                    || settings.getLanguage().equals(LANGUAGE.JAVA14)
                    || settings.getLanguage().equals(LANGUAGE.JAVA13)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });

                if (ownerClass instanceof ExternalClassInfo) {
                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                    methodInfo.setOuterUnit(ownerClass);
                    final List<ParameterInfo> parameters = ExternalParameterInfo.createParameters(
                            actualParameters, methodInfo);
                    methodInfo.addParameters(parameters);
                    methodInfoManager.add(methodInfo);

                    // 外部クラスに新規で外部メソッドを追加したので型は不明
                    final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                            qualifierUsage, methodInfo, UnknownTypeInfo.getInstance(), usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    resolved.addArguments(actualParameters);
                    resolved.addTypeArguments(typeArguments);
                    return resolved;
                }

                else if (ownerClass instanceof TargetClassInfo) {

                    // 利用可能なメソッド一覧を取得, NameResolver.getAvailableMethodはつかってはだめ．
                    //　なぜなら，このコンテキストでは可視化修飾子に関係なく，すべてのメソッドが利用可能
                    final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();
                    availableMethods.addAll(((TargetClassInfo) ownerClass).getDefinedMethods());

                    // 利用可能なメソッドから，未解決メソッドと一致するものを検索
                    // メソッド名，引数の型のリストを用いて，このメソッドの呼び出しであるかどうかを判定
                    for (final MethodInfo availableMethod : availableMethods) {

                        // 呼び出し可能なメソッドが見つかった場合
                        if (availableMethod.canCalledWith(methodName, actualParameters)) {
                            final TypeInfo returnType = availableMethod.getReturnType();

                            // 返り値が型パラメータの場合
                            if (returnType instanceof TypeParameterTypeInfo) {
                                final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                        .getReferncedTypeParameter();

                                final TypeInfo typeArgument;

                                // メソッドの型パラメータから検索
                                if (availableMethod.isDefined(referencedTypeParameter)) {
                                    final int index = referencedTypeParameter.getIndex();
                                    if (index < typeArguments.size()) {
                                        typeArgument = typeArguments.get(index);
                                    } else {
                                        final ClassInfo objectClass = classInfoManager
                                                .getClassInfo(new String[] { "java", "lang",
                                                        "Object" });
                                        typeArgument = new ClassTypeInfo(objectClass);
                                    }
                                }

                                // クラスの型パラメータから検索
                                else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                        .isDefined(referencedTypeParameter)) {
                                    typeArgument = ((ClassTypeInfo) qualifierType)
                                            .getTypeArgument(referencedTypeParameter);
                                }

                                // 本来はここはエラーを出すべき
                                else {
                                    final ClassInfo objectClass = classInfoManager
                                            .getClassInfo(new String[] { "java", "lang", "Object" });
                                    typeArgument = new ClassTypeInfo(objectClass);
                                }

                                final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                        qualifierUsage, availableMethod, typeArgument, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                resolved.addArguments(actualParameters);
                                resolved.addTypeArguments(typeArguments);
                                return resolved;
                            }

                            // 返り値が型パラメータでない場合
                            else {
                                final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                        qualifierUsage, availableMethod, returnType, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                resolved.addArguments(actualParameters);
                                resolved.addTypeArguments(typeArguments);
                                return resolved;
                            }
                        }
                    }
                }
            }
        }

        assert false : "Here should not be reached!";
        final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(methodName);
        final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                unknownMethod, UnknownTypeInfo.getInstance(), usingMethod, fromLine, fromColumn,
                toLine, toColumn);
        resolved.addArguments(actualParameters);
        resolved.addTypeArguments(typeArguments);
        return resolved;
    }

    /**
     * メソッド呼び出しが実行される変数の型を返す
     * 
     * @return メソッド呼び出しが実行される変数の型
     */
    public UnresolvedExpressionInfo<?> getQualifier() {
        return this.qualifierUsage;
    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public final String getName() {
        return this.methodName;
    }

    /**
     * メソッド名を保存するための変数
     */
    protected String methodName;

    public List<UnresolvedMemberImportStatementInfo> getImportStatements() {
        return this.memberImportStatements;
    }

    /**
     * メソッド呼び出しが実行される変数の参照を保存するための変数
     */
    private final UnresolvedExpressionInfo<?> qualifierUsage;

    private final List<UnresolvedMemberImportStatementInfo> memberImportStatements;
}
