package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;


/**
 * 配列型の定義を表すためのクラス．
 * できれば使いたくはない．
 * 
 * @author higo
 *
 */
@SuppressWarnings( { "serial" })
public final class ArrayTypeClassInfo extends ClassInfo {

    /**
     * 配列の型を与えて，オブジェクトを初期化
     * 
     * @param arrayType 配列の型
     */
    public ArrayTypeClassInfo(final ArrayTypeInfo arrayType) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, NONAME, false, 0, 0, 0, 0);

        if (null == arrayType) {
            throw new IllegalArgumentException();
        }
        this.arrayType = arrayType;
    }

    /**
     * 配列の型を返す
     * 
     * @return 配列の型
     */
    public ArrayTypeInfo getArrayType() {
        return this.arrayType;
    }

    /**
     * 変数利用の一覧を返す．
     * どの変数も用いられていないので，空のsetが返される
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    private final ArrayTypeInfo arrayType;

    /**
     * 配列型を表すためのクラスなので名前はない．
     * 名前がないことを表す定数．
     */
    public static final String NONAME = "noname";
}
