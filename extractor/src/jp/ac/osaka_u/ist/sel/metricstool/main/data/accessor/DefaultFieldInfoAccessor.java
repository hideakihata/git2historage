package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * プラグインが FileInfo にアクセスするために用いるインターフェース
 * 
 * @author higo
 *
 */
public class DefaultFieldInfoAccessor implements FieldInfoAccessor {

    /**
     * FileInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return FileInfo のイテレータ
     */
    @Override
    public Iterator<TargetFieldInfo> iterator() {
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final SortedSet<TargetFieldInfo> fieldInfos = fieldInfoManager.getTargetFieldInfos();
        return fieldInfos.iterator();
    }

    /**
     * 対象ファイルの数を返すメソッド.
     * @return 対象ファイルの数
     */
    @Override
    public int getFieldCount() {
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        return fieldInfoManager.getTargetFieldCount();
    }

}
