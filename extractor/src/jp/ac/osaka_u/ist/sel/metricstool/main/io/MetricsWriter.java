package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * メトリクスを書き出すjクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface MetricsWriter {

    /**
     * メトリクスをファイルに書き出す
     */
    public abstract void write();

    /**
     * メトリクス値がないことを表す文字
     */
    char NO_METRIC = '-';

}