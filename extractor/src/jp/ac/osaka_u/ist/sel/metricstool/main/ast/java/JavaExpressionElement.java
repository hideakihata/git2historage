package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;


public class JavaExpressionElement extends ExpressionElement {

    public JavaExpressionElement(final boolean isClass, final boolean isSuper, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        this.isSuper = isSuper;
        this.isClass = isClass;
    }

    public boolean isClass() {
        return this.isClass;
    }

    public boolean isSuper() {
        return this.isSuper;
    }

    private final boolean isSuper;

    private final boolean isClass;

}
