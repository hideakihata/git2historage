package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayLengthUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 未解決フィールド使用を保存するためのクラス
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldUsageInfo extends UnresolvedVariableUsageInfo<FieldUsageInfo> {

    /**
     * フィールド使用が実行される変数の型名と変数名，利用可能な名前空間を与えてオブジェクトを初期化
     * 
     * @param memberImportStatements 利用可能な名前空間
     * @param qualifierUsage フィールド使用が実行される親エンティティ
     * @param fieldName 変数名
     * @param reference フィールド使用が参照か
     * @param assignment フィールド使用が代入か
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedFieldUsageInfo(
            final List<UnresolvedMemberImportStatementInfo> memberImportStatements,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage,
            final String fieldName, final boolean reference, final boolean assignment,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fieldName, reference, assignment, outerUnit, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == memberImportStatements) || (null == qualifierUsage) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.memberImportStatements = memberImportStatements;
        this.qualifierUsage = qualifierUsage;
        this.fieldName = fieldName;
    }

    /**
     * 未解決フィールド使用を解決し，その型を返す．
     * 
     * @param usingClass 未解決フィールド使用が行われているクラス
     * @param usingMethod 未解決フィールド使用が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みフィールド使用
     */
    @Override
    public FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // フィールド名，参照・代入を取得
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // フィールド使用がくっついている型("."の前のやつ)を解決
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifierUsage();
        final ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "resolveEntityUsage returned null!";

        final TypeInfo qualifierType = qualifierUsage.getType();
        this.resolvedInfo = this.resolve(usingClass, usingMethod, qualifierUsage, qualifierType,
                fieldName, reference, assignment, fromLine, fromColumn, toLine, toColumn,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != this.resolvedInfo : "resolvedInfo must not be null!";
        return this.resolvedInfo;
    }

    private FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ExpressionInfo qualifierUsage,
            final TypeInfo qualifierType, final String fieldName, final boolean reference,
            final boolean assignment, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 型パラメータの場合はその継承型を求める
        if (qualifierType instanceof TypeParameterTypeInfo) {

            final TypeParameterInfo qualifierParameterType = ((TypeParameterTypeInfo) qualifierType)
                    .getReferncedTypeParameter();
            if (qualifierParameterType.hasExtendsType()) {
                for (final TypeInfo extendsType : qualifierParameterType.getExtendsTypes()) {
                    final FieldUsageInfo resolved = this.resolve(usingClass, usingMethod,
                            qualifierUsage, extendsType, fieldName, reference, assignment,
                            fromLine, fromColumn, toLine, toColumn, classInfoManager,
                            fieldInfoManager, methodInfoManager);
                    if (null != resolved) {
                        return resolved;
                    }
                }
            }

            else {
                final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final FieldUsageInfo resolved = this.resolve(usingClass, usingMethod,
                        qualifierUsage, new ClassTypeInfo(objectClass), fieldName, reference,
                        assignment, fromLine, fromColumn, toLine, toColumn, classInfoManager,
                        fieldInfoManager, methodInfoManager);
                return resolved;

            }
        }

        // 親が解決できなかった場合はどうしようもない
        else if (qualifierType instanceof UnknownTypeInfo) {

            final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);

            final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                    UnknownTypeInfo.getInstance(), unknownField, reference, assignment,
                    usingMethod, fromLine, fromColumn, toLine, toColumn);
            return resolved;

            //親がクラス型の場合
        } else if (qualifierType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) qualifierType).getReferencedClass();
            // 親が対象クラス(TargetClassInfo)だった場合
            if (ownerClass instanceof TargetClassInfo) {

                // まずは利用可能なフィールドから検索
                {
                    // 利用可能なフィールド一覧を取得
                    final List<FieldInfo> availableFields = NameResolver.getAvailableFields(
                            (TargetClassInfo) ownerClass, usingClass);

                    // 利用可能なフィールドを，未解決フィールド名で検索
                    for (final FieldInfo availableField : availableFields) {

                        // 一致するフィールド名が見つかった場合
                        if (fieldName.equals(availableField.getName())) {

                            final FieldUsageInfo resolved = FieldUsageInfo.getInstance(
                                    qualifierUsage, qualifierUsage.getType(), availableField,
                                    reference, assignment, usingMethod, fromLine, fromColumn,
                                    toLine, toColumn);
                            return resolved;
                        }
                    }
                }

                // スタティックインポートされているフィールドを探す
                {
                    for (final UnresolvedMemberImportStatementInfo unresolvedMemberImportStatement : this
                            .getAvailableNamespaces()) {
                        final MemberImportStatementInfo memberImportStatement = unresolvedMemberImportStatement
                                .resolve(usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                        for (final Member importedMember : memberImportStatement.getImportedUnits()) {
                            if (importedMember instanceof FieldInfo) {
                                final FieldInfo importedField = (FieldInfo) importedMember;
                                if (fieldName.equals(importedField.getName())) {
                                    final ClassInfo classInfo = importedField.getOwnerClass();
                                    final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                                    final ClassReferenceInfo classReference = new ClassReferenceInfo(
                                            classType, usingMethod, fromLine, fromColumn, fromLine,
                                            fromColumn);
                                    final FieldUsageInfo resolved = FieldUsageInfo.getInstance(
                                            classReference, classType, importedField, reference,
                                            assignment, usingMethod, fromLine, fromColumn, toLine,
                                            toColumn);
                                    return resolved;
                                }
                            }
                        }
                    }
                }

                // 利用可能なフィールドが見つからなかった場合は，外部クラスである親クラスがあるはず
                // そのクラスの変数を使用しているとみなす
                {
                    for (ClassInfo classInfo = ownerClass; true; classInfo = ((InnerClassInfo) classInfo)
                            .getOuterClass()) {

                        final ExternalClassInfo externalSuperClass = NameResolver
                                .getExternalSuperClass(classInfo);
                        if (null != externalSuperClass) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                    externalSuperClass);
                            fieldInfoManager.add(fieldInfo);

                            // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                            final FieldUsageInfo resolved = FieldUsageInfo
                                    .getInstance(qualifierUsage, qualifierUsage.getType(),
                                            fieldInfo, reference, assignment, usingMethod,
                                            fromLine, fromColumn, toLine, toColumn);
                            return resolved;
                        }

                        if (!(classInfo instanceof TargetInnerClassInfo)) {
                            break;
                        }
                    }
                }

                // 見つからなかった処理を行う
                {
                    err.println("Resolved as an external element, \"" + this.getFieldName() + "\""
                            + " line:" + this.getFromLine() + " column:" + this.getFromColumn()
                            + " on \"" + usingClass.getOwnerFile().getName());

                    final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                    final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                            UnknownTypeInfo.getInstance(), unknownField, reference, assignment,
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                    return resolved;
                }

                // 親が外部クラス（ExternalClassInfo）だった場合
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                        (ExternalClassInfo) ownerClass);
                fieldInfoManager.add(fieldInfo);

                // 外部クラスに新規で外部変数(ExternalFieldInfo)を追加したので型は不明．
                final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                        qualifierUsage.getType(), fieldInfo, reference, assignment, usingMethod,
                        fromLine, fromColumn, toLine, toColumn);
                return resolved;
            }

        } else if (qualifierType instanceof ArrayTypeInfo) {

            // TODO ここは言語依存にするしかないのか？ 配列.length など

            // Java 言語で フィールド名が length だった場合は int 型を返す
            // TODO　ちゃんとかきなおさないといけない
            final Settings settings = Settings.getInstance();
            if ((settings.getLanguage().equals(LANGUAGE.JAVA15)
                    || settings.getLanguage().equals(LANGUAGE.JAVA14) || settings.getLanguage()
                    .equals(LANGUAGE.JAVA13))
                    && fieldName.equals("length")) {

                final FieldUsageInfo resolved = new ArrayLengthUsageInfo(qualifierUsage,
                        (ArrayTypeInfo) qualifierType, usingMethod, fromLine, fromColumn, toLine,
                        toColumn);
                return resolved;
            }
        }

        return null;
    }

    /**
     * 使用可能な名前空間を返す
     * 
     * @return 使用可能な名前空間を返す
     */
    public List<UnresolvedMemberImportStatementInfo> getAvailableNamespaces() {
        return this.memberImportStatements;
    }

    /**
     * フィールド使用が実行される変数の未解決型名を返す
     * 
     * @return フィールド使用が実行される変数の未解決型名
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * フィールド名を返す
     * 
     * @return フィールド名
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * 使用可能な名前空間を保存するための変数
     */
    private final List<UnresolvedMemberImportStatementInfo> memberImportStatements;

    /**
     * フィールド名を保存するための変数
     */
    private final String fieldName;

    /**
     * フィールド使用が実行される変数の未解決型名を保存するための変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage;
}
