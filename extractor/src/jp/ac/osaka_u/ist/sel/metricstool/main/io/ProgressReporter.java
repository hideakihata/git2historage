package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * 進捗情報報告用インタフェース
 * @author kou-tngt
 *
 */
public interface ProgressReporter {
    /**
     * 進捗情報送信の終了を報告するメソッド
     */
    public void reportProgressEnd();

    /**
     * 進捗情報を報告するメソッド
     * @param percentage 進捗値（%）
     * @throws IllegalArgumentException percentageが0-100の間に入ってない場合
     * @throws IllegalStateException percentageが前回報告した値より下がった場合
     */
    public void reportProgress(int percentage);
}
