package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * {@link ProgressReporter}のデフォルト実装 コンストラクタに {@link ProgressSource} を与える. 同じ {@link ProgressSource}
 * インスタンスから，このクラスのインスタンスを複数作ることはできない
 * 
 * @author kou-tngt
 * 
 */
public class DefaultProgressReporter implements ProgressReporter {

    /**
     * 唯一のコンストラクタ. 引数に {@link ProgressSource} を受け取り，報告用の接続を確立する
     * 
     * @param source 進捗報告をするプラグイン
     * @throws AlreadyConnectedException すでに同じ{@link ProgressSource}が別のレポーターで接続を作っている場合
     */
    public DefaultProgressReporter(final ProgressSource source) throws AlreadyConnectedException {
        // このソース用のコネクタを作って，接続
        this.connector = ProgressConnector.getConnector(source);
        this.connector.connect(this);
    }

    /**
     * 進捗情報送信の終了を報告するメソッド
     */
    public void reportProgressEnd() {
        if (null != this.connector) {
            this.connector.progressEnd();
            this.connector = null;
        }
    }

    /**
     * 進捗情報を報告するメソッド
     * 
     * @param percentage 進捗値（%）
     * @throws IllegalArgumentException percentageが0-100の間に入ってない場合
     * @throws IllegalStateException percentageが前回報告した値より下がった場合
     * @see ProgressReporter#reportProgress(int)
     */
    public void reportProgress(final int percentage) {
        if (0 > percentage || 100 < percentage) {
            throw new IllegalArgumentException("reported value " + percentage
                    + " was out of range(0-100).");
        }

        if (percentage < this.previousValue) {
            // 前回の報告より値が減った
            throw new IllegalStateException("reported value was decreased.");
        }

        if (null != this.connector) {
            try {
                this.previousValue = percentage;
                this.connector.reportProgress(percentage);
            } catch (final ConnectionException e) {
                // 接続されてない＝特別権限を持つスレッドに切断された＝プラグイン実行スレッドが近々（強制）終了＝何も通知しない
                this.connector = null;
            }
        }
    }

    /**
     * このレポーターの接続先
     */
    private ProgressConnector connector;

    /**
     * 1回前にに報告した進捗情報値
     */
    private int previousValue;

}
