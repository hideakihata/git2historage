package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 変数の使用やメソッドの呼び出しなど，プログラム要素の使用を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class ExpressionInfo implements ConditionInfo {

    /**
     *
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ExpressionInfo(final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.ownerExecutableElement = null;
        this.ownerConditionalBlock = null;
        this.ownerMethod = ownerMethod;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * 式内で定義された変数のSetを返す
     * 
     * @return 式内で定義された変数のSet 
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * エンティティ使用の型を返す．
     * 
     * @return エンティティ使用の型
     */
    public abstract TypeInfo getType();

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int compareTo(Position o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (o instanceof ExecutableElementInfo) {
            final ClassInfo ownerClass1 = this.getOwnerMethod().getOwnerClass();
            final ClassInfo ownerClass2 = ((ExecutableElementInfo) o).getOwnerMethod()
                    .getOwnerClass();
            int classOrder = ownerClass1.compareTo(ownerClass2);
            if (0 != classOrder) {
                return classOrder;
            }
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
    }

    /**
     * 外側のExecutableElementを返す
     * 
     * @return 外側のExecutableElement
     */
    public final ExecutableElementInfo getOwnerExecutableElement() {
        assert null != this.ownerExecutableElement : "this.ownerExecutableElement must not be null!";
        return this.ownerExecutableElement;
    }

    /**
     * この式を持つ文を返す
     * 
     * @return この式を持つ文
     */
    public final StatementInfo getOwnerStatement() {

        final ExecutableElementInfo ownerExecutableElement = this.getOwnerExecutableElement();
        if (ownerExecutableElement instanceof StatementInfo) {
            return (StatementInfo) ownerExecutableElement;
        }

        if (ownerExecutableElement instanceof ExpressionInfo) {
            return ((ExpressionInfo) ownerExecutableElement).getOwnerStatement();
        }

        // ownerExecutableElement が StatementInfo でも ExpressionInfo　でもないときはIllegalStateException
        throw new IllegalStateException(
                "ownerExecutableElement must be StatementInfo or ExpressionInfo.");
        //return null;
    }

    /**
     * この式を持つ式を返す
     * 
     * @return この式を持つ式
     */
    public final ExpressionInfo getOwnerExpression() {

        final ExecutableElementInfo ownerExecutableElement = this.getOwnerExecutableElement();
        if (ownerExecutableElement instanceof ExpressionInfo) {
            return (ExpressionInfo) ownerExecutableElement;
        }

        // ownerExecutableElementがExpressionInfoでない場合は，nullを返す
        return null;
    }

    /**
     * 直接のオーナーであるExecutableElementをセットする
     * 
     * @param ownerExecutableElement 直接のオーナーであるExecutableElement
     */
    public void setOwnerExecutableElement(final ExecutableElementInfo ownerExecutableElement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerExecutableElement) {
            throw new IllegalArgumentException();
        }

        this.ownerExecutableElement = ownerExecutableElement;
    }

    /**
     * この式を条件として持つConditionalBlockInfo返す
     */
    @Override
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerConditionalBlock;
    }

    /**
     * この式を条件として持つConditionalBlockInfoを設定する
     * 引数がnullであることを許容する．
     */
    @Override
    public void setOwnerConditionalBlock(final ConditionalBlockInfo ownerConditionalBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        //assert null == this.ownerConditionalBlock : "this.ownerConditionalBlock must be null!";
        this.ownerConditionalBlock = ownerConditionalBlock;
    }

    /**
     * オーナーメソッドを返す
     * 
     * @return オーナーメソッド
     */
    @Override
    public final CallableUnitInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    /**
     * 文を直接所有する空間を返す
     * 
     * @return 文を直接所有する空間
     */
    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.getOwnerStatement().getOwnerSpace();
    }

    private ExecutableElementInfo ownerExecutableElement;

    /**
     * この式を条件として所有するConditionalBlockInfoを保存するための変数
     */
    private ConditionalBlockInfo ownerConditionalBlock;

    /**
     * オーナーメソッドを保存するための変数
     */
    private final CallableUnitInfo ownerMethod;

    /**
     * 開始行を保存するための変数
     */
    private final int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private final int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private final int toLine;

    /**
     * 開始列を保存するための変数
     */
    private final int toColumn;

}
