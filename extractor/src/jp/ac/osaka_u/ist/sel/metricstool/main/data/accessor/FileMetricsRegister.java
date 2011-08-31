package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * このインターフェースは，ファイルメトリクスを登録するためのメソッド群を提供する．
 * 
 * @author higo
 * 
 */
public interface FileMetricsRegister {

    /**
     * 第一引数のファイルのメトリクス値（第二引数）を登録する
     * 
     * @param fileInfo メトリクスの計測対象ファイル
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている場合にスローされる
     */
    void registMetric(FileInfo fileInfo, Number value) throws MetricAlreadyRegisteredException;
}
