package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Comparator;


/**
 * プラグインインスタンスのコンパレータ.
 * プラグインのクラス名のみで比較する.
 * @author kou-tngt
 *
 */
public class ClassNamePluginComparator implements Comparator<AbstractPlugin> {

    /**
     * プラグインインスタンスのクラス名のみで比較する.
     * @param o1 比較されプラグイン
     * @param o2 比較するプラグイン
     * @return o1がo2よりも順序的に小さければ負の数，同じであれば0，大きければ正の数を返す.
     */
    public int compare(final AbstractPlugin o1, final AbstractPlugin o2) {
        return o1.getClass().getCanonicalName().compareTo(o2.getClass().getCanonicalName());
    }

}
