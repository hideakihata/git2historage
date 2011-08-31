package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * クラスの参照を表すクラス．
 * ReferenceTypeInfo　は「参照型」を表すのに対して，
 * このクラスはクラスの参照に関する情報（参照位置など）を表す
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ClassReferenceInfo extends ExpressionInfo {

    /**
     * 参照型を与えてオブジェクトを初期化
     * 
     * @param referenceType このクラス参照の参照型
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ClassReferenceInfo(final ClassTypeInfo referenceType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * このクラス参照の参照型を返す
     * 
     * @return このクラス参照の参照型
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    /**
     * このクラス参照で参照されているクラスを返す
     * 
     * @return このクラス参照で参照されているクラス
     */
    public ClassInfo getReferencedClass() {
        return this.referenceType.getReferencedClass();
    }

    /**
     * クラス参照において変数が使用されることはないので空のセットを返す
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
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * このクラス参照のテキスト表現（String型）を返す
     * 
     * @return このクラス参照のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final ClassInfo classInfo = this.getReferencedClass();
        return classInfo.getFullQualifiedName(".");
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
        final ClassTypeInfo classType = (ClassTypeInfo) this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ClassReferenceInfo newClassReference = new ClassReferenceInfo(classType, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newClassReference.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newClassReference.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newClassReference;

    }

    /**
     * このクラス参照の参照型を保存する変数
     */
    private final ClassTypeInfo referenceType;
}
