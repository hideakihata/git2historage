package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * superを用いたコンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class SuperConstructorCallInfo extends ClassConstructorCallInfo {

    /**
     * 型を与えてコンストラクタ呼び出しを初期化
     * 
     * @param classType 呼び出しの型
     * @param callee 呼び出されているコンストラクタ
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public SuperConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(classType, callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }
    
    /**
     * このコンストラクタ呼び出しのテキスト表現（型）を返す
     * 
     * @return このコンストラクタ呼び出しのテキスト表現（型）を返す
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("super(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }
        if (0 < this.getArguments().size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }
}
