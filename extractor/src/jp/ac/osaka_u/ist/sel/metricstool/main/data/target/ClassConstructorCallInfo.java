package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * クラスのコンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ClassConstructorCallInfo extends ConstructorCallInfo<ClassTypeInfo> {

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
    public ClassConstructorCallInfo(final ClassTypeInfo classType, final ConstructorInfo callee,
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

        sb.append("new ");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append("(");

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

    @Override
    public ExecutableElementInfo copy() {

        final ClassTypeInfo classType = this.getType();
        final ConstructorInfo callee = this.getCallee();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ClassConstructorCallInfo newCall = new ClassConstructorCallInfo(classType, callee,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }
}
