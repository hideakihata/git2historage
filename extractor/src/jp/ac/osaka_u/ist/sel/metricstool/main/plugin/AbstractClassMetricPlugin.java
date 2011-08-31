package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * クラスのメトリクスを計測するプラグイン実装用の抽象クラス.
 * 
 * {@link AbstractPlugin} の一部のメソッドがオーバーライドされている.
 * 
 * このクラスを継承するクラスは {@link #measureClassMetric(TargetClassInfo)} を実装する必要がある. 
 * 必要があれば {@link #setupExecute()}, {@link #teardownExecute()} をオーバーライドする.
 * 
 * @author rniitani
 */
public abstract class AbstractClassMetricPlugin extends AbstractPlugin {

    public AbstractClassMetricPlugin() {
        super();
    }

    /**
     * クラス毎にメトリクスを計測する.
     * 
     * @see #registClassMetric(TargetClassInfo)
     */
    @Override
    protected void execute() {

        setupExecute();
        try {
            // クラス情報アクセサを取得
            final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();

            // 進捗報告用
            int measuredClassCount = 0;
            final int maxClassCount = classAccessor.getClassCount();

            // 全クラスについて
            for (final TargetClassInfo targetClass : classAccessor) {

                // クラスのメトリクスを登録する
                registClassMetric(targetClass);

                // 1クラスごとに%で進捗報告
                this.reportProgress(++measuredClassCount * 100 / maxClassCount);
            }
        } finally {
            teardownExecute();
        }
    }

    /**
     * {@link #execute()} の最初に実行される処理.
     * 
     * 必要があればオーバーライドする.
     */
    protected void setupExecute() {
    }

    /**
     * {@link #execute()} の最後に実行される処理.
     * 
     * 必要があればオーバーライドする.
     */
    protected void teardownExecute() {
    }

    /**
     * クラスのメトリクスを計測して登録する.
     * 
     * {@link MetricAlreadyRegisteredException} に対する標準の例外処理を提供する.
     * 計測は {@link #measureClassMetric(TargetClassInfo)} をオーバーライドして実装する.
     * 
     * @param targetClass 対象のクラス
     */
    protected void registClassMetric(TargetClassInfo targetClass) {
        try {
            this.registMetric(targetClass, measureClassMetric(targetClass));
        } catch (final MetricAlreadyRegisteredException e) {
            this.err.println(e);
        }
    }

    /**
     * クラスのメトリクスを計測する.
     * 
     * @param targetClass 対象のクラス
     */
    abstract protected Number measureClassMetric(TargetClassInfo targetClass);

    /**
     * このプラグインがメトリクスを計測できる言語を返す.
     * 
     * クラスについて計測するのが前提なのでオブジェクト指向言語を対象とする.
     * 
     * @return オブジェクト指向言語の配列
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return LanguageUtil.getObjectOrientedLanguages();
    }

    /**
     * このプラグインが計測するメトリクスのタイプを返す.
     * 
     * @return クラスのメトリクスを計測するので {@link METRIC_TYPE#CLASS_METRIC} を返す.
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }

    /**
     * このプラグインがクラスに関する情報を利用するかどうかを返すメソッド.
     * 
     * @return クラスのメトリクスを計測するので true を返す.
     */
    @Override
    protected boolean useClassInfo() {
        return true;
    }

}