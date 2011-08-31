package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


/**
 * 未解決変数使用を保存するためのクラス
 * 
 * @author t-miyake, higo
 * @param <T> 解決済みの型
 */
public abstract class UnresolvedVariableUsageInfo<T extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>
        extends UnresolvedExpressionInfo<T> {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param usedVariableName 変数名
     * @param reference 参照であるかどうか
     * @param assignment 代入であるかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference,
            final boolean assignment, final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        if (null == usedVariableName) {
            throw new IllegalArgumentException("usedVarialbeName is null");
        }

        this.usedVariableName = usedVariableName;
        this.reference = reference;
        this.assignment = assignment;

        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * この変数使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * この変数使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public final boolean isAssignment() {
        return this.assignment;
    }

    /**
     * 使用されている変数の名前を返す
     * @return 使用されている変数の名前
     */
    public String getUsedVariableName() {
        return this.usedVariableName;
    }

    /**
     * 使用されている変数の名前を保存する変数
     */
    protected final String usedVariableName;

    private boolean reference;

    private boolean assignment;

    /**
     * エラーメッセージ出力用のプリンタ
     */
    protected static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedVariableUsage";
        }
    }, MESSAGE_TYPE.ERROR);
}
