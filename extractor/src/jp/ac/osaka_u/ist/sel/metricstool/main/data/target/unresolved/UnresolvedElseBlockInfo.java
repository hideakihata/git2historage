package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 else ブロックを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedElseBlockInfo extends UnresolvedBlockInfo<ElseBlockInfo> implements
        UnresolvedSubsequentialBlockInfo<UnresolvedIfBlockInfo> {

    /**
     * 外側のブロックと対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param ownerIfBlock
     * @param outerSpace 外側のブロック
     */
    public UnresolvedElseBlockInfo(final UnresolvedIfBlockInfo ownerIfBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerIfBlock) {
            throw new IllegalArgumentException("ownerIfBlock is null");
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    /**
     * この未解決 else ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public ElseBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この else ブロックの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ElseBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // この else ブロックが属する if ブロックを取得
        final UnresolvedIfBlockInfo unresolvedOwnerIfBlock = this.getOwnerBlock();
        final IfBlockInfo ownerIfBlock = unresolvedOwnerIfBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerIfBlock);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * このメソッドは将来廃止予定であり，使用は推奨されない
     * {@link UnresolvedElseBlockInfo#getOwnerBlock()}を使用すべきである．
     * 
     * @return この else ブロックと対応する if ブロック
     * @deprecated
     */
    public UnresolvedIfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    @Override
    public UnresolvedIfBlockInfo getOwnerBlock() {
        return this.ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private final UnresolvedIfBlockInfo ownerIfBlock;

}
