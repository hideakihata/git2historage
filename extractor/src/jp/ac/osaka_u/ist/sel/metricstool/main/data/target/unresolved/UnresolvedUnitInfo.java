package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


/**
 * 未解決プログラムユニットを表すクラス
 * 
 * @author higo
 * 
 * @param <T> 解決済みユニットの型
 */
public abstract class UnresolvedUnitInfo<T extends UnitInfo> implements Resolvable<T>,
        PositionSetting {

    /**
     * 開始行をセットする
     * 
     * @param fromLine 開始行
     */
    public final void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * 開始列をセットする
     * 
     * @param fromColumn 開始列
     */
    public final void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * 終了行をセットする
     * 
     * @param toLine 終了行
     */
    public final void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * 終了列をセットする
     * 
     * @param toColumn 終了列
     */
    public final void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

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

    /**
     * このユニットの行数を返す
     * 
     * @return ユニットの行数
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public final T getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public int compareTo(Position o) {

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
        } else {
            return 0;
        }
    }
    
    /**
     * 開始行を保存するための変数
     */
    private int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private int toLine;

    /**
     * 開始列を保存するための変数
     */
    private int toColumn;

    /**
     * 名前解決された情報を格納するための変数
     */
    protected T resolvedInfo;
}
