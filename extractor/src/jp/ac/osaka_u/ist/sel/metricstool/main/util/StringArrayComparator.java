package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Comparator;


/**
 * Stringの配列を比較するためのクラス
 * 
 * @author higo
 */
public class StringArrayComparator implements Comparator<String[]> {

    /**
     * このクラスの単一オブジェクト．
     * 引数なしコンストラクタは private で宣言されているため新たにオブジェクトを作成することはできない．
     */
    public static final StringArrayComparator SINGLETON = new StringArrayComparator();

    /**
     * 二つのString[]を比較する
     */
    public int compare(final String[] array1, final String[] array2) {

        if ((null == array1) || (null == array2)) {
            throw new IllegalArgumentException();
        }

        for (int index = 0;; index++) {

            if ((array1.length <= index) && (array2.length <= index)) {
                return 0;

            } else if ((array1.length <= index) && (index < array2.length)) {
                return -1;

            } else if ((index < array1.length) && (array2.length <= index)) {
                return 1;
            } else {
                final int order = array1[index].compareTo(array2[index]);
                if (0 != order) {
                    return order;
                }
            }
        }
    }

    /**
     * シングルトンパターンを使っているために private にしている
     */
    private StringArrayComparator() {
    }
}
