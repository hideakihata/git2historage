package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


/**
 * 配列の初期化を表すクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class ArrayInitializerInfo extends ExpressionInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param elements 初期化式
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayInitializerInfo(List<ExpressionInfo> elements, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == elements) {
            throw new IllegalArgumentException("elements is null");
        }
        this.elementInitialiers = Collections.unmodifiableList(elements);

        for (final ExpressionInfo element : this.elementInitialiers) {
            element.setOwnerExecutableElement(this);
        }
    }

    /**
     * 要素の初期化式を返す
     * 
     * @return 要素の初期化式
     */
    public List<ExpressionInfo> getElementInitializers() {
        return this.elementInitialiers;
    }

    /**
     * 配列の長さを返す
     * 
     * @return　配列の長さ
     */
    public final int getArrayLength() {
        return this.elementInitialiers.size();
    }

    @Override
    public String getText() {
        final StringBuilder text = new StringBuilder();
        text.append("{");

        final Iterator<ExpressionInfo> elements = this.elementInitialiers.iterator();
        if (elements.hasNext()) {
            text.append(elements.next().getText());
        }
        while (elements.hasNext()) {
            text.append(", ");
            text.append(elements.next().getText());
        }

        text.append("}");
        return text.toString();
    }

    @Override
    public TypeInfo getType() {

        final List<ExpressionInfo> elements = this.getElementInitializers();
        if (0 == elements.size()) {
            final ClassInfoManager classManager = DataManager.getInstance().getClassInfoManager();
            final ClassInfo objectClass = classManager.getClassInfo(new String[] { "java", "lang",
                    "Object" });
            final ClassTypeInfo objectType = new ClassTypeInfo(objectClass);
            return new ArrayTypeInfo(objectType, 1);
        }

        else {

            final TypeInfo elementType = elements.get(0).getType();
            if (elementType instanceof ArrayTypeInfo) {
                final ArrayTypeInfo arrayElementType = (ArrayTypeInfo) elementType;
                return new ArrayTypeInfo(arrayElementType.getElementType(), arrayElementType
                        .getDimension() + 1);
            }

            else {
                return new ArrayTypeInfo(elementType, 1);
            }
        }
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usages = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final ExpressionInfo element : this.getElementInitializers()) {
            usages.addAll(element.getVariableUsages());
        }
        return Collections.unmodifiableSet(usages);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        for (final ExpressionInfo element : this.getElementInitializers()) {
            calls.addAll(element.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        for (final ExpressionInfo elementInitializer : this.getElementInitializers()) {
            thrownExpressions.addAll(elementInitializer.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {
        final List<ExpressionInfo> newInitializers = new LinkedList<ExpressionInfo>();
        for (final ExpressionInfo initialier : this.getElementInitializers()) {
            newInitializers.add((ExpressionInfo) initialier.copy());
        }
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(newInitializers,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayInitializer.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayInitializer.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayInitializer;
    }

    private final List<ExpressionInfo> elementInitialiers;

}
