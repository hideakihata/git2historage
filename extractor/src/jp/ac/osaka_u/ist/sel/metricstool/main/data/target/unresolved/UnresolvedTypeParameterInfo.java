package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決型パラメータを表す抽象クラス
 * 
 * @author higo
 * 
 */
public class UnresolvedTypeParameterInfo implements Resolvable<TypeParameterInfo> {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param ownerUnit この型パラメータを定義しているユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param index 何番目の型パラメータであるかを表す
     */
    public UnresolvedTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit, final String name,
            final int index) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new IllegalArgumentException();
        }

        // ownerUnitがメソッドかクラスでない場合はエラー
        if ((!(ownerUnit instanceof UnresolvedClassInfo))
                && (!(ownerUnit instanceof UnresolvedCallableUnitInfo<?>))) {
            throw new IllegalArgumentException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.index = index;
        this.extendsTypes = new ArrayList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
    }

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param ownerUnit この型パラメータを定義しているユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param index 何番目の型パラメータであるかを表す
     * @param extendsType 未解決基底クラス型
     */
    public UnresolvedTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit, final String name,
            final int index,
            final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> extendsType) {
        this(ownerUnit, name, index);
        this.addExtendsType(extendsType);
    }

    /**
     * 既に名前解決されているかどうかを返す
     * 
     * @return 既に名前解決されている場合は true, そうでない場合は false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     * @throws NotResolvedException
     */
    public final TypeParameterInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 名前解決を行う
     * 
     * @param usingClass 名前解決を行うエンティティがあるクラス
     * @param usingMethod 名前解決を行うエンティティがあるメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * 
     * @return 解決済みのエンティティ
     */
    public TypeParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　型パラメータの所有ユニットを解決
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final TypeParameterizable ownerUnit = (TypeParameterizable) unresolvedOwnerUnit.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final String name = this.getName();
        final int index = this.getIndex();

        this.resolvedInfo = new TypeParameterInfo(ownerUnit, name, index);
        return this.resolvedInfo;
    }

    /**
     * この型パラメータを宣言しているユニット(クラス or メソッド)を返す
     * 
     * @return この型パラメータを宣言しているユニット(クラス or メソッド)
     */
    public final UnresolvedUnitInfo<?> getOwnerUnit() {
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
     * 型パラメータのインデックスを返す
     * 
     * @return　型パラメータのインデックス
     */
    public final int getIndex() {
        return this.index;
    }

    /**
     * 基底クラス型を追加する
     * 
     * @param extendsType
     */
    public final void addExtendsType(
            final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == extendsType) {
            throw new IllegalArgumentException();
        }

        this.extendsTypes.addAll(extendsType);
    }

    /**
     * 基底クラスの未解決型情報を返す
     * 
     * @return 基底クラスの未解決型情報
     */
    public final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getExtendsType() {
        return this.extendsTypes.get(0);
    }

    public final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> getExtendsTypes() {
        return Collections.unmodifiableList(this.extendsTypes);
    }

    /**
     * 基底クラスを持つかどうかを返す
     * 
     * @return 基底クラスを持つ場合は true, 持たない場合は false
     */
    public final boolean hasExtendsType() {
        return 0 < this.extendsTypes.size();
    }

    /**
     * 型パラメータを宣言しているユニットを保存するための変数
     */
    private final UnresolvedUnitInfo<?> ownerUnit;

    /**
     * 型パラメータ名を保存するための変数
     */
    private final String name;

    /**
     * 基底クラスを保存するための変数
     */
    private final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> extendsTypes;

    /**
     * 型パラメータのインデックスを保存するための変数
     */
    private final int index;

    /**
     * 名前解決された情報を保存するための変数
     */
    protected TypeParameterInfo resolvedInfo;
}
