package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * インスタンスイニシャライザを表すクラス
 * 
 * @author t-miyake
 *
 */
public class InstanceInitializerInfo extends InitializerInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 5833181372993442712L;

    /**
     * オブジェクトを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public InstanceInitializerInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
