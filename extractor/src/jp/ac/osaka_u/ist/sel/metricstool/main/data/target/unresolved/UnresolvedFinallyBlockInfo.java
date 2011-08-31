package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 finally ブロック情報を表すクラス
 * 
 * @author higo
 */
public final class UnresolvedFinallyBlockInfo extends UnresolvedBlockInfo<FinallyBlockInfo>
        implements UnresolvedSubsequentialBlockInfo<UnresolvedTryBlockInfo> {

    /**
     * 対応する try ブロック情報と外側のブロック情報を与えて finally ブロックを初期化
     * 
     * @param ownerTryBlock 対応する try ブロック
     * @param outerSpace 外側のブロック
     */
    public UnresolvedFinallyBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerTryBlock) {
            throw new IllegalArgumentException("ownerTryBlock is null");
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * この未解決 finally 節を解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public FinallyBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この finally 節の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new FinallyBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // この finally 節が属する try ブロックを取得
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerTryBlock);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
     * 対応する try ブロックを返す
     * このメソッドは将来廃止されるため，使用は推奨されない
     * {@link UnresolvedFinallyBlockInfo#getOwnerBlock()} を使用すべきである．
     * 
     * @return 対応する try ブロック
     * @deprecated
     */
    public UnresolvedTryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    @Override
    public UnresolvedTryBlockInfo getOwnerBlock() {
        return this.ownerTryBlock;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;

}
