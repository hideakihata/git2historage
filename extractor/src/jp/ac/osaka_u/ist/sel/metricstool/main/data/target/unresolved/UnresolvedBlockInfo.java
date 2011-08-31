package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


/**
 * if文やwhile文などのメソッド内の構造（ブロック）を表すためのクラス
 * 
 * @author higo
 * @param <T> 解決済みのブロックの型
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> extends UnresolvedLocalSpaceInfo<T>
        implements UnresolvedStatementInfo<T> {

    /**
     * このブロックの外側に位置するブロックを与えて，オブジェクトを初期化
     * 
     * @param outerSpace このブロックの外側に位置するブロック
     * 
     */
    public UnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    public void initBody() {
        this.statements.clear();
    }

    /**
     * このブロックが属する空間を返す
     * @return このブロックが属する空間
     */
    public UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> getOuterSpace() {
        return (UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>) this.getOuterUnit();
    }
}
