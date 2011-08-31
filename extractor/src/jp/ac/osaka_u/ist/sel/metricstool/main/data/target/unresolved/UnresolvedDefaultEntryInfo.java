package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedDefaultEntryInfo extends UnresolvedCaseEntryInfo {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedDefaultEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {
        super(correspondingSwitchBlock, outerUnit);
    }

    /**
     * この未解決 default エントリを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public final DefaultEntryInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return (DefaultEntryInfo) this.getResolved();
        }

        // この default エントリが属する switch 文を取得
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = this.getOwnerSwitchBlock();
        final SwitchBlockInfo ownerSwitchBlock = unresolvedOwnerSwitchBlock.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // この default エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // default　エントリオブジェクトを作成
        this.resolvedInfo = new DefaultEntryInfo(ownerSwitchBlock, fromLine, fromColumn, toLine,
                toColumn);
        return (DefaultEntryInfo) this.resolvedInfo;
    }
}
