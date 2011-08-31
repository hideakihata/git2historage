package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決のクラス参照，メソッド呼び出し，フィールド使用などを現すクラスの共通の基底クラス
 * 
 * @author higo
 * @param <T> 解決済みの型
 */
public abstract class UnresolvedExpressionInfo<T extends ExpressionInfo> implements
        UnresolvedConditionInfo<T> {

    protected UnresolvedExpressionInfo() {
        this(null, 0, 0, 0, 0);
    }

    protected UnresolvedExpressionInfo(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this.outerUnit = outerUnit;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;

        this.resolvedInfo = null;
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
     * 既に解決済みかどうかを返す．
     * 
     * @return 解決済みである場合は true，そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みクラス参照を返す
     * 
     * @return 解決済みクラス参照
     * @throws NotResolvedException 解決されていない場合にスローされる
     */
    @Override
    public final T getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

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

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {
        final UnresolvedLocalSpaceInfo<?> outerUnit = (UnresolvedLocalSpaceInfo<?>) this
                .getOuterUnit();
        return outerUnit instanceof UnresolvedCallableUnitInfo<?> ? (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outerUnit
                : outerUnit.getOuterCallableUnit();
    }

    @Override
    public UnresolvedClassInfo getOuterClass() {
        return this.getOuterCallableUnit().getOuterClass();
    }

    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null";
        return this.outerUnit;
    }

    @Override
    public void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    /**
     * 解決済み情報を保存するための変数
     */
    protected T resolvedInfo;

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

    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;
}
