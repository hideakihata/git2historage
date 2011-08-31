package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * 
 * @author y-higo
 *
 * @param <T>
 */
public interface Resolvable<T> {

    /**
     * 名前解決を行い，解決済みオブジェクトを返す
     * 
     * @param usingClass 名前解決を行うエンティティがあるクラス
     * @param usingMethod 名前解決を行うエンティティがあるメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * 
     * @return 解決済みのオブジェクト
     */
    public abstract T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    public abstract T getResolved();

    /**
     * 既に名前解決されたかどうかを返す
     * 
     * @return 名前解決されている場合は true，そうでない場合は false
     */
    public abstract boolean alreadyResolved();
}
