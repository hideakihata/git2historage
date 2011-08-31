package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * {@link BuildDataManager}でのブロック類(クラス，メソッド，ブロック)を管理するためのスタック
 * クラス，メソッド，ブロックそれぞれを区別せずにスタックに積むほか，便宜上それぞれのスタックも保持している．
 * @author g-yamada
 *
 */
public final class UnitStack extends Stack<UnresolvedUnitInfo<? extends UnitInfo>> {

    /**
     * コンストラクタ
     */
    public UnitStack() {
        super();
        this.classStack = new Stack<UnresolvedClassInfo>();
        this.callableUnitStack = new Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>>();
        this.blockStack = new Stack<UnresolvedBlockInfo<? extends BlockInfo>>();
    }

    /**
     * 自身のスタックから要素をひとつポップし，ポップした要素に応じて対応する個別のスタックもポップする
     */
    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> pop() {
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        final UnresolvedUnitInfo<? extends UnitInfo> result = super.pop();

        if (result instanceof UnresolvedBlockInfo) {
            this.blockStack.pop();
        } else if (result instanceof UnresolvedCallableUnitInfo) {
            this.callableUnitStack.pop();
        } else if (result instanceof UnresolvedClassInfo) {
            this.classStack.pop();
        } else {
            assert false : "here should not be reached";
        }

        return result;
    }

    /**
     * クラスをスタックに積む
     * @param unit このスタックに積むクラス
     */
    public void push(final UnresolvedClassInfo unit) {
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.classStack.push(unit);
    }

    /**
     * 呼び出し可能な要素(メソッド，コンストラクタ)をスタックに積む
     * @param unit このスタックに積む呼び出し可能な要素
     */
    public void push(final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> unit) {
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.callableUnitStack.push(unit);
    }

    /**
     * ブロックをスタックに積む
     * @param unit このスタックに積むブロック
     */
    public void push(final UnresolvedBlockInfo<? extends BlockInfo> unit) {
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.blockStack.push(unit);
    }
    
    public void clear(){
        super.clear();
        this.classStack.clear();
        this.blockStack.clear();
        this.callableUnitStack.clear();
    }

    /**
     * 直近のクラスを取得する．スタックからポップしない
     * @return 直近のクラス
     */
    public UnresolvedClassInfo getLatestClass() {
        return this.classStack.isEmpty() ? null : this.classStack.peek();
    }

    /**
     * 直近の呼び出し可能な要素(メソッド/コンストラクタ)を取得する．スタックからポップしない．
     * @return 直近の呼び出し可能な要素
     */
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getLatestCallableUnit() {
        return this.callableUnitStack.isEmpty() ? null : this.callableUnitStack.peek();
    }

    /**
     * 直近のブロックを取得する．スタックからポップしない
     * @return 直近のブロック
     */
    public UnresolvedBlockInfo<? extends BlockInfo> getLatestBlock() {
        return this.blockStack.isEmpty() ? null : this.blockStack.peek();
    }

    /**
     * クラスがスタックの最も上にあるかどうかを返す
     * @return クラスがスタックの最も上にあればtrue，そうでなければfalse
     */
    public boolean isClassAtPeek() {
        return this.peek() == this.getLatestClass();
    }

    /**
     * ブロックがスタックの最も上にあるかどうかを返す
     * @return ブロックがスタックの最も上にあればtrue, そうでなければfalse
     */
    public boolean isBlockAtPeek() {
        return this.peek() == this.getLatestBlock();
    }

    /**
     * 呼び出し可能な要素がスタックの最も上にあるかどうかを返す
     * @return 呼び出し可能な要素がスタックの最も上にあればtrue, そうでなければfalse
     */
    public boolean isCallableUnitAtPeek() {
        return this.peek() == this.getLatestCallableUnit();
    }
    
    /**
     * クラスのみを管理しているスタックを取得する
     * @return クラスのみを管理しているスタック
     */
    public Stack<UnresolvedClassInfo> getClassStack(){
        return this.classStack;
    }
    
    /**
     * 呼び出し可能な要素のみを管理しているスタックを取得する
     * @return 呼び出し可能な要素のみを管理しているスタック
     */
    public Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> getCallableUnitStack(){
        return this.callableUnitStack;
    }

    // 今のところ必要ない
//    public Stack<UnresolvedBlockInfo<? extends BlockInfo>> getBlockStack(){};
    
    /**
     * クラスのみを管理するスタック
     */
    private final Stack<UnresolvedClassInfo> classStack;

    /**
     * 呼び出し可能な要素のみを管理するクラス
     */
    private final Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> callableUnitStack;

    /**
     * ブロックのみを管理するクラス
     */
    private final Stack<UnresolvedBlockInfo<? extends BlockInfo>> blockStack;

    /**
     * 自動生成したシリアルID
     */
    private static final long serialVersionUID = 3545194400868254302L;
}
