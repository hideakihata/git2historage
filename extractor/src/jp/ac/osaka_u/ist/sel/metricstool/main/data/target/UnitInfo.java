package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * プログラムユニットを表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public abstract class UnitInfo implements Position, Serializable {

    /**
     * 必要な情報を与えてオブジェクトを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    UnitInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * このユニット内における変数使用のSetを返す
     * 
     * @return このユニット内における変数使用のSet
     */
    public abstract Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * このユニット内で定義された変数のSetを返す
     * 
     * @return このユニット内で定義された変数のSet
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * このユニット内における呼び出しのSetを返す
     * 
     * @return このユニット内における呼び出しのSet
     */
    public abstract Set<CallInfo<? extends CallableUnitInfo>> getCalls();

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    @Override
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    @Override
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    @Override
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * このユニットの行数を返す
     * 
     * @return　このユニットの行数
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    /**
     * 子クラスによるオーバーライドを避けるための処置
     */
    public abstract int hashCode();

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
