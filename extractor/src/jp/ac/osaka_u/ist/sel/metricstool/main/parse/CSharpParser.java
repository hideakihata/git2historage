// $ANTLR : "CSharp.g" -> "CSharpParser.java"$

package jp.ac.osaka_u.ist.sel.metricstool.main.parse;

import antlr.CommonAST;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp.CSharpASTNodeFactory;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/** cSharp Recognizer
 *
 * Based heavily on the Grammer example that comes with ANTLR. See
 * http://www.antlr.org.
 *
 * This grammar is in the PUBLIC DOMAIN
 */
public class CSharpParser extends antlr.LLkParser       implements CSharpTokenTypes
 {

	private CSharpASTNodeFactory astNode;
	
	public CSharpParser(final CSharpLexer lexer) {
		this(lexer,2);
		this.astNode = new CSharpASTNodeFactory(this.getASTFactory());
	}
	
	@Override
	public void setASTFactory(ASTFactory f) {
        super.setASTFactory(f);
        this.astNode.setAstFactory(f);
    }

protected CSharpParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public CSharpParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected CSharpParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public CSharpParser(TokenStream lexer) {
  this(lexer,2);
}

public CSharpParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void compilationUnit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST compilationUnit_AST = null;
		
		try {      // for error handling
			{
			_loop3:
			do {
				if ((LA(1)==HASH) && (LA(2)==LITERAL_define)) {
					symbolDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop3;
				}
				
			} while (true);
			}
			{
			_loop5:
			do {
				if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
					region();
				}
				else {
					break _loop5;
				}
				
			} while (true);
			}
			{
			_loop7:
			do {
				if ((LA(1)==LITERAL_using)) {
					usingDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop7;
				}
				
			} while (true);
			}
			{
			_loop9:
			do {
				if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
					region();
				}
				else {
					break _loop9;
				}
				
			} while (true);
			}
			{
			_loop11:
			do {
				if ((LA(1)==HASH||LA(1)==LBRACK) && (_tokenSet_1.member(LA(2)))) {
					customAttribute();
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			{
			_loop13:
			do {
				if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
					region();
				}
				else {
					break _loop13;
				}
				
			} while (true);
			}
			{
			_loop15:
			do {
				if ((LA(1)==LITERAL_namespace)) {
					packageDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_2.member(LA(1))) && (_tokenSet_3.member(LA(2)))) {
					typeDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop15;
				}
				
			} while (true);
			}
			{
			_loop17:
			do {
				if ((LA(1)==HASH)) {
					region();
				}
				else {
					break _loop17;
				}
				
			} while (true);
			}
			match(Token.EOF_TYPE);
			compilationUnit_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = compilationUnit_AST;
	}
	
	public final void symbolDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST symbolDefinition_AST = null;
		
		try {      // for error handling
			match(HASH);
			CommonAST tmp3_AST = null;
			tmp3_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp3_AST);
			match(LITERAL_define);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			symbolDefinition_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			} else {
			  throw ex;
			}
		}
		returnAST = symbolDefinition_AST;
	}
	
	public final void region() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST region_AST = null;
		
		try {      // for error handling
			CommonAST tmp4_AST = null;
			tmp4_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp4_AST);
			match(HASH);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				CommonAST tmp5_AST = null;
				tmp5_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(IDENT);
				break;
			}
			case LITERAL_if:
			{
				{
				CommonAST tmp6_AST = null;
				tmp6_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(LITERAL_if);
				CommonAST tmp7_AST = null;
				tmp7_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(IDENT);
				}
				break;
			}
			case LITERAL_elif:
			{
				CommonAST tmp8_AST = null;
				tmp8_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
				match(LITERAL_elif);
				break;
			}
			case LITERAL_else:
			{
				CommonAST tmp9_AST = null;
				tmp9_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(LITERAL_else);
				break;
			}
			case LITERAL_endif:
			{
				CommonAST tmp10_AST = null;
				tmp10_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(LITERAL_endif);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			region_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = region_AST;
	}
	
	public final void usingDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST usingDefinition_AST = null;
		Token  i = null;
		CommonAST i_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = (CommonAST)astFactory.create(i);
			astFactory.makeASTRoot(currentAST, i_AST);
			match(LITERAL_using);
			{
			if ((LA(1)==IDENT) && (LA(2)==ASSIGN)) {
				CommonAST tmp11_AST = null;
				tmp11_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(IDENT);
				CommonAST tmp12_AST = null;
				tmp12_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(ASSIGN);
			}
			else if ((LA(1)==IDENT) && (LA(2)==SEMI||LA(2)==DOT)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				i_AST.setType(IMPORT);
			}
			identifierStar();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMI);
			usingDefinition_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			} else {
			  throw ex;
			}
		}
		returnAST = usingDefinition_AST;
	}
	
	public final void customAttribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST customAttribute_AST = null;
		
		try {      // for error handling
			{
			_loop361:
			do {
				if ((LA(1)==HASH)) {
					region();
				}
				else {
					break _loop361;
				}
				
			} while (true);
			}
			CommonAST tmp14_AST = null;
			tmp14_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp14_AST);
			match(LBRACK);
			{
			if ((LA(1)==IDENT) && (LA(2)==COLON)) {
				CommonAST tmp15_AST = null;
				tmp15_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp15_AST);
				match(IDENT);
				match(COLON);
			}
			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_8.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(RBRACK);
			customAttribute_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = customAttribute_AST;
	}
	
	public final void packageDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST packageDefinition_AST = null;
		
		try {      // for error handling
			packageHead();
			astFactory.addASTChild(currentAST, returnAST);
			match(LCURLY);
			{
			_loop21:
			do {
				if ((_tokenSet_2.member(LA(1)))) {
					typeDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop21;
				}
				
			} while (true);
			}
			match(RCURLY);
			packageDefinition_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = packageDefinition_AST;
	}
	
	public final void typeDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST typeDefinition_AST = null;
		CommonAST m_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LBRACK:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_enum:
			case LITERAL_struct:
			case LITERAL_class:
			case LITERAL_interface:
			{
				{
				_loop27:
				do {
					if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
						region();
					}
					else {
						break _loop27;
					}
					
				} while (true);
				}
				{
				_loop29:
				do {
					if ((LA(1)==HASH||LA(1)==LBRACK)) {
						customAttribute();
					}
					else {
						break _loop29;
					}
					
				} while (true);
				}
				modifiers();
				m_AST = (CommonAST)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_class:
				{
					classDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_struct:
				{
					structDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_enum:
				{
					enumDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop32:
				do {
					if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
						region();
					}
					else {
						break _loop32;
					}
					
				} while (true);
				}
				typeDefinition_AST = (CommonAST)currentAST.root;
				break;
			}
			case SEMI:
			{
				CommonAST tmp20_AST = null;
				tmp20_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp20_AST);
				match(SEMI);
				typeDefinition_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = typeDefinition_AST;
	}
	
	public final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST identifier_AST = null;
		
		try {      // for error handling
			CommonAST tmp21_AST = null;
			tmp21_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp21_AST);
			match(IDENT);
			{
			_loop52:
			do {
				if ((LA(1)==DOT)) {
					CommonAST tmp22_AST = null;
					tmp22_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp22_AST);
					match(DOT);
					CommonAST tmp23_AST = null;
					tmp23_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp23_AST);
					match(IDENT);
				}
				else {
					break _loop52;
				}
				
			} while (true);
			}
			identifier_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_12);
			} else {
			  throw ex;
			}
		}
		returnAST = identifier_AST;
	}
	
	public final void packageHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST packageHead_AST = null;
		Token  p = null;
		CommonAST p_AST = null;
		
		try {      // for error handling
			p = LT(1);
			p_AST = (CommonAST)astFactory.create(p);
			astFactory.makeASTRoot(currentAST, p_AST);
			match(LITERAL_namespace);
			if ( inputState.guessing==0 ) {
				p_AST.setType(PACKAGE_DEF);
			}
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			packageHead_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = packageHead_AST;
	}
	
	public final void identifierStar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST identifierStar_AST = null;
		
		try {      // for error handling
			CommonAST tmp24_AST = null;
			tmp24_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(IDENT);
			{
			_loop55:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					CommonAST tmp25_AST = null;
					tmp25_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp25_AST);
					match(DOT);
					CommonAST tmp26_AST = null;
					tmp26_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp26_AST);
					match(IDENT);
				}
				else {
					break _loop55;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case DOT:
			{
				CommonAST tmp27_AST = null;
				tmp27_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp27_AST);
				match(DOT);
				CommonAST tmp28_AST = null;
				tmp28_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp28_AST);
				match(STAR);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			identifierStar_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = identifierStar_AST;
	}
	
	public final void modifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST modifiers_AST = null;
		
		try {      // for error handling
			{
			_loop59:
			do {
				if ((_tokenSet_15.member(LA(1)))) {
					modifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop59;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				modifiers_AST = (CommonAST)currentAST.root;
				modifiers_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(MODIFIERS,"MODIFIERS")).add(modifiers_AST));
				currentAST.root = modifiers_AST;
				currentAST.child = modifiers_AST!=null &&modifiers_AST.getFirstChild()!=null ?
					modifiers_AST.getFirstChild() : modifiers_AST;
				currentAST.advanceChildToEnd();
			}
			modifiers_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			} else {
			  throw ex;
			}
		}
		returnAST = modifiers_AST;
	}
	
	public final void classDefinition(
		CommonAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST classDefinition_AST = null;
		CommonAST n_AST = null;
		CommonAST sc_AST = null;
		CommonAST ic_AST = null;
		CommonAST cb_AST = null;
		
		try {      // for error handling
			match(LITERAL_class);
			name();
			n_AST = (CommonAST)returnAST;
			superClassClause();
			sc_AST = (CommonAST)returnAST;
			implementsClause();
			ic_AST = (CommonAST)returnAST;
			classBlock();
			cb_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				classDefinition_AST = (CommonAST)currentAST.root;
				classDefinition_AST = (CommonAST)astFactory.make( (new ASTArray(6)).add((CommonAST)astFactory.create(CLASS_DEF,"CLASS_DEF")).add(modifiers).add(n_AST).add(sc_AST).add(ic_AST).add(cb_AST));
				currentAST.root = classDefinition_AST;
				currentAST.child = classDefinition_AST!=null &&classDefinition_AST.getFirstChild()!=null ?
					classDefinition_AST.getFirstChild() : classDefinition_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			} else {
			  throw ex;
			}
		}
		returnAST = classDefinition_AST;
	}
	
	public final void interfaceDefinition(
		CommonAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST interfaceDefinition_AST = null;
		CommonAST n_AST = null;
		CommonAST ie_AST = null;
		CommonAST cb_AST = null;
		
		try {      // for error handling
			match(LITERAL_interface);
			name();
			n_AST = (CommonAST)returnAST;
			interfaceExtends();
			ie_AST = (CommonAST)returnAST;
			classBlock();
			cb_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				interfaceDefinition_AST = (CommonAST)currentAST.root;
				interfaceDefinition_AST = (CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(INTERFACE_DEF,"INTERFACE_DEF")).add(modifiers).add(n_AST).add(ie_AST).add(cb_AST));
				currentAST.root = interfaceDefinition_AST;
				currentAST.child = interfaceDefinition_AST!=null &&interfaceDefinition_AST.getFirstChild()!=null ?
					interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_18);
			} else {
			  throw ex;
			}
		}
		returnAST = interfaceDefinition_AST;
	}
	
	public final void structDefinition(
		CommonAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST structDefinition_AST = null;
		CommonAST n_AST = null;
		CommonAST sc_AST = null;
		CommonAST sb_AST = null;
		
		try {      // for error handling
			match(LITERAL_struct);
			name();
			n_AST = (CommonAST)returnAST;
			superClassClause();
			sc_AST = (CommonAST)returnAST;
			structBlock();
			sb_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				structDefinition_AST = (CommonAST)currentAST.root;
				structDefinition_AST = (CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(CLASS_DEF,"STRUCT_DEF")).add(modifiers).add(n_AST).add(sc_AST).add(sb_AST));
				currentAST.root = structDefinition_AST;
				currentAST.child = structDefinition_AST!=null &&structDefinition_AST.getFirstChild()!=null ?
					structDefinition_AST.getFirstChild() : structDefinition_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = structDefinition_AST;
	}
	
	public final void enumDefinition(
		CommonAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST enumDefinition_AST = null;
		CommonAST n_AST = null;
		
		try {      // for error handling
			match(LITERAL_enum);
			name();
			n_AST = (CommonAST)returnAST;
			{
			switch ( LA(1)) {
			case COLON:
			{
				CommonAST tmp33_AST = null;
				tmp33_AST = (CommonAST)astFactory.create(LT(1));
				match(COLON);
				type();
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LCURLY);
			{
			_loop69:
			do {
				if ((LA(1)==IDENT)) {
					name();
					{
					switch ( LA(1)) {
					case COMMA:
					{
						CommonAST tmp35_AST = null;
						tmp35_AST = (CommonAST)astFactory.create(LT(1));
						match(COMMA);
						break;
					}
					case RCURLY:
					case IDENT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop69;
				}
				
			} while (true);
			}
			match(RCURLY);
			CommonAST tmp37_AST = null;
			tmp37_AST = (CommonAST)astFactory.create(LT(1));
			match(SEMI);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = enumDefinition_AST;
	}
	
/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST declaration_AST = null;
		CommonAST m_AST = null;
		CommonAST t_AST = null;
		CommonAST v_AST = null;
		
		try {      // for error handling
			modifiers();
			m_AST = (CommonAST)returnAST;
			typeSpec(false);
			t_AST = (CommonAST)returnAST;
			variableDefinitions(m_AST,t_AST);
			v_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				declaration_AST = (CommonAST)currentAST.root;
				declaration_AST = v_AST;
				currentAST.root = declaration_AST;
				currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
					declaration_AST.getFirstChild() : declaration_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = declaration_AST;
	}
	
	public final void typeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST typeSpec_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			{
				classTypeSpec(addImagNode);
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			{
				builtInTypeSpec(addImagNode);
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			typeSpec_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = typeSpec_AST;
	}
	
	public final void variableDefinitions(
		CommonAST mods, CommonAST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST variableDefinitions_AST = null;
		
		try {      // for error handling
			variableDeclarator((CommonAST) getASTFactory().dupTree(mods),
						   (CommonAST) getASTFactory().dupTree(t));
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop165:
			do {
				if ((LA(1)==COMMA)) {
					CommonAST tmp38_AST = null;
					tmp38_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp38_AST);
					match(COMMA);
					variableDeclarator((CommonAST) getASTFactory().dupTree(mods),
							   (CommonAST) getASTFactory().dupTree(t));
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop165;
				}
				
			} while (true);
			}
			variableDefinitions_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDefinitions_AST;
	}
	
	public final void classTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST classTypeSpec_AST = null;
		Token  lb = null;
		CommonAST lb_AST = null;
		
		try {      // for error handling
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop40:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (CommonAST)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					{
					_loop39:
					do {
						if ((LA(1)==COMMA)) {
							CommonAST tmp39_AST = null;
							tmp39_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp39_AST);
							match(COMMA);
						}
						else {
							break _loop39;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop40;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case STAR:
			{
				match(STAR);
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case ASSIGN:
			case SEMI:
			case COMMA:
			case RBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case COLON:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_try:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				classTypeSpec_AST = (CommonAST)currentAST.root;
				
							if ( addImagNode ) {
								classTypeSpec_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(classTypeSpec_AST));
							}
						
				currentAST.root = classTypeSpec_AST;
				currentAST.child = classTypeSpec_AST!=null &&classTypeSpec_AST.getFirstChild()!=null ?
					classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			classTypeSpec_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = classTypeSpec_AST;
	}
	
	public final void builtInTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST builtInTypeSpec_AST = null;
		Token  lb = null;
		CommonAST lb_AST = null;
		
		try {      // for error handling
			builtInType();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop46:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (CommonAST)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					{
					_loop45:
					do {
						if ((LA(1)==COMMA)) {
							CommonAST tmp42_AST = null;
							tmp42_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp42_AST);
							match(COMMA);
						}
						else {
							break _loop45;
						}
						
					} while (true);
					}
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop46;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case STAR:
			{
				match(STAR);
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case ASSIGN:
			case SEMI:
			case COMMA:
			case RBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case COLON:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_try:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				builtInTypeSpec_AST = (CommonAST)currentAST.root;
				
							if ( addImagNode ) {
								builtInTypeSpec_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(builtInTypeSpec_AST));
							}
						
				currentAST.root = builtInTypeSpec_AST;
				currentAST.child = builtInTypeSpec_AST!=null &&builtInTypeSpec_AST.getFirstChild()!=null ?
					builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			builtInTypeSpec_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = builtInTypeSpec_AST;
	}
	
	public final void builtInType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST builtInType_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_void:
			{
				CommonAST tmp45_AST = null;
				tmp45_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(LITERAL_void);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_boolean:
			{
				CommonAST tmp46_AST = null;
				tmp46_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(LITERAL_boolean);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_byte:
			{
				CommonAST tmp47_AST = null;
				tmp47_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp47_AST);
				match(LITERAL_byte);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_char:
			{
				CommonAST tmp48_AST = null;
				tmp48_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(LITERAL_char);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_short:
			{
				CommonAST tmp49_AST = null;
				tmp49_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp49_AST);
				match(LITERAL_short);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_int:
			{
				CommonAST tmp50_AST = null;
				tmp50_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp50_AST);
				match(LITERAL_int);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_float:
			{
				CommonAST tmp51_AST = null;
				tmp51_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp51_AST);
				match(LITERAL_float);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_long:
			{
				CommonAST tmp52_AST = null;
				tmp52_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp52_AST);
				match(LITERAL_long);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_double:
			{
				CommonAST tmp53_AST = null;
				tmp53_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp53_AST);
				match(LITERAL_double);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_bool:
			{
				CommonAST tmp54_AST = null;
				tmp54_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp54_AST);
				match(LITERAL_bool);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_sbyte:
			{
				CommonAST tmp55_AST = null;
				tmp55_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp55_AST);
				match(LITERAL_sbyte);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_uint:
			{
				CommonAST tmp56_AST = null;
				tmp56_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp56_AST);
				match(LITERAL_uint);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_ulong:
			{
				CommonAST tmp57_AST = null;
				tmp57_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp57_AST);
				match(LITERAL_ulong);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_ushort:
			{
				CommonAST tmp58_AST = null;
				tmp58_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(LITERAL_ushort);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_decimal:
			{
				CommonAST tmp59_AST = null;
				tmp59_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp59_AST);
				match(LITERAL_decimal);
				builtInType_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
		returnAST = builtInType_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST type_AST = null;
		CommonAST i_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				i_AST = (CommonAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					type_AST = (CommonAST)currentAST.root;
					type_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(type_AST));
					currentAST.root = type_AST;
					currentAST.child = type_AST!=null &&type_AST.getFirstChild()!=null ?
						type_AST.getFirstChild() : type_AST;
					currentAST.advanceChildToEnd();
				}
				type_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				type_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_21);
			} else {
			  throw ex;
			}
		}
		returnAST = type_AST;
	}
	
	public final void modifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST modifier_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_private:
			{
				CommonAST tmp60_AST = null;
				tmp60_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp60_AST);
				match(LITERAL_private);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_public:
			{
				CommonAST tmp61_AST = null;
				tmp61_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(LITERAL_public);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_protected:
			{
				CommonAST tmp62_AST = null;
				tmp62_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp62_AST);
				match(LITERAL_protected);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_static:
			{
				CommonAST tmp63_AST = null;
				tmp63_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
				match(LITERAL_static);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_transient:
			{
				CommonAST tmp64_AST = null;
				tmp64_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
				match(LITERAL_transient);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case FINAL:
			{
				CommonAST tmp65_AST = null;
				tmp65_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp65_AST);
				match(FINAL);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case ABSTRACT:
			{
				CommonAST tmp66_AST = null;
				tmp66_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp66_AST);
				match(ABSTRACT);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_native:
			{
				CommonAST tmp67_AST = null;
				tmp67_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
				match(LITERAL_native);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_synchronized:
			{
				CommonAST tmp68_AST = null;
				tmp68_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
				match(LITERAL_synchronized);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_const:
			{
				CommonAST tmp69_AST = null;
				tmp69_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(LITERAL_const);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_volatile:
			{
				CommonAST tmp70_AST = null;
				tmp70_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp70_AST);
				match(LITERAL_volatile);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case STRICTFP:
			{
				CommonAST tmp71_AST = null;
				tmp71_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp71_AST);
				match(STRICTFP);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_override:
			{
				CommonAST tmp72_AST = null;
				tmp72_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp72_AST);
				match(LITERAL_override);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_sealed:
			{
				CommonAST tmp73_AST = null;
				tmp73_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
				match(LITERAL_sealed);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_virtual:
			{
				CommonAST tmp74_AST = null;
				tmp74_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
				match(LITERAL_virtual);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_internal:
			{
				CommonAST tmp75_AST = null;
				tmp75_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp75_AST);
				match(LITERAL_internal);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case 104:
			{
				CommonAST tmp76_AST = null;
				tmp76_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(104);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_extern:
			{
				CommonAST tmp77_AST = null;
				tmp77_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp77_AST);
				match(LITERAL_extern);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_readonly:
			{
				CommonAST tmp78_AST = null;
				tmp78_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(LITERAL_readonly);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_event:
			{
				CommonAST tmp79_AST = null;
				tmp79_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp79_AST);
				match(LITERAL_event);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_delegate:
			{
				CommonAST tmp80_AST = null;
				tmp80_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(LITERAL_delegate);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_new:
			{
				CommonAST tmp81_AST = null;
				tmp81_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
				match(LITERAL_new);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_unsafe:
			{
				CommonAST tmp82_AST = null;
				tmp82_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
				match(LITERAL_unsafe);
				modifier_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_22);
			} else {
			  throw ex;
			}
		}
		returnAST = modifier_AST;
	}
	
	public final void regionNotIf() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST regionNotIf_AST = null;
		
		try {      // for error handling
			CommonAST tmp83_AST = null;
			tmp83_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp83_AST);
			match(HASH);
			CommonAST tmp84_AST = null;
			tmp84_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp84_AST);
			match(IDENT);
			regionNotIf_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = regionNotIf_AST;
	}
	
	public final void name() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST name_AST = null;
		
		try {      // for error handling
			CommonAST tmp85_AST = null;
			tmp85_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp85_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				name_AST = (CommonAST)currentAST.root;
				name_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(NAME,"NAME")).add(name_AST));
				currentAST.root = name_AST;
				currentAST.child = name_AST!=null &&name_AST.getFirstChild()!=null ?
					name_AST.getFirstChild() : name_AST;
				currentAST.advanceChildToEnd();
			}
			name_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_24);
			} else {
			  throw ex;
			}
		}
		returnAST = name_AST;
	}
	
	public final void superClassClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST superClassClause_AST = null;
		CommonAST id_AST = null;
		
		try {      // for error handling
			{
			{
			switch ( LA(1)) {
			case COLON:
			{
				CommonAST tmp86_AST = null;
				tmp86_AST = (CommonAST)astFactory.create(LT(1));
				match(COLON);
				break;
			}
			case LCURLY:
			case IDENT:
			case 121:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop77:
			do {
				if ((LA(1)==IDENT)) {
					identifier();
					id_AST = (CommonAST)returnAST;
					{
					switch ( LA(1)) {
					case COMMA:
					{
						CommonAST tmp87_AST = null;
						tmp87_AST = (CommonAST)astFactory.create(LT(1));
						match(COMMA);
						break;
					}
					case LCURLY:
					case IDENT:
					case 121:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop77;
				}
				
			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				superClassClause_AST = (CommonAST)currentAST.root;
				superClassClause_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(id_AST));
				currentAST.root = superClassClause_AST;
				currentAST.child = superClassClause_AST!=null &&superClassClause_AST.getFirstChild()!=null ?
					superClassClause_AST.getFirstChild() : superClassClause_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_25);
			} else {
			  throw ex;
			}
		}
		returnAST = superClassClause_AST;
	}
	
	public final void structBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST structBlock_AST = null;
		
		try {      // for error handling
			match(LCURLY);
			{
			_loop94:
			do {
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case HASH:
				case LCURLY:
				case IDENT:
				case LBRACK:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LITERAL_bool:
				case LITERAL_sbyte:
				case LITERAL_uint:
				case LITERAL_ulong:
				case LITERAL_ushort:
				case LITERAL_decimal:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_static:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_synchronized:
				case LITERAL_const:
				case LITERAL_volatile:
				case LITERAL_override:
				case LITERAL_sealed:
				case LITERAL_virtual:
				case LITERAL_internal:
				case 104:
				case LITERAL_extern:
				case LITERAL_readonly:
				case LITERAL_event:
				case LITERAL_delegate:
				case LITERAL_new:
				case LITERAL_unsafe:
				case LITERAL_enum:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_implicit:
				case LITERAL_explicit:
				case LITERAL_operator:
				case BNOT:
				{
					{
					{
					_loop91:
					do {
						if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
							region();
						}
						else {
							break _loop91;
						}
						
					} while (true);
					}
					field();
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop93:
					do {
						if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
							region();
						}
						else {
							break _loop93;
						}
						
					} while (true);
					}
					}
					break;
				}
				case SEMI:
				{
					CommonAST tmp89_AST = null;
					tmp89_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp89_AST);
					match(SEMI);
					break;
				}
				default:
				{
					break _loop94;
				}
				}
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				structBlock_AST = (CommonAST)currentAST.root;
				structBlock_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(OBJBLOCK,"OBJBLOCK")).add(structBlock_AST));
				currentAST.root = structBlock_AST;
				currentAST.child = structBlock_AST!=null &&structBlock_AST.getFirstChild()!=null ?
					structBlock_AST.getFirstChild() : structBlock_AST;
				currentAST.advanceChildToEnd();
			}
			structBlock_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = structBlock_AST;
	}
	
	public final void implementsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST implementsClause_AST = null;
		Token  i = null;
		CommonAST i_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case 121:
			{
				i = LT(1);
				i_AST = (CommonAST)astFactory.create(i);
				match(121);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop102:
				do {
					if ((LA(1)==COMMA)) {
						CommonAST tmp91_AST = null;
						tmp91_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp91_AST);
						match(COMMA);
						identifier();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop102;
					}
					
				} while (true);
				}
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				implementsClause_AST = (CommonAST)currentAST.root;
				implementsClause_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE")).add(implementsClause_AST));
				currentAST.root = implementsClause_AST;
				currentAST.child = implementsClause_AST!=null &&implementsClause_AST.getFirstChild()!=null ?
					implementsClause_AST.getFirstChild() : implementsClause_AST;
				currentAST.advanceChildToEnd();
			}
			implementsClause_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = implementsClause_AST;
	}
	
	public final void classBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST classBlock_AST = null;
		
		try {      // for error handling
			match(LCURLY);
			{
			_loop86:
			do {
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case HASH:
				case LCURLY:
				case IDENT:
				case LBRACK:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LITERAL_bool:
				case LITERAL_sbyte:
				case LITERAL_uint:
				case LITERAL_ulong:
				case LITERAL_ushort:
				case LITERAL_decimal:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_static:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_synchronized:
				case LITERAL_const:
				case LITERAL_volatile:
				case LITERAL_override:
				case LITERAL_sealed:
				case LITERAL_virtual:
				case LITERAL_internal:
				case 104:
				case LITERAL_extern:
				case LITERAL_readonly:
				case LITERAL_event:
				case LITERAL_delegate:
				case LITERAL_new:
				case LITERAL_unsafe:
				case LITERAL_enum:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_implicit:
				case LITERAL_explicit:
				case LITERAL_operator:
				case BNOT:
				{
					{
					{
					_loop83:
					do {
						if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
							region();
						}
						else {
							break _loop83;
						}
						
					} while (true);
					}
					field();
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop85:
					do {
						if ((LA(1)==HASH) && (_tokenSet_0.member(LA(2)))) {
							region();
						}
						else {
							break _loop85;
						}
						
					} while (true);
					}
					}
					break;
				}
				case SEMI:
				{
					match(SEMI);
					break;
				}
				default:
				{
					break _loop86;
				}
				}
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				classBlock_AST = (CommonAST)currentAST.root;
				classBlock_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(OBJBLOCK,"OBJBLOCK")).add(classBlock_AST));
				currentAST.root = classBlock_AST;
				currentAST.child = classBlock_AST!=null &&classBlock_AST.getFirstChild()!=null ?
					classBlock_AST.getFirstChild() : classBlock_AST;
				currentAST.advanceChildToEnd();
			}
			classBlock_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_26);
			} else {
			  throw ex;
			}
		}
		returnAST = classBlock_AST;
	}
	
	public final void interfaceExtends() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST interfaceExtends_AST = null;
		Token  e = null;
		CommonAST e_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				e = LT(1);
				e_AST = (CommonAST)astFactory.create(e);
				match(LITERAL_extends);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop98:
				do {
					if ((LA(1)==COMMA)) {
						CommonAST tmp95_AST = null;
						tmp95_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp95_AST);
						match(COMMA);
						identifier();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop98;
					}
					
				} while (true);
				}
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				interfaceExtends_AST = (CommonAST)currentAST.root;
				interfaceExtends_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(interfaceExtends_AST));
				currentAST.root = interfaceExtends_AST;
				currentAST.child = interfaceExtends_AST!=null &&interfaceExtends_AST.getFirstChild()!=null ?
					interfaceExtends_AST.getFirstChild() : interfaceExtends_AST;
				currentAST.advanceChildToEnd();
			}
			interfaceExtends_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = interfaceExtends_AST;
	}
	
	public final void field() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST field_AST = null;
		CommonAST mods_AST = null;
		CommonAST h_AST = null;
		CommonAST s_AST = null;
		CommonAST sh_AST = null;
		CommonAST sb_AST = null;
		CommonAST enh_AST = null;
		CommonAST enb_AST = null;
		CommonAST pt_AST = null;
		CommonAST p_AST = null;
		CommonAST pb_AST = null;
		CommonAST cd_AST = null;
		CommonAST id_AST = null;
		CommonAST t2_AST = null;
		CommonAST pr_AST = null;
		CommonAST cs_AST = null;
		CommonAST t_AST = null;
		CommonAST n_AST = null;
		CommonAST param_AST = null;
		CommonAST rt_AST = null;
		CommonAST tc_AST = null;
		CommonAST s2_AST = null;
		CommonAST f_AST = null;
		CommonAST s3_AST = null;
		CommonAST s4_AST = null;
		
		try {      // for error handling
			if ((_tokenSet_27.member(LA(1))) && (_tokenSet_28.member(LA(2)))) {
				{
				_loop105:
				do {
					if ((LA(1)==HASH||LA(1)==LBRACK)) {
						customAttribute();
					}
					else {
						break _loop105;
					}
					
				} while (true);
				}
				modifiers();
				mods_AST = (CommonAST)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_enum:
				{
					enumHead();
					enh_AST = (CommonAST)returnAST;
					enumBody();
					enb_AST = (CommonAST)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (CommonAST)currentAST.root;
						field_AST = ((CommonAST)astFactory.make( (new ASTArray(4)).add((CommonAST)astFactory.create(ENUM_DEF,"ENUM_DEF")).add(mods_AST).add(enh_AST).add(enb_AST)));
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				case LITERAL_class:
				{
					classDefinition(mods_AST);
					cd_AST = (CommonAST)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (CommonAST)currentAST.root;
						field_AST = cd_AST;
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition(mods_AST);
					id_AST = (CommonAST)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (CommonAST)currentAST.root;
						field_AST = id_AST;
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				case LITERAL_implicit:
				case LITERAL_explicit:
				case LITERAL_operator:
				{
					{
					switch ( LA(1)) {
					case LITERAL_implicit:
					{
						match(LITERAL_implicit);
						break;
					}
					case LITERAL_explicit:
					{
						match(LITERAL_explicit);
						break;
					}
					case LITERAL_operator:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(LITERAL_operator);
					typeSpec(true);
					t2_AST = (CommonAST)returnAST;
					match(LPAREN);
					parameterDeclarationList();
					pr_AST = (CommonAST)returnAST;
					match(RPAREN);
					compoundStatement();
					cs_AST = (CommonAST)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (CommonAST)currentAST.root;
						field_AST = ((CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(OPER_OVERLOAD_DEF,"OPER_OVERLOAD_DEF")).add(mods_AST).add(t2_AST).add(pr_AST).add(cs_AST)));
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				default:
					boolean synPredMatched108 = false;
					if (((LA(1)==IDENT||LA(1)==BNOT) && (LA(2)==IDENT||LA(2)==LPAREN))) {
						int _m108 = mark();
						synPredMatched108 = true;
						inputState.guessing++;
						try {
							{
							ctorHead();
							constructorBody();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched108 = false;
						}
						rewind(_m108);
inputState.guessing--;
					}
					if ( synPredMatched108 ) {
						ctorHead();
						h_AST = (CommonAST)returnAST;
						constructorBody();
						s_AST = (CommonAST)returnAST;
						if ( inputState.guessing==0 ) {
							field_AST = (CommonAST)currentAST.root;
							field_AST = (CommonAST)astFactory.make( (new ASTArray(4)).add((CommonAST)astFactory.create(CTOR_DEF,"CTOR_DEF")).add(mods_AST).add(h_AST).add(s_AST));
							currentAST.root = field_AST;
							currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
								field_AST.getFirstChild() : field_AST;
							currentAST.advanceChildToEnd();
						}
					}
					else {
						boolean synPredMatched110 = false;
						if (((LA(1)==IDENT) && (LA(2)==LPAREN))) {
							int _m110 = mark();
							synPredMatched110 = true;
							inputState.guessing++;
							try {
								{
								structHead();
								structBody();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched110 = false;
							}
							rewind(_m110);
inputState.guessing--;
						}
						if ( synPredMatched110 ) {
							structHead();
							sh_AST = (CommonAST)returnAST;
							structBody();
							sb_AST = (CommonAST)returnAST;
							if ( inputState.guessing==0 ) {
								field_AST = (CommonAST)currentAST.root;
								field_AST = ((CommonAST)astFactory.make( (new ASTArray(4)).add((CommonAST)astFactory.create(SCTOR_DEF,"SCTOR_DEF")).add(mods_AST).add(sh_AST).add(sb_AST)));
								currentAST.root = field_AST;
								currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
									field_AST.getFirstChild() : field_AST;
								currentAST.advanceChildToEnd();
							}
						}
						else {
							boolean synPredMatched114 = false;
							if (((_tokenSet_29.member(LA(1))) && (_tokenSet_30.member(LA(2))))) {
								int _m114 = mark();
								synPredMatched114 = true;
								inputState.guessing++;
								try {
									{
									type();
									propertyHead();
									propertyBody();
									}
								}
								catch (RecognitionException pe) {
									synPredMatched114 = false;
								}
								rewind(_m114);
inputState.guessing--;
							}
							if ( synPredMatched114 ) {
								type();
								pt_AST = (CommonAST)returnAST;
								propertyHead();
								p_AST = (CommonAST)returnAST;
								propertyBody();
								pb_AST = (CommonAST)returnAST;
								if ( inputState.guessing==0 ) {
									field_AST = (CommonAST)currentAST.root;
									field_AST =((CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(PROPERTY_DEF,"PROPERTY_DEF")).add(mods_AST).add((CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(pt_AST))).add(p_AST).add(pb_AST)));
									currentAST.root = field_AST;
									currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
										field_AST.getFirstChild() : field_AST;
									currentAST.advanceChildToEnd();
								}
							}
							else if ((_tokenSet_29.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
								typeSpec(false);
								t_AST = (CommonAST)returnAST;
								{
								if ((_tokenSet_32.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
									modifiers();
									name();
									n_AST = (CommonAST)returnAST;
									match(LPAREN);
									parameterDeclarationList();
									param_AST = (CommonAST)returnAST;
									match(RPAREN);
									declaratorBrackets(t_AST);
									rt_AST = (CommonAST)returnAST;
									{
									switch ( LA(1)) {
									case LITERAL_throws:
									{
										throwsClause();
										tc_AST = (CommonAST)returnAST;
										break;
									}
									case LCURLY:
									case SEMI:
									{
										break;
									}
									default:
									{
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
									}
									{
									switch ( LA(1)) {
									case LCURLY:
									{
										compoundStatement();
										s2_AST = (CommonAST)returnAST;
										break;
									}
									case SEMI:
									{
										CommonAST tmp103_AST = null;
										tmp103_AST = (CommonAST)astFactory.create(LT(1));
										match(SEMI);
										break;
									}
									default:
									{
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
									}
									if ( inputState.guessing==0 ) {
										field_AST = (CommonAST)currentAST.root;
										field_AST = ((CommonAST)astFactory.make( (new ASTArray(7)).add((CommonAST)astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods_AST).add((CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(rt_AST))).add(n_AST).add(param_AST).add(tc_AST).add(s2_AST)));
										currentAST.root = field_AST;
										currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
											field_AST.getFirstChild() : field_AST;
										currentAST.advanceChildToEnd();
									}
								}
								else if ((LA(1)==IDENT) && ((LA(2) >= ASSIGN && LA(2) <= COMMA))) {
									fieldDefinitions(mods_AST,t_AST);
									f_AST = (CommonAST)returnAST;
									CommonAST tmp104_AST = null;
									tmp104_AST = (CommonAST)astFactory.create(LT(1));
									match(SEMI);
									if ( inputState.guessing==0 ) {
										field_AST = (CommonAST)currentAST.root;
										field_AST = f_AST;
										currentAST.root = field_AST;
										currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
											field_AST.getFirstChild() : field_AST;
										currentAST.advanceChildToEnd();
									}
								}
								else {
									throw new NoViableAltException(LT(1), getFilename());
								}
								
								}
							}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}}}
						}
					}
					else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
						match(LITERAL_static);
						compoundStatement();
						s3_AST = (CommonAST)returnAST;
						if ( inputState.guessing==0 ) {
							field_AST = (CommonAST)currentAST.root;
							field_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(STATIC_INIT,"STATIC_INIT")).add(s3_AST));
							currentAST.root = field_AST;
							currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
								field_AST.getFirstChild() : field_AST;
							currentAST.advanceChildToEnd();
						}
					}
					else if ((LA(1)==LCURLY)) {
						compoundStatement();
						s4_AST = (CommonAST)returnAST;
						if ( inputState.guessing==0 ) {
							field_AST = (CommonAST)currentAST.root;
							field_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(s4_AST));
							currentAST.root = field_AST;
							currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
								field_AST.getFirstChild() : field_AST;
							currentAST.advanceChildToEnd();
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
				}
				catch (RecognitionException ex) {
					if (inputState.guessing==0) {
						reportError(ex);
						recover(ex,_tokenSet_34);
					} else {
					  throw ex;
					}
				}
				returnAST = field_AST;
			}
			
	public final void ctorHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST ctorHead_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case BNOT:
			{
				match(BNOT);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			name();
			astFactory.addASTChild(currentAST, returnAST);
			match(LPAREN);
			parameterDeclarationList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			{
			switch ( LA(1)) {
			case COLON:
			{
				explicitCtorInvocation();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			case LITERAL_throws:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LITERAL_throws:
			{
				throwsClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			ctorHead_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = ctorHead_AST;
	}
	
	public final void constructorBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST constructorBody_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (CommonAST)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(BLOCK);
			}
			{
			boolean synPredMatched122 = false;
			if (((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2))))) {
				int _m122 = mark();
				synPredMatched122 = true;
				inputState.guessing++;
				try {
					{
					explicitConstructorInvocation();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched122 = false;
				}
				rewind(_m122);
inputState.guessing--;
			}
			if ( synPredMatched122 ) {
				explicitConstructorInvocation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_37.member(LA(1))) && (_tokenSet_38.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			_loop124:
			do {
				if ((_tokenSet_23.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop124;
				}
				
			} while (true);
			}
			match(RCURLY);
			constructorBody_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_34);
			} else {
			  throw ex;
			}
		}
		returnAST = constructorBody_AST;
	}
	
	public final void structHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST structHead_AST = null;
		CommonAST n_AST = null;
		
		try {      // for error handling
			name();
			n_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(LPAREN);
			parameterDeclarationList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			structHead_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = structHead_AST;
	}
	
	public final void structBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST structBody_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (CommonAST)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(BLOCK);
			}
			{
			boolean synPredMatched128 = false;
			if (((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2))))) {
				int _m128 = mark();
				synPredMatched128 = true;
				inputState.guessing++;
				try {
					{
					explicitConstructorInvocation();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched128 = false;
				}
				rewind(_m128);
inputState.guessing--;
			}
			if ( synPredMatched128 ) {
				explicitConstructorInvocation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_37.member(LA(1))) && (_tokenSet_38.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			_loop130:
			do {
				if ((_tokenSet_23.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop130;
				}
				
			} while (true);
			}
			match(RCURLY);
			structBody_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_34);
			} else {
			  throw ex;
			}
		}
		returnAST = structBody_AST;
	}
	
	public final void enumHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST enumHead_AST = null;
		CommonAST n_AST = null;
		
		try {      // for error handling
			CommonAST tmp113_AST = null;
			tmp113_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp113_AST);
			match(LITERAL_enum);
			name();
			n_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COLON:
			{
				CommonAST tmp114_AST = null;
				tmp114_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp114_AST);
				match(COLON);
				type();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			enumHead_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = enumHead_AST;
	}
	
	public final void enumBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST enumBody_AST = null;
		Token  en = null;
		CommonAST en_AST = null;
		
		try {      // for error handling
			en = LT(1);
			en_AST = (CommonAST)astFactory.create(en);
			match(LCURLY);
			{
			_loop154:
			do {
				if ((LA(1)==IDENT)) {
					enumConstant();
					astFactory.addASTChild(currentAST, returnAST);
					{
					switch ( LA(1)) {
					case COMMA:
					{
						CommonAST tmp115_AST = null;
						tmp115_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp115_AST);
						match(COMMA);
						break;
					}
					case RCURLY:
					case IDENT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					break _loop154;
				}
				
			} while (true);
			}
			match(RCURLY);
			match(SEMI);
			enumBody_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_34);
			} else {
			  throw ex;
			}
		}
		returnAST = enumBody_AST;
	}
	
	public final void propertyHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propertyHead_AST = null;
		CommonAST n_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LBRACK:
			{
				CommonAST tmp118_AST = null;
				tmp118_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp118_AST);
				match(LBRACK);
				CommonAST tmp119_AST = null;
				tmp119_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp119_AST);
				match(RBRACK);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			name();
			n_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			propertyHead_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = propertyHead_AST;
	}
	
	public final void propertyBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propertyBody_AST = null;
		Token  pc = null;
		CommonAST pc_AST = null;
		
		try {      // for error handling
			pc = LT(1);
			pc_AST = (CommonAST)astFactory.create(pc);
			match(LCURLY);
			{
			{
			_loop134:
			do {
				if ((LA(1)==IDENT)) {
					propinnerBody();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop134;
				}
				
			} while (true);
			}
			}
			match(RCURLY);
			propertyBody_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_34);
			} else {
			  throw ex;
			}
		}
		returnAST = propertyBody_AST;
	}
	
	public final void parameterDeclarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST parameterDeclarationList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FINAL:
			case HASH:
			case IDENT:
			case LBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_params:
			case LITERAL_ref:
			case LITERAL_out:
			{
				{
				_loop197:
				do {
					if ((LA(1)==LITERAL_params)) {
						CommonAST tmp121_AST = null;
						tmp121_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp121_AST);
						match(LITERAL_params);
					}
					else {
						break _loop197;
					}
					
				} while (true);
				}
				{
				_loop199:
				do {
					if ((LA(1)==LITERAL_ref)) {
						CommonAST tmp122_AST = null;
						tmp122_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp122_AST);
						match(LITERAL_ref);
					}
					else {
						break _loop199;
					}
					
				} while (true);
				}
				{
				_loop201:
				do {
					if ((LA(1)==LITERAL_out)) {
						CommonAST tmp123_AST = null;
						tmp123_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp123_AST);
						match(LITERAL_out);
					}
					else {
						break _loop201;
					}
					
				} while (true);
				}
				parameterDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop209:
				do {
					if ((LA(1)==COMMA)) {
						CommonAST tmp124_AST = null;
						tmp124_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp124_AST);
						match(COMMA);
						{
						_loop204:
						do {
							if ((LA(1)==LITERAL_params)) {
								CommonAST tmp125_AST = null;
								tmp125_AST = (CommonAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp125_AST);
								match(LITERAL_params);
							}
							else {
								break _loop204;
							}
							
						} while (true);
						}
						{
						_loop206:
						do {
							if ((LA(1)==LITERAL_ref)) {
								CommonAST tmp126_AST = null;
								tmp126_AST = (CommonAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp126_AST);
								match(LITERAL_ref);
							}
							else {
								break _loop206;
							}
							
						} while (true);
						}
						{
						_loop208:
						do {
							if ((LA(1)==LITERAL_out)) {
								CommonAST tmp127_AST = null;
								tmp127_AST = (CommonAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp127_AST);
								match(LITERAL_out);
							}
							else {
								break _loop208;
							}
							
						} while (true);
						}
						parameterDeclaration();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop209;
					}
					
				} while (true);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				parameterDeclarationList_AST = (CommonAST)currentAST.root;
				parameterDeclarationList_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(PARAMETERS,"PARAMETERS")).add(parameterDeclarationList_AST));
				currentAST.root = parameterDeclarationList_AST;
				currentAST.child = parameterDeclarationList_AST!=null &&parameterDeclarationList_AST.getFirstChild()!=null ?
					parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
				currentAST.advanceChildToEnd();
			}
			parameterDeclarationList_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclarationList_AST;
	}
	
	public final void compoundStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST compoundStatement_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (CommonAST)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(BLOCK);
			}
			{
			_loop218:
			do {
				if ((_tokenSet_23.member(LA(1)))) {
					{
					_loop217:
					do {
						if ((LA(1)==HASH) && (LA(2)==IDENT)) {
							regionNotIf();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop217;
						}
						
					} while (true);
					}
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop218;
				}
				
			} while (true);
			}
			match(RCURLY);
			compoundStatement_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_40);
			} else {
			  throw ex;
			}
		}
		returnAST = compoundStatement_AST;
	}
	
	public final void declaratorBrackets(
		CommonAST typ
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST declaratorBrackets_AST = null;
		Token  lb = null;
		CommonAST lb_AST = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				declaratorBrackets_AST = (CommonAST)currentAST.root;
				declaratorBrackets_AST=typ;
				currentAST.root = declaratorBrackets_AST;
				currentAST.child = declaratorBrackets_AST!=null &&declaratorBrackets_AST.getFirstChild()!=null ?
					declaratorBrackets_AST.getFirstChild() : declaratorBrackets_AST;
				currentAST.advanceChildToEnd();
			}
			{
			_loop171:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (CommonAST)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_DECLARATOR);
					}
					{
					_loop170:
					do {
						if ((LA(1)==COMMA)) {
							CommonAST tmp129_AST = null;
							tmp129_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp129_AST);
							match(COMMA);
						}
						else {
							break _loop170;
						}
						
					} while (true);
					}
					match(RBRACK);
				}
				else {
					break _loop171;
				}
				
			} while (true);
			}
			declaratorBrackets_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_41);
			} else {
			  throw ex;
			}
		}
		returnAST = declaratorBrackets_AST;
	}
	
	public final void throwsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST throwsClause_AST = null;
		
		try {      // for error handling
			CommonAST tmp131_AST = null;
			tmp131_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp131_AST);
			match(LITERAL_throws);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop193:
			do {
				if ((LA(1)==COMMA)) {
					CommonAST tmp132_AST = null;
					tmp132_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp132_AST);
					match(COMMA);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop193;
				}
				
			} while (true);
			}
			throwsClause_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_42);
			} else {
			  throw ex;
			}
		}
		returnAST = throwsClause_AST;
	}
	
	public final void fieldDefinitions(
		CommonAST mods, CommonAST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST fieldDefinitions_AST = null;
		
		try {      // for error handling
			fieldDeclarator((CommonAST) getASTFactory().dupTree(mods),
						   (CommonAST) getASTFactory().dupTree(t));
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop161:
			do {
				if ((LA(1)==COMMA)) {
					CommonAST tmp133_AST = null;
					tmp133_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp133_AST);
					match(COMMA);
					fieldDeclarator((CommonAST) getASTFactory().dupTree(mods),
							   (CommonAST) getASTFactory().dupTree(t));
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop161;
				}
				
			} while (true);
			}
			fieldDefinitions_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = fieldDefinitions_AST;
	}
	
	public final void explicitConstructorInvocation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST explicitConstructorInvocation_AST = null;
		Token  lp1 = null;
		CommonAST lp1_AST = null;
		Token  lp2 = null;
		CommonAST lp2_AST = null;
		Token  lp3 = null;
		CommonAST lp3_AST = null;
		
		try {      // for error handling
			{
			if ((LA(1)==LITERAL_this) && (LA(2)==LPAREN)) {
				match(LITERAL_this);
				lp1 = LT(1);
				lp1_AST = (CommonAST)astFactory.create(lp1);
				astFactory.makeASTRoot(currentAST, lp1_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp1_AST.setType(CTOR_CALL);
				}
			}
			else if ((LA(1)==LITERAL_base) && (LA(2)==LPAREN)) {
				match(LITERAL_base);
				lp2 = LT(1);
				lp2_AST = (CommonAST)astFactory.create(lp2);
				astFactory.makeASTRoot(currentAST, lp2_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp2_AST.setType(SUPER_CTOR_CALL);
				}
			}
			else if ((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2)))) {
				primaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				match(DOT);
				match(LITERAL_base);
				lp3 = LT(1);
				lp3_AST = (CommonAST)astFactory.create(lp3);
				astFactory.makeASTRoot(currentAST, lp3_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp3_AST.setType(SUPER_CTOR_CALL);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			explicitConstructorInvocation_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		returnAST = explicitConstructorInvocation_AST;
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST statement_AST = null;
		
		try {      // for error handling
			traditionalStatement();
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_43);
			} else {
			  throw ex;
			}
		}
		returnAST = statement_AST;
	}
	
	public final void propinnerBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propinnerBody_AST = null;
		Token  pib = null;
		CommonAST pib_AST = null;
		
		try {      // for error handling
			pib = LT(1);
			pib_AST = (CommonAST)astFactory.create(pib);
			astFactory.makeASTRoot(currentAST, pib_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
				if(pib_AST.getText().equals("set")){
						pib_AST.setType(PROPERTY_SET_BODY);
				} else if(pib_AST.getText().equals("get")) {
						pib_AST.setType(PROPERTY_GET_BODY);
				}
				
			}
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				match(LCURLY);
				{
				_loop142:
				do {
					if ((_tokenSet_23.member(LA(1)))) {
						statement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop142;
					}
					
				} while (true);
				}
				match(RCURLY);
				break;
			}
			case RCURLY:
			case IDENT:
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case SEMI:
			{
				CommonAST tmp146_AST = null;
				tmp146_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp146_AST);
				match(SEMI);
				break;
			}
			case RCURLY:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			propinnerBody_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		returnAST = propinnerBody_AST;
	}
	
	public final void propertyBody2(
		CommonAST modifiers, CommonAST type, CommonAST propertyName
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propertyBody2_AST = null;
		Token  pc = null;
		CommonAST pc_AST = null;
		
		try {      // for error handling
			pc = LT(1);
			pc_AST = (CommonAST)astFactory.create(pc);
			match(LCURLY);
			{
			{
			_loop138:
			do {
				if ((LA(1)==IDENT)) {
					propinnerBody2(modifiers, type, propertyName);
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop138;
				}
				
			} while (true);
			}
			}
			match(RCURLY);
			propertyBody2_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = propertyBody2_AST;
	}
	
	public final void propinnerBody2(
		CommonAST modifiers, CommonAST type, CommonAST propertyName
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propinnerBody2_AST = null;
		Token  i = null;
		CommonAST i_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = (CommonAST)astFactory.create(i);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				propinnerBody2_AST = (CommonAST)currentAST.root;
				
							if(i_AST.getText().equals("set")){
								propinnerBody2_AST = (CommonAST)this.astNode.createPropertySetterHeadNode(modifiers, type, propertyName);
							} else if(i_AST.getText().equals("get")) {
								propinnerBody2_AST = (CommonAST)this.astNode.createPropertyGetterHeadNode(modifiers, type, propertyName);
							}
						
				currentAST.root = propinnerBody2_AST;
				currentAST.child = propinnerBody2_AST!=null &&propinnerBody2_AST.getFirstChild()!=null ?
					propinnerBody2_AST.getFirstChild() : propinnerBody2_AST;
				currentAST.advanceChildToEnd();
			}
			propinnerBlock();
			astFactory.addASTChild(currentAST, returnAST);
			propinnerBody2_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		returnAST = propinnerBody2_AST;
	}
	
	public final void setOrget() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST setOrget_AST = null;
		
		try {      // for error handling
			CommonAST tmp148_AST = null;
			tmp148_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp148_AST);
			match(IDENT);
			setOrget_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = setOrget_AST;
	}
	
	public final void propinnerBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST propinnerBlock_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				lc = LT(1);
				lc_AST = (CommonAST)astFactory.create(lc);
				astFactory.makeASTRoot(currentAST, lc_AST);
				match(LCURLY);
				if ( inputState.guessing==0 ) {
					lc_AST.setType(BLOCK);
				}
				{
				_loop149:
				do {
					if ((_tokenSet_23.member(LA(1)))) {
						statement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop149;
					}
					
				} while (true);
				}
				match(RCURLY);
				break;
			}
			case RCURLY:
			case IDENT:
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case SEMI:
			{
				CommonAST tmp150_AST = null;
				tmp150_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp150_AST);
				match(SEMI);
				break;
			}
			case RCURLY:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			propinnerBlock_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		returnAST = propinnerBlock_AST;
	}
	
	public final void enumConstant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST enumConstant_AST = null;
		CommonAST i_AST = null;
		
		try {      // for error handling
			name();
			i_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				enumConstant_AST = (CommonAST)currentAST.root;
				enumConstant_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(ENUM_CONSTANT_DEF,"ENUM_CONSTANT_DEF")).add(i_AST));
				currentAST.root = enumConstant_AST;
				currentAST.child = enumConstant_AST!=null &&enumConstant_AST.getFirstChild()!=null ?
					enumConstant_AST.getFirstChild() : enumConstant_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_45);
			} else {
			  throw ex;
			}
		}
		returnAST = enumConstant_AST;
	}
	
	public final void argList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST argList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RPAREN:
			case LITERAL_params:
			case LITERAL_ref:
			case LITERAL_out:
			{
				{
				_loop345:
				do {
					if ((LA(1)==LITERAL_params)) {
						CommonAST tmp151_AST = null;
						tmp151_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp151_AST);
						match(LITERAL_params);
					}
					else {
						break _loop345;
					}
					
				} while (true);
				}
				{
				_loop347:
				do {
					if ((LA(1)==LITERAL_ref)) {
						CommonAST tmp152_AST = null;
						tmp152_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp152_AST);
						match(LITERAL_ref);
					}
					else {
						break _loop347;
					}
					
				} while (true);
				}
				{
				_loop349:
				do {
					if ((LA(1)==LITERAL_out)) {
						CommonAST tmp153_AST = null;
						tmp153_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp153_AST);
						match(LITERAL_out);
					}
					else {
						break _loop349;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					argList_AST = (CommonAST)currentAST.root;
					argList_AST = (CommonAST)astFactory.create(ELIST,"ELIST");
					currentAST.root = argList_AST;
					currentAST.child = argList_AST!=null &&argList_AST.getFirstChild()!=null ?
						argList_AST.getFirstChild() : argList_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			argList_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		returnAST = argList_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST primaryExpression_AST = null;
		Token  lbt = null;
		CommonAST lbt_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				CommonAST tmp154_AST = null;
				tmp154_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp154_AST);
				match(IDENT);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				constant();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_true:
			{
				CommonAST tmp155_AST = null;
				tmp155_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp155_AST);
				match(LITERAL_true);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_false:
			{
				CommonAST tmp156_AST = null;
				tmp156_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp156_AST);
				match(LITERAL_false);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_this:
			{
				CommonAST tmp157_AST = null;
				tmp157_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp157_AST);
				match(LITERAL_this);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_null:
			{
				CommonAST tmp158_AST = null;
				tmp158_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp158_AST);
				match(LITERAL_null);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_new:
			{
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LPAREN:
			{
				match(LPAREN);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_base:
			{
				CommonAST tmp161_AST = null;
				tmp161_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp161_AST);
				match(LITERAL_base);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop337:
				do {
					if ((LA(1)==LBRACK)) {
						lbt = LT(1);
						lbt_AST = (CommonAST)astFactory.create(lbt);
						astFactory.makeASTRoot(currentAST, lbt_AST);
						match(LBRACK);
						if ( inputState.guessing==0 ) {
							lbt_AST.setType(ARRAY_DECLARATOR);
						}
						match(RBRACK);
					}
					else {
						break _loop337;
					}
					
				} while (true);
				}
				CommonAST tmp163_AST = null;
				tmp163_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp163_AST);
				match(DOT);
				CommonAST tmp164_AST = null;
				tmp164_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp164_AST);
				match(LITERAL_class);
				primaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = primaryExpression_AST;
	}
	
	public final void fieldDeclarator(
		CommonAST mods, CommonAST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST fieldDeclarator_AST = null;
		CommonAST id_AST = null;
		CommonAST d_AST = null;
		CommonAST v_AST = null;
		
		try {      // for error handling
			name();
			id_AST = (CommonAST)returnAST;
			declaratorBrackets(t);
			d_AST = (CommonAST)returnAST;
			varInitializer();
			v_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				fieldDeclarator_AST = (CommonAST)currentAST.root;
				fieldDeclarator_AST = (CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(FIELD_DEF,"FIELD_DEF")).add(mods).add((CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(d_AST))).add(id_AST).add(v_AST));
				currentAST.root = fieldDeclarator_AST;
				currentAST.child = fieldDeclarator_AST!=null &&fieldDeclarator_AST.getFirstChild()!=null ?
					fieldDeclarator_AST.getFirstChild() : fieldDeclarator_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = fieldDeclarator_AST;
	}
	
	public final void varInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST varInitializer_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				CommonAST tmp165_AST = null;
				tmp165_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp165_AST);
				match(ASSIGN);
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case COMMA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			varInitializer_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = varInitializer_AST;
	}
	
/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
	public final void variableDeclarator(
		CommonAST mods, CommonAST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST variableDeclarator_AST = null;
		CommonAST id_AST = null;
		CommonAST d_AST = null;
		CommonAST v_AST = null;
		
		try {      // for error handling
			name();
			id_AST = (CommonAST)returnAST;
			declaratorBrackets(t);
			d_AST = (CommonAST)returnAST;
			varInitializer();
			v_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				variableDeclarator_AST = (CommonAST)currentAST.root;
				variableDeclarator_AST = (CommonAST)astFactory.make( (new ASTArray(5)).add((CommonAST)astFactory.create(LOCAL_VARIABLE_DEF,"LOCAL_VARIABLE_DEF")).add(mods).add((CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(d_AST))).add(id_AST).add(v_AST));
				currentAST.root = variableDeclarator_AST;
				currentAST.child = variableDeclarator_AST!=null &&variableDeclarator_AST.getFirstChild()!=null ?
					variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDeclarator_AST;
	}
	
	public final void initializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST initializer_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (CommonAST)currentAST.root;
				break;
			}
			case LCURLY:
			{
				arrayInitializer();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_48);
			} else {
			  throw ex;
			}
		}
		returnAST = initializer_AST;
	}
	
	public final void arrayInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST arrayInitializer_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (CommonAST)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(ARRAY_INIT);
			}
			{
			switch ( LA(1)) {
			case LCURLY:
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop177:
				do {
					if ((LA(1)==COMMA) && (_tokenSet_49.member(LA(2)))) {
						CommonAST tmp166_AST = null;
						tmp166_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp166_AST);
						match(COMMA);
						initializer();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop177;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case COMMA:
				{
					CommonAST tmp167_AST = null;
					tmp167_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp167_AST);
					match(COMMA);
					break;
				}
				case RCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			CommonAST tmp168_AST = null;
			tmp168_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp168_AST);
			match(RCURLY);
			arrayInitializer_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = arrayInitializer_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST expression_AST = null;
		
		try {      // for error handling
			assignmentExpression();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				expression_AST = (CommonAST)currentAST.root;
				expression_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(EXPR,"EXPR")).add(expression_AST));
				currentAST.root = expression_AST;
				currentAST.child = expression_AST!=null &&expression_AST.getFirstChild()!=null ?
					expression_AST.getFirstChild() : expression_AST;
				currentAST.advanceChildToEnd();
			}
			expression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_50);
			} else {
			  throw ex;
			}
		}
		returnAST = expression_AST;
	}
	
	public final void explicitCtorInvocation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST explicitCtorInvocation_AST = null;
		Token  b = null;
		CommonAST b_AST = null;
		Token  t = null;
		CommonAST t_AST = null;
		
		try {      // for error handling
			match(COLON);
			{
			switch ( LA(1)) {
			case LITERAL_base:
			{
				b = LT(1);
				b_AST = (CommonAST)astFactory.create(b);
				astFactory.makeASTRoot(currentAST, b_AST);
				match(LITERAL_base);
				if ( inputState.guessing==0 ) {
					b_AST.setType(SUPER_CTOR_CALL);
				}
				break;
			}
			case LITERAL_this:
			{
				t = LT(1);
				t_AST = (CommonAST)astFactory.create(t);
				astFactory.makeASTRoot(currentAST, t_AST);
				match(LITERAL_this);
				if ( inputState.guessing==0 ) {
					t_AST.setType(CTOR_CALL);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LPAREN);
			argList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				explicitCtorInvocation_AST = (CommonAST)currentAST.root;
				explicitCtorInvocation_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(EXPR_STATE,"EXPR_STATE")).add(explicitCtorInvocation_AST));
				currentAST.root = explicitCtorInvocation_AST;
				currentAST.child = explicitCtorInvocation_AST!=null &&explicitCtorInvocation_AST.getFirstChild()!=null ?
					explicitCtorInvocation_AST.getFirstChild() : explicitCtorInvocation_AST;
				currentAST.advanceChildToEnd();
			}
			explicitCtorInvocation_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_51);
			} else {
			  throw ex;
			}
		}
		returnAST = explicitCtorInvocation_AST;
	}
	
	public final void parameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST parameterDeclaration_AST = null;
		CommonAST pm_AST = null;
		CommonAST t_AST = null;
		CommonAST id_AST = null;
		CommonAST pd_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case HASH:
			case LBRACK:
			{
				customAttribute();
				break;
			}
			case FINAL:
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			parameterModifier();
			pm_AST = (CommonAST)returnAST;
			typeSpec(false);
			t_AST = (CommonAST)returnAST;
			name();
			id_AST = (CommonAST)returnAST;
			declaratorBrackets(t_AST);
			pd_AST = (CommonAST)returnAST;
			if ( inputState.guessing==0 ) {
				parameterDeclaration_AST = (CommonAST)currentAST.root;
				parameterDeclaration_AST = (CommonAST)astFactory.make( (new ASTArray(4)).add((CommonAST)astFactory.create(PARAMETER_DEF,"PARAMETER_DEF")).add(pm_AST).add((CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(TYPE,"TYPE")).add(pd_AST))).add(id_AST));
				currentAST.root = parameterDeclaration_AST;
				currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
					parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_52);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclaration_AST;
	}
	
	public final void parameterModifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST parameterModifier_AST = null;
		Token  f = null;
		CommonAST f_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FINAL:
			{
				f = LT(1);
				f_AST = (CommonAST)astFactory.create(f);
				astFactory.addASTChild(currentAST, f_AST);
				match(FINAL);
				break;
			}
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				parameterModifier_AST = (CommonAST)currentAST.root;
				parameterModifier_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(MODIFIERS,"MODIFIERS")).add(f_AST));
				currentAST.root = parameterModifier_AST;
				currentAST.child = parameterModifier_AST!=null &&parameterModifier_AST.getFirstChild()!=null ?
					parameterModifier_AST.getFirstChild() : parameterModifier_AST;
				currentAST.advanceChildToEnd();
			}
			parameterModifier_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_29);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterModifier_AST;
	}
	
	public final void traditionalStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST traditionalStatement_AST = null;
		CommonAST ex_AST = null;
		CommonAST m_AST = null;
		Token  c = null;
		CommonAST c_AST = null;
		Token  i1 = null;
		CommonAST i1_AST = null;
		Token  i2 = null;
		CommonAST i2_AST = null;
		CommonAST d_AST = null;
		Token  lc = null;
		CommonAST lc_AST = null;
		Token  s = null;
		CommonAST s_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LCURLY:
			{
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_if:
			{
				i1 = LT(1);
				i1_AST = (CommonAST)astFactory.create(i1);
				astFactory.makeASTRoot(currentAST, i1_AST);
				match(LITERAL_if);
				conditionalClause();
				astFactory.addASTChild(currentAST, returnAST);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				{
				if ((LA(1)==LITERAL_else) && (_tokenSet_23.member(LA(2)))) {
					elseStatement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_43.member(LA(1))) && (_tokenSet_53.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case HASH:
			{
				match(HASH);
				i2 = LT(1);
				i2_AST = (CommonAST)astFactory.create(i2);
				astFactory.makeASTRoot(currentAST, i2_AST);
				match(LITERAL_if);
				if ( inputState.guessing==0 ) {
					i2_AST.setType(IF);
				}
				directiveConditionalClause();
				d_AST = (CommonAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop227:
				do {
					if ((_tokenSet_23.member(LA(1))) && (_tokenSet_54.member(LA(2)))) {
						statement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop227;
					}
					
				} while (true);
				}
				{
				if ((LA(1)==HASH) && (LA(2)==LITERAL_elif)) {
					directiveElifStatement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==HASH) && (LA(2)==LITERAL_else)) {
					directiveElseStatement(i2_AST);
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==HASH) && (LA(2)==LITERAL_endif)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				if ( inputState.guessing==0 ) {
					i2_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add(i2_AST).add(d_AST));
				}
				match(HASH);
				match(LITERAL_endif);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_for:
			{
				CommonAST tmp175_AST = null;
				tmp175_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp175_AST);
				match(LITERAL_for);
				match(LPAREN);
				forInit();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				forCond();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				forIter();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_foreach:
			{
				CommonAST tmp180_AST = null;
				tmp180_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp180_AST);
				match(LITERAL_foreach);
				forEachClause();
				astFactory.addASTChild(currentAST, returnAST);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_while:
			{
				CommonAST tmp181_AST = null;
				tmp181_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp181_AST);
				match(LITERAL_while);
				conditionalClause();
				astFactory.addASTChild(currentAST, returnAST);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_do:
			{
				CommonAST tmp182_AST = null;
				tmp182_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp182_AST);
				match(LITERAL_do);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				match(LITERAL_while);
				conditionalClause();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_break:
			{
				CommonAST tmp185_AST = null;
				tmp185_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp185_AST);
				match(LITERAL_break);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					CommonAST tmp186_AST = null;
					tmp186_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp186_AST);
					match(IDENT);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_continue:
			{
				CommonAST tmp188_AST = null;
				tmp188_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp188_AST);
				match(LITERAL_continue);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					CommonAST tmp189_AST = null;
					tmp189_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp189_AST);
					match(IDENT);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_return:
			{
				CommonAST tmp191_AST = null;
				tmp191_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp191_AST);
				match(LITERAL_return);
				{
				switch ( LA(1)) {
				case IDENT:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LITERAL_bool:
				case LITERAL_sbyte:
				case LITERAL_uint:
				case LITERAL_ulong:
				case LITERAL_ushort:
				case LITERAL_decimal:
				case LITERAL_new:
				case LPAREN:
				case LITERAL_this:
				case LITERAL_base:
				case BNOT:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_null:
				case NUM_INT:
				case CHAR_LITERAL:
				case STRING_LITERAL:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_DOUBLE:
				{
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_switch:
			{
				CommonAST tmp193_AST = null;
				tmp193_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp193_AST);
				match(LITERAL_switch);
				conditionalClause();
				astFactory.addASTChild(currentAST, returnAST);
				lc = LT(1);
				lc_AST = (CommonAST)astFactory.create(lc);
				astFactory.makeASTRoot(currentAST, lc_AST);
				match(LCURLY);
				if ( inputState.guessing==0 ) {
					lc_AST.setType(BLOCK);
				}
				{
				_loop233:
				do {
					if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
						casesGroup();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop233;
					}
					
				} while (true);
				}
				match(RCURLY);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_try:
			{
				tryBlock();
				astFactory.addASTChild(currentAST, returnAST);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_throw:
			{
				CommonAST tmp195_AST = null;
				tmp195_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp195_AST);
				match(LITERAL_throw);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case SEMI:
			{
				s = LT(1);
				s_AST = (CommonAST)astFactory.create(s);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					s_AST.setType(EMPTY_STAT);
				}
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			case LITERAL_goto:
			{
				CommonAST tmp197_AST = null;
				tmp197_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp197_AST);
				match(LITERAL_goto);
				CommonAST tmp198_AST = null;
				tmp198_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp198_AST);
				match(IDENT);
				match(SEMI);
				traditionalStatement_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched222 = false;
				if (((_tokenSet_55.member(LA(1))) && (_tokenSet_56.member(LA(2))))) {
					int _m222 = mark();
					synPredMatched222 = true;
					inputState.guessing++;
					try {
						{
						declaration();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched222 = false;
					}
					rewind(_m222);
inputState.guessing--;
				}
				if ( synPredMatched222 ) {
					declaration();
					astFactory.addASTChild(currentAST, returnAST);
					match(SEMI);
					traditionalStatement_AST = (CommonAST)currentAST.root;
				}
				else {
					boolean synPredMatched224 = false;
					if (((_tokenSet_7.member(LA(1))) && (_tokenSet_57.member(LA(2))))) {
						int _m224 = mark();
						synPredMatched224 = true;
						inputState.guessing++;
						try {
							{
							expression();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched224 = false;
						}
						rewind(_m224);
inputState.guessing--;
					}
					if ( synPredMatched224 ) {
						expression();
						ex_AST = (CommonAST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
						match(SEMI);
						if ( inputState.guessing==0 ) {
							traditionalStatement_AST = (CommonAST)currentAST.root;
							traditionalStatement_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(EXPR_STATE,"EXPR_STATE")).add(ex_AST));
							currentAST.root = traditionalStatement_AST;
							currentAST.child = traditionalStatement_AST!=null &&traditionalStatement_AST.getFirstChild()!=null ?
								traditionalStatement_AST.getFirstChild() : traditionalStatement_AST;
							currentAST.advanceChildToEnd();
						}
						traditionalStatement_AST = (CommonAST)currentAST.root;
					}
					else if ((_tokenSet_58.member(LA(1))) && (_tokenSet_59.member(LA(2)))) {
						modifiers();
						m_AST = (CommonAST)returnAST;
						classDefinition(m_AST);
						astFactory.addASTChild(currentAST, returnAST);
						traditionalStatement_AST = (CommonAST)currentAST.root;
					}
					else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
						CommonAST tmp202_AST = null;
						tmp202_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp202_AST);
						match(IDENT);
						c = LT(1);
						c_AST = (CommonAST)astFactory.create(c);
						astFactory.makeASTRoot(currentAST, c_AST);
						match(COLON);
						if ( inputState.guessing==0 ) {
							c_AST.setType(LABELED_STAT);
						}
						statement();
						astFactory.addASTChild(currentAST, returnAST);
						traditionalStatement_AST = (CommonAST)currentAST.root;
					}
					else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
						CommonAST tmp203_AST = null;
						tmp203_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp203_AST);
						match(LITERAL_synchronized);
						match(LPAREN);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						match(RPAREN);
						compoundStatement();
						astFactory.addASTChild(currentAST, returnAST);
						traditionalStatement_AST = (CommonAST)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					recover(ex,_tokenSet_43);
				} else {
				  throw ex;
				}
			}
			returnAST = traditionalStatement_AST;
		}
		
	public final void conditionalClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST conditionalClause_AST = null;
		CommonAST ex_AST = null;
		
		try {      // for error handling
			match(LPAREN);
			expression();
			ex_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				conditionalClause_AST = (CommonAST)currentAST.root;
				conditionalClause_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(COND_CLAUSE,"COND_CLAUSE")).add(ex_AST));
				currentAST.root = conditionalClause_AST;
				currentAST.child = conditionalClause_AST!=null &&conditionalClause_AST.getFirstChild()!=null ?
					conditionalClause_AST.getFirstChild() : conditionalClause_AST;
				currentAST.advanceChildToEnd();
			}
			conditionalClause_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = conditionalClause_AST;
	}
	
	public final void elseStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST elseStatement_AST = null;
		
		try {      // for error handling
			CommonAST tmp208_AST = null;
			tmp208_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp208_AST);
			match(LITERAL_else);
			statement();
			astFactory.addASTChild(currentAST, returnAST);
			elseStatement_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_43);
			} else {
			  throw ex;
			}
		}
		returnAST = elseStatement_AST;
	}
	
	public final void directiveConditionalClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST directiveConditionalClause_AST = null;
		CommonAST ex_AST = null;
		
		try {      // for error handling
			expression();
			ex_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				directiveConditionalClause_AST = (CommonAST)currentAST.root;
				directiveConditionalClause_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(COND_CLAUSE,"COND_CLAUSE")).add(ex_AST));
				currentAST.root = directiveConditionalClause_AST;
				currentAST.child = directiveConditionalClause_AST!=null &&directiveConditionalClause_AST.getFirstChild()!=null ?
					directiveConditionalClause_AST.getFirstChild() : directiveConditionalClause_AST;
				currentAST.advanceChildToEnd();
			}
			directiveConditionalClause_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = directiveConditionalClause_AST;
	}
	
	public final void directiveElifStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST directiveElifStatement_AST = null;
		Token  i = null;
		CommonAST i_AST = null;
		CommonAST d_AST = null;
		CommonAST d1_AST = null;
		CommonAST d2_AST = null;
		
		try {      // for error handling
			match(HASH);
			i = LT(1);
			i_AST = (CommonAST)astFactory.create(i);
			astFactory.makeASTRoot(currentAST, i_AST);
			match(LITERAL_elif);
			if ( inputState.guessing==0 ) {
				i_AST.setType(IF);
			}
			directiveConditionalClause();
			d_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop243:
			do {
				if ((_tokenSet_23.member(LA(1))) && (_tokenSet_54.member(LA(2)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop243;
				}
				
			} while (true);
			}
			{
			if ((LA(1)==HASH) && (LA(2)==LITERAL_elif)) {
				directiveElifStatement();
				d1_AST = (CommonAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==HASH) && (LA(2)==LITERAL_else)) {
				directiveElseStatement(i_AST);
				d2_AST = (CommonAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==HASH) && (LA(2)==LITERAL_endif)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				i_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add(i_AST).add(d_AST));
			}
			if ( inputState.guessing==0 ) {
				directiveElifStatement_AST = (CommonAST)currentAST.root;
				directiveElifStatement_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(ELSE,"ELSE")).add(i_AST));
				currentAST.root = directiveElifStatement_AST;
				currentAST.child = directiveElifStatement_AST!=null &&directiveElifStatement_AST.getFirstChild()!=null ?
					directiveElifStatement_AST.getFirstChild() : directiveElifStatement_AST;
				currentAST.advanceChildToEnd();
			}
			directiveElifStatement_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_60);
			} else {
			  throw ex;
			}
		}
		returnAST = directiveElifStatement_AST;
	}
	
	public final void directiveElseStatement(
		AST parentIf
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST directiveElseStatement_AST = null;
		Token  e = null;
		CommonAST e_AST = null;
		
		try {      // for error handling
			match(HASH);
			e = LT(1);
			e_AST = (CommonAST)astFactory.create(e);
			astFactory.makeASTRoot(currentAST, e_AST);
			match(LITERAL_else);
			{
			_loop240:
			do {
				if ((_tokenSet_23.member(LA(1))) && (_tokenSet_54.member(LA(2)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop240;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				parentIf = (CommonAST)astFactory.make( (new ASTArray(2)).add(parentIf).add(e_AST));
			}
			directiveElseStatement_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_60);
			} else {
			  throw ex;
			}
		}
		returnAST = directiveElseStatement_AST;
	}
	
	public final void forInit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST forInit_AST = null;
		
		try {      // for error handling
			{
			boolean synPredMatched257 = false;
			if (((_tokenSet_55.member(LA(1))) && (_tokenSet_56.member(LA(2))))) {
				int _m257 = mark();
				synPredMatched257 = true;
				inputState.guessing++;
				try {
					{
					declaration();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched257 = false;
				}
				rewind(_m257);
inputState.guessing--;
			}
			if ( synPredMatched257 ) {
				declaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_7.member(LA(1))) && (_tokenSet_61.member(LA(2)))) {
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==SEMI)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (CommonAST)currentAST.root;
				forInit_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(FOR_INIT,"FOR_INIT")).add(forInit_AST));
				currentAST.root = forInit_AST;
				currentAST.child = forInit_AST!=null &&forInit_AST.getFirstChild()!=null ?
					forInit_AST.getFirstChild() : forInit_AST;
				currentAST.advanceChildToEnd();
			}
			forInit_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = forInit_AST;
	}
	
	public final void forCond() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST forCond_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				forCond_AST = (CommonAST)currentAST.root;
				forCond_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(FOR_CONDITION,"FOR_CONDITION")).add(forCond_AST));
				currentAST.root = forCond_AST;
				currentAST.child = forCond_AST!=null &&forCond_AST.getFirstChild()!=null ?
					forCond_AST.getFirstChild() : forCond_AST;
				currentAST.advanceChildToEnd();
			}
			forCond_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = forCond_AST;
	}
	
	public final void forIter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST forIter_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				forIter_AST = (CommonAST)currentAST.root;
				forIter_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(FOR_ITERATOR,"FOR_ITERATOR")).add(forIter_AST));
				currentAST.root = forIter_AST;
				currentAST.child = forIter_AST!=null &&forIter_AST.getFirstChild()!=null ?
					forIter_AST.getFirstChild() : forIter_AST;
				currentAST.advanceChildToEnd();
			}
			forIter_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		returnAST = forIter_AST;
	}
	
	public final void forEachClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST forEachClause_AST = null;
		CommonAST ex_AST = null;
		
		try {      // for error handling
			match(LPAREN);
			forEachParameter();
			astFactory.addASTChild(currentAST, returnAST);
			CommonAST tmp212_AST = null;
			tmp212_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp212_AST);
			match(LITERAL_in);
			expression();
			ex_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				forEachClause_AST = (CommonAST)currentAST.root;
				forEachClause_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(FOR_EACH_CLAUSE,"FOR_EACH_CLAUSE")).add(forEachClause_AST));
				currentAST.root = forEachClause_AST;
				currentAST.child = forEachClause_AST!=null &&forEachClause_AST.getFirstChild()!=null ?
					forEachClause_AST.getFirstChild() : forEachClause_AST;
				currentAST.advanceChildToEnd();
			}
			forEachClause_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = forEachClause_AST;
	}
	
	public final void casesGroup() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST casesGroup_AST = null;
		
		try {      // for error handling
			{
			int _cnt248=0;
			_loop248:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_62.member(LA(2)))) {
					aCase();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt248>=1 ) { break _loop248; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt248++;
			} while (true);
			}
			caseSList();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				casesGroup_AST = (CommonAST)currentAST.root;
				casesGroup_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(CASE_GROUP,"CASE_GROUP")).add(casesGroup_AST));
				currentAST.root = casesGroup_AST;
				currentAST.child = casesGroup_AST!=null &&casesGroup_AST.getFirstChild()!=null ?
					casesGroup_AST.getFirstChild() : casesGroup_AST;
				currentAST.advanceChildToEnd();
			}
			casesGroup_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		returnAST = casesGroup_AST;
	}
	
	public final void tryBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST tryBlock_AST = null;
		
		try {      // for error handling
			CommonAST tmp214_AST = null;
			tmp214_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp214_AST);
			match(LITERAL_try);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop264:
			do {
				if ((LA(1)==LITERAL_catch)) {
					handler();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop264;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_finally:
			{
				finallyHandler();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case SEMI:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_class:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_case:
			case LITERAL_default:
			case LITERAL_try:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tryBlock_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_43);
			} else {
			  throw ex;
			}
		}
		returnAST = tryBlock_AST;
	}
	
	public final void forEachParameter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST forEachParameter_AST = null;
		
		try {      // for error handling
			modifiers();
			astFactory.addASTChild(currentAST, returnAST);
			type();
			astFactory.addASTChild(currentAST, returnAST);
			name();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				forEachParameter_AST = (CommonAST)currentAST.root;
				forEachParameter_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(LOCAL_VARIABLE_DEF,"LOCAL_VARIABLE_DEF")).add(forEachParameter_AST));
				currentAST.root = forEachParameter_AST;
				currentAST.child = forEachParameter_AST!=null &&forEachParameter_AST.getFirstChild()!=null ?
					forEachParameter_AST.getFirstChild() : forEachParameter_AST;
				currentAST.advanceChildToEnd();
			}
			forEachParameter_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_64);
			} else {
			  throw ex;
			}
		}
		returnAST = forEachParameter_AST;
	}
	
	public final void aCase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST aCase_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_case:
			{
				CommonAST tmp215_AST = null;
				tmp215_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp215_AST);
				match(LITERAL_case);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_default:
			{
				CommonAST tmp216_AST = null;
				tmp216_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp216_AST);
				match(LITERAL_default);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(COLON);
			aCase_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_65);
			} else {
			  throw ex;
			}
		}
		returnAST = aCase_AST;
	}
	
	public final void caseSList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST caseSList_AST = null;
		
		try {      // for error handling
			{
			_loop253:
			do {
				if ((_tokenSet_23.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop253;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				caseSList_AST = (CommonAST)currentAST.root;
				caseSList_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(SLIST,"SLIST")).add(caseSList_AST));
				currentAST.root = caseSList_AST;
				currentAST.child = caseSList_AST!=null &&caseSList_AST.getFirstChild()!=null ?
					caseSList_AST.getFirstChild() : caseSList_AST;
				currentAST.advanceChildToEnd();
			}
			caseSList_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		returnAST = caseSList_AST;
	}
	
	public final void expressionList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST expressionList_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop271:
			do {
				if ((LA(1)==COMMA)) {
					CommonAST tmp218_AST = null;
					tmp218_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp218_AST);
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop271;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				expressionList_AST = (CommonAST)currentAST.root;
				expressionList_AST = (CommonAST)astFactory.make( (new ASTArray(2)).add((CommonAST)astFactory.create(ELIST,"ELIST")).add(expressionList_AST));
				currentAST.root = expressionList_AST;
				currentAST.child = expressionList_AST!=null &&expressionList_AST.getFirstChild()!=null ?
					expressionList_AST.getFirstChild() : expressionList_AST;
				currentAST.advanceChildToEnd();
			}
			expressionList_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_66);
			} else {
			  throw ex;
			}
		}
		returnAST = expressionList_AST;
	}
	
	public final void handler() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST handler_AST = null;
		CommonAST pd_AST = null;
		
		try {      // for error handling
			CommonAST tmp219_AST = null;
			tmp219_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp219_AST);
			match(LITERAL_catch);
			match(LPAREN);
			parameterDeclaration();
			pd_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				pd_AST.setType(LOCAL_PARAMETER_DEF);
			}
			handler_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_67);
			} else {
			  throw ex;
			}
		}
		returnAST = handler_AST;
	}
	
	public final void finallyHandler() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST finallyHandler_AST = null;
		
		try {      // for error handling
			CommonAST tmp222_AST = null;
			tmp222_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp222_AST);
			match(LITERAL_finally);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			finallyHandler_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_43);
			} else {
			  throw ex;
			}
		}
		returnAST = finallyHandler_AST;
	}
	
	public final void assignmentExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST assignmentExpression_AST = null;
		
		try {      // for error handling
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASSIGN:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			{
				{
				switch ( LA(1)) {
				case ASSIGN:
				{
					CommonAST tmp223_AST = null;
					tmp223_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp223_AST);
					match(ASSIGN);
					break;
				}
				case PLUS_ASSIGN:
				{
					CommonAST tmp224_AST = null;
					tmp224_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp224_AST);
					match(PLUS_ASSIGN);
					break;
				}
				case MINUS_ASSIGN:
				{
					CommonAST tmp225_AST = null;
					tmp225_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp225_AST);
					match(MINUS_ASSIGN);
					break;
				}
				case STAR_ASSIGN:
				{
					CommonAST tmp226_AST = null;
					tmp226_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp226_AST);
					match(STAR_ASSIGN);
					break;
				}
				case DIV_ASSIGN:
				{
					CommonAST tmp227_AST = null;
					tmp227_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp227_AST);
					match(DIV_ASSIGN);
					break;
				}
				case MOD_ASSIGN:
				{
					CommonAST tmp228_AST = null;
					tmp228_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp228_AST);
					match(MOD_ASSIGN);
					break;
				}
				case SR_ASSIGN:
				{
					CommonAST tmp229_AST = null;
					tmp229_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp229_AST);
					match(SR_ASSIGN);
					break;
				}
				case BSR_ASSIGN:
				{
					CommonAST tmp230_AST = null;
					tmp230_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp230_AST);
					match(BSR_ASSIGN);
					break;
				}
				case SL_ASSIGN:
				{
					CommonAST tmp231_AST = null;
					tmp231_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp231_AST);
					match(SL_ASSIGN);
					break;
				}
				case BAND_ASSIGN:
				{
					CommonAST tmp232_AST = null;
					tmp232_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp232_AST);
					match(BAND_ASSIGN);
					break;
				}
				case BXOR_ASSIGN:
				{
					CommonAST tmp233_AST = null;
					tmp233_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp233_AST);
					match(BXOR_ASSIGN);
					break;
				}
				case BOR_ASSIGN:
				{
					CommonAST tmp234_AST = null;
					tmp234_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp234_AST);
					match(BOR_ASSIGN);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case SEMI:
			case COMMA:
			case RBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case COLON:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_try:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			assignmentExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_50);
			} else {
			  throw ex;
			}
		}
		returnAST = assignmentExpression_AST;
	}
	
	public final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST conditionalExpression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case QUESTION:
			{
				CommonAST tmp235_AST = null;
				tmp235_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp235_AST);
				match(QUESTION);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				CommonAST tmp236_AST = null;
				tmp236_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp236_AST);
				match(COLON);
				conditionalExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case ASSIGN:
			case SEMI:
			case COMMA:
			case RBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case COLON:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_try:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			conditionalExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_68);
			} else {
			  throw ex;
			}
		}
		returnAST = conditionalExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop279:
			do {
				if ((LA(1)==LOR)) {
					CommonAST tmp237_AST = null;
					tmp237_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp237_AST);
					match(LOR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop279;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_69);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST logicalAndExpression_AST = null;
		
		try {      // for error handling
			inclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop282:
			do {
				if ((LA(1)==LAND)) {
					CommonAST tmp238_AST = null;
					tmp238_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp238_AST);
					match(LAND);
					inclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop282;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_70);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalAndExpression_AST;
	}
	
	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST inclusiveOrExpression_AST = null;
		
		try {      // for error handling
			exclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop285:
			do {
				if ((LA(1)==BOR)) {
					CommonAST tmp239_AST = null;
					tmp239_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp239_AST);
					match(BOR);
					exclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop285;
				}
				
			} while (true);
			}
			inclusiveOrExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_71);
			} else {
			  throw ex;
			}
		}
		returnAST = inclusiveOrExpression_AST;
	}
	
	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST exclusiveOrExpression_AST = null;
		
		try {      // for error handling
			andExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop288:
			do {
				if ((LA(1)==BXOR)) {
					CommonAST tmp240_AST = null;
					tmp240_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp240_AST);
					match(BXOR);
					andExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop288;
				}
				
			} while (true);
			}
			exclusiveOrExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_72);
			} else {
			  throw ex;
			}
		}
		returnAST = exclusiveOrExpression_AST;
	}
	
	public final void andExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST andExpression_AST = null;
		
		try {      // for error handling
			equalityExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop291:
			do {
				if ((LA(1)==BAND)) {
					CommonAST tmp241_AST = null;
					tmp241_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp241_AST);
					match(BAND);
					equalityExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop291;
				}
				
			} while (true);
			}
			andExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_73);
			} else {
			  throw ex;
			}
		}
		returnAST = andExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST equalityExpression_AST = null;
		
		try {      // for error handling
			relationalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop295:
			do {
				if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
					{
					switch ( LA(1)) {
					case NOT_EQUAL:
					{
						CommonAST tmp242_AST = null;
						tmp242_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp242_AST);
						match(NOT_EQUAL);
						break;
					}
					case EQUAL:
					{
						CommonAST tmp243_AST = null;
						tmp243_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp243_AST);
						match(EQUAL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop295;
				}
				
			} while (true);
			}
			equalityExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_74);
			} else {
			  throw ex;
			}
		}
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST relationalExpression_AST = null;
		CommonAST s_AST = null;
		CommonAST a_AST = null;
		
		try {      // for error handling
			shiftExpression();
			s_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case HASH:
			case LCURLY:
			case RCURLY:
			case IDENT:
			case ASSIGN:
			case SEMI:
			case COMMA:
			case RBRACK:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_override:
			case LITERAL_sealed:
			case LITERAL_virtual:
			case LITERAL_internal:
			case 104:
			case LITERAL_extern:
			case LITERAL_readonly:
			case LITERAL_event:
			case LITERAL_delegate:
			case LITERAL_new:
			case LITERAL_unsafe:
			case LITERAL_if:
			case COLON:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LITERAL_for:
			case LITERAL_foreach:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_goto:
			case LITERAL_try:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				{
				_loop300:
				do {
					if (((LA(1) >= LT && LA(1) <= GE))) {
						{
						switch ( LA(1)) {
						case LT:
						{
							CommonAST tmp244_AST = null;
							tmp244_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp244_AST);
							match(LT);
							break;
						}
						case GT:
						{
							CommonAST tmp245_AST = null;
							tmp245_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp245_AST);
							match(GT);
							break;
						}
						case LE:
						{
							CommonAST tmp246_AST = null;
							tmp246_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp246_AST);
							match(LE);
							break;
						}
						case GE:
						{
							CommonAST tmp247_AST = null;
							tmp247_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp247_AST);
							match(GE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						shiftExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop300;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_instanceof:
			{
				CommonAST tmp248_AST = null;
				tmp248_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp248_AST);
				match(LITERAL_instanceof);
				typeSpec(true);
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_as:
			{
				as(s_AST);
				a_AST = (CommonAST)returnAST;
				if ( inputState.guessing==0 ) {
					relationalExpression_AST = (CommonAST)currentAST.root;
					relationalExpression_AST = a_AST;
					currentAST.root = relationalExpression_AST;
					currentAST.child = relationalExpression_AST!=null &&relationalExpression_AST.getFirstChild()!=null ?
						relationalExpression_AST.getFirstChild() : relationalExpression_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			case LITERAL_is:
			{
				CommonAST tmp249_AST = null;
				tmp249_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp249_AST);
				match(LITERAL_is);
				typeSpec(true);
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			relationalExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = relationalExpression_AST;
	}
	
	public final void shiftExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST shiftExpression_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop305:
			do {
				if (((LA(1) >= SL && LA(1) <= BSR))) {
					{
					switch ( LA(1)) {
					case SL:
					{
						CommonAST tmp250_AST = null;
						tmp250_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp250_AST);
						match(SL);
						break;
					}
					case SR:
					{
						CommonAST tmp251_AST = null;
						tmp251_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp251_AST);
						match(SR);
						break;
					}
					case BSR:
					{
						CommonAST tmp252_AST = null;
						tmp252_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp252_AST);
						match(BSR);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					additiveExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop305;
				}
				
			} while (true);
			}
			shiftExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_75);
			} else {
			  throw ex;
			}
		}
		returnAST = shiftExpression_AST;
	}
	
	public final void as(
		AST e
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST as_AST = null;
		CommonAST t_AST = null;
		
		try {      // for error handling
			match(LITERAL_as);
			typeSpec(true);
			t_AST = (CommonAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				as_AST = (CommonAST)currentAST.root;
				as_AST = ((CommonAST)astFactory.make( (new ASTArray(3)).add((CommonAST)astFactory.create(TYPECAST,"TYPECAST")).add(t_AST).add(e)));
				currentAST.root = as_AST;
				currentAST.child = as_AST!=null &&as_AST.getFirstChild()!=null ?
					as_AST.getFirstChild() : as_AST;
				currentAST.advanceChildToEnd();
			}
			as_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = as_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST additiveExpression_AST = null;
		
		try {      // for error handling
			multiplicativeExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop309:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_7.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						CommonAST tmp254_AST = null;
						tmp254_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp254_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						CommonAST tmp255_AST = null;
						tmp255_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp255_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplicativeExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop309;
				}
				
			} while (true);
			}
			additiveExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_76);
			} else {
			  throw ex;
			}
		}
		returnAST = additiveExpression_AST;
	}
	
	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST multiplicativeExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop313:
			do {
				if ((_tokenSet_77.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						CommonAST tmp256_AST = null;
						tmp256_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp256_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						CommonAST tmp257_AST = null;
						tmp257_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp257_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						CommonAST tmp258_AST = null;
						tmp258_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp258_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop313;
				}
				
			} while (true);
			}
			multiplicativeExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_76);
			} else {
			  throw ex;
			}
		}
		returnAST = multiplicativeExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST unaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INC:
			{
				CommonAST tmp259_AST = null;
				tmp259_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp259_AST);
				match(INC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case DEC:
			{
				CommonAST tmp260_AST = null;
				tmp260_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp260_AST);
				match(DEC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case MINUS:
			{
				CommonAST tmp261_AST = null;
				tmp261_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp261_AST);
				match(MINUS);
				if ( inputState.guessing==0 ) {
					tmp261_AST.setType(UNARY_MINUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case PLUS:
			{
				CommonAST tmp262_AST = null;
				tmp262_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp262_AST);
				match(PLUS);
				if ( inputState.guessing==0 ) {
					tmp262_AST.setType(UNARY_PLUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				unaryExpressionNotPlusMinus();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_78);
			} else {
			  throw ex;
			}
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST unaryExpressionNotPlusMinus_AST = null;
		Token  lpb = null;
		CommonAST lpb_AST = null;
		Token  lp = null;
		CommonAST lp_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BNOT:
			{
				CommonAST tmp263_AST = null;
				tmp263_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp263_AST);
				match(BNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (CommonAST)currentAST.root;
				break;
			}
			case LNOT:
			{
				CommonAST tmp264_AST = null;
				tmp264_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp264_AST);
				match(LNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (CommonAST)currentAST.root;
				break;
			}
			case IDENT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_bool:
			case LITERAL_sbyte:
			case LITERAL_uint:
			case LITERAL_ulong:
			case LITERAL_ushort:
			case LITERAL_decimal:
			case LITERAL_new:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_base:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				{
				if ((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_decimal))) {
					lpb = LT(1);
					lpb_AST = (CommonAST)astFactory.create(lpb);
					astFactory.makeASTRoot(currentAST, lpb_AST);
					match(LPAREN);
					if ( inputState.guessing==0 ) {
						lpb_AST.setType(TYPECAST);
					}
					builtInTypeSpec(true);
					astFactory.addASTChild(currentAST, returnAST);
					match(RPAREN);
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					boolean synPredMatched318 = false;
					if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
						int _m318 = mark();
						synPredMatched318 = true;
						inputState.guessing++;
						try {
							{
							match(LPAREN);
							classTypeSpec(true);
							match(RPAREN);
							unaryExpressionNotPlusMinus();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched318 = false;
						}
						rewind(_m318);
inputState.guessing--;
					}
					if ( synPredMatched318 ) {
						lp = LT(1);
						lp_AST = (CommonAST)astFactory.create(lp);
						astFactory.makeASTRoot(currentAST, lp_AST);
						match(LPAREN);
						if ( inputState.guessing==0 ) {
							lp_AST.setType(TYPECAST);
						}
						classTypeSpec(true);
						astFactory.addASTChild(currentAST, returnAST);
						match(RPAREN);
						unaryExpressionNotPlusMinus();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((_tokenSet_35.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
						postfixExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpressionNotPlusMinus_AST = (CommonAST)currentAST.root;
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					recover(ex,_tokenSet_78);
				} else {
				  throw ex;
				}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}
		
	public final void postfixExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST postfixExpression_AST = null;
		Token  lbc = null;
		CommonAST lbc_AST = null;
		Token  pb = null;
		CommonAST pb_AST = null;
		Token  pbs = null;
		CommonAST pbs_AST = null;
		Token  lp = null;
		CommonAST lp_AST = null;
		Token  in = null;
		CommonAST in_AST = null;
		Token  de = null;
		CommonAST de_AST = null;
		
		try {      // for error handling
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop333:
			do {
				if ((LA(1)==DOT)) {
					CommonAST tmp267_AST = null;
					tmp267_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp267_AST);
					match(DOT);
					{
					switch ( LA(1)) {
					case IDENT:
					{
						CommonAST tmp268_AST = null;
						tmp268_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp268_AST);
						match(IDENT);
						break;
					}
					case LITERAL_this:
					{
						CommonAST tmp269_AST = null;
						tmp269_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp269_AST);
						match(LITERAL_this);
						break;
					}
					case LITERAL_class:
					{
						CommonAST tmp270_AST = null;
						tmp270_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp270_AST);
						match(LITERAL_class);
						break;
					}
					case LITERAL_new:
					{
						newExpression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case LITERAL_base:
					{
						CommonAST tmp271_AST = null;
						tmp271_AST = (CommonAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp271_AST);
						match(LITERAL_base);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
					{
					int _cnt323=0;
					_loop323:
					do {
						if ((LA(1)==LBRACK)) {
							lbc = LT(1);
							lbc_AST = (CommonAST)astFactory.create(lbc);
							astFactory.makeASTRoot(currentAST, lbc_AST);
							match(LBRACK);
							if ( inputState.guessing==0 ) {
								lbc_AST.setType(ARRAY_DECLARATOR);
							}
							match(RBRACK);
						}
						else {
							if ( _cnt323>=1 ) { break _loop323; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt323++;
					} while (true);
					}
					CommonAST tmp273_AST = null;
					tmp273_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp273_AST);
					match(DOT);
					CommonAST tmp274_AST = null;
					tmp274_AST = (CommonAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp274_AST);
					match(LITERAL_class);
				}
				else {
					boolean synPredMatched325 = false;
					if (((LA(1)==LBRACK) && (LA(2)==IDENT))) {
						int _m325 = mark();
						synPredMatched325 = true;
						inputState.guessing++;
						try {
							{
							match(LBRACK);
							identifier();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched325 = false;
						}
						rewind(_m325);
inputState.guessing--;
					}
					if ( synPredMatched325 ) {
						pb = LT(1);
						pb_AST = (CommonAST)astFactory.create(pb);
						astFactory.makeASTRoot(currentAST, pb_AST);
						match(LBRACK);
						if ( inputState.guessing==0 ) {
							pb_AST.setType(INDEX_OP);
						}
						{
						int _cnt328=0;
						_loop328:
						do {
							if ((LA(1)==IDENT)) {
								identifier();
								astFactory.addASTChild(currentAST, returnAST);
								{
								switch ( LA(1)) {
								case COMMA:
								{
									CommonAST tmp275_AST = null;
									tmp275_AST = (CommonAST)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp275_AST);
									match(COMMA);
									break;
								}
								case IDENT:
								case RBRACK:
								{
									break;
								}
								default:
								{
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
								}
							}
							else {
								if ( _cnt328>=1 ) { break _loop328; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt328++;
						} while (true);
						}
						match(RBRACK);
					}
					else {
						boolean synPredMatched330 = false;
						if (((LA(1)==LBRACK) && (_tokenSet_7.member(LA(2))))) {
							int _m330 = mark();
							synPredMatched330 = true;
							inputState.guessing++;
							try {
								{
								match(LBRACK);
								expression();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched330 = false;
							}
							rewind(_m330);
inputState.guessing--;
						}
						if ( synPredMatched330 ) {
							pbs = LT(1);
							pbs_AST = (CommonAST)astFactory.create(pbs);
							astFactory.makeASTRoot(currentAST, pbs_AST);
							match(LBRACK);
							if ( inputState.guessing==0 ) {
								pbs_AST.setType(INDEX_OP);
							}
							expression();
							astFactory.addASTChild(currentAST, returnAST);
							{
							_loop332:
							do {
								if ((LA(1)==COMMA)) {
									CommonAST tmp277_AST = null;
									tmp277_AST = (CommonAST)astFactory.create(LT(1));
									astFactory.addASTChild(currentAST, tmp277_AST);
									match(COMMA);
									expression();
									astFactory.addASTChild(currentAST, returnAST);
								}
								else {
									break _loop332;
								}
								
							} while (true);
							}
							match(RBRACK);
						}
						else if ((LA(1)==LPAREN) && (_tokenSet_79.member(LA(2)))) {
							lp = LT(1);
							lp_AST = (CommonAST)astFactory.create(lp);
							astFactory.makeASTRoot(currentAST, lp_AST);
							match(LPAREN);
							if ( inputState.guessing==0 ) {
								lp_AST.setType(METHOD_CALL);
							}
							argList();
							astFactory.addASTChild(currentAST, returnAST);
							match(RPAREN);
						}
						else {
							break _loop333;
						}
						}}
					} while (true);
					}
					{
					if ((LA(1)==INC) && (_tokenSet_78.member(LA(2)))) {
						in = LT(1);
						in_AST = (CommonAST)astFactory.create(in);
						astFactory.makeASTRoot(currentAST, in_AST);
						match(INC);
						if ( inputState.guessing==0 ) {
							in_AST.setType(POST_INC);
						}
					}
					else if ((LA(1)==DEC) && (_tokenSet_78.member(LA(2)))) {
						de = LT(1);
						de_AST = (CommonAST)astFactory.create(de);
						astFactory.makeASTRoot(currentAST, de_AST);
						match(DEC);
						if ( inputState.guessing==0 ) {
							de_AST.setType(POST_DEC);
						}
					}
					else if ((_tokenSet_78.member(LA(1))) && (_tokenSet_80.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					postfixExpression_AST = (CommonAST)currentAST.root;
				}
				catch (RecognitionException ex) {
					if (inputState.guessing==0) {
						reportError(ex);
						recover(ex,_tokenSet_78);
					} else {
					  throw ex;
					}
				}
				returnAST = postfixExpression_AST;
			}
			
/** object instantiation.
 *  Trees are built as illustrated by the following input/tree pairs:
 *
 *  new T()
 *
 *  new
 *   |
 *   T --  ELIST
 *           |
 *          arg1 -- arg2 -- .. -- argn
 *
 *  new int[]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *
 *  new int[] {1,2}
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR -- ARRAY_INIT
 *                                  |
 *                                EXPR -- EXPR
 *                                  |      |
 *                                  1      2
 *
 *  new int[3]
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *                |
 *              EXPR
 *                |
 *                3
 *
 *  new int[1][2]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *               |
 *         ARRAY_DECLARATOR -- EXPR
 *               |              |
 *             EXPR             1
 *               |
 *               2
 *
 */
	public final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST newExpression_AST = null;
		
		try {      // for error handling
			CommonAST tmp280_AST = null;
			tmp280_AST = (CommonAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp280_AST);
			match(LITERAL_new);
			type();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				{
				if ((LA(1)==LCURLY) && (_tokenSet_34.member(LA(2)))) {
					classBlock();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_46.member(LA(1))) && (_tokenSet_81.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			case LBRACK:
			{
				newArrayDeclarator();
				astFactory.addASTChild(currentAST, returnAST);
				{
				if ((LA(1)==LCURLY) && (_tokenSet_82.member(LA(2)))) {
					arrayInitializer();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_46.member(LA(1))) && (_tokenSet_81.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			newExpression_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = newExpression_AST;
	}
	
	public final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST constant_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				CommonAST tmp283_AST = null;
				tmp283_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp283_AST);
				match(NUM_INT);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			case CHAR_LITERAL:
			{
				CommonAST tmp284_AST = null;
				tmp284_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp284_AST);
				match(CHAR_LITERAL);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			{
				CommonAST tmp285_AST = null;
				tmp285_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp285_AST);
				match(STRING_LITERAL);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				CommonAST tmp286_AST = null;
				tmp286_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp286_AST);
				match(NUM_FLOAT);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			case NUM_LONG:
			{
				CommonAST tmp287_AST = null;
				tmp287_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp287_AST);
				match(NUM_LONG);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			{
				CommonAST tmp288_AST = null;
				tmp288_AST = (CommonAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp288_AST);
				match(NUM_DOUBLE);
				constant_AST = (CommonAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = constant_AST;
	}
	
	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		CommonAST newArrayDeclarator_AST = null;
		Token  lb = null;
		CommonAST lb_AST = null;
		
		try {      // for error handling
			{
			int _cnt357=0;
			_loop357:
			do {
				if ((LA(1)==LBRACK) && (_tokenSet_83.member(LA(2)))) {
					lb = LT(1);
					lb_AST = (CommonAST)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_INSTANTIATION);
					}
					{
					switch ( LA(1)) {
					case IDENT:
					case LITERAL_void:
					case LITERAL_boolean:
					case LITERAL_byte:
					case LITERAL_char:
					case LITERAL_short:
					case LITERAL_int:
					case LITERAL_float:
					case LITERAL_long:
					case LITERAL_double:
					case LITERAL_bool:
					case LITERAL_sbyte:
					case LITERAL_uint:
					case LITERAL_ulong:
					case LITERAL_ushort:
					case LITERAL_decimal:
					case LITERAL_new:
					case LPAREN:
					case LITERAL_this:
					case LITERAL_base:
					case BNOT:
					case PLUS:
					case MINUS:
					case INC:
					case DEC:
					case LNOT:
					case LITERAL_true:
					case LITERAL_false:
					case LITERAL_null:
					case NUM_INT:
					case CHAR_LITERAL:
					case STRING_LITERAL:
					case NUM_FLOAT:
					case NUM_LONG:
					case NUM_DOUBLE:
					{
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case COMMA:
					case RBRACK:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					{
					_loop356:
					do {
						if ((LA(1)==COMMA)) {
							{
							CommonAST tmp289_AST = null;
							tmp289_AST = (CommonAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp289_AST);
							match(COMMA);
							}
							{
							switch ( LA(1)) {
							case IDENT:
							case LITERAL_void:
							case LITERAL_boolean:
							case LITERAL_byte:
							case LITERAL_char:
							case LITERAL_short:
							case LITERAL_int:
							case LITERAL_float:
							case LITERAL_long:
							case LITERAL_double:
							case LITERAL_bool:
							case LITERAL_sbyte:
							case LITERAL_uint:
							case LITERAL_ulong:
							case LITERAL_ushort:
							case LITERAL_decimal:
							case LITERAL_new:
							case LPAREN:
							case LITERAL_this:
							case LITERAL_base:
							case BNOT:
							case PLUS:
							case MINUS:
							case INC:
							case DEC:
							case LNOT:
							case LITERAL_true:
							case LITERAL_false:
							case LITERAL_null:
							case NUM_INT:
							case CHAR_LITERAL:
							case STRING_LITERAL:
							case NUM_FLOAT:
							case NUM_LONG:
							case NUM_DOUBLE:
							{
								expression();
								astFactory.addASTChild(currentAST, returnAST);
								break;
							}
							case COMMA:
							case RBRACK:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
						}
						else {
							break _loop356;
						}
						
					} while (true);
					}
					match(RBRACK);
				}
				else {
					if ( _cnt357>=1 ) { break _loop357; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt357++;
			} while (true);
			}
			newArrayDeclarator_AST = (CommonAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = newArrayDeclarator_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"BLOCK",
		"MODIFIERS",
		"OBJBLOCK",
		"SLIST",
		"CTOR_DEF",
		"METHOD_DEF",
		"VARIABLE_DEF",
		"INSTANCE_INIT",
		"STATIC_INIT",
		"TYPE",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"PACKAGE_DEF",
		"ARRAY_DECLARATOR",
		"EXTENDS_CLAUSE",
		"IMPLEMENTS_CLAUSE",
		"PARAMETERS",
		"PARAMETER_DEF",
		"LABELED_STAT",
		"TYPECAST",
		"INDEX_OP",
		"POST_INC",
		"POST_DEC",
		"METHOD_CALL",
		"EXPR",
		"ARRAY_INIT",
		"IMPORT",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"CASE_GROUP",
		"ELIST",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"FOR_EACH_CLAUSE",
		"EMPTY_STAT",
		"\"final\"",
		"\"abstract\"",
		"\"strictfp\"",
		"SUPER_CTOR_CALL",
		"CTOR_CALL",
		"PROPERTY_DEF",
		"ENUM_DEF",
		"STRUCT_DEF",
		"SCTOR_DEF",
		"EXPR_STATE",
		"FIELD_DEF",
		"NAME",
		"ENUM_CONSTANT_DEF",
		"LOCAL_PARAMETER_DEF",
		"ARRAY_INSTANTIATION",
		"COND_CLAUSE",
		"LOCAL_VARIABLE_DEF",
		"PROPERTY_SET_BODY",
		"PROPERTY_GET_BODY",
		"IF",
		"ELSE",
		"OPER_OVERLOAD_DEF",
		"HASH",
		"\"define\"",
		"LCURLY",
		"RCURLY",
		"\"namespace\"",
		"\"using\"",
		"IDENT",
		"ASSIGN",
		"SEMI",
		"LBRACK",
		"COMMA",
		"RBRACK",
		"STAR",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"\"bool\"",
		"\"sbyte\"",
		"\"uint\"",
		"\"ulong\"",
		"\"ushort\"",
		"\"decimal\"",
		"DOT",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"static\"",
		"\"transient\"",
		"\"native\"",
		"\"synchronized\"",
		"\"const\"",
		"\"volatile\"",
		"\"override\"",
		"\"sealed\"",
		"\"virtual\"",
		"\"internal\"",
		"\"internal protected\"",
		"\"extern\"",
		"\"readonly\"",
		"\"event\"",
		"\"delegate\"",
		"\"new\"",
		"\"unsafe\"",
		"\"if\"",
		"\"elif\"",
		"\"else\"",
		"\"endif\"",
		"\"enum\"",
		"COLON",
		"\"struct\"",
		"\"class\"",
		"\"interface\"",
		"\"extends\"",
		"\":\"",
		"\"implicit\"",
		"\"explicit\"",
		"\"operator\"",
		"LPAREN",
		"RPAREN",
		"\"this\"",
		"\"base\"",
		"BNOT",
		"\"throws\"",
		"\"params\"",
		"\"ref\"",
		"\"out\"",
		"\"for\"",
		"\"foreach\"",
		"\"while\"",
		"\"do\"",
		"\"break\"",
		"\"continue\"",
		"\"return\"",
		"\"switch\"",
		"\"throw\"",
		"\"goto\"",
		"\"in\"",
		"\"case\"",
		"\"default\"",
		"\"try\"",
		"\"catch\"",
		"\"finally\"",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"QUESTION",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"NOT_EQUAL",
		"EQUAL",
		"LT",
		"GT",
		"LE",
		"GE",
		"\"instanceof\"",
		"\"is\"",
		"\"as\"",
		"SL",
		"SR",
		"BSR",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"LNOT",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"NUM_INT",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"QUOTE",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 0L, 2111062325329936L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[8];
		data[1]=-6915382780876556272L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 4611693715008782336L, 65442931951009984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-6849975033297635116L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 4611693715008782338L, 65442931951009996L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 4611693715008782338L, 2083055565080099039L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[8];
		data[1]=-6917493843201886192L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[8];
		data[1]=-6917493843134775632L;
		data[2]=-4194301L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 4611693715008782338L, 2083055565080099028L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 4611693715008782338L, 65442931951009988L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 4611693715008782338L, 65442931951009990L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-2091640552003993601L;
		data[2]=-29271198558257213L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 0L, 1L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 0L, 64L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 7696581394432L, 140737354137600L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 0L, 2082914827725961232L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-4833769775119206185L;
		data[2]=-29273397576925245L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 4611693715008782338L, 2083055565080099031L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29271198558257213L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536100130829L;
		data[2]=-29271198558257213L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 0L, 2305843009213694097L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 7696581394432L, 2083055565080098832L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6899233154222000047L;
		data[2]=-29273397577318461L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 0L, 7138205409382236659L, 65536L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 0L, 144115188075855873L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-217580156997337097L;
		data[2]=-3211325L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 4611693715008782336L, 2074048365825357968L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = new long[8];
		data[0]=7696581394432L;
		data[1]=-4841369599423284080L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 0L, 67106832L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 0L, 67109008L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 7696581394432L, 140737421247632L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 7696581394432L, 140737354137616L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 7696581394432L, 2305983746567831568L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 4611693715008782336L, 2074048365825358035L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = new long[8];
		data[1]=-6917493843201886192L;
		data[2]=-288230376151711743L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = new long[8];
		data[1]=-6917493843134777200L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6899233154222000045L;
		data[2]=-29273397577318461L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-4838836324632888077L;
		data[2]=-3604541L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 0L, 4611686018427387904L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-4842776974373947181L;
		data[2]=-29273397573779517L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 0L, 4611686018427388257L, 4L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 0L, 65L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6898670204268578733L;
		data[2]=-29273397576925245L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 0L, 18L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { 0L, 274L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536100130829L;
		data[2]=-3604541L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 0L, 320L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { 0L, 322L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = new long[8];
		data[1]=-6917493843201886191L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	private static final long[] mk_tokenSet_50() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240877L;
		data[2]=-29273397577318461L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
	private static final long[] mk_tokenSet_51() {
		long[] data = { 0L, 1L, 4L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
	private static final long[] mk_tokenSet_52() {
		long[] data = { 0L, 4611686018427388160L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
	private static final long[] mk_tokenSet_53() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-4836865999795913485L;
		data[2]=-65597L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
	private static final long[] mk_tokenSet_54() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6894729554527519501L;
		data[2]=-3604541L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
	private static final long[] mk_tokenSet_55() {
		long[] data = { 7696581394432L, 140737421244432L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
	private static final long[] mk_tokenSet_56() {
		long[] data = { 7696581394432L, 140737488354448L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
	private static final long[] mk_tokenSet_57() {
		long[] data = new long[8];
		data[1]=-6917493843134776080L;
		data[2]=-4194301L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
	private static final long[] mk_tokenSet_58() {
		long[] data = { 7696581394432L, 18155135863619584L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
	private static final long[] mk_tokenSet_59() {
		long[] data = { 7696581394432L, 18155135863619600L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
	private static final long[] mk_tokenSet_60() {
		long[] data = { 4611686018427387904L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
	private static final long[] mk_tokenSet_61() {
		long[] data = new long[8];
		data[1]=-6917493843134775824L;
		data[2]=-4194301L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
	private static final long[] mk_tokenSet_62() {
		long[] data = new long[8];
		data[1]=-6912990243574515696L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
	private static final long[] mk_tokenSet_63() {
		long[] data = { 0L, 2L, 393216L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
	private static final long[] mk_tokenSet_64() {
		long[] data = { 0L, 0L, 65536L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());
	private static final long[] mk_tokenSet_65() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6899233154222000045L;
		data[2]=-29273397576925245L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());
	private static final long[] mk_tokenSet_66() {
		long[] data = { 0L, 4611686018427387968L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());
	private static final long[] mk_tokenSet_67() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-6898670204268578733L;
		data[2]=-29273397573779517L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_67 = new BitSet(mk_tokenSet_67());
	private static final long[] mk_tokenSet_68() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273388991578173L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_68 = new BitSet(mk_tokenSet_68());
	private static final long[] mk_tokenSet_69() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273380401643581L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_69 = new BitSet(mk_tokenSet_69());
	private static final long[] mk_tokenSet_70() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273363221774397L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_70 = new BitSet(mk_tokenSet_70());
	private static final long[] mk_tokenSet_71() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273328862036029L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_71 = new BitSet(mk_tokenSet_71());
	private static final long[] mk_tokenSet_72() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273260142559293L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_72 = new BitSet(mk_tokenSet_72());
	private static final long[] mk_tokenSet_73() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29273122703605821L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_73 = new BitSet(mk_tokenSet_73());
	private static final long[] mk_tokenSet_74() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-29272847825698877L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_74 = new BitSet(mk_tokenSet_74());
	private static final long[] mk_tokenSet_75() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-28991922604802109L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_75 = new BitSet(mk_tokenSet_75());
	private static final long[] mk_tokenSet_76() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167240845L;
		data[2]=-27021597767827517L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_76 = new BitSet(mk_tokenSet_76());
	private static final long[] mk_tokenSet_77() {
		long[] data = { 0L, 1024L, 27021597764222976L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_77 = new BitSet(mk_tokenSet_77());
	private static final long[] mk_tokenSet_78() {
		long[] data = new long[8];
		data[0]=4611693715008782336L;
		data[1]=-2283043536167239821L;
		data[2]=-3604541L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_78 = new BitSet(mk_tokenSet_78());
	private static final long[] mk_tokenSet_79() {
		long[] data = new long[8];
		data[1]=-2305807824774498288L;
		data[2]=-29273397577908165L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_79 = new BitSet(mk_tokenSet_79());
	private static final long[] mk_tokenSet_80() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-216172782113783817L;
		data[2]=-3211321L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_80 = new BitSet(mk_tokenSet_80());
	private static final long[] mk_tokenSet_81() {
		long[] data = new long[8];
		data[0]=4611693715008782338L;
		data[1]=-216172782113783817L;
		data[2]=-3211265L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_81 = new BitSet(mk_tokenSet_81());
	private static final long[] mk_tokenSet_82() {
		long[] data = new long[8];
		data[1]=-6917493843201886189L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_82 = new BitSet(mk_tokenSet_82());
	private static final long[] mk_tokenSet_83() {
		long[] data = new long[8];
		data[1]=-6917493843201885424L;
		data[2]=-29273397577908221L;
		data[3]=7L;
		return data;
	}
	public static final BitSet _tokenSet_83 = new BitSet(mk_tokenSet_83());
	
	}
