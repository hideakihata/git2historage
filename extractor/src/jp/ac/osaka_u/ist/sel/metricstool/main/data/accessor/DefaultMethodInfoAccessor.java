package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import java.util.Iterator;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * プラグインが MethodInfo にアクセスするために用いるインターフェース
 * 
 * @author higo
 * 
 */
public class DefaultMethodInfoAccessor implements MethodInfoAccessor {

    /**
     * MethodInfo のイテレータを返す． このイテレータは参照専用であり変更処理を行うことはできない．
     * 
     * @return MethodInfo のイテレータ
     */
    @Override
    public Iterator<TargetMethodInfo> iterator() {
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();
        final SortedSet<TargetMethodInfo> methodInfos = methodInfoManager.getTargetMethodInfos();
        return methodInfos.iterator();
    }

    /**
     * 対象メソッドの数を返すメソッド.
     * 
     * @return 対象メソッドの数
     */
    @Override
    public int getMethodCount() {
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();
        return methodInfoManager.getTargetMethodCount();
    }

}
