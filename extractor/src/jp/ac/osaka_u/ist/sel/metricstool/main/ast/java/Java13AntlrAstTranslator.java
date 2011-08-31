package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AccessModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BlockNameToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DefinitionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.DescriptionToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.IdentifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.InstanceToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.MemberTypeModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ModifierToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SyntaxToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java13Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java13TokenTypes;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java14Parser;
import antlr.collections.AST;


/**
 * {@link Java13Parser}から生成されるASTノードを {@link AstToken}に変換するクラス.
 * 
 * @author kou-tngt
 *
 */
public class Java13AntlrAstTranslator implements AstTokenTranslator<AST> {

    /**
     *  {@link Java14Parser}から生成されるASTノードを {@link AstToken}に変換する.
     *  
     *  @param node 変換対象のノード
     *  @return 変換結果のAstToken
     */
    public AstToken translate(AST node) {
        int type = node.getType();
        AstToken result = null;

        //識別子だけは名前を使った専用のキャッシュを使う.
        if (type == Java13TokenTypes.IDENT) {
            String name = node.getText();
            if (identifierTokenMap.containsKey(name)) {
                return identifierTokenMap.get(name);
            } else {
                return new IdentifierToken(node.getText());
            }
        }

        //他の型はキャッシュを探して返す
        if (tokenMap.containsKey(type)) {
            return tokenMap.get(type);
        }

        //キャッシュがないので総当り
        switch (type) {
        case Java13TokenTypes.PACKAGE_DEF:
            result = DefinitionToken.NAMESPACE_DEFINITION;
            break;
        case Java13TokenTypes.ANNOTATIONS:
        case Java13TokenTypes.ANNOTATION_DEF:
            result = VisitControlToken.SKIP;
            //アノテーション関連は全部無視
            break;
        case Java13TokenTypes.IMPORT:
            //        case Java15TokenTypes.STATIC_IMPORT:
            result = JavaAstToken.CLASS_IMPORT;
            break;
        case Java13TokenTypes.DOT:
            result = SyntaxToken.NAME_SEPARATOR;
            break;
        case Java13TokenTypes.BLOCK:
            result = SyntaxToken.BLOCK_START;
            break;
        case Java13TokenTypes.ARRAY_INIT:
            result = JavaAstToken.ARRAY_INIT;
            break;
        case Java13TokenTypes.OBJBLOCK:
            result = SyntaxToken.CLASSBLOCK_START;
            break;
        case Java13TokenTypes.CLASS_DEF:
            result = DefinitionToken.CLASS_DEFINITION;
            break;
        case Java13TokenTypes.INTERFACE_DEF:
            result = JavaAstToken.INTERFACE_DEFINITION;
            break;
        //        case Java13TokenTypes.ENUM_DEF://enumはクラスとして扱う
        //            result = DefinitionToken.CLASS_DEFINITION;
        //            break;
        //        case Java13TokenTypes.ENUM_CONSTANT_DEF:
        //            result = JavaAstToken.ENUM_CONSTANT;
        //            break;
        case Java13TokenTypes.FIELD_DEF:
            result = DefinitionToken.FIELD_DEFINITION;
            break;
        case Java13TokenTypes.METHOD_PARAMETER_DEF:
            result = DefinitionToken.METHOD_PARAMETER_DEFINITION;
            break;
        case Java13TokenTypes.VARIABLE_PARAMETER_DEF:
            result = DefinitionToken.VARIABLE_PARAMETER_DEFINTION;
            break;
        case Java13TokenTypes.METHOD_DEF:
            result = DefinitionToken.METHOD_DEFINITION;
            break;
        case Java13TokenTypes.CTOR_DEF:
            result = DefinitionToken.CONSTRUCTOR_DEFINITION;
            break;
        case Java13TokenTypes.LOCAL_VARIABLE_DEF:
            result = DefinitionToken.LOCAL_VARIABLE_DEFINITION;
            break;
        case Java13TokenTypes.LOCAL_PARAMETER_DEF:
            result = DefinitionToken.LOCAL_PARAMETER_DEFINITION;
            break;
        case Java13TokenTypes.MODIFIERS:
            result = DescriptionToken.MODIFIER;
            break;
        case Java13TokenTypes.TYPE:
            result = DescriptionToken.TYPE;
            break;
        case Java13TokenTypes.NAME:
            result = DescriptionToken.NAME;
            break;
        case Java13TokenTypes.ARRAY_DECLARATOR:
            result = SyntaxToken.ARRAY;
            break;
        case Java13TokenTypes.ARRAY_INSTANTIATION:
            result = JavaAstToken.ARRAY_INSTANTIATION;
            break;
        case Java13TokenTypes.EXTENDS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;
        case Java13TokenTypes.IMPLEMENTS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;

        //        case Java15TokenTypes.TYPE_PARAMETERS:
        //            result = DefinitionToken.TYPE_PARAMETERS_DEFINITION;
        //            break;

        case Java13TokenTypes.TYPE_PARAMETER:
            result = DefinitionToken.TYPE_PARAMETER_DEFINITION;
            break;

        case Java13TokenTypes.TYPE_UPPER_BOUNDS:
            result = DescriptionToken.TYPE_UPPER_BOUNDS;
            break;

        case Java13TokenTypes.TYPE_LOWER_BOUNDS:
            result = DescriptionToken.TYPE_LOWER_BOUNDS;
            break;

        case Java13TokenTypes.TYPE_ARGUMENTS:
            result = DescriptionToken.TYPE_ARGUMENTS;
            break;

        case Java13TokenTypes.TYPE_ARGUMENT:
            result = DescriptionToken.TYPE_ARGUMENT;
            break;

        case Java13TokenTypes.WILDCARD_TYPE:
            result = DescriptionToken.TYPE_WILDCARD;
            break;

        case Java13TokenTypes.LITERAL_public:
            result = new AccessModifierToken("public", true, true, true);
            break;
        case Java13TokenTypes.LITERAL_private:
            result = new AccessModifierToken("private", false, false, false);
            break;
        case Java13TokenTypes.LITERAL_protected:
            result = new AccessModifierToken("protected", false, true, true);
            break;
        case Java13TokenTypes.LITERAL_static:
            result = MemberTypeModifierToken.STATIC;
            break;
        case Java13TokenTypes.LITERAL_synchronized:
            result = new ModifierToken("synchronized") {
                @Override
                public boolean isSynchronized() {
                    return true;
                }
            };
            break;
        case Java13TokenTypes.ABSTRACT:
            result = new ModifierToken("abstract");
            break;
        case Java13TokenTypes.FINAL:
            result = new ModifierToken("final");
            break;
        case Java13TokenTypes.LITERAL_boolean:
            result = BuiltinTypeToken.BOOLEAN;
            break;
        case Java13TokenTypes.LITERAL_byte:
            result = BuiltinTypeToken.BYTE;
            break;
        case Java13TokenTypes.LITERAL_char:
            result = BuiltinTypeToken.CHAR;
            break;
        case Java13TokenTypes.LITERAL_double:
            result = BuiltinTypeToken.DOUBLE;
            break;
        case Java13TokenTypes.LITERAL_float:
            result = BuiltinTypeToken.FLOAT;
            break;
        case Java13TokenTypes.LITERAL_int:
            result = BuiltinTypeToken.INT;
            break;
        case Java13TokenTypes.LITERAL_long:
            result = BuiltinTypeToken.LONG;
            break;
        case Java13TokenTypes.LITERAL_short:
            result = BuiltinTypeToken.SHORT;
            break;
        case Java13TokenTypes.LITERAL_void:
            result = BuiltinTypeToken.VOID;
            break;
        case Java13TokenTypes.EXPR:
            result = DescriptionToken.EXPRESSION;
            break;
        case Java13TokenTypes.PAREN_EXPR:
            result = DescriptionToken.PAREN_EXPR;
            break;
        case Java13TokenTypes.EXPR_STATE:
            result = DescriptionToken.EXPRESSION_STATEMENT;
            break;
        case Java13TokenTypes.LABELED_STAT:
            result = DescriptionToken.LABELED_STATEMENT;
            break;
        case Java13TokenTypes.SLIST:
            result = DescriptionToken.SLIST;
            break;
        case Java13TokenTypes.METHOD_CALL:
            result = SyntaxToken.METHOD_CALL;
            break;
        case Java13TokenTypes.CTOR_CALL:
            result = JavaAstToken.CONSTRUCTOR_CALL;
            break;
        case Java13TokenTypes.SUPER_CTOR_CALL:
            result = JavaAstToken.SUPER_CONSTRUCTOR_CALL;
            break;
        case Java13TokenTypes.TYPECAST:
            result = OperatorToken.CAST;
            break;
        case Java13TokenTypes.LITERAL_new:
            result = SyntaxToken.NEW;
            break;

        case Java13TokenTypes.INDEX_OP:
            result = OperatorToken.ARRAY;
            break;

        case Java13TokenTypes.ASSIGN:
            result = OperatorToken.ASSIGNMENT;
            return result;

        case Java13TokenTypes.PLUS_ASSIGN:
        case Java13TokenTypes.MINUS_ASSIGN:
        case Java13TokenTypes.STAR_ASSIGN:
        case Java13TokenTypes.DIV_ASSIGN:
        case Java13TokenTypes.MOD_ASSIGN:
        case Java13TokenTypes.SR_ASSIGN:
        case Java13TokenTypes.BSR_ASSIGN:
        case Java13TokenTypes.SL_ASSIGN:
        case Java13TokenTypes.BAND_ASSIGN:
        case Java13TokenTypes.BXOR_ASSIGN:
        case Java13TokenTypes.BOR_ASSIGN:
            result = OperatorToken.COMPOUND_ASSIGNMENT;
            break;

        case Java13TokenTypes.LOR:
        case Java13TokenTypes.LAND:
            result = OperatorToken.LOGICAL_BINOMIAL;
            break;

        case Java13TokenTypes.NOT_EQUAL:
        case Java13TokenTypes.EQUAL:
        case Java13TokenTypes.LE:
        case Java13TokenTypes.GE:
        case Java13TokenTypes.LITERAL_instanceof:
        case Java13TokenTypes.LT:
        case Java13TokenTypes.GT:
            result = OperatorToken.COMPARATIVE;
            break;

        case Java13TokenTypes.SL:
        case Java13TokenTypes.SR:
        case Java13TokenTypes.BSR:
            result = OperatorToken.SHIFT;
            break;

        case Java13TokenTypes.BAND:
        case Java13TokenTypes.BOR:
        case Java13TokenTypes.BXOR:
            result = OperatorToken.BIT_BINOMIAL;
            break;

        case Java13TokenTypes.PLUS:
        case Java13TokenTypes.MINUS:
        case Java13TokenTypes.DIV:
        case Java13TokenTypes.MOD:
        case Java13TokenTypes.STAR:
            result = OperatorToken.ARITHMETICH_BINOMIAL;
            break;

        case Java13TokenTypes.INC:
        case Java13TokenTypes.DEC:
        case Java13TokenTypes.POST_INC:
        case Java13TokenTypes.POST_DEC:
            result = OperatorToken.INCL_AND_DECL;
            break;

        case Java13TokenTypes.LNOT:
            result = OperatorToken.LOGICAL_UNARY;
            break;

        case Java13TokenTypes.BNOT:
            result = OperatorToken.BIT_UNARY;
            break;

        case Java13TokenTypes.UNARY_MINUS:
        case Java13TokenTypes.UNARY_PLUS:
            result = OperatorToken.ARITHMETHIC_UNARY;
            break;

        case Java13TokenTypes.QUESTION:
            result = OperatorToken.TERNARY;
            break;

        case Java13TokenTypes.LITERAL_true:
            result = new ConstantToken("true", PrimitiveTypeInfo.BOOLEAN);
            break;
        case Java13TokenTypes.LITERAL_false:
            result = new ConstantToken("false", PrimitiveTypeInfo.BOOLEAN);
            break;

        case Java13TokenTypes.LITERAL_null:
            result = InstanceToken.NULL;
            break;
        case Java13TokenTypes.LITERAL_this:
            result = InstanceToken.THIS;
            break;
        case Java13TokenTypes.LITERAL_super:
            result = JavaAstToken.SUPER;
            break;
        case Java13TokenTypes.LITERAL_class:
            result = JavaAstToken.CLASS;
            break;

        case Java13TokenTypes.NUM_INT:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.INT);
        case Java13TokenTypes.CHAR_LITERAL:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.CHAR);
        case Java13TokenTypes.STRING_LITERAL:
            final ClassInfo stringClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "String" });
            if (null == stringClass) {
                final StringBuilder text = new StringBuilder();
                text.append("Class java.lang.String cannot be detected in your setting.");
                text.append(System.getProperty("line.separator"));
                text.append("Please use -b option to specify jar file");
                text.append(" where java.lang.String is included.");
                System.getProperty("line.separator");
                throw new IllegalStateException(text.toString());
            }
            return new ConstantToken(node.getText(), new ClassTypeInfo(stringClass));
        case Java13TokenTypes.NUM_FLOAT:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.FLOAT);
        case Java13TokenTypes.NUM_LONG:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.LONG);
        case Java13TokenTypes.NUM_DOUBLE:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.DOUBLE);
        case Java13TokenTypes.STATIC_INIT:
            //result = DefinitionToken.STATIC_INIT;
            //break;

        case Java13TokenTypes.INSTANCE_INIT:
            result = VisitControlToken.SKIP;
            break;
        case Java13TokenTypes.ANNOTATION:
        case Java13TokenTypes.LITERAL_throws:
            result = VisitControlToken.SKIP;
            break;
        case Java13TokenTypes.THROWS_CLAUSE:
            result = JavaAstToken.THROWS;
            break;
        case Java13TokenTypes.LITERAL_break:
            result = SyntaxToken.BREAK;
            break;
        case Java13TokenTypes.LITERAL_throw:
            result = SyntaxToken.THROW;
            break;
        case Java13TokenTypes.LITERAL_return:
            result = SyntaxToken.RETURN;
            break;
        //        case Java13TokenTypes.PARAMETERS:
        //        case Java13TokenTypes.ELIST:
        case Java13TokenTypes.LITERAL_if:
            result = BlockNameToken.IF_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_else:
            result = BlockNameToken.ELSE_BLOCK;
            break;
        case Java13TokenTypes.FOR:
            result = BlockNameToken.FOR_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_try:
            result = BlockNameToken.TRY_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_catch:
            result = BlockNameToken.CATCH_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_finally:
            result = BlockNameToken.FINALLY_BLOCK;
            break;
        //        case Java13TokenTypes.LITERAL_return:
        case Java13TokenTypes.LITERAL_do:
            result = BlockNameToken.DO_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_while:
            result = BlockNameToken.WHILE_BLOCK;
            break;
        case Java13TokenTypes.LITERAL_switch:
            result = BlockNameToken.SWITCH_BLOCK;
            break;
        case Java13TokenTypes.CASE_GROUP:
            result = BlockNameToken.CASE_GROUP_DEFINITION;
            break;
        case Java13TokenTypes.LITERAL_case:
            result = BlockNameToken.CASE_ENTRY;
            break;
        case Java13TokenTypes.LITERAL_default:
            result = BlockNameToken.DEFAULT_ENTRY;
            break;
        case Java13TokenTypes.COND_CLAUSE:
        case Java13TokenTypes.FOR_CONDITION:
            result = DescriptionToken.CONDITIONAL_CLAUSE;
            break;
        case Java13TokenTypes.FOR_INIT:
            result = DescriptionToken.FOR_INIT;
            break;
        case Java13TokenTypes.FOR_ITERATOR:
            result = DescriptionToken.FOR_ITERATOR;
            break;
        default:
            //変換できなかったノードは取りあえずその子供に進む
            result = VisitControlToken.ENTER;
            break;
        }

        tokenMap.put(type, result);

        return result;
    }

    /**
     * トークンのキャッシュ
     */
    private final Map<Integer, AstToken> tokenMap = new HashMap<Integer, AstToken>();

    /**
     * 識別子トークンのキャッシュ
     */
    private final Map<String, AstToken> identifierTokenMap = new HashMap<String, AstToken>();
}
