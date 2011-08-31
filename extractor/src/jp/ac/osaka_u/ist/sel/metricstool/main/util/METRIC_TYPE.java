package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * @author kou-tngt,higo
 * 
 * メトリクスの種類を表すEnum．
 */
public enum METRIC_TYPE {
    /**
     * ファイル単位のメトリクス
     */
    FILE_METRIC,

    /**
     * クラス単位のメトリクス
     */
    CLASS_METRIC,

    /**
     * メソッド単位のメトリクス
     */
    METHOD_METRIC,
    
    /**
     * フィールド単位のメトリクス
     */
    FIELD_METRIC
}
