package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import antlr.CommonAST;
import antlr.Token;
import antlr.collections.AST;


public class CommonASTWithLineNumber extends CommonAST {

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

    @Override
    public final int getLine() {
        return this.fromLine;
    }

    @Override
    public final int getColumn() {
        return this.fromColumn;
    }
    
    public final void setPosition(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    @Override
    public void initialize(final Token tok) {
        if(null == tok || null == tok.getText()) {
            return;
        }
        super.initialize(tok);

        this.updatePosition(tok.getLine(), tok.getColumn(), tok.getLine(), tok.getColumn()
                + tok.getText().length());
    }

    @Override
    public void initialize(AST t) {
        super.initialize(t);

        this.updatePosition(t);
    }

    public void updatePosition(final AST ast) {
        if(null == ast) {
            return;
        }

        CommonASTWithLineNumber newAst = (CommonASTWithLineNumber) ast;
        this.updatePosition(newAst.getFromLine(), newAst.getFromColumn(), newAst.getToLine(),
                newAst.getToColumn());
        this.updatePosition(ast.getNextSibling());
    }

    public void updatePosition(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        this.updateFromPosition(fromLine, fromColumn);
        this.updateToPosition(toLine, toColumn);
    }

    private void updateFromPosition(final int fromLine, final int fromColumn) {
        if(0 == fromLine) {
            return;
            
        }
        
        if (0 == this.fromLine || !this.isAhead(fromLine, fromColumn)) {
            this.fromLine = fromLine;
            this.fromColumn = fromColumn;
        }
    }

    private void updateToPosition(final int toLine, final int toColumn) {
        if(0 == toLine) {
            return;
        }
        
        if (0 == this.toLine || !this.isBehind(toLine, toColumn)) {
            this.toLine = toLine;
            this.toColumn = toColumn;
        }
    }

    private boolean isAhead(final int fromLine, final int fromColumn) {
        if (fromLine > this.getFromLine()) {
                return true;
        } else if (fromLine == this.getFromLine() && fromColumn > this.getFromColumn()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isBehind(final int toLine, final int toColumn) {
        if (toLine < this.getToLine()) {
            return true;
        } else if (toLine == this.getToLine() && toColumn < this.getToColumn()) {
            return true;
        } else {
            return false;
        }
    }

    private int fromLine = 0;

    private int fromColumn = 0;

    private int toLine = 0;

    private int toColumn = 0;
}
