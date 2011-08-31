package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * if ブロックや for ブロックなど メソッド内の構造的なまとまりの単位を表す抽象クラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public abstract class BlockInfo extends LocalSpaceInfo implements StatementInfo {

    /**
     * 位置情報を与えて初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    BlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このブロックオブジェクトを他のブロックオブジェクトと比較する
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof BlockInfo)) {
            return false;
        }

        return 0 == this.compareTo((BlockInfo) o);
    }

    /**
     * このブロック文のハッシュコードを返す
     */
    @Override
    public final int hashCode() {
        return this.getOwnerClass().hashCode() + this.getFromLine() + this.getFromColumn()
                + this.getToLine() + this.getToColumn();
    }

    /**
     * このブロックを所有するを返す
     * 
     * @return このブロックを所有するメソッド
     */
    @Override
    public final CallableUnitInfo getOwnerMethod() {

        final LocalSpaceInfo outerSpace = this.getOwnerSpace();
        if (outerSpace instanceof CallableUnitInfo) {
            return (CallableUnitInfo) outerSpace;
        }

        if (outerSpace instanceof BlockInfo) {
            return ((BlockInfo) outerSpace).getOwnerMethod();
        }

        throw new IllegalStateException();
    }

    /**
     * このブロックが繰り返し文であるかどうか返す
     * @return 繰り返し文であるならtrue
     */
    public boolean isLoopStatement() {
        return false;
    }

    /**
     * このブロックを直接所有するローカル空間を返す
     * 
     * @return このブロックを直接所有するローカル空間
     */
    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return (LocalSpaceInfo) super.getOuterUnit();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        for (final StatementInfo innerStatement : this.getStatements()) {
            thrownExpressions.addAll(innerStatement.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }
}
