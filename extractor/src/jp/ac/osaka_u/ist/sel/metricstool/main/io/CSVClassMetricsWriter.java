package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * クラスメトリクスをCSVァイルに書き出すクラス
 * 
 * @author higo
 * 
 */
public final class CSVClassMetricsWriter implements ClassMetricsWriter, CSVWriter, MessageSource {

    /**
     * CSVファイルを与える
     * 
     * @param fileName CSVファイル名
     */
    public CSVClassMetricsWriter(final String fileName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileName) {
            throw new NullPointerException();
        }

        this.fileName = fileName;
    }

    /**
     * クラスメトリクスをCSVファイルに書き出す
     */
    public void write() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName));

            // メトリクス名などを書き出し
            writer.write(CLASS_NAME);
            for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                    .getPlugins()) {
                final PluginInfo pluginInfo = plugin.getPluginInfo();
                if (METRIC_TYPE.CLASS_METRIC == pluginInfo.getMetricType()) {
                    String metricName = pluginInfo.getMetricName();
                    writer.write(SEPARATOR);
                    writer.write(metricName);
                }
            }

            writer.newLine();

            // メトリクス値を書き出し
            for (final ClassMetricsInfo classMetricsInfo : DataManager.getInstance()
                    .getClassMetricsInfoManager()) {
                final ClassInfo classInfo = classMetricsInfo.getMeasuredObject();

                final String measuredName = classInfo.getMeasuredUnitName();
                writer.write(measuredName);
                for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                        .getPlugins()) {
                    final PluginInfo pluginInfo = plugin.getPluginInfo();
                    if (METRIC_TYPE.CLASS_METRIC == pluginInfo.getMetricType()) {

                        try {
                            writer.write(SEPARATOR);
                            final Number value = classMetricsInfo.getMetric(plugin);
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
     * MessagerPrinter を用いるために必要なメソッド．メッセージ送信者名を返す
     * 
     * @see MessagePrinter
     * @see MessageSource
     * 
     * @return メッセージ送信者名
     */
    public String getMessageSourceName() {
        return this.getClass().toString();
    }

    /**
     * クラスメトリクスを書きだすファイル名を保存するための変数
     */
    private final String fileName;
}
