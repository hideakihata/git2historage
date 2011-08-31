package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.MetricTypeAndNamePluginComparator;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;

/**
 * メトリクスを表す抽象クラス
 * 
 * @author higo
 *
 * @param <T> メトリクスの計測単位
 */
public abstract class MetricsInfo<T extends MetricMeasurable> implements MessageSource {

    /**
     * 計測対象オブジェクトを与えて初期化
     * 
     * @param measuredObject 計測対象オブジェクト
     */
    public MetricsInfo(final T measuredObject) {

        if (null == measuredObject) {
            throw new NullPointerException();
        }

        this.measuredObject = measuredObject;
        this.metrics = Collections.synchronizedSortedMap(new TreeMap<AbstractPlugin, Number>(
                new MetricTypeAndNamePluginComparator()));
    }

    /**
     * このメトリクスの計測対象オブジェクトを返す
     * 
     * @return このメトリクスの計測対象オブジェクト
     */
    public final T getMeasuredObject() {
        return this.measuredObject;
    }

    /**
     * 引数で指定したプラグインによって登録されたメトリクス情報を取得するメソッド．
     * 
     * @param key ほしいメトリクスを登録したプラグイン
     * @return メトリクス値
     * @throws MetricNotRegisteredException メトリクスが登録されていないときにスローされる
     */
    public final Number getMetric(final AbstractPlugin key) throws MetricNotRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }

        final Number value = this.metrics.get(key);
        if (null == value) {
            throw new MetricNotRegisteredException();
        }

        return value;
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public final void putMetric(final AbstractPlugin key, final Number value)
            throws MetricAlreadyRegisteredException {

        if ((null == key) || (null == value)) {
            throw new NullPointerException();
        }

        if (this.metrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.metrics.put(key, value);
    }

    /**
     * このメトリクス情報に不足がないかをチェックする
     * 
     * @throws MetricNotRegisteredException
     */
    final void checkMetrics(final Set<AbstractPlugin> usingPlugins)
            throws MetricNotRegisteredException {

        for (final AbstractPlugin plugin : usingPlugins) {
            final Number value = this.getMetric(plugin);
            if (null == value) {
                final PluginInfo pluginInfo = plugin.getPluginInfo();
                final String metricName = pluginInfo.getMetricName();
                final String measuredUnitName = this.measuredObject.getMeasuredUnitName();
                final String message = "Metric \"" + metricName + "\" of " + measuredUnitName
                        + " is not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
        }
    }

    private final T measuredObject;

    /**
     * メトリクスを保存するための変数
     */
    private final SortedMap<AbstractPlugin, Number> metrics;
}
