package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;


/**
 * 未解決 static initializer を表すクラス
 * 
 * @author t-miyake, higo
 */
public class UnresolvedStaticInitializerInfo extends
        UnresolvedInitializerInfo<StaticInitializerInfo> {

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public boolean isStaticMember() {
        return true;
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    protected StaticInitializerInfo buildResolvedInfo(int fromLine, int fromColumn, int toLine,
            int toColumn) {
        return new StaticInitializerInfo(fromLine, fromColumn, toLine, toColumn);
    }
}
