package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * フィールドメトリクスを書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface FieldMetricsWriter extends MetricsWriter {

    /**
     * フィールド名のタイトル文字列
     */
    String FIELD_NAME = new String("\"Field Name\"");
}
