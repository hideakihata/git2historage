package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.MethodInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

public abstract class AbstractMethodMetricPlugin extends AbstractPlugin {

    @Override
    protected void execute() {
        setupExecute();
        try {
            // クラス情報アクセサを取得
            final MethodInfoAccessor methodAccessor = this.getMethodInfoAccessor();

            // 進捗報告用
            int measuredMethodCount = 0;
            final int maxMethodCount = methodAccessor.getMethodCount();

            // 全クラスについて
            for (final TargetMethodInfo targetMethod : methodAccessor) {
                
                // クラスのメトリクスを登録する
                registMethodMetric(targetMethod);

                // 1クラスごとに%で進捗報告
                this.reportProgress(++measuredMethodCount * 100 / maxMethodCount);
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
    protected void registMethodMetric(TargetMethodInfo targetMethod) {
        try {
            this.registMetric(targetMethod, measureMethodMetric(targetMethod));
        } catch (final MetricAlreadyRegisteredException e) {
            this.err.println(e);
        }
    }

    /**
     * メソッドのメトリクスを計測する.
     * 
     * @param targetMethod 対象のメソッド
     */
    abstract protected Number measureMethodMetric(TargetMethodInfo targetMethod);
    

    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.METHOD_METRIC;
    }

}
