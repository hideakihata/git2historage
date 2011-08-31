package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.TypeConverter;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * ニ項演算使用を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class BinominalOperationInfo extends ExpressionInfo {

    /**
     * ニ項演算の各オペランド，オペレータを与えてオブジェクトを初期化
     * 
     * @param operator オペレータ
     * @param firstOperand 第一オペランド
     * @param secondOperand 第二オペランド
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public BinominalOperationInfo(final OPERATOR operator, final ExpressionInfo firstOperand,
            final ExpressionInfo secondOperand, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;

        this.firstOperand.setOwnerExecutableElement(this);
        this.secondOperand.setOwnerExecutableElement(this);

    }

    /**
     * この二項演算の型を返す
     * 
     * @return この二項演算の型
     */
    @Override
    public TypeInfo getType() {

        final LANGUAGE language = Settings.getInstance().getLanguage();

        final ClassInfo DOUBLE = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.DOUBLE);
        final ClassInfo FLOAT = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.FLOAT);
        final ClassInfo LONG = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.LONG);
        final ClassInfo INTEGER = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.INT);
        final ClassInfo SHORT = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.SHORT);
        final ClassInfo CHARACTER = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.CHAR);
        final ClassInfo BYTE = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.BYTE);
        final ClassInfo BOOLEAN = TypeConverter.getTypeConverter(language).getWrapperClass(
                PrimitiveTypeInfo.BOOLEAN);

        final TypeInfo firstOperandType = this.getFirstOperand().getType();
        final TypeInfo secondOperandType = this.getSecondOperand().getType();

        switch (language) {
        case JAVA15:
        case JAVA14:
        case JAVA13:

            final ClassInfo stringClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "String" });
            final TypeInfo STRING = new ClassTypeInfo(stringClass);

            switch (this.getOperator().getOperatorType()) {
            case ARITHMETIC:

                if (firstOperandType.equals(STRING) || secondOperandType.equals(STRING)) {
                    return STRING;

                } else if (firstOperandType.equals(DOUBLE)
                        || firstOperandType.equals(PrimitiveTypeInfo.DOUBLE)
                        || secondOperandType.equals(DOUBLE)
                        || secondOperandType.equals(PrimitiveTypeInfo.DOUBLE)) {
                    return PrimitiveTypeInfo.DOUBLE;

                } else if (firstOperandType.equals(FLOAT)
                        || firstOperandType.equals(PrimitiveTypeInfo.FLOAT)
                        || secondOperandType.equals(FLOAT)
                        || secondOperandType.equals(PrimitiveTypeInfo.FLOAT)) {
                    return PrimitiveTypeInfo.FLOAT;

                } else if (firstOperandType.equals(LONG)
                        || firstOperandType.equals(PrimitiveTypeInfo.LONG)
                        || secondOperandType.equals(LONG)
                        || secondOperandType.equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (firstOperandType.equals(INTEGER)
                        || firstOperandType.equals(PrimitiveTypeInfo.INT)
                        || secondOperandType.equals(INTEGER)
                        || secondOperandType.equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (firstOperandType.equals(SHORT)
                        || firstOperandType.equals(PrimitiveTypeInfo.SHORT)
                        || secondOperandType.equals(SHORT)
                        || secondOperandType.equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;

                } else if (firstOperandType.equals(CHARACTER)
                        || firstOperandType.equals(PrimitiveTypeInfo.CHAR)
                        || secondOperandType.equals(CHARACTER)
                        || secondOperandType.equals(PrimitiveTypeInfo.CHAR)) {
                    return PrimitiveTypeInfo.CHAR;

                } else if (firstOperandType.equals(BYTE)
                        || firstOperandType.equals(PrimitiveTypeInfo.BYTE)
                        || secondOperandType.equals(BYTE)
                        || secondOperandType.equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if ((firstOperandType instanceof UnknownTypeInfo)
                        || (secondOperandType instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();
                }

                //それ以外の時はjava.lang.String型になる
                //"+"を定義した後は，+かどうかをチェックする必要あり
                return STRING;

            case COMPARATIVE:
                return PrimitiveTypeInfo.BOOLEAN;
            case LOGICAL:
                return PrimitiveTypeInfo.BOOLEAN;
            case BITS:

                if (firstOperandType.equals(LONG)
                        || firstOperandType.equals(PrimitiveTypeInfo.LONG)
                        || secondOperandType.equals(LONG)
                        || secondOperandType.equals(PrimitiveTypeInfo.LONG)) {
                    return PrimitiveTypeInfo.LONG;

                } else if (firstOperandType.equals(INTEGER)
                        || firstOperandType.equals(PrimitiveTypeInfo.INT)
                        || secondOperandType.equals(INTEGER)
                        || secondOperandType.equals(PrimitiveTypeInfo.INT)) {
                    return PrimitiveTypeInfo.INT;

                } else if (firstOperandType.equals(SHORT)
                        || firstOperandType.equals(PrimitiveTypeInfo.SHORT)
                        || secondOperandType.equals(SHORT)
                        || secondOperandType.equals(PrimitiveTypeInfo.SHORT)) {
                    return PrimitiveTypeInfo.SHORT;

                } else if (firstOperandType.equals(BYTE)
                        || firstOperandType.equals(PrimitiveTypeInfo.BYTE)
                        || secondOperandType.equals(BYTE)
                        || secondOperandType.equals(PrimitiveTypeInfo.BYTE)) {
                    return PrimitiveTypeInfo.BYTE;

                } else if (firstOperandType.equals(CHARACTER)
                        || firstOperandType.equals(PrimitiveTypeInfo.CHAR)
                        || secondOperandType.equals(CHARACTER)
                        || secondOperandType.equals(PrimitiveTypeInfo.CHAR)) {
                    return PrimitiveTypeInfo.CHAR;

                } else if (firstOperandType.equals(BOOLEAN)
                        || firstOperandType.equals(PrimitiveTypeInfo.BOOLEAN)
                        || secondOperandType.equals(BOOLEAN)
                        || secondOperandType.equals(PrimitiveTypeInfo.BOOLEAN)) {
                    return PrimitiveTypeInfo.BOOLEAN;

                } else if ((firstOperandType instanceof UnknownTypeInfo)
                        || (secondOperandType instanceof UnknownTypeInfo)) {

                    return UnknownTypeInfo.getInstance();

                } else {
                    assert false : "Here shouldn't be reached!";
                }

            case SHIFT:
                return firstOperandType;
            case ASSIGNMENT:
                return firstOperandType;
            default:
                assert false : "Here shouldn't be reached";
            }

            break;

        default:
            assert false : "Here shouldn't be reached";
        }

        return UnknownTypeInfo.getInstance();
    }

    /**
     * 演算子を取得する
     * 
     * @return 演算子
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 第一オペランドを取得する
     * 
     * @return 第一オペランド
     */
    public ExpressionInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * 第二オペランドを取得する
     * 
     * @return 第二オペランド
     */
    public ExpressionInfo getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * この二項演算における変数使用群を返す
     * 
     * return この二項演算における変数使用群
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getFirstOperand().getVariableUsages());
        variableUsages.addAll(this.getSecondOperand().getVariableUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        final ExpressionInfo firstOperand = this.getFirstOperand();
        calls.addAll(firstOperand.getCalls());
        final ExpressionInfo secondOperand = this.getSecondOperand();
        calls.addAll(secondOperand.getCalls());
        return Collections.unmodifiableSet(calls);
    }

    /**
     * この二項演算のテキスト表現（String型）を返す
     * 
     * @return この二項演算のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo firstExpression = this.getFirstOperand();
        sb.append(firstExpression.getText());

        sb.append(" ");

        final OPERATOR operator = this.getOperator();
        sb.append(operator.getToken());

        sb.append(" ");

        final ExpressionInfo secondExpression = this.getSecondOperand();
        sb.append(secondExpression.getText());

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        thrownExpressions.addAll(this.getFirstOperand().getThrownExceptions());
        thrownExpressions.addAll(this.getSecondOperand().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {
        final OPERATOR operator = this.getOperator();
        final ExpressionInfo firstOperand = (ExpressionInfo) this.getFirstOperand().copy();
        final ExpressionInfo secondOperand = (ExpressionInfo) this.getSecondOperand().copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(operator,
                firstOperand, secondOperand, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newBinominalOperation.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newBinominalOperation.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newBinominalOperation;
    }

    private final ExpressionInfo firstOperand;

    private final ExpressionInfo secondOperand;

    private final OPERATOR operator;
}
