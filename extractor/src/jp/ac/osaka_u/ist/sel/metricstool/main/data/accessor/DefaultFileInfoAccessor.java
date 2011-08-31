package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfoManager;


/**
 * プラグインが FileInfo にアクセスするために用いるインターフェース
 * 
 * @author higo
 *
 */
public class DefaultFileInfoAccessor implements FileInfoAccessor {

    /**
     * FileInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return FileInfo のイテレータ
     */
    @Override
    public Iterator<FileInfo> iterator() {
        final FileInfoManager fileInfoManager = DataManager.getInstance().getFileInfoManager();
        final SortedSet<FileInfo> fileInfos = fileInfoManager.getFileInfos();
        return fileInfos.iterator();
    }

    /**
     * 対象ファイルの数を返すメソッド.
     * @return 対象ファイルの数
     */
    @Override
    public int getFileCount() {
        final FileInfoManager fileInfoManager = DataManager.getInstance().getFileInfoManager();
        return fileInfoManager.getFileCount();
    }

}
