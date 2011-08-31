package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * このインターフェースは，フィールドメトリクスを登録するためのメソッド群を提供する．
 * 
 * @author higo
 * 
 */
public interface FieldMetricsRegister {

    /**
     * 第一引数のフィールドのメトリクス値（第二引数）を登録する
     * 
     * @param fieldInfo メトリクスの計測対象フィールド
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている場合にスローされる
     */
    void registMetric(TargetFieldInfo fieldInfo, Number value)
            throws MetricAlreadyRegisteredException;
}
