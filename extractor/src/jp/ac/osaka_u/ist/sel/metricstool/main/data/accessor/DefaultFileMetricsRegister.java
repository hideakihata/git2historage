package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * プラグインがファイルメトリクスを登録するために用いるクラス．
 * 
 * @author higo
 */
public class DefaultFileMetricsRegister implements FileMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultFileMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.FILE_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be file type!");
        }

        this.plugin = plugin;
    }

    /**
     * 第一引数のファイルのメトリクス値（第二引数）を登録する
     * 
     * @param fileInfo メトリクスを登録するファイル
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException すでにメトリクスが登録されている場合にスローされる例外
     */
    @Override
    public void registMetric(final FileInfo fileInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fileInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final FileMetricsInfoManager manager = DataManager.getInstance()
                .getFileMetricsInfoManager();
        manager.putMetric(fileInfo, this.plugin, value);
    }

    /**
     * プラグインオブジェクトを保存しておくための変数
     */
    private final AbstractPlugin plugin;
}
