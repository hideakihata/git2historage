package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * クラスメトリクスをファイルに書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 */
public interface ClassMetricsWriter extends MetricsWriter {

    /**
     * クラス名のタイトル文字列
     */
    String CLASS_NAME = new String("\"Class Name\"");
}
