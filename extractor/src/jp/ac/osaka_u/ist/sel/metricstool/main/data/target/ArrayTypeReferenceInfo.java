package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 配列型参照を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayTypeReferenceInfo extends ExpressionInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param arrayType 参照されている配列の型
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arrayType) {
            throw new IllegalArgumentException();
        }

        this.arrayType = arrayType;
    }

    /**
     * 型を返す
     */
    @Override
    public TypeInfo getType() {
        return this.arrayType;
    }

    /**
     * 配列の型参照において変数が使用されることはないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのセット
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * この配列型参照のテキスト表現（String型）を返す
     * 
     * @return この配列型のテキスト表現（String型）
     */
    @Override
    public String getText() {
        final TypeInfo type = this.getType();
        return type.getTypeName();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ArrayTypeInfo arrayType = (ArrayTypeInfo) this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayTypeReferenceInfo newArrayTypeReference = new ArrayTypeReferenceInfo(arrayType,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayTypeReference.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayTypeReference.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayTypeReference;

    }

    private final ArrayTypeInfo arrayType;
}
