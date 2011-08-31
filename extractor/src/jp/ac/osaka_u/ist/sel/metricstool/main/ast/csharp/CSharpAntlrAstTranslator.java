/**
 * 
 */
package jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstToken;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpTokenTypes;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import antlr.collections.AST;


/**
 * {@link Java15Parser}から生成されるASTノードを {@link AstToken}に変換するクラス.
 * 
 * @author kou-tngt
 *
 */
public class CSharpAntlrAstTranslator implements AstTokenTranslator<AST> {

    /**
     *  {@link Java15Parser}から生成されるASTノードを {@link AstToken}に変換する.
     *  
     *  @param node 変換対象のノード
     *  @return 変換結果のAstToken
     */
    public AstToken translate(AST node) {
        int type = node.getType();
        AstToken result = null;

        //識別子だけは名前を使った専用のキャッシュを使う.
        if (type == CSharpTokenTypes.IDENT) {
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
        case CSharpTokenTypes.PACKAGE_DEF:
            result = DefinitionToken.NAMESPACE_DEFINITION;
            break;
        /*case CSharpTokenTypes.ANNOTATIONS:
        case CSharpTokenTypes.ANNOTATION_DEF:
            result = VisitControlToken.SKIP;
            //アノテーション関連は全部無視
            break;*/
        case CSharpTokenTypes.IMPORT:
            //        case CSharpTokenTypes.STATIC_IMPORT:
            result = JavaAstToken.CLASS_IMPORT;
            break;
        case CSharpTokenTypes.DOT:
            result = SyntaxToken.NAME_SEPARATOR;
            break;
        case CSharpTokenTypes.BLOCK:
            result = SyntaxToken.BLOCK_START;
            break;
        case CSharpTokenTypes.ARRAY_INIT:
            result = JavaAstToken.ARRAY_INIT;
            break;
        case CSharpTokenTypes.OBJBLOCK:
            result = SyntaxToken.CLASSBLOCK_START;
            break;
        case CSharpTokenTypes.CLASS_DEF:
            result = DefinitionToken.CLASS_DEFINITION;
            break;
        case CSharpTokenTypes.INTERFACE_DEF:
            result = JavaAstToken.INTERFACE_DEFINITION;
            break;
        case CSharpTokenTypes.ENUM_DEF://enumはクラスとして扱う
            result = DefinitionToken.CLASS_DEFINITION;
            break;
        case CSharpTokenTypes.ENUM_CONSTANT_DEF:
            result = JavaAstToken.ENUM_CONSTANT;
            break;
        case CSharpTokenTypes.FIELD_DEF:
            // C#ではただのVARIABLE_DEFになっている構文定義ファイルを書き換える必要がある．
            result = DefinitionToken.FIELD_DEFINITION;
            break;
        case CSharpTokenTypes.PARAMETER_DEF:
            result = DefinitionToken.METHOD_PARAMETER_DEFINITION;
            break;
        case CSharpTokenTypes.METHOD_DEF:
            result = DefinitionToken.METHOD_DEFINITION;
            break;
        case CSharpTokenTypes.CTOR_DEF:
            result = DefinitionToken.CONSTRUCTOR_DEFINITION;
            break;
        case CSharpTokenTypes.LOCAL_VARIABLE_DEF:
            result = DefinitionToken.LOCAL_VARIABLE_DEFINITION;
            break;
        case CSharpTokenTypes.LOCAL_PARAMETER_DEF:
            result = DefinitionToken.LOCAL_PARAMETER_DEFINITION;
            break;
        case CSharpTokenTypes.MODIFIERS:
            result = DescriptionToken.MODIFIER;
            break;
        case CSharpTokenTypes.TYPE:
            result = DescriptionToken.TYPE;
            break;
        case CSharpTokenTypes.NAME:
            result = DescriptionToken.NAME;
            break;
        case CSharpTokenTypes.ARRAY_DECLARATOR:
            result = SyntaxToken.ARRAY;
            break;
        case CSharpTokenTypes.ARRAY_INSTANTIATION:
            result = JavaAstToken.ARRAY_INSTANTIATION;
            break;
        case CSharpTokenTypes.EXTENDS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;
        case CSharpTokenTypes.IMPLEMENTS_CLAUSE:
            result = DescriptionToken.INHERITANCE;
            break;

        //        case CSharpTokenTypes.TYPE_PARAMETERS:
        //            result = DefinitionToken.TYPE_PARAMETERS_DEFINITION;
        //            break;

        /*case CSharpTokenTypes.TYPE_PARAMETER:
            result = DefinitionToken.TYPE_PARAMETER_DEFINITION;
            break;

        case CSharpTokenTypes.TYPE_UPPER_BOUNDS:
            result = DescriptionToken.TYPE_UPPER_BOUNDS;
            break;

        case CSharpTokenTypes.TYPE_LOWER_BOUNDS:
            result = DescriptionToken.TYPE_LOWER_BOUNDS;
            break;

        case CSharpTokenTypes.TYPE_ARGUMENTS:
            result = DescriptionToken.TYPE_ARGUMENTS;
            break;

        case CSharpTokenTypes.TYPE_ARGUMENT:
            result = DescriptionToken.TYPE_ARGUMENT;
            break;

        case CSharpTokenTypes.WILDCARD_TYPE:
            result = DescriptionToken.TYPE_WILDCARD;
            break;*/

        case CSharpTokenTypes.LITERAL_public:
            result = new AccessModifierToken("public", true, true, true);
            break;
        case CSharpTokenTypes.LITERAL_private:
            result = new AccessModifierToken("private", false, false, false);
            break;
        case CSharpTokenTypes.LITERAL_protected:
            result = new AccessModifierToken("protected", false, true, true);
            break;
        case CSharpTokenTypes.LITERAL_static:
            result = MemberTypeModifierToken.STATIC;
            break;
        case CSharpTokenTypes.LITERAL_synchronized:
            result = new ModifierToken("synchronized") {
                @Override
                public boolean isSynchronized() {
                    return true;
                }
            };
            break;
        case CSharpTokenTypes.FINAL:
            result = new ModifierToken("final");
            break;
        case CSharpTokenTypes.LITERAL_boolean:
        case CSharpTokenTypes.LITERAL_bool:
            result = BuiltinTypeToken.BOOLEAN;
            break;
        case CSharpTokenTypes.LITERAL_byte:
            result = BuiltinTypeToken.BYTE;
            break;
        case CSharpTokenTypes.LITERAL_char:
            result = BuiltinTypeToken.CHAR;
            break;
        case CSharpTokenTypes.LITERAL_double:
            result = BuiltinTypeToken.DOUBLE;
            break;
        case CSharpTokenTypes.LITERAL_float:
            result = BuiltinTypeToken.FLOAT;
            break;
        case CSharpTokenTypes.LITERAL_int:
            result = BuiltinTypeToken.INT;
            break;
        case CSharpTokenTypes.LITERAL_long:
            result = BuiltinTypeToken.LONG;
            break;
        case CSharpTokenTypes.LITERAL_short:
            result = BuiltinTypeToken.SHORT;
            break;
        case CSharpTokenTypes.LITERAL_void:
            result = BuiltinTypeToken.VOID;
            break;
        case CSharpTokenTypes.EXPR:
            result = DescriptionToken.EXPRESSION;
            break;
        case CSharpTokenTypes.EXPR_STATE:
            result = DescriptionToken.EXPRESSION_STATEMENT;
            break;
        case CSharpTokenTypes.LABELED_STAT:
            result = DescriptionToken.LABELED_STATEMENT;
            break;
        case CSharpTokenTypes.SLIST:
            result = DescriptionToken.SLIST;
            break;
        case CSharpTokenTypes.METHOD_CALL:
            result = SyntaxToken.METHOD_CALL;
            break;
        case CSharpTokenTypes.CTOR_CALL:
            result = JavaAstToken.CONSTRUCTOR_CALL;
            break;
        case CSharpTokenTypes.SUPER_CTOR_CALL:
            result = JavaAstToken.SUPER_CONSTRUCTOR_CALL;
            break;
        case CSharpTokenTypes.TYPECAST:
            result = OperatorToken.CAST;
            break;
        case CSharpTokenTypes.LITERAL_new:
            result = SyntaxToken.NEW;
            break;

        case CSharpTokenTypes.INDEX_OP:
            result = OperatorToken.ARRAY;
            break;

        case CSharpTokenTypes.ASSIGN:
            result = OperatorToken.ASSIGNMENT;
            return result;

        case CSharpTokenTypes.PLUS_ASSIGN:
        case CSharpTokenTypes.MINUS_ASSIGN:
        case CSharpTokenTypes.STAR_ASSIGN:
        case CSharpTokenTypes.DIV_ASSIGN:
        case CSharpTokenTypes.MOD_ASSIGN:
        case CSharpTokenTypes.SR_ASSIGN:
        case CSharpTokenTypes.BSR_ASSIGN:
        case CSharpTokenTypes.SL_ASSIGN:
        case CSharpTokenTypes.BAND_ASSIGN:
        case CSharpTokenTypes.BXOR_ASSIGN:
        case CSharpTokenTypes.BOR_ASSIGN:
            result = OperatorToken.COMPOUND_ASSIGNMENT;
            break;

        case CSharpTokenTypes.LOR:
        case CSharpTokenTypes.LAND:
            result = OperatorToken.LOGICAL_BINOMIAL;
            break;

        case CSharpTokenTypes.NOT_EQUAL:
        case CSharpTokenTypes.EQUAL:
        case CSharpTokenTypes.LE:
        case CSharpTokenTypes.GE:
        case CSharpTokenTypes.LITERAL_instanceof:
        case CSharpTokenTypes.LT:
        case CSharpTokenTypes.GT:
            result = OperatorToken.COMPARATIVE;
            break;

        case CSharpTokenTypes.SL:
        case CSharpTokenTypes.SR:
        case CSharpTokenTypes.BSR:
            result = OperatorToken.SHIFT;
            break;

        case CSharpTokenTypes.BAND:
        case CSharpTokenTypes.BOR:
        case CSharpTokenTypes.BXOR:
            result = OperatorToken.BIT_BINOMIAL;
            break;

        case CSharpTokenTypes.PLUS:
        case CSharpTokenTypes.MINUS:
        case CSharpTokenTypes.DIV:
        case CSharpTokenTypes.MOD:
        case CSharpTokenTypes.STAR:
            result = OperatorToken.ARITHMETICH_BINOMIAL;
            break;

        case CSharpTokenTypes.INC:
        case CSharpTokenTypes.DEC:
        case CSharpTokenTypes.POST_INC:
        case CSharpTokenTypes.POST_DEC:
            result = OperatorToken.INCL_AND_DECL;
            break;

        case CSharpTokenTypes.LNOT:
            result = OperatorToken.LOGICAL_UNARY;
            break;

        case CSharpTokenTypes.BNOT:
            result = OperatorToken.BIT_UNARY;
            break;

        case CSharpTokenTypes.UNARY_MINUS:
        case CSharpTokenTypes.UNARY_PLUS:
            result = OperatorToken.ARITHMETHIC_UNARY;
            break;

        case CSharpTokenTypes.QUESTION:
            result = OperatorToken.TERNARY;
            break;

        case CSharpTokenTypes.LITERAL_true:
            result = new ConstantToken("true", PrimitiveTypeInfo.BOOLEAN);
            break;
        case CSharpTokenTypes.LITERAL_false:
            result = new ConstantToken("false", PrimitiveTypeInfo.BOOLEAN);
            break;

        case CSharpTokenTypes.LITERAL_null:
            result = InstanceToken.NULL;
            break;
        case CSharpTokenTypes.LITERAL_this:
            result = InstanceToken.THIS;
            break;
        case CSharpTokenTypes.LITERAL_base:
            result = JavaAstToken.SUPER;
            break;
        case CSharpTokenTypes.LITERAL_class:
            result = JavaAstToken.CLASS;
            break;

        case CSharpTokenTypes.NUM_INT:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.INT);
        case CSharpTokenTypes.CHAR_LITERAL:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.CHAR);
        case CSharpTokenTypes.STRING_LITERAL:
            final ClassInfo stringClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "String" });
            return new ConstantToken(node.getText(), new ClassTypeInfo(stringClass));
        case CSharpTokenTypes.NUM_FLOAT:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.FLOAT);
        case CSharpTokenTypes.NUM_LONG:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.LONG);
        case CSharpTokenTypes.NUM_DOUBLE:
            return new ConstantToken(node.getText(), PrimitiveTypeInfo.DOUBLE);
        case CSharpTokenTypes.STATIC_INIT:
            //result = DefinitionToken.STATIC_INIT;
            //break;

        case CSharpTokenTypes.INSTANCE_INIT:
            //case CSharpTokenTypes.ANNOTATION:
        case CSharpTokenTypes.LITERAL_throws:
            result = VisitControlToken.SKIP;
            break;
        case CSharpTokenTypes.LITERAL_break:
            result = SyntaxToken.BREAK;
            break;
        case CSharpTokenTypes.LITERAL_throw:
            result = SyntaxToken.THROW;
            break;
        case CSharpTokenTypes.LITERAL_return:
            result = SyntaxToken.RETURN;
            break;
        //        case CSharpTokenTypes.PARAMETERS:
        //        case CSharpTokenTypes.ELIST:
        case CSharpTokenTypes.IF:
        case CSharpTokenTypes.LITERAL_if:
            result = BlockNameToken.IF_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_else:
        case CSharpTokenTypes.ELSE:
            result = BlockNameToken.ELSE_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_for:
        case CSharpTokenTypes.LITERAL_foreach:
            // TODO いずれforとforeachは区別すべき
            result = BlockNameToken.FOR_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_try:
            result = BlockNameToken.TRY_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_catch:
            result = BlockNameToken.CATCH_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_finally:
            result = BlockNameToken.FINALLY_BLOCK;
            break;
        //        case CSharpTokenTypes.LITERAL_return:
        case CSharpTokenTypes.LITERAL_do:
            result = BlockNameToken.DO_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_while:
            result = BlockNameToken.WHILE_BLOCK;
            break;
        case CSharpTokenTypes.LITERAL_switch:
            result = BlockNameToken.SWITCH_BLOCK;
            break;
        case CSharpTokenTypes.CASE_GROUP:
            result = BlockNameToken.CASE_GROUP_DEFINITION;
            break;
        case CSharpTokenTypes.LITERAL_case:
            result = BlockNameToken.CASE_ENTRY;
            break;
        case CSharpTokenTypes.LITERAL_default:
            result = BlockNameToken.DEFAULT_ENTRY;
            break;
        case CSharpTokenTypes.COND_CLAUSE:
            result = DescriptionToken.CONDITIONAL_CLAUSE;
            break;
        case CSharpTokenTypes.PROPERTY_DEF:
            result = DefinitionToken.PROPERTY_DEFINITION;
            break;
        case CSharpTokenTypes.PROPERTY_GET_BODY:
            result = SyntaxToken.PROPERTY_GET_BODY;
            break;
        case CSharpTokenTypes.PROPERTY_SET_BODY:
            result = SyntaxToken.PROPERTY_SET_BODY;
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
