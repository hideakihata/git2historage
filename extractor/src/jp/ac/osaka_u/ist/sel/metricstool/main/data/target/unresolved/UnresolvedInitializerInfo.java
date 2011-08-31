package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決イニシャライザの共通の親クラス
 * <br>
 * イニシャライザとは，スタティック・イニシャライザやインスタンス・イニシャライザ　などである 
 * 
 * @param <T> 解決済みイニシャライザ情報の型
 * @author　g-yamada
 *
 */
public abstract class UnresolvedInitializerInfo<T extends InitializerInfo> extends
        UnresolvedCallableUnitInfo<T> {

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedInitializerInfo(final UnresolvedClassInfo ownerClass, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public T resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo = this.buildResolvedInfo(this.getFromLine(), this.getFromColumn(), this
                .getToLine(), this.getToColumn());
        this.resolvedInfo.setOuterUnit(ownerClass);

        return this.resolvedInfo;
    }

    protected abstract T buildResolvedInfo(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn);
}
