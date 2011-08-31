package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * メソッドメトリクスを登録するためのデータクラス
 * 
 * @author higo
 * 
 */
public final class MethodMetricsInfo extends MetricsInfo<TargetMethodInfo> {

    /**
     * 計測対象オブジェクトを与えて初期化
     * 
     * @param method 計測対象メソッド
     */
    public MethodMetricsInfo(final TargetMethodInfo method) {
        super(method);
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
