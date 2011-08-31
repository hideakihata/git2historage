package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedConstructorCallInfo<T extends UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>, R extends ConstructorCallInfo<? extends ReferenceTypeInfo>>
        extends UnresolvedCallInfo<R> {

    /**
     * コンストラクタ呼び出しが実行される参照型を与えてオブジェクトを初期化
     * 
     * @param unresolvedReferenceType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedConstructorCallInfo(final T unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
    }

    /**
     * コンストラクタ呼び出しが実行される参照型を与えて初期化
     * @param unresolvedReferenceType コンストラクタ呼び出しが実行される型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedConstructorCallInfo(final T unresolvedReferenceType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this(unresolvedReferenceType);

        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * この未解決コンストラクタ呼び出しの型を返す
     * 
     * @return この未解決コンストラクタ呼び出しの型
     */
    public T getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final T unresolvedReferenceType;

}
