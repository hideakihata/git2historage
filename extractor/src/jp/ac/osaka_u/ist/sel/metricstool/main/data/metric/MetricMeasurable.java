package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * メトリクスの計測可能単位であることを表すインターフェース
 * 
 * @author higo
 */
public interface MetricMeasurable {

    /**
     * 計測単位名を返すメソッド
     * 
     * @return 計測単位名
     */
    String getMeasuredUnitName();
}
