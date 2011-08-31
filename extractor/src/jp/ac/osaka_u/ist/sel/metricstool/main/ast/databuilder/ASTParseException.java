package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

/**
 * @author t-miyake
 *
 */
public class ASTParseException extends Exception {


    /**
     * 
     */
    private static final long serialVersionUID = 1582219133141241487L;

    /**
     * 
     */
    public ASTParseException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public ASTParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public ASTParseException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public ASTParseException(Throwable cause) {
        super(cause);
    }
    
    
}
