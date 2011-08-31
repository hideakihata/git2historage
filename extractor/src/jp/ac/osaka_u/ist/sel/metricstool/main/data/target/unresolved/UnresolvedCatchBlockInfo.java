package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 catch ブロック情報を表すクラス
 * 
 * @author higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo<CatchBlockInfo> implements
        UnresolvedSubsequentialBlockInfo<UnresolvedTryBlockInfo> {

    /**
     * 対応するtry文と外側のブロックを与えて catch ブロックを初期化
     * 
     * @param ownerTryBlock 対応するtry文
     * @param outerSpace 外側のブロック
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo ownerTryBlock,
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        if (null == ownerTryBlock) {
            throw new IllegalArgumentException("ownerTryBlock is null");
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * この未解決 catch 節を解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public CatchBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この catchブロックの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        //　解決済み catchブロックオブジェクトを作成
        this.resolvedInfo = new CatchBlockInfo(fromLine, fromColumn, toLine, toColumn);

        // この catch 節が属する try 文を取得
        final UnresolvedTryBlockInfo unresolvedOwnerTryBlock = this.getOwnerBlock();
        final TryBlockInfo ownerTryBlock = unresolvedOwnerTryBlock.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOwnerBlock(ownerTryBlock);

        // 外側のユニットを解決
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

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
    @Override
    public final void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // キャッチする例外を解決
        final UnresolvedLocalVariableInfo unresolvedCaughtException = this.getCaughtException();
        final LocalVariableInfo caughtException = unresolvedCaughtException.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setCaughtException(caughtException);

    }

    /**
     * 対応する try ブロックを返す
     * このメソッドは将来廃止予定であり，使用は推奨されない
     * {@link UnresolvedCatchBlockInfo#getOwnerBlock()} を使用すべきである．
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

    public UnresolvedLocalVariableInfo getCaughtException() {
        assert null != this.caughtException : "this.caughtException must not be null!";
        return this.caughtException;
    }

    public void setCaughtException(UnresolvedLocalVariableInfo caughtException) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caughtException) {
            throw new IllegalArgumentException("caughtException is null");
        }
        this.caughtException = caughtException;
    }

    private final UnresolvedTryBlockInfo ownerTryBlock;

    private UnresolvedLocalVariableInfo caughtException;

}
