package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * プラグインがクラスメトリクスを登録するために用いるクラス．
 * 
 * @author higo
 */
public class DefaultClassMetricsRegister implements ClassMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultClassMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.CLASS_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be class type!");
        }

        this.plugin = plugin;
    }

    /**
     * 第一引数のクラスのメトリクス値（第二引数）を登録する
     * @param classInfo メトリクスを登録するクラス
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException すでにメトリクスが登録されている場合にスローされる例外
     */
    @Override
    public void registMetric(final TargetClassInfo classInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == classInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final ClassMetricsInfoManager manager = DataManager.getInstance()
                .getClassMetricsInfoManager();
        manager.putMetric(classInfo, this.plugin, value);
    }

    private final AbstractPlugin plugin;
}
