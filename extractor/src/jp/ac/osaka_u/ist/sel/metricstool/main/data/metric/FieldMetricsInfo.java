package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * フィールドメトリクスを登録するためのデータクラス
 * 
 * @author higo
 * 
 */
public final class FieldMetricsInfo extends MetricsInfo<TargetFieldInfo> {

    /**
     * 計測対象フィールドを与えて初期化
     * 
     * @param fieldInfo 計測対象フィールド
     */
    public FieldMetricsInfo(final TargetFieldInfo fieldInfo) {
        super(fieldInfo);
    }

    /**
     * メッセージの送信者名を返す
     * 
     * @return メッセージの送信者名
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
    }
}
