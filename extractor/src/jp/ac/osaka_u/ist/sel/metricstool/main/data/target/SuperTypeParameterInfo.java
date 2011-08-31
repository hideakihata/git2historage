package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 型パラメータ（表記法式が <X super Y>）を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * 型パラメータ名，派生クラス型を与えてオブジェクトを初期化
     * 
     * @param ownerUnit この型パラメータの所有ユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param index 何番目の型パラメータかを表す
     * @param extendsType 基底クラス型
     * @param superType 派生クラス型
     */
    public SuperTypeParameterInfo(final TypeParameterizable ownerUnit, final String name,
            final int index, final TypeInfo extendsType, final TypeInfo superType) {

        super(ownerUnit, name, index);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * 派生クラス型を返す
     * 
     * @return 派生クラス型
     */
    public TypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス型を保存する
     */
    private final TypeInfo superType;
}
