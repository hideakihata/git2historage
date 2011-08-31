package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールドの使用を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * 使用されているフィールドを与えてオブジェクトを初期化
     * 
     * @param qualifierExpression フィールド使用が実行される親の式
     * @param usedField 使用されているフィールド
     * @param reference 参照であるかどうか
     * @param assignment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    protected FieldUsageInfo(final ExpressionInfo qualifierExpression,
            final TypeInfo qualifierType, final FieldInfo usedField, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.qualifierExpression = qualifierExpression;
        this.qualifierType = qualifierType;

        // フィールドの使用情報を格納
        if (reference) {
            usedField.addReferencer(ownerMethod);
        }

        if (assignment) {
            usedField.addAssignmenter(ownerMethod);
        }
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);
        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * このフィールド使用の親，つまりこのフィールド使用がくっついている式を返す
     * 
     * @return このフィールド使用の親
     */
    public final TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * フィールド使用が実行される親の式を返す
     * @return フィールド使用が実行される親の式
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * この変数使用のテキスト表現（型）を返す
     * 
     * @return この変数使用のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        if (null != quantifierExpression) {
            sb.append(quantifierExpression.getText());
            sb.append(".");
        }

        final FieldInfo field = this.getUsedVariable();
        sb.append(field.getName());

        return sb.toString();
    }

    /**
     * この式（フィールド使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        variableUsages.addAll(qualifierExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final TypeInfo qualifierType = this.getQualifierType();
        final FieldInfo usedField = this.getUsedVariable();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final FieldUsageInfo newFieldUsage = new FieldUsageInfo(qualifierExpression, qualifierType,
                usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newFieldUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newFieldUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newFieldUsage;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getQualifierExpression().getCalls();
    }

    private final TypeInfo qualifierType;

    /**
     * フィールド参照が実行される親の式("."の前のやつ)を保存する変数
     */
    private final ExpressionInfo qualifierExpression;

    /**
     * 必要な情報を与えて，インスタンスを取得
     *
     * @param qualifierExpression 親の式
     * @param qualifierType 親エンティティの型
     * @param usedField 使用されているフィールド
     * @param reference 参照であるかどうか
     * @param assignment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return フィールド使用のインスタンス
     */
    public static FieldUsageInfo getInstance(final ExpressionInfo qualifierExpression,
            final TypeInfo qualifierType, final FieldInfo usedField, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final FieldUsageInfo instance = new FieldUsageInfo(qualifierExpression, qualifierType,
                usedField, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
        addFieldUsage(instance);
        return instance;
    }

    /**
     * フィールド使用のインスタンスをフィールドからフィールド使用へのマップに追加
     * @param fieldUsage フィールド使用
     */
    private static void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fieldUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final FieldInfo usedField = fieldUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedField)) {
            USAGE_MAP.get(usedField).add(fieldUsage);
        } else {
            final Set<FieldUsageInfo> usages = Collections
                    .synchronizedSet(new HashSet<FieldUsageInfo>());
            usages.add(fieldUsage);
            USAGE_MAP.put(usedField, usages);
        }
    }

    /**
     * 与えられたフィールドの使用情報のセットを取得
     * @param field 使用情報を取得したいフィールド
     * @return フィールド使用のセット．引数で与えられたフィールドが使用されていない場合はnull
     */
    public final static Set<FieldUsageInfo> getUsages(final FieldInfo field) {
        if (USAGE_MAP.containsKey(field)) {
            return USAGE_MAP.get(field);
        } else {
            return Collections.<FieldUsageInfo> emptySet();
        }
    }

    /**
     * 与えられた変数利用のCollectionに含まれるフィールド利用のSetを返す
     * 
     * @param variableUsages 変数利用のCollection
     * @return 与えられた変数利用のCollectionに含まれるフィールド利用のSet
     */
    public final static Set<FieldUsageInfo> getFieldUsages(
            Collection<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages) {
        final Set<FieldUsageInfo> fieldUsages = new HashSet<FieldUsageInfo>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage instanceof FieldUsageInfo) {
                fieldUsages.add((FieldUsageInfo) variableUsage);
            }
        }
        return Collections.unmodifiableSet(fieldUsages);
    }

    private static final ConcurrentMap<FieldInfo, Set<FieldUsageInfo>> USAGE_MAP = new ConcurrentHashMap<FieldInfo, Set<FieldUsageInfo>>();
}
