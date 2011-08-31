package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 型パラメータを表す抽象クラス
 * 
 * @author higo
 * 
 */
public class TypeParameterInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param ownerUnit 型パラメータを宣言しているユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param index 何番目の型パラメータかを表す
     */
    public TypeParameterInfo(final TypeParameterizable ownerUnit, final String name, final int index) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new NullPointerException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.index = index;
        this.extendsTypes = new ArrayList<TypeInfo>();
    }

    /**
     * この型パラメータが引数で与えられた型と等しいかどうかを判定する
     * 
     * @param o 比較対象型情報
     * @return 等しい場合は true，等しくない場合は false;
     */
    public boolean equals(final TypeInfo o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof TypeParameterInfo)) {
            return false;
        }

        return this.getName().equals(((TypeParameterInfo) o).getName());
    }

    /**
     * 型パラメータを宣言しているユニット(クラス or メソッド)を返す
     * 
     * @return  型パラメータを宣言しているユニット(クラス or メソッド)
     */
    public final TypeParameterizable getOwnerUnit() {
        return this.ownerUnit;
    }

    /**
     * 型パラメータ名を返す
     * 
     * @return 型パラメータ名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 型名（実際には型パラメータ名）を返す
     * 
     * @return 型名
     */
    public final String getTypeName() {

        StringBuilder sb = new StringBuilder(this.name);
        if (this.hasExtendsType()) {
            sb.append(" extends ");

            for (final TypeInfo extendsType : this.getExtendsTypes()) {
                sb.append(extendsType.getTypeName());
                sb.append(" & ");
            }

            sb.delete(sb.length() - 3, sb.length());
        }

        return sb.toString();
    }

    /**
     * 型パラメータのインデックスを返す
     * 
     * @return　型パラメータのインデックス
     */
    public final int getIndex() {
        return this.index;
    }

    /**
     * 基底クラス型のListを返す
     * 
     * @return 基底クラス型のList
     */
    public final List<TypeInfo> getExtendsTypes() {
        return Collections.unmodifiableList(this.extendsTypes);
    }

    public void addExtendsType(final TypeInfo extendsType) {

        if (null == extendsType) {
            throw new IllegalArgumentException();
        }

        this.extendsTypes.add(extendsType);
    }

    /**
     * * 基底クラスを持つかどうかを返す
     * 
     * @return 基底クラスを持つ場合は true,持たない場合は false
     */
    public final boolean hasExtendsType() {
        return 0 != this.extendsTypes.size();
    }

    /**
     * 型パラメータを所有しているユニットを保存するための変数
     */
    private final TypeParameterizable ownerUnit;

    /**
     * 型パラメータ名を保存するための変数
     */
    private final String name;

    /**
     * この型パラメータが何番目のものかを表す変数
     */
    private final int index;

    /**
     * 基底クラス型のListを保存するための変数
     */
    private List<TypeInfo> extendsTypes;
}
