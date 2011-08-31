package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 単文の情報を保有する抽象クラス．
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public abstract class SingleStatementInfo implements StatementInfo {

    /**
     * 位置情報を与えて初期化
     * 
     * @param ownerSpace 文を直接所有する空間
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public SingleStatementInfo(final LocalSpaceInfo ownerSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == ownerSpace) {
            throw new IllegalArgumentException("ownerSpace is null.");
        }

        this.ownerSpace = ownerSpace;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;

    }

    @Override
    public int compareTo(Position o) {

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

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.ownerSpace;
    }

    @Override
    public final CallableUnitInfo getOwnerMethod() {

        final LocalSpaceInfo ownerSpace = this.getOwnerSpace();
        if (ownerSpace instanceof CallableUnitInfo) {
            return (CallableUnitInfo) ownerSpace;
        }

        if (ownerSpace instanceof BlockInfo) {
            return ((BlockInfo) ownerSpace).getOwnerMethod();
        }

        throw new IllegalStateException();
    }

    @Override
    public final int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    @Override
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 文を直接所有する空間を表す変数
     */
    private final LocalSpaceInfo ownerSpace;

    /**
     * 開始行を表す変数
     */
    private final int fromLine;

    /**
     * 開始列を表す変数
     */
    private final int fromColumn;

    /**
     * 終了行を表す変数
     */
    private final int toLine;

    /**
     * 終了列を表す変数
     */
    private final int toColumn;

}
