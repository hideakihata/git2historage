package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;


/**
 * クラスのインスタンスイニシャライザの未解決情報を保存するクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedInstanceInitializerInfo extends
        UnresolvedInitializerInfo<InstanceInitializerInfo> {

    /**
     * このインスタンスイニシャライザを所有するクラスを与えて初期化
     * @param ownerClass インスタンスイニシャライザを所有するクラス
     */
    public UnresolvedInstanceInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedInstanceInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    public boolean isStaticMember() {
        return false;
    }

    @Override
    protected InstanceInitializerInfo buildResolvedInfo(int fromLine, int fromColumn, int toLine,
            int toColumn) {
        return new InstanceInitializerInfo(fromLine, fromColumn, toLine, toColumn);
    }
}
