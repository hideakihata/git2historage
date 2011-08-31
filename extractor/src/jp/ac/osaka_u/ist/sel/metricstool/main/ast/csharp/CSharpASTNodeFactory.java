package jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpTokenTypes;
import antlr.ASTFactory;
import antlr.collections.AST;


public class CSharpASTNodeFactory {

    public CSharpASTNodeFactory(final ASTFactory astFactory) {
        if (null == astFactory) {
            throw new IllegalArgumentException("astFactory is null.");
        }
        this.astFactory = astFactory;
    }

    private ASTFactory astFactory;

    public AST createPropertyGetterHeadNode(final AST modifier, final AST propertyType,
            final AST propertyName) {
        final AST definition = this.createMethodDefinitionNode();

        // プロパティの修飾子をセッタメソッドとして扱うときの修飾子として設定
        definition.addChild(this.cretateCloneAST(modifier));

        // プロパティの型をゲッタメソッドとして扱うときの返り値として設定
        definition.addChild(this.cretateCloneAST(propertyType));

        definition.addChild(this.cretateCloneAST(propertyName));

        return definition;
    }

    public AST createPropertySetterHeadNode(final AST modifier, final AST propertyType,
            final AST propertyName) {
        final AST definition = this.createMethodDefinitionNode();
        // プロパティの修飾子をセッタメソッドとして扱うときの修飾子として設定
        definition.addChild(this.cretateCloneAST(modifier));

        // プロパティをセッタメソッドとして扱うときの返り値の型voidを設定
        final AST methodType = this.astFactory.create(CSharpTokenTypes.TYPE, "TYPE");
        methodType.addChild(this.astFactory.create(CSharpTokenTypes.LITERAL_void, "void"));
        definition.addChild(methodType);

        definition.addChild(this.cretateCloneAST(propertyName));

        // プロパティをセッタメソッドとして扱うときのパラメータ定義を設定
        definition.addChild(this.cretateSetterParameterNode(propertyType));

        return definition;
    }

    public AST cretateSetterParameterNode(final AST type) {
        final AST definitionHeader = this.astFactory.create(CSharpTokenTypes.PARAMETERS,
                "PARAMETERS");

        final AST definition = this.astFactory.create(CSharpTokenTypes.PARAMETER_DEF,
                "PARAMETER_DEF");
        definitionHeader.addChild(definition);

        // 修飾子のノードを追加．
        definition.addChild(this.astFactory.create(CSharpTokenTypes.MODIFIERS, "MODIFIERS"));

        // 型のノードを追加
        definition.addChild(this.cretateCloneAST(type));

        // 名前のノードを追加．プロパティのset内で初期定義されているパラメータ名はvalue
        final AST name = this.astFactory.create(CSharpTokenTypes.NAME, "NAME");
        name.addChild(this.astFactory.create(CSharpTokenTypes.IDENT, "value"));
        definition.addChild(name);

        return definitionHeader;
    }

    private AST createMethodDefinitionNode() {
        return this.astFactory.create(CSharpTokenTypes.METHOD_DEF, "METHOD_DEF");
    }

    private AST cretateCloneAST(final AST ast) {
        final AST cloneAst = this.astFactory.create(ast);
        final AST firstChild = ast.getFirstChild();

        if (null != firstChild) {
            cloneAst.addChild(this.cretateCloneAST(firstChild));
            AST nextChild = firstChild.getNextSibling();
            while (null != nextChild) {
                cloneAst.addChild(this.cretateCloneAST(nextChild));
                nextChild = nextChild.getNextSibling();
            }
        }

        return cloneAst;
    }

    public void setAstFactory(ASTFactory astFactory) {
        this.astFactory = astFactory;
    }

}
