package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Comparator;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;


/**
 * プラグインを比較するコンパレータ.
 * メトリクスのタイプ->メトリクス名->クラス名の順で比較する.
 * クラス名まで一致するプラグインは同一と判定する.
 * @author kou-tngt
 *
 */
public class MetricTypeAndNamePluginComparator implements Comparator<AbstractPlugin> {

    /**
     * メトリクスのタイプ->メトリクス名->クラス名の順で比較する.
     * クラス名まで一致するプラグインは同一と判定する.
     * @param o1 比較されプラグイン
     * @param o2 比較するプラグイン
     * @return o1がo2よりも順序的に小さければ負の数，同じであれば0，大きければ正の数を返す.
     */
    public int compare(final AbstractPlugin o1, final AbstractPlugin o2) {
        final PluginInfo info1 = o1.getPluginInfo();
        final PluginInfo info2 = o2.getPluginInfo();

        int result = info1.getMetricType().compareTo(info2.getMetricType());
        if (0 != result) {
            return result;
        }

        result = info1.getMetricName().compareTo(info2.getMetricName());
        if (0 != result) {
            return result;
        }

        return o1.getClass().getCanonicalName().compareTo(o2.getClass().getCanonicalName());
    }
}
