package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;




/**
 * このインターフェースは，ファイル情報を取得するためのメソッド郡を提供する．
 * 
 * @author higo
 *
 */
public interface FileInfoAccessor extends Iterable<FileInfo>{

    /**
     * 対象ファイルの数を返すメソッド.
     * @return 対象ファイルの数
     */
    public int getFileCount();
}
