package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラスメトリクスを管理するクラス．
 * 
 * @author higo
 * 
 */
public final class ClassMetricsInfoManager implements Iterable<ClassMetricsInfo>, MessageSource {

    /**
     * メトリクス情報一覧のイテレータを返す．
     * 
     * @return メトリクス情報のイテレータ
     */
    public Iterator<ClassMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<ClassMetricsInfo> unmodifiableClassMetricsInfoCollection = Collections
                .unmodifiableCollection(this.classMetricsInfos.values());
        return unmodifiableClassMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたクラスのメトリクス情報を返す． 引数で指定されたクラスのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param classInfo ほしいメトリクス情報のクラス
     * @return メトリクス情報
     */
    public ClassMetricsInfo get(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        return this.classMetricsInfos.get(classInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param classInfo メトリクス計測対象のクラスオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final TargetClassInfo classInfo, final AbstractPlugin plugin,
            final Number value) throws MetricAlreadyRegisteredException {

        ClassMetricsInfo classMetricsInfo = this.classMetricsInfos.get(classInfo);

        // 対象クラスの classMetricsInfo が無い場合は，new して Map に登録する
        if (null == classMetricsInfo) {
            classMetricsInfo = new ClassMetricsInfo(classInfo);
            this.classMetricsInfos.put(classInfo, classMetricsInfo);
        }

        classMetricsInfo.putMetric(plugin, value);
    }

    /**
     * クラスメトリクスに登録漏れがないかをチェックする
     * 
     * @throws MetricNotRegisteredException 登録漏れがあった場合にスローされる
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {

            ClassMetricsInfo classMetricsInfo = this.get(classInfo);
            if (null == classMetricsInfo) {
                String message = "Class \"" + classInfo.getFullQualifiedName(".")
                        + "\" metrics are not registered!";
                MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            classMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getClassMetricPlugins());
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
     * クラスメトリクスマネージャのオブジェクトを生成する． 
     */
    public ClassMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.classMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<TargetClassInfo, ClassMetricsInfo>());
    }

    /**
     * クラスメトリクスのマップを保存するための変数
     */
    private final SortedMap<TargetClassInfo, ClassMetricsInfo> classMetricsInfos;
}
