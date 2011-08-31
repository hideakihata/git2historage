package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.util.EventObject;


/**
 * 進捗情報イベント
 * 
 * @author kou-tngt
 *
 */
public class ProgressEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = -8735402526941031611L;
    /**
     * コンストラクタ
     * 
     * @param source 進捗状況を送ったプラグイン
     * @param value 進捗状況を表す値(%)
     */
    public ProgressEvent(final ProgressSource source, final int value) {
        super(source);
        this.value = value;
        this.source = source;
    }

    /**
     * 進捗状況を取り出す
     * 
     * @return このイベントが表す進捗状況値
     */
    public int getProgressValue() {
        return this.value;
    }
    
    /**
     * このイベントを発行したプラグインを返す
     * @return このイベントを発行したプラグイン
     * 
     * @see java.util.EventObject#getSource()
     */
    @Override
    public ProgressSource getSource(){
        return this.source;
    }

    
    private final ProgressSource source;
    /**
     * 進捗状況を表す値（%）
     */
    private final int value;

}
