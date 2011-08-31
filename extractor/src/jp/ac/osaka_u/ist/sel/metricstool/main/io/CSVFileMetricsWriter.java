package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * ファイルメトリクスをCSVァイルに書き出すクラス
 * 
 * @author higo
 * 
 */
public final class CSVFileMetricsWriter implements FileMetricsWriter, CSVWriter, MessageSource {

    /**
     * CSVファイルを与える
     * 
     * @param fileName CSVファイル名
     */
    public CSVFileMetricsWriter(final String fileName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileName) {
            throw new NullPointerException();
        }

        this.fileName = fileName;
    }

    /**
     * ファイルメトリクスをCSVファイルに書き出す
     */
    public void write() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            // メトリクス名などを書き出し
            writer.write(FILE_NAME);
            for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                    .getPlugins()) {
                final PluginInfo pluginInfo = plugin.getPluginInfo();
                if (METRIC_TYPE.FILE_METRIC == pluginInfo.getMetricType()) {
                    String metricName = pluginInfo.getMetricName();
                    writer.write(SEPARATOR);
                    writer.write(metricName);
                }
            }

            writer.newLine();

            // メトリクス値を書き出し
            for (final FileMetricsInfo fileMetricsInfo : DataManager.getInstance()
                    .getFileMetricsInfoManager()) {
                final FileInfo fileInfo = fileMetricsInfo.getMeasuredObject();

                final String measuredName = fileInfo.getMeasuredUnitName();
                writer.write(measuredName);
                for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                        .getPlugins()) {
                    final PluginInfo pluginInfo = plugin.getPluginInfo();
                    if (METRIC_TYPE.FILE_METRIC == pluginInfo.getMetricType()) {

                        try {
                            writer.write(SEPARATOR);
                            final Number value = fileMetricsInfo.getMetric(plugin);
                            writer.write(value.toString());
                        } catch (MetricNotRegisteredException e) {
                            writer.write(NO_METRIC);
                        }
                    }
                }
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            MessagePrinter printer = new DefaultMessagePrinter(this,
                    MessagePrinter.MESSAGE_TYPE.ERROR);
            printer.println("IO Error Happened on " + this.fileName);
        }
    }

    /**
     * MessagerPrinter を用いるために必要なメソッド
     * 
     * @see MessagePrinter
     * @see MessageSource
     * 
     * @return メッセージ送信者名を返す
     */
    public String getMessageSourceName() {
        return this.getClass().toString();
    }

    /**
     * ファイルメトリクスを書きだすファイル名を保存するための変数
     */
    private final String fileName;
}
