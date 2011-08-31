package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ファイルメトリクスを管理するクラス．
 * 
 * @author higo
 * 
 */
public final class FileMetricsInfoManager implements Iterable<FileMetricsInfo>, MessageSource {

    /**
     * メトリクス情報一覧のイテレータを返す．
     * 
     * @return メトリクス情報のイテレータ
     */
    public Iterator<FileMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<FileMetricsInfo> unmodifiableFileMetricsInfoCollection = Collections
                .unmodifiableCollection(this.fileMetricsInfos.values());
        return unmodifiableFileMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたファイルのメトリクス情報を返す． 引数で指定されたファイルのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param fileInfo ほしいメトリクス情報のファイル
     * @return メトリクス情報
     */
    public FileMetricsInfo get(final FileInfo fileInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo) {
            throw new NullPointerException();
        }

        return this.fileMetricsInfos.get(fileInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param fileInfo メトリクス計測対象のファイルオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final FileInfo fileInfo, final AbstractPlugin plugin, final Number value)
            throws MetricAlreadyRegisteredException {

        FileMetricsInfo fileMetricsInfo = this.fileMetricsInfos.get(fileInfo);

        // 対象ファイルの fileMetricsInfo が無い場合は，new して Map に登録する
        if (null == fileMetricsInfo) {
            fileMetricsInfo = new FileMetricsInfo(fileInfo);
            this.fileMetricsInfos.put(fileInfo, fileMetricsInfo);
        }

        fileMetricsInfo.putMetric(plugin, value);
    }

    /**
     * ファイルメトリクスに登録漏れがないかをチェックする
     * 
     * @throws MetricNotRegisteredException 登録漏れがあった場合にスローされる
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final FileInfo fileInfo : DataManager.getInstance().getFileInfoManager()
                .getFileInfos()) {

            final FileMetricsInfo fileMetricsInfo = this.get(fileInfo);
            if (null == fileMetricsInfo) {
                final String message = "File \"" + fileInfo.getName()
                        + "\" metrics are not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            fileMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getFileMetricPlugins());
        }
    }

    /**
     * メッセージ送信者名を返す
     * 
     * @return メッセージ送信者
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }

    /**
     * ファイルメトリクスマネージャのオブジェクトを生成する．
     * 
     */
    public FileMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.fileMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<FileInfo, FileMetricsInfo>());
    }

    /**
     * ファイルメトリクスのマップを保存するための変数
     */
    private final SortedMap<FileInfo, FileMetricsInfo> fileMetricsInfos;
}
