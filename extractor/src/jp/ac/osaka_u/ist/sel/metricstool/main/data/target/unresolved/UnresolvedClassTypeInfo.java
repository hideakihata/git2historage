package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決クラス型を表すクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedClassTypeInfo implements UnresolvedReferenceTypeInfo<ReferenceTypeInfo> {

    /**
     * 利用可能な名前空間名，参照名を与えて初期化
     * 
     * @param availableNamespaces 名前空間名
     * @param referenceName 参照名
     */
    public UnresolvedClassTypeInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<? extends TypeInfo>>();
    }

    /**
     * この未解決クラス型がすでに解決済みかどうかを返す．
     * 
     * @return 解決済みの場合は true，解決されていない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * この未解決クラス型の解決済みの型を返す
     */
    @Override
    public ReferenceTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public ReferenceTypeInfo resolve(final TargetClassInfo usingClass,
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

        // import 文で指定されているクラスが登録されていないなら，外部クラスとして登録する
        for (final UnresolvedClassImportStatementInfo availableNamespace : this
                .getAvailableNamespaces()) {

            if (!availableNamespace.isAll()) {
                final String[] fullQualifiedName = availableNamespace.getImportName();
                if (!classInfoManager.hasClassInfo(fullQualifiedName)) {
                    final ExternalClassInfo externalClassInfo = new ExternalClassInfo(
                            fullQualifiedName);
                    classInfoManager.add(externalClassInfo);
                }
            }
        }

        // 登録されているクラス名から検出
        final String[] referenceName = this.getReferenceName();
        final Collection<ClassInfo> candidateClasses = classInfoManager
                .getClassInfos(referenceName[referenceName.length - 1]);

        //複数項参照の場合は完全限定名かどうかを調べる，単項参照の場合はデフォルトパッケージから調べる
        {
            final ClassInfo matchedClass = classInfoManager.getClassInfo(referenceName);
            if (null != matchedClass) {
                final ClassTypeInfo classType = new ClassTypeInfo(matchedClass);
                for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    classType.addTypeArgument(typeArgument);
                }
                this.resolvedInfo = classType;
                return this.resolvedInfo;
            }
        }

        // インポートされているクラスから検索（単項参照の場合）
        // ただし，自分と同じパッケージのクラスは除外する
        if (this.isMoniminalReference()) {
            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                // 自分と同じパッケージは除外する
                {
                    final TargetClassInfo outestUsingClass = (usingClass instanceof TargetInnerClassInfo) ? (TargetClassInfo) TargetInnerClassInfo
                            .getOutestClass((TargetInnerClassInfo) usingClass)
                            : usingClass;
                    if (classImportStatement.getNamespace().equals(outestUsingClass.getNamespace())) {
                        continue;
                    }
                }

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {
                    if (candidateClasses.contains(importedClass)) {
                        final ClassTypeInfo classType = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }
        }

        // 単項参照の場合はインポート文を用いないでも利用可能なクラスから検索
        // インポートされているクラスからの検索よりも下にないといけない
        if (this.isMoniminalReference()) {

            for (final ClassInfo availableClass : NameResolver.getAvailableClasses(usingClass)) {
                if (candidateClasses.contains(availableClass)) {
                    final ClassTypeInfo classType = new ClassTypeInfo(availableClass);
                    for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        classType.addTypeArgument(typeArgument);
                    }
                    this.resolvedInfo = classType;
                    return this.resolvedInfo;
                }
            }
        }

        // 単行参照の場合は，自分のパッケージのクラスも利用可能なので，検索
        if (this.isMoniminalReference()) {
            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                // 自分と同じパッケージ以外は除外する
                {
                    final TargetClassInfo outestUsingClass = (usingClass instanceof TargetInnerClassInfo) ? (TargetClassInfo) TargetInnerClassInfo
                            .getOutestClass((TargetInnerClassInfo) usingClass)
                            : usingClass;
                    if (!classImportStatement.getNamespace()
                            .equals(outestUsingClass.getNamespace())) {
                        continue;
                    }
                }

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {
                    if (candidateClasses.contains(importedClass)) {
                        final ClassTypeInfo classType = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }
        }

        // インポートされているクラスから検索（複数項参照の場合） 
        if (!this.isMoniminalReference()) {

            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {

                    for (final ClassInfo candidateClass : candidateClasses) {

                        final String[] candidateFQName = candidateClass.getFullQualifiedName();

                        CLASS: for (final ClassInfo accessibleInnerClass : TargetClassInfo
                                .getAccessibleInnerClasses(importedClass)) {

                            final String[] availableFQName = accessibleInnerClass
                                    .getFullQualifiedName();

                            for (int index = 1; index <= referenceName.length; index++) {
                                if (!availableFQName[availableFQName.length - index]
                                        .equals(referenceName[referenceName.length - index])) {
                                    continue CLASS;
                                }
                            }

                            for (int index = 1; index <= referenceName.length; index++) {
                                if (!candidateFQName[candidateFQName.length - index]
                                        .equals(referenceName[referenceName.length - index])) {
                                    continue CLASS;
                                }
                            }

                            final ClassTypeInfo classType = new ClassTypeInfo(candidateClass);
                            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                classType.addTypeArgument(typeArgument);
                            }
                            this.resolvedInfo = classType;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        // 単項参照の場合は型パラメータかどうかを調べる
        if (this.isMoniminalReference()) {

            TypeParameterizable typeParameterizableUnit = null != usingMethod ? usingMethod
                    : usingClass;
            do {
                for (final TypeParameterInfo typeParameter : typeParameterizableUnit
                        .getTypeParameters()) {
                    if (typeParameter.getName().equals(referenceName[0])) {
                        this.resolvedInfo = new TypeParameterTypeInfo(typeParameter);
                        return this.resolvedInfo;
                    }
                }
                typeParameterizableUnit = typeParameterizableUnit.getOuterTypeParameterizableUnit();
            } while (null != typeParameterizableUnit);
        }

        //ここにくるのは，クラスが見つからなかったとき
        if (this.isMoniminalReference()) {

            //System.out.println(referenceName[0]);
            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;

        } else {

            // インポート名を参照名を組み合わせることができるかを試す
            // たとえば， import A.B.C で，参照名が，C.Dであれば，完全限定名がA.B.C.Dのクラスがあることになる
            for (final UnresolvedClassImportStatementInfo availableNamespace : this
                    .getAvailableNamespaces()) {

                if (!availableNamespace.isAll()) {
                    final String[] importedName = availableNamespace.getFullQualifiedName();
                    if (importedName[importedName.length - 1].equals(referenceName[0])) {
                        final String[] fqName = new String[referenceName.length
                                + importedName.length - 1];
                        int index = 0;
                        for (; index < importedName.length; index++) {
                            fqName[index] = importedName[index];
                        }
                        for (int i = 1; i < referenceName.length; i++, index++) {
                            fqName[index] = referenceName[i];
                        }

                        final ExternalClassInfo externalClassInfo = new ExternalClassInfo(fqName);
                        final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }

            }

            final ExternalClassInfo externalClassInfo = referenceName.length > 2 ? new ExternalClassInfo(
                    referenceName)
                    : new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;
        }

        return this.resolvedInfo;
    }

    public ReferenceTypeInfo resolveAsSuperType(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        return this.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);
        //        // 不正な呼び出しでないかをチェック
        //        MetricsToolSecurityManager.getInstance().checkAccess();
        //        if ((null == usingClass) || (null == classInfoManager)) {
        //            throw new IllegalArgumentException();
        //        }
        //
        //        // 既に解決済みである場合は，キャッシュを返す
        //        if (this.alreadyResolved()) {
        //            return this.getResolved();
        //        }
        //
        //        final String[] referenceName = this.getReferenceName();
        //        final Collection<ClassInfo> candidates = classInfoManager
        //                .getClassInfosWithSuffix(referenceName);
        //
        //        if (candidates.isEmpty()) {
        //
        //            final ExternalClassInfo superClass = 1 == referenceName.length ? new ExternalClassInfo(
        //                    referenceName[0]) : new ExternalClassInfo(referenceName);
        //            classInfoManager.add(superClass);
        //            final ClassTypeInfo superClassType = new ClassTypeInfo(superClass);
        //            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
        //                    .getTypeArguments()) {
        //                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
        //                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        //                superClassType.addTypeArgument(typeArgument);
        //            }
        //            this.resolvedInfo = superClassType;
        //            return this.resolvedInfo;
        //        }
        //
        //        else {
        //            int longestMatchedLength = -1;
        //            ClassInfo bestCandidate = null;
        //            for (final ClassInfo candidate : candidates) {
        //
        //                final int matchedLength = this.getMatchedLength(usingClass.getFullQualifiedName(),
        //                        candidate.getFullQualifiedName());
        //                if (longestMatchedLength < matchedLength) {
        //                    longestMatchedLength = matchedLength;
        //                    bestCandidate = candidate;
        //                }
        //            }
        //
        //            final ClassTypeInfo superClassType = new ClassTypeInfo(bestCandidate);
        //            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
        //                    .getTypeArguments()) {
        //                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
        //                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        //                superClassType.addTypeArgument(typeArgument);
        //            }
        //            this.resolvedInfo = superClassType;
        //            return this.resolvedInfo;
        //        }
    }

    private int getMatchedLength(final String[] array1, final String[] array2) {

        for (int index = 0; true; index++) {

            if (array1.length <= index) {
                return index;
            }

            if (array2.length <= index) {
                return index;
            }

            if (!array1[index].equals(array2[index])) {
                return index;
            }
        }
    }

    /**
     * 利用可能な名前空間，型の完全修飾名を与えて初期化
     * @param referenceName 型の完全修飾名
     */
    public UnresolvedClassTypeInfo(final String[] referenceName) {
        this(new LinkedList<UnresolvedClassImportStatementInfo>(), referenceName);
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public final void addTypeArgument(
            final UnresolvedTypeInfo<? extends TypeInfo> typeParameterUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * このクラス参照で使用されている型パラメータの List を返す
     * 
     * @return このクラス参照で使用されている型パラメータの List
     */
    public final List<UnresolvedTypeInfo<? extends TypeInfo>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型の名前を返す
     * 
     * @return この参照型の名前を返す
     */
    @Override
    public final String getTypeName() {
        return this.referenceName[this.referenceName.length - 1];
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
     * この参照が単項かどうかを返す
     * 
     * @return　単項である場合はtrue，そうでない場合はfalse
     */
    public final boolean isMoniminalReference() {
        return 1 == this.referenceName.length;
    }

    /**
     * 未解決クラスを与えると，その未解決参照型を返す
     * 
     * @param referencedClass 未解決クラス
     * @return 与えられた未解決クラスの未解決参照型
     */
    public final static UnresolvedClassTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedClassTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * この未解決参照型が表す未解決クラス参照を返す
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return この未解決参照型が表す未解決クラス参照
     */
    public final UnresolvedClassReferenceInfo getUsage(
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        UnresolvedClassReferenceInfo usage = new UnresolvedClassReferenceInfo(
                this.availableNamespaces, this.referenceName);

        usage.setOuterUnit(outerUnit);
        usage.setFromLine(fromLine);
        usage.setFromColumn(fromColumn);
        usage.setToLine(toLine);
        usage.setToColumn(toColumn);

        for (UnresolvedTypeInfo<? extends TypeInfo> typeArgument : this.typeArguments) {
            usage.addTypeArgument(typeArgument);
        }
        return usage;
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
     * 型引数を保存するための変数
     */
    private final List<UnresolvedTypeInfo<? extends TypeInfo>> typeArguments;

    private ReferenceTypeInfo resolvedInfo;

}
