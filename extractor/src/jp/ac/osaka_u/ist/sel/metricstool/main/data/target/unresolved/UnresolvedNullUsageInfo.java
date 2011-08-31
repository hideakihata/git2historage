package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決null使用を表すためのクラス．
 * UnresolvedEntityUsageInfoとEntityUsageInfoがクラスであるため，
 * NullUsageInfoでこれら両方を継承することができない．
 * そのための妥協案として作成したクラス．
 * 
 * @author higo
 *
 */
public final class UnresolvedNullUsageInfo extends UnresolvedExpressionInfo<NullUsageInfo> {

    /**
     * オブジェクトを初期化
     */
    public UnresolvedNullUsageInfo() {

        this.resolvedInfo = null;
    }

    /**
    * 使用情報の名前解決する
    * 
    * @return 解決済みの使用情報
    */
    @Override
    public NullUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        /*// 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new NullUsageInfo(usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

}
