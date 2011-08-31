package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


public abstract class ExpressionElement {

    protected ExpressionElement(final UnresolvedExpressionInfo<? extends ExpressionInfo> usage) {
        if(null == usage) {
            throw new IllegalArgumentException("usage is null");
        }
        
        this.usage = usage;
        
        this.fromLine = usage.getFromLine();
        this.fromColumn = usage.getFromColumn();
        this.toLine = usage.getToLine();
        this.toColumn = usage.getToColumn();
    }

    protected ExpressionElement(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this.usage = null;
        
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }
    
    protected ExpressionElement() {
        this(0, 0, 0, 0);
    }

    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getUsage() {
        return this.usage;
    }

    public final int getFromLine() {
        return this.fromLine;
    }

    public final int getFromColumn() {
        return this.fromColumn;
    }

    public final int getToLine() {
        return this.toLine;
    }

    public final int getToColumn() {
        return this.toColumn;
    }

    protected UnresolvedExpressionInfo<? extends ExpressionInfo> usage;

    protected final int fromLine;

    protected final int fromColumn;

    protected final int toLine;

    protected final int toColumn;
}
