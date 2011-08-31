package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;
import java.util.Set;


/**
 * コンストラクタを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class ConstructorInfo extends CallableUnitInfo {

    /**
     * 必要な情報を与えて初期化
     * 
     * @param modifiers このコンストラクタの修飾子
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ConstructorInfo(final Set<ModifierInfo> modifiers, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(modifiers, fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * このコンストラクタが，引数で与えられた情報を使って呼び出すことができるかどうかを判定する
     * 
     * @param actualParameters 引数の型のリスト
     * @return 呼び出せる場合は true，そうでない場合は false
     */
    @Override
    public final boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        return super.canCalledWith(actualParameters);
    }

    @Override
    public final String getSignatureText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getOwnerClass().getClassName());

        text.append("(");
        for (final ParameterInfo parameter : this.getParameters()) {
            text.append(parameter.getType().getTypeName());
            text.append(",");
        }
        if (0 < this.getParameterNumber()) {
            text.deleteCharAt(text.length() - 1);
        }
        text.append(")");

        return text.toString();
    }
}
