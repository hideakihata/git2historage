package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決暗黙スタティックイニシャライザを表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedImplicitInstanceInitializerInfo extends UnresolvedInstanceInitializerInfo {

    /**
     * このスタティックイニシャライザを所有するクラスの未解決情報をあえて初期化
     * @param ownerClass このスタティックイニシャライザを所有するクラスの未解決情報
     */
    public UnresolvedImplicitInstanceInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    @Override
    public InstanceInitializerInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == usingClass) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final TargetClassInfo ownerClass = this.getOwnerClass().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        
        this.resolvedInfo = ownerClass.getImplicitInstanceInitializer();

        return this.resolvedInfo;
    }
}
