package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * ファイルメトリクスを登録するためのデータクラス
 * 
 * @author higo
 * 
 */
public final class FileMetricsInfo extends MetricsInfo<FileInfo> {

    /**
     * 計測対象ファイルを与えて初期化
     * 
     * @param fileInfo 計測対象ファイル
     */
    public FileMetricsInfo(final FileInfo fileInfo) {
        super(fileInfo);
    }

    /**
     * メッセージの送信者名を返す
     * 
     * @return メッセージの送信者名
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
}
