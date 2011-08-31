package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.AST;


/**
 * @author t-miyake
 *
 */
public class MasuAstFactory extends ASTFactory {

    
    
    public MasuAstFactory() {
        super();

        this.setASTNodeClass(CommonASTWithLineNumber.class);
    }

    @Override
    public void setASTNodeClass(final Class c) {
        if(!CommonASTWithLineNumber.class.getName().equals(c.getName())) {
            throw new IllegalArgumentException();
        }
        
        super.setASTNodeClass(c);
    }

    @Override
    public void setASTNodeClass(final String t) {
        if(!CommonASTWithLineNumber.class.getName().equals(t)) {
            throw new IllegalArgumentException();
        }
        
        super.setASTNodeClass(t);
    }

    @Override
    public AST make(final AST[] nodes) {

        if (nodes == null || nodes.length == 0) return null;
        
        AST root = nodes[0];
        AST tail = null;
        if (root != null) {
            root.setFirstChild(null);   // don't leave any old pointers set
        }
        // link in children;
        for (int i = 1; i < nodes.length; i++) {
            if (nodes[i] == null) continue; // ignore null nodes
            if (root == null) {
                // Set the root and set it up for a flat list
                root = tail = nodes[i];
            }
            else if (tail == null) {
                root.setFirstChild(nodes[i]);
                tail = root.getFirstChild();
            }
            else {
                tail.setNextSibling(nodes[i]);
                tail = tail.getNextSibling();
            }
            
            ((CommonASTWithLineNumber) root).updatePosition(tail);
            
            // Chase tail to last sibling
            while (tail.getNextSibling() != null) {
                tail = tail.getNextSibling();
            }
        }
        return root;
    }

   
    
    
    
}
