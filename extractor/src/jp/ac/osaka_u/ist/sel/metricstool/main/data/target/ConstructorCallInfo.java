package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * コンストラクタ呼び出しを保存する変数
 * 
 * @author higo

 * @param <T> 型の情報，クラス型か配列型か
 */
@SuppressWarnings("serial")
public abstract class ConstructorCallInfo<T extends ReferenceTypeInfo> extends
        CallInfo<ConstructorInfo> {

    /**
     * 型を与えてコンストラクタ呼び出しを初期化
     * 
     * @param referenceType 呼び出しの型
     * @param callee 呼び出されているコンストラクタ
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ConstructorCallInfo(final T referenceType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * コンストラクタ呼び出しの型を返す
     */
    @Override
    public T getType() {
        return this.referenceType;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public final Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.add(this);
        for (final ExpressionInfo argument : this.getArguments()) {
            calls.addAll(argument.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    private final T referenceType;

}
