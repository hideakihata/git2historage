package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param ownerSwitchBlock 対応する switchブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public DefaultEntryInfo(final SwitchBlockInfo ownerSwitchBlock, int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSwitchBlock, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このdefaultエントリのテキスト表現（String型）を返す
     * 
     * @return このdefaultエントリのテキスト表現（String型）
     */
    @Override
    public String getText() {
        return "default:";
    }
}
