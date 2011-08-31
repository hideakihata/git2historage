package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 try ブロックを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedTryBlockInfo extends UnresolvedBlockInfo<TryBlockInfo> {

    /**
     * 外側のブロック情報を与えて，try ブロック情報を初期化
     * 
     * @param outerSpace 外側のブロック
     */
    public UnresolvedTryBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.sequentCatchBlocks = new HashSet<UnresolvedCatchBlockInfo>();
        this.sequentFinallyBlock = null;
    }

    /**
     * この未解決 try ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public TryBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この for エントリの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new TryBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        // 対応するfinally節を解決
        if (this.hasFinallyBlock()) {
            final UnresolvedFinallyBlockInfo unresolvedFinallyBlock = this.getSequentFinallyBlock();
            final FinallyBlockInfo finallyBlock = unresolvedFinallyBlock.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.setSequentFinallyBlock(finallyBlock);
        }

        // 対応するcatch節を解決し，解決済みtryブロックオブジェクトに追加
        for (final UnresolvedCatchBlockInfo unresolvedCatchBlock : this.getSequentCatchBlocks()) {
            final CatchBlockInfo catchBlock = unresolvedCatchBlock.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addSequentCatchBlock(catchBlock);
        }

        return this.resolvedInfo;
    }

    /**
     * このローカル領域のインナー領域を名前解決する
     * 
     * @param usingClass この領域が存在しているクラス
     * @param usingMethod この領域が存在しているメソッド
     * @param classInfoManager クラスマネージャ
     * @param fieldInfoManager フィールドマネージャ
     * @param methodInfoManager メソッドマネージャ
     */
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // 対応するfinally節を解決
        if (this.hasFinallyBlock()) {
            final UnresolvedFinallyBlockInfo unresolvedFinallyBlock = this.getSequentFinallyBlock();
            unresolvedFinallyBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }

        // 対応するcatch節を解決し，解決済みtryブロックオブジェクトに追加
        for (final UnresolvedCatchBlockInfo unresolvedCatchBlock : this.getSequentCatchBlocks()) {
            unresolvedCatchBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
        }
    }

    /**
     * 対応するcatchブロックを追加する
     * @param catchBlock 対応するcatchブロック
     */
    public void addSequentCatchBlock(final UnresolvedCatchBlockInfo catchBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * 対応するcatchブロックのSetを返す
     * @return 対応するcatchブロックのSet
     */
    public Set<UnresolvedCatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * 対応するfinallyブロックを返す
     * @return 対応するfinallyブロック．finallyブロックが宣言されていないときはnull
     */
    public UnresolvedFinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * 対応するfinallyブロックをセットする
     * @param finallyBlock 対応するfinallyブロック
     */
    public void setSequentFinallyBlock(final UnresolvedFinallyBlockInfo finallyBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == finallyBlock) {
            throw new IllegalArgumentException("finallyBlock is null");
        }
        this.sequentFinallyBlock = finallyBlock;
    }

    /**
     * 対応するfinallyブロックが存在するかどうか返す
     * @return 対応するfinallyブロックが存在するならtrue
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * 対応するcatchブロックを保存する変数
     */
    private final Set<UnresolvedCatchBlockInfo> sequentCatchBlocks;

    /**
     * 対応する finally ブロックを保存する変数
     */
    private UnresolvedFinallyBlockInfo sequentFinallyBlock;

}
