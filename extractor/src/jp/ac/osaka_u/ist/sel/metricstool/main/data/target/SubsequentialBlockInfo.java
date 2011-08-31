package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ElseBlock, CatchBlock，FinallyBlockのように，
 * あるBlockの存在下でのみ定義されるブロックであることを示すインタフェイス
 * 
 * @author g-yamada
 *
 * @param <T> このブロックがくっついているブロックの型 
 */
public interface SubsequentialBlockInfo<T extends BlockInfo> {

    /**
     * このブロックがくっついているブロックを返す
     *
     * @return このブロックがくっついているブロック
     */
    public T getOwnerBlock();

    /**
     * このブロックがくっついているブロックをセットする
     * 
     * @param ownerBlock このブロックがつっくいているブロック
     */
    public void setOwnerBlock(T ownerBlock);
}
