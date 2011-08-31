package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列型の length フィールド使用を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ArrayLengthUsageInfo extends FieldUsageInfo {

    /**
     * 親となるエンティティ使用を与えてオブジェクトを初期化
     *
     * @param qualifierExpression 親エンティティ
     * @param qualifierType 親エンティティの型（必要ないかも．．．）
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayLengthUsageInfo(final ExpressionInfo qualifierExpression,
            final ArrayTypeInfo qualifierType, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(qualifierExpression, qualifierType,
                ArrayLengthInfo.getArrayLengthInfo(qualifierType), true, false, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * length フィールド使用の型を返す．
     * 
     * @return length フィールド使用の型
     */
    @Override
    public TypeInfo getType() {
        return PrimitiveTypeInfo.INT;
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final ArrayTypeInfo qualifierType = (ArrayTypeInfo) this.getQualifierType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayLengthUsageInfo newArrayLengthUsage = new ArrayLengthUsageInfo(
                qualifierExpression, qualifierType, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayLengthUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayLengthUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayLengthUsage;
    }
}
