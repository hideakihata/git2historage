package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 外部クラスに定義されているメソッド情報を保存するためのクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * アクセス制御子まで分かっている場合
     *  
     * @param methodName メソッド名
     */
    public ExternalMethodInfo(final Set<ModifierInfo> modifiers, final String methodName,
            final boolean instance) {

        super(modifiers, methodName, instance, getDummyPosition(), getDummyPosition(),
                getDummyPosition(), getDummyPosition());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * 外部クラスに定義されているメソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, true, getDummyPosition(),
                getDummyPosition(), getDummyPosition(), getDummyPosition());

        this.setOuterUnit(ExternalClassInfo.UNKNOWN);
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * 空のSortedSetを返す
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(new TreeSet<StatementInfo>());
    }
}
