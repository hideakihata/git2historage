package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;

/**
 * ElseBlock, CatchBlock, FinallyBLock のよううに，
 * あるBlockの存在下でのみ定義される未解決ブロックであることを表すインタフェイス
 * @author g-yamada
 *
 * @param <T> このブロックがくっついている未解決ブロックの型
 */
public interface UnresolvedSubsequentialBlockInfo<T extends UnresolvedBlockInfo<? extends BlockInfo>>{
/**
 * このブロックがくっついている未解決ブロックを返す
 * 
 * @return このブロックがくっついている未解決ブロック
 */
    public T getOwnerBlock();

}
