package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールドメトリクスを管理するクラス．
 * 
 * @author higo
 * 
 */
public final class FieldMetricsInfoManager implements Iterable<FieldMetricsInfo>, MessageSource {

    /**
    * メトリクス情報一覧のイテレータを返す．
    * 
    * @return メトリクス情報のイテレータ
    */
    public Iterator<FieldMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<FieldMetricsInfo> unmodifiableFieldMetricsInfoCollection = Collections
                .unmodifiableCollection(this.fieldMetricsInfos.values());
        return unmodifiableFieldMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたフィールドのメトリクス情報を返す． 引数で指定されたフィールドのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param fieldInfo フィールド
     * @return メトリクス情報
     */
    public FieldMetricsInfo get(final TargetFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        return this.fieldMetricsInfos.get(fieldInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param fieldInfo メトリクス計測対象のフィールドオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final TargetFieldInfo fieldInfo, final AbstractPlugin plugin,
            final Number value) throws MetricAlreadyRegisteredException {

        FieldMetricsInfo fieldMetricsInfo = this.fieldMetricsInfos.get(fieldInfo);

        // 対象フィールドの fieldMetricsInfo が無い場合は，new して Map に登録する
        if (null == fieldMetricsInfo) {
            fieldMetricsInfo = new FieldMetricsInfo(fieldInfo);
            this.fieldMetricsInfos.put(fieldInfo, fieldMetricsInfo);
        }

        fieldMetricsInfo.putMetric(plugin, value);
    }

    /**
     * フィールドメトリクスに登録漏れがないかをチェックする
     * 
     * @throws MetricNotRegisteredException 登録漏れがあった場合にスローされる
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();

        for (final TargetFieldInfo fieldInfo : DataManager.getInstance().getFieldInfoManager()
                .getTargetFieldInfos()) {

            final FieldMetricsInfo fieldMetricsInfo = this.get(fieldInfo);
            if (null == fieldMetricsInfo) {
                final String message = "Field \"" + fieldInfo.getName()
                        + "\" metrics are not registered!";
                final MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
            }
            fieldMetricsInfo.checkMetrics(DataManager.getInstance().getPluginManager()
                    .getFileMetricPlugins());
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
     * フィールドメトリクスマネージャのオブジェクトを生成する． 
     * 
     */
    public FieldMetricsInfoManager() {
        //MetricsToolSecurityManager.getInstance().checkAccess();
        this.fieldMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<TargetFieldInfo, FieldMetricsInfo>());
    }

    /**
    * ファイルメトリクスのマップを保存するための変数
    */
    private final SortedMap<TargetFieldInfo, FieldMetricsInfo> fieldMetricsInfos;
}
