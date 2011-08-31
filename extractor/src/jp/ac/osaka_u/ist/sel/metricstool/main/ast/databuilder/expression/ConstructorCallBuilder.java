package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;


public class ConstructorCallBuilder extends ExpressionBuilder {

    public ConstructorCallBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (token.isInstantiation()) {
            buildNewConstructorCall(event.getStartLine(), event.getStartColumn(),
                    event.getEndLine(), event.getEndColumn());
        }

    }

    protected void buildNewConstructorCall(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        TypeElement type = null;
        final List<ExpressionElement> parameters = new LinkedList<ExpressionElement>();
        for (final ExpressionElement element : getAvailableElements()) {
            if (element instanceof TypeElement) {
                if (null != type) {
                    assert false;
                }
                type = (TypeElement) element;
            } else {
                parameters.add(element);
            }
        }

        assert null != type;
        if (null != type) {
            final UnresolvedClassTypeInfo referenceType = (UnresolvedClassTypeInfo) type.getType();
            final UnresolvedClassConstructorCallInfo constructorCall = new UnresolvedClassConstructorCallInfo(
                    referenceType, this.buildDataManager.getCurrentUnit(), fromLine, fromColumn,
                    toLine, toColumn);

            resolveParameters(constructorCall, parameters);
            pushElement(new UsageElement(constructorCall));
            this.buildDataManager.addMethodCall(constructorCall);
        }
    }

    protected void resolveParameters(final UnresolvedConstructorCallInfo<?, ?> constructorCall,
            final List<ExpressionElement> elements) {
        for (final ExpressionElement argument : elements) {
            if (argument instanceof IdentifierElement) {
                constructorCall.addArgument(((IdentifierElement) argument).resolveAsVariable(
                        this.buildDataManager, true, false));
            } else if (argument instanceof TypeArgumentElement) {
                TypeArgumentElement typeArgument = (TypeArgumentElement) argument;

                // TODO C# などの場合はプリミティブ型も型引数に指定可能
                assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state; type argument was not reference type.";
                constructorCall.addTypeArgument((UnresolvedReferenceTypeInfo<?>) typeArgument
                        .getType());
            } else {
                constructorCall.addArgument(argument.getUsage());
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isInstantiation();
    }

}
