package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッドメトリクスを管理するクラス．
 * 
 * @author higo
 * 
 */
public final class MethodMetricsInfoManager implements Iterable<MethodMetricsInfo>, MessageSource {

    /**
     * メトリクス情報一覧のイテレータを返す．
     * 
     * @return メトリクス情報のイテレータ
     */
    public Iterator<MethodMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<MethodMetricsInfo> unmodifiableMethodMetricsInfoCollection = Collections
                .unmodifiableCollection(this.methodMetricsInfos.values());
        return unmodifiableMethodMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたメソッドのメトリクス情報を返す． 引数で指定されたメソッドのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param methodInfo ほしいメトリクス情報のメソッド
     * @return メトリクス情報
     */
    public MethodMetricsInfo get(final TargetMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        return this.methodMetricsInfos.get(methodInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param methodInfo メトリクス計測対象のメソッドオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final TargetMethodInfo methodInfo, final AbstractPlugin plugin,
            final Number value) throws MetricAlreadyRegisteredException {

        MethodMetricsInfo methodMetricsInfo = this.methodMetricsInfos.get(methodInfo);

        // 対象メソッドの methodMetricsInfo が無い場合は，new して Map に登録する
        if (null == methodMetricsInfo) {
            methodMetricsInfo = new MethodMetricsInfo(methodInfo);
            this.methodMetricsInfos.put(methodInfo, methodMetricsInfo);
        }

        methodMetricsInfo.putMetric(plugin, value);
    }

    /**
     * メソッドメトリクスに登録漏れがないかをチェックする
     * 
     * @throws MetricNotRegisteredException 登録漏れがあった場合にスローされる
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final TargetMethodInfo methodInfo : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {

            final MethodMetricsInfo methodMetricsInfo = this.get(methodInfo);
            if (null == methodMetricsInfo) {
                final String methodName = methodInfo.getMethodName();
                final ClassInfo ownerClassInfo = methodInfo.getOwnerClass();
                final String ownerClassName = ownerClassInfo.getFullQualifiedName(".");
                final String message = "Metrics of " + ownerClassName + "::" + methodName
                        + " are not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            methodMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getMethodMetricPlugins());
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
     * メソッドメトリクスマネージャのオブジェクトを生成する． 
     * 
     */
    public MethodMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.methodMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<TargetMethodInfo, MethodMetricsInfo>());
    }

    /**
     * メソッドメトリクスのマップを保存するための変数
     */
    private final SortedMap<TargetMethodInfo, MethodMetricsInfo> methodMetricsInfos;
}
