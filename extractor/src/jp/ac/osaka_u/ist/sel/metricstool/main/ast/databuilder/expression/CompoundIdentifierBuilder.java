package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


/**
 * 
 * 
 * @author kou-tngt
 */
public class CompoundIdentifierBuilder extends ExpressionBuilder {

    /**
     * @param expressionManager
     */
    public CompoundIdentifierBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) throws ASTParseException {

        AstToken token = event.getToken();
        if (token.isNameSeparator()) {
            buildCompoundIdentifierElementAvoidTypeArguments();
        }
    }

    protected void buildCompoundIdentifierElementAvoidTypeArguments() throws ASTParseException {
        ExpressionElement[] elements = getAvailableElements();

        if (2 == elements.length) {
            buildCompoundIdentifierElement(elements);
        } else {
            ExpressionElement[] typeArgumentAvoidedElements = new ExpressionElement[2];

            for (int i = 0, j = 0; i < elements.length; i++) {
                if (!(elements[i] instanceof TypeArgumentElement)) {
                    typeArgumentAvoidedElements[j++] = elements[i];
                }

                assert (j != 3) : "Illega state: too many non argument elements.";
            }

            buildCompoundIdentifierElement(typeArgumentAvoidedElements);

            for (int i = 0; i < elements.length; i++) {
                if (elements[i] instanceof TypeArgumentElement) {
                    pushElement(elements[i]);
                }
            }
        }
    }

    protected void buildCompoundIdentifierElement(ExpressionElement[] elements)
            throws ASTParseException {

        if (elements.length == 2) {
            ExpressionElement left = elements[0];
            ExpressionElement right = elements[1];
            if (right instanceof SingleIdentifierElement) {
                //右側は普通は単一の識別子のはず

                SingleIdentifierElement rightIdentifier = (SingleIdentifierElement) right;
                String rightName = rightIdentifier.getName();

                UnresolvedExpressionInfo<? extends ExpressionInfo> leftElementType = null;
                if (left instanceof ParenthesizedIdentifierElement) {
                    final ParenthesizedIdentifierElement leftIdentifier = (ParenthesizedIdentifierElement) left;
                    leftElementType = leftIdentifier.resolveAsVariable(buildDataManager, true, false);
                } else if (left instanceof FieldOrMethodElement) {
                    IdentifierElement leftIdentifier = (IdentifierElement) left;
                    leftElementType = leftIdentifier.resolveAsVariable(this.buildDataManager, true,
                            false);
                } else {
                    leftElementType = left.getUsage();
                }

                if (null != leftElementType) {
                    //左側の型が決定できたので右側はフィールド名かメソッド名だろう
                    pushElement(new FieldOrMethodElement(leftElementType, rightName,
                            rightIdentifier.getFromLine(), rightIdentifier.getFromColumn(),
                            rightIdentifier.getToLine(), rightIdentifier.getToColumn()));
                } else if (left instanceof IdentifierElement) {
                    //全体をなんかよく分からん識別子として扱う
                    IdentifierElement leftIdentifier = (IdentifierElement) left;
                    pushElement(new CompoundIdentifierElement(leftIdentifier, rightName,
                            rightIdentifier.getFromLine(), rightIdentifier.getFromColumn(),
                            rightIdentifier.getToLine(), rightIdentifier.getToColumn()));
                } else {
                    assert (false) : "Illegal state: unknown left element type.";
                }
            } else if (right instanceof UsageElement && ((UsageElement) right).isMemberCall()) {
                //a.new X というJavaの内部クラスのnew文っぽいケースの場合
                pushElement(right);
            } else {
                assert (false) : "Illegal state: unexpected element type.";
            }
        } else {
            assert (false) : "Illegal state: two elements must be available.";
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isNameSeparator();
    }

}
