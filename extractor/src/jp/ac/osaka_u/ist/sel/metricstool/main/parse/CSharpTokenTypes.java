// $ANTLR : "CSharp.g" -> "CSharpParser.java"$

package jp.ac.osaka_u.ist.sel.metricstool.main.parse;

import antlr.CommonAST;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp.CSharpASTNodeFactory;

public interface CSharpTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int BLOCK = 4;
	int MODIFIERS = 5;
	int OBJBLOCK = 6;
	int SLIST = 7;
	int CTOR_DEF = 8;
	int METHOD_DEF = 9;
	int VARIABLE_DEF = 10;
	int INSTANCE_INIT = 11;
	int STATIC_INIT = 12;
	int TYPE = 13;
	int CLASS_DEF = 14;
	int INTERFACE_DEF = 15;
	int PACKAGE_DEF = 16;
	int ARRAY_DECLARATOR = 17;
	int EXTENDS_CLAUSE = 18;
	int IMPLEMENTS_CLAUSE = 19;
	int PARAMETERS = 20;
	int PARAMETER_DEF = 21;
	int LABELED_STAT = 22;
	int TYPECAST = 23;
	int INDEX_OP = 24;
	int POST_INC = 25;
	int POST_DEC = 26;
	int METHOD_CALL = 27;
	int EXPR = 28;
	int ARRAY_INIT = 29;
	int IMPORT = 30;
	int UNARY_MINUS = 31;
	int UNARY_PLUS = 32;
	int CASE_GROUP = 33;
	int ELIST = 34;
	int FOR_INIT = 35;
	int FOR_CONDITION = 36;
	int FOR_ITERATOR = 37;
	int FOR_EACH_CLAUSE = 38;
	int EMPTY_STAT = 39;
	int FINAL = 40;
	int ABSTRACT = 41;
	int STRICTFP = 42;
	int SUPER_CTOR_CALL = 43;
	int CTOR_CALL = 44;
	int PROPERTY_DEF = 45;
	int ENUM_DEF = 46;
	int STRUCT_DEF = 47;
	int SCTOR_DEF = 48;
	int EXPR_STATE = 49;
	int FIELD_DEF = 50;
	int NAME = 51;
	int ENUM_CONSTANT_DEF = 52;
	int LOCAL_PARAMETER_DEF = 53;
	int ARRAY_INSTANTIATION = 54;
	int COND_CLAUSE = 55;
	int LOCAL_VARIABLE_DEF = 56;
	int PROPERTY_SET_BODY = 57;
	int PROPERTY_GET_BODY = 58;
	int IF = 59;
	int ELSE = 60;
	int OPER_OVERLOAD_DEF = 61;
	int HASH = 62;
	int LITERAL_define = 63;
	int LCURLY = 64;
	int RCURLY = 65;
	int LITERAL_namespace = 66;
	int LITERAL_using = 67;
	int IDENT = 68;
	int ASSIGN = 69;
	int SEMI = 70;
	int LBRACK = 71;
	int COMMA = 72;
	int RBRACK = 73;
	int STAR = 74;
	int LITERAL_void = 75;
	int LITERAL_boolean = 76;
	int LITERAL_byte = 77;
	int LITERAL_char = 78;
	int LITERAL_short = 79;
	int LITERAL_int = 80;
	int LITERAL_float = 81;
	int LITERAL_long = 82;
	int LITERAL_double = 83;
	int LITERAL_bool = 84;
	int LITERAL_sbyte = 85;
	int LITERAL_uint = 86;
	int LITERAL_ulong = 87;
	int LITERAL_ushort = 88;
	int LITERAL_decimal = 89;
	int DOT = 90;
	int LITERAL_private = 91;
	int LITERAL_public = 92;
	int LITERAL_protected = 93;
	int LITERAL_static = 94;
	int LITERAL_transient = 95;
	int LITERAL_native = 96;
	int LITERAL_synchronized = 97;
	int LITERAL_const = 98;
	int LITERAL_volatile = 99;
	int LITERAL_override = 100;
	int LITERAL_sealed = 101;
	int LITERAL_virtual = 102;
	int LITERAL_internal = 103;
	// "internal protected" = 104
	int LITERAL_extern = 105;
	int LITERAL_readonly = 106;
	int LITERAL_event = 107;
	int LITERAL_delegate = 108;
	int LITERAL_new = 109;
	int LITERAL_unsafe = 110;
	int LITERAL_if = 111;
	int LITERAL_elif = 112;
	int LITERAL_else = 113;
	int LITERAL_endif = 114;
	int LITERAL_enum = 115;
	int COLON = 116;
	int LITERAL_struct = 117;
	int LITERAL_class = 118;
	int LITERAL_interface = 119;
	int LITERAL_extends = 120;
	// ":" = 121
	int LITERAL_implicit = 122;
	int LITERAL_explicit = 123;
	int LITERAL_operator = 124;
	int LPAREN = 125;
	int RPAREN = 126;
	int LITERAL_this = 127;
	int LITERAL_base = 128;
	int BNOT = 129;
	int LITERAL_throws = 130;
	int LITERAL_params = 131;
	int LITERAL_ref = 132;
	int LITERAL_out = 133;
	int LITERAL_for = 134;
	int LITERAL_foreach = 135;
	int LITERAL_while = 136;
	int LITERAL_do = 137;
	int LITERAL_break = 138;
	int LITERAL_continue = 139;
	int LITERAL_return = 140;
	int LITERAL_switch = 141;
	int LITERAL_throw = 142;
	int LITERAL_goto = 143;
	int LITERAL_in = 144;
	int LITERAL_case = 145;
	int LITERAL_default = 146;
	int LITERAL_try = 147;
	int LITERAL_catch = 148;
	int LITERAL_finally = 149;
	int PLUS_ASSIGN = 150;
	int MINUS_ASSIGN = 151;
	int STAR_ASSIGN = 152;
	int DIV_ASSIGN = 153;
	int MOD_ASSIGN = 154;
	int SR_ASSIGN = 155;
	int BSR_ASSIGN = 156;
	int SL_ASSIGN = 157;
	int BAND_ASSIGN = 158;
	int BXOR_ASSIGN = 159;
	int BOR_ASSIGN = 160;
	int QUESTION = 161;
	int LOR = 162;
	int LAND = 163;
	int BOR = 164;
	int BXOR = 165;
	int BAND = 166;
	int NOT_EQUAL = 167;
	int EQUAL = 168;
	int LT = 169;
	int GT = 170;
	int LE = 171;
	int GE = 172;
	int LITERAL_instanceof = 173;
	int LITERAL_is = 174;
	int LITERAL_as = 175;
	int SL = 176;
	int SR = 177;
	int BSR = 178;
	int PLUS = 179;
	int MINUS = 180;
	int DIV = 181;
	int MOD = 182;
	int INC = 183;
	int DEC = 184;
	int LNOT = 185;
	int LITERAL_true = 186;
	int LITERAL_false = 187;
	int LITERAL_null = 188;
	int NUM_INT = 189;
	int CHAR_LITERAL = 190;
	int STRING_LITERAL = 191;
	int NUM_FLOAT = 192;
	int NUM_LONG = 193;
	int NUM_DOUBLE = 194;
	int QUOTE = 195;
	int WS = 196;
	int SL_COMMENT = 197;
	int ML_COMMENT = 198;
	int ESC = 199;
	int HEX_DIGIT = 200;
	int VOCAB = 201;
	int EXPONENT = 202;
	int FLOAT_SUFFIX = 203;
}
