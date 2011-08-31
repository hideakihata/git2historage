package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * プラグインがメソッドメトリクスを登録するために用いるクラス．
 * 
 * @author higo
 */
public class DefaultMethodMetricsRegister implements MethodMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultMethodMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.METHOD_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be method type!");
        }

        this.plugin = plugin;
    }

    /**
     * 第一引数のメソッドのメトリクス値（第二引数）を登録する
     * 
     * @param methodInfo メトリクスを登録するメソッド
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException すでにメトリクスが登録されている場合にスローされる例外    
     */
    @Override
    public void registMetric(final TargetMethodInfo methodInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == methodInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final MethodMetricsInfoManager manager = DataManager.getInstance()
                .getMethodMetricsInfoManager();
        manager.putMetric(methodInfo, this.plugin, value);
    }

    /**
     * プラグインオブジェクトを保存しておくための変数
     */
    private final AbstractPlugin plugin;

}
