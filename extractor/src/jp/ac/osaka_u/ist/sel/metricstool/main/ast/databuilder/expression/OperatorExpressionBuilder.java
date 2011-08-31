package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class OperatorExpressionBuilder extends ExpressionBuilder {

    public OperatorExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (isTriggerToken(token)) {
            this.buildOperatorElement(((OperatorToken) token), event);
        }
    }

    protected void buildOperatorElement(final OperatorToken token, final AstVisitEvent event) {
        //演算子が必要とする項の数
        final int term = token.getTermCount();

        //型決定に関わる項のインデックスの配列
        final int[] typeSpecifiedTermIndexes = token.getTypeSpecifiedTermIndexes();

        final ExpressionElement[] elements = this.getAvailableElements();

        assert (term > 0 && term == elements.length) : "Illegal state: unexpected element count.";

        if (term > 0 && term == elements.length) {
            final OPERATOR_TYPE operatorType = token.getOperator();
            final OPERATOR operator = OPERATOR.getOperator(event.getText());

            //各項の型を記録する配列
            final UnresolvedExpressionInfo<? extends ExpressionInfo>[] termTypes = new UnresolvedExpressionInfo<?>[elements.length];

            //最左辺値について
            final ExpressionElement primary = elements[0];
            if (primary instanceof IdentifierElement) {
                //識別子の場合
                final IdentifierElement leftElement = (IdentifierElement) elements[0];

                //左辺値への代入があるかどうか
                boolean assignmentLeft = false;
                //左辺値への参照があるかどうか
                boolean referenceLeft = true;
                if (null != operator) {
                    assignmentLeft = operator.isFirstIsAssignmentee();
                    referenceLeft = operator.isFirstIsReferencee();
                }

                //参照なら被参照変数として解決して結果の型を取得する
                termTypes[0] = leftElement.resolveAsVariable(this.buildDataManager, referenceLeft,
                        assignmentLeft);
            } else if (primary instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) primary;
                if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                    // キャストがあるとおそらくここに到達
                    // TODO UnresolvedReferenceTypeInfoにすべき
                    termTypes[0] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage(
                            this.buildDataManager.getCurrentUnit(),
                            typeElement.getFromLine(), typeElement.getFromColumn(),
                            typeElement.getToLine(), typeElement.getToColumn());
                } else if (typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                    UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement
                            .getType();
                    termTypes[0] = new UnresolvedArrayTypeReferenceInfo(arrayType,
                            this.buildDataManager.getCurrentUnit(), typeElement.fromLine,
                            typeElement.fromColumn, typeElement.toLine, typeElement.toColumn);
                } else {

                    termTypes[0] = elements[0].getUsage();
                }
            } else {
                //それ以外の場合は直接型を取得する
                termTypes[0] = primary.getUsage();
            }

            //2項目以降について
            for (int i = 1; i < term; i++) {
                if (elements[i] instanceof IdentifierElement) {
                    //識別子なら勝手に参照として解決して方を取得する
                    termTypes[i] = ((IdentifierElement) elements[i]).resolveAsVariable(
                            this.buildDataManager, true, false);
                } else if (elements[i] instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) elements[i];
                    if (typeElement.getType() instanceof UnresolvedClassTypeInfo) {
                        termTypes[i] = ((UnresolvedClassTypeInfo) typeElement.getType()).getUsage(
                                this.buildDataManager.getCurrentUnit(), typeElement.getFromLine(),
                                typeElement.getFromColumn(), typeElement.getToLine(),
                                typeElement.getToColumn());
                    } else if (typeElement.getType() instanceof UnresolvedArrayTypeInfo) {
                        // ここに到達するのはinstanceof type[]とき
                        UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo) typeElement
                                .getType();
                        termTypes[i] = new UnresolvedArrayTypeReferenceInfo(arrayType,
                                this.buildDataManager.getCurrentUnit(), typeElement.fromLine,
                                typeElement.fromColumn, typeElement.toLine, typeElement.toColumn);
                    } else {
                        termTypes[i] = elements[i].getUsage();
                    }
                } else {
                    //それ以外なら直接型を取得する
                    termTypes[i] = elements[i].getUsage();
                }
            }

            if (2 == term && null != operatorType) {
                //オペレーターインスタンスがセットされている2項演算子＝名前解決部に型決定処理を委譲する
                assert (null != termTypes[0]) : "Illega state: first term type was not decided.";
                assert (null != termTypes[1]) : "Illega state: second term type was not decided.";
                assert null != operator : "Illegal state: operator is null";

                final UnresolvedBinominalOperationInfo operation = new UnresolvedBinominalOperationInfo(
                        operator, termTypes[0], termTypes[1]);
                operation.setOuterUnit(this.buildDataManager.getCurrentUnit());
                operation.setFromLine(termTypes[0].getFromLine());
                operation.setFromColumn(termTypes[0].getFromColumn());
                operation.setToLine(termTypes[1].getToLine());
                operation.setToColumn(termTypes[1].getToColumn());

                pushElement(new UsageElement(operation));

            } else if (3 == term && token.equals(OperatorToken.TERNARY)) {
                assert null != termTypes[0] : "Illegal stete : first term type was not decided.";
                assert null != termTypes[1] : "Illegal stete : first term type was not decided.";
                assert null != termTypes[2] : "Illegal stete : first term type was not decided.";

                final UnresolvedTernaryOperationInfo operation = new UnresolvedTernaryOperationInfo(
                        termTypes[0], termTypes[1], termTypes[2]);
                operation.setOuterUnit(this.buildDataManager.getCurrentUnit());
                operation.setFromLine(termTypes[0].getFromLine());
                operation.setFromColumn(termTypes[0].getFromColumn());
                operation.setToLine(termTypes[2].getToLine());
                operation.setToColumn(termTypes[2].getToColumn());

                pushElement(new UsageElement(operation));
            } else {
                //自分で型決定する
                UnresolvedExpressionInfo<? extends ExpressionInfo> resultType = null;

                if (null != operator && null != operator.getSpecifiedResultType()) {
                    //オペレータによってすでに結果の型が決定している
                    assert null != operator : "Illegal state: operator is null";
                    resultType = new UnresolvedMonominalOperationInfo(termTypes[0], operator);
                    resultType.setOuterUnit(this.buildDataManager.getCurrentUnit());
                    if ((termTypes[0].getFromLine() < event.getStartLine())
                            || (termTypes[0].getFromLine() == event.getStartLine() && termTypes[0]
                                    .getFromColumn() < event.getStartColumn())) {
                        resultType.setFromLine(termTypes[0].getFromLine());
                        resultType.setFromColumn(termTypes[0].getFromColumn());
                        resultType.setToLine(event.getEndLine());
                        resultType.setToColumn(event.getEndColumn());
                    } else {
                        resultType.setFromLine(event.getStartLine());
                        resultType.setFromColumn(event.getStartColumn());
                        resultType.setToLine(termTypes[0].getToLine());
                        resultType.setToColumn(termTypes[0].getToColumn());
                    }
                } else if (1 == term
                        && (OPERATOR.MINUS.equals(operator) || OPERATOR.PLUS.equals(operator))) {
                    resultType = new UnresolvedMonominalOperationInfo(termTypes[0], operator);
                    resultType.setOuterUnit(this.buildDataManager.getCurrentUnit());
                    resultType.setFromLine(event.getStartLine());
                    resultType.setFromColumn(event.getStartColumn());
                    resultType.setToLine(termTypes[0].getToLine());
                    resultType.setToColumn(termTypes[0].getToColumn());
                } else if (token.equals(OperatorToken.ARRAY)) {
                    //配列記述子の場合は特別処理
                    final UnresolvedExpressionInfo<? extends ExpressionInfo> ownerType;
                    if (elements[0] instanceof IdentifierElement) {
                        ownerType = ((IdentifierElement) elements[0]).resolveAsVariable(
                                this.buildDataManager, true, false);
                    } else {
                        ownerType = elements[0].getUsage();
                    }
                    assert null != elements[1] : "Illegal state: expression that show index of array is not found.";
                    resultType = new UnresolvedArrayElementUsageInfo(ownerType,
                            elements[1].getUsage());
                    resultType.setOuterUnit(this.buildDataManager.getCurrentUnit());
                    resultType.setFromLine(ownerType.getFromLine());
                    resultType.setFromColumn(ownerType.getFromColumn());
                    resultType.setToLine(event.getEndLine());
                    resultType.setToColumn(event.getEndColumn());
                } else if (token.equals(OperatorToken.CAST) && elements[0] instanceof TypeElement) {
                    final UnresolvedTypeInfo<? extends TypeInfo> castType = ((TypeElement) elements[0])
                            .getType();
                    final UnresolvedExpressionInfo<? extends ExpressionInfo> castedUsage = elements[1]
                            .getUsage();
                    resultType = new UnresolvedCastUsageInfo(castType, castedUsage);
                    resultType.setOuterUnit(this.buildDataManager.getCurrentUnit());
                    resultType.setFromLine(event.getStartLine());
                    resultType.setFromColumn(event.getStartColumn());
                    resultType.setToLine(castedUsage.getToLine());
                    resultType.setToColumn(castedUsage.getToColumn());
                } else {
                    //型決定に関連する項を左から順番に漁っていって最初に決定できた奴に勝手に決める
                    for (int i = 0; i < typeSpecifiedTermIndexes.length; i++) {
                        resultType = termTypes[typeSpecifiedTermIndexes[i]];
                        if (null != resultType) {
                            break;
                        }
                    }
                }

                assert (null != resultType) : "Illegal state: operation resultType was not decided.";

                this.pushElement(new UsageElement(resultType));
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isOperator();
    }

}
