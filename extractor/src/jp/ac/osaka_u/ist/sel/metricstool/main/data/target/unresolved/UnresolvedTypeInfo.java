package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


/**
 * Unresolvedな型を表すインターフェース．
 * 
 * @author higo
 * 
 * @param <T> 解決された型を表す型パラメータ
 */
public interface UnresolvedTypeInfo<T extends TypeInfo> {

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
    T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    T getResolved();

    /**
     * 既に名前解決されたかどうかを返す
     * 
     * @return 名前解決されている場合は true，そうでない場合は false
     */
    boolean alreadyResolved();

    /**
     * 型名を返す
     * 
     * @return
     */
    String getTypeName();
}
