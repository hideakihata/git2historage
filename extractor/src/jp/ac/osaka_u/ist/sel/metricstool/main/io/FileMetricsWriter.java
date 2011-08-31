package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * ファイルメトリクスを書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface FileMetricsWriter extends MetricsWriter {

    /**
     * ファイル名のタイトル文字列
     */
    String FILE_NAME = new String("\"File Name\"");
}
