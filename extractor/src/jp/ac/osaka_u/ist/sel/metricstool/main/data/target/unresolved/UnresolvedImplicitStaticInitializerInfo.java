package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決暗黙インスタンスイニシャライザを表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedImplicitStaticInitializerInfo extends UnresolvedStaticInitializerInfo {

    /**
     * このインスタンスイニシャライザを所有するクラスの未解決情報を与えて初期化
     * @param ownerClass このインスタンスイニシャライザを所有するクラスの未解決情報
     */
    public UnresolvedImplicitStaticInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    @Override
    public StaticInitializerInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
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

        this.resolvedInfo = ownerClass.getImplicitStaticInitializer();
        
        return this.resolvedInfo;
    }

}
