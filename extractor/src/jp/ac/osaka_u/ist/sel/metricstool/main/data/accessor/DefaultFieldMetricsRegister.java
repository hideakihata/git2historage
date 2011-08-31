package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * プラグインがフィールドメトリクスを登録するために用いるクラス．
 * 
 * @author higo
 */
public class DefaultFieldMetricsRegister implements FieldMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultFieldMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.FIELD_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be field type!");
        }

        this.plugin = plugin;
    }

    /**
     * 第一引数のファイルのメトリクス値（第二引数）を登録する
     * 
     * @param fieldInfo メトリクスを登録するフィールド
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException すでにメトリクスが登録されている場合にスローされる例外
     */
    public void registMetric(final TargetFieldInfo fieldInfo, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == fieldInfo) || (null == value)) {
            throw new NullPointerException();
        }

        final FieldMetricsInfoManager manager = DataManager.getInstance()
                .getFieldMetricsInfoManager();
        manager.putMetric(fieldInfo, this.plugin, value);
    }

    /**
     * プラグインオブジェクトを保存しておくための変数
     */
    private final AbstractPlugin plugin;
}
