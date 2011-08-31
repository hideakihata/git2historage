// $ANTLR : "java15.g" -> "Java15Parser.java"$

	package jp.ac.osaka_u.ist.sel.metricstool.main.parse;
	
	import java.util.Stack;
	import java.util.SortedSet;
	import java.util.TreeSet;
	import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.*;

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

/** Java 1.5 Recognizer
/** Java 1.5 Recognizer
 *
 * Run 'java Main [-showtree] directory-full-of-java-files'
 *
 * [The -showtree option pops up a Swing frame that shows
 *  the AST constructed from the parser.]
 *
 * Run 'java Main <directory full of java files>'
 *
 * Contributing authors:
 *		John Mitchell		johnm@non.net
 *		Terence Parr		parrt@magelang.com
 *		John Lilley		jlilley@empathy.com
 *		Scott Stanchfield	thetick@magelang.com
 *		Markus Mohnen		mohnen@informatik.rwth-aachen.de
 *		Peter Williams		pete.williams@sun.com
 *		Allan Jacobs		Allan.Jacobs@eng.sun.com
 *		Steve Messick		messick@redhills.com
 *		John Pybus		john@pybus.org
 *
 * Version 1.00 December 9, 1997 -- initial release
 * Version 1.01 December 10, 1997
 *		fixed bug in octal def (0..7 not 0..8)
 * Version 1.10 August 1998 (parrt)
 *		added tree construction
 *		fixed definition of WS,comments for mac,pc,unix newlines
 *		added unary plus
 * Version 1.11 (Nov 20, 1998)
 *		Added "shutup" option to turn off last ambig warning.
 *		Fixed inner class def to allow named class defs as statements
 *		synchronized requires compound not simple statement
 *		add [] after builtInType DOT class in primaryExpression
 *		"const" is reserved but not valid..removed from modifiers
 * Version 1.12 (Feb 2, 1999)
 *		Changed LITERAL_xxx to xxx in tree grammar.
 *		Updated java.g to use tokens {...} now for 2.6.0 (new feature).
 *
 * Version 1.13 (Apr 23, 1999)
 *		Didn't have (stat)? for else clause in tree parser.
 *		Didn't gen ASTs for interface extends.  Updated tree parser too.
 *		Updated to 2.6.0.
 * Version 1.14 (Jun 20, 1999)
 *		Allowed final/abstract on local classes.
 *		Removed local interfaces from methods
 *		Put instanceof precedence where it belongs...in relationalExpr
 *			It also had expr not type as arg; fixed it.
 *		Missing ! on SEMI in classBlock
 *		fixed: (expr) + "string" was parsed incorrectly (+ as unary plus).
 *		fixed: didn't like Object[].class in parser or tree parser
 * Version 1.15 (Jun 26, 1999)
 *		Screwed up rule with instanceof in it. :(  Fixed.
 *		Tree parser didn't like (expr).something; fixed.
 *		Allowed multiple inheritance in tree grammar. oops.
 * Version 1.16 (August 22, 1999)
 *		Extending an interface built a wacky tree: had extra EXTENDS.
 *		Tree grammar didn't allow multiple superinterfaces.
 *		Tree grammar didn't allow empty var initializer: {}
 * Version 1.17 (October 12, 1999)
 *		ESC lexer rule allowed 399 max not 377 max.
 *		java.tree.g didn't handle the expression of synchronized
 *		statements.
 * Version 1.18 (August 12, 2001)
 *	  	Terence updated to Java 2 Version 1.3 by
 *		observing/combining work of Allan Jacobs and Steve
 *		Messick.  Handles 1.3 src.  Summary:
 *		o  primary didn't include boolean.class kind of thing
 *	  	o  constructor calls parsed explicitly now:
 * 		   see explicitConstructorInvocation
 *		o  add strictfp modifier
 *	  	o  missing objBlock after new expression in tree grammar
 *		o  merged local class definition alternatives, moved after declaration
 *		o  fixed problem with ClassName.super.field
 *	  	o  reordered some alternatives to make things more efficient
 *		o  long and double constants were not differentiated from int/float
 *		o  whitespace rule was inefficient: matched only one char
 *		o  add an examples directory with some nasty 1.3 cases
 *		o  made Main.java use buffered IO and a Reader for Unicode support
 *		o  supports UNICODE?
 *		   Using Unicode charVocabulay makes code file big, but only
 *		   in the bitsets at the end. I need to make ANTLR generate
 *		   unicode bitsets more efficiently.
 * Version 1.19 (April 25, 2002)
 *		Terence added in nice fixes by John Pybus concerning floating
 *		constants and problems with super() calls.  John did a nice
 *		reorg of the primary/postfix expression stuff to read better
 *		and makes f.g.super() parse properly (it was METHOD_CALL not
 *		a SUPER_CTOR_CALL).  Also:
 *
 *		o  "finally" clause was a root...made it a child of "try"
 *		o  Added stuff for asserts too for Java 1.4, but *commented out*
 *		   as it is not backward compatible.
 *
 * Version 1.20 (October 27, 2002)
 *
 *	  Terence ended up reorging John Pybus' stuff to
 *	  remove some nondeterminisms and some syntactic predicates.
 *	  Note that the grammar is stricter now; e.g., this(...) must
 *	be the first statement.
 *
 *	  Trinary ?: operator wasn't working as array name:
 *		  (isBig ? bigDigits : digits)[i];
 *
 *	  Checked parser/tree parser on source for
 *		  Resin-2.0.5, jive-2.1.1, jdk 1.3.1, Lucene, antlr 2.7.2a4,
 *		and the 110k-line jGuru server source.
 *
 * Version 1.21 (October 17, 2003)
 *  Fixed lots of problems including:
 *  Ray Waldin: add typeDefinition to interfaceBlock in java.tree.g
 *  He found a problem/fix with floating point that start with 0
 *  Ray also fixed problem that (int.class) was not recognized.
 *  Thorsten van Ellen noticed that \n are allowed incorrectly in strings.
 *  TJP fixed CHAR_LITERAL analogously.
 *
 * Version 1.21.2 (March, 2003)
 *	  Changes by Matt Quail to support generics (as per JDK1.5/JSR14)
 *	  Notes:
 *	  o We only allow the "extends" keyword and not the "implements"
 *		keyword, since thats what JSR14 seems to imply.
 *	  o Thanks to Monty Zukowski for his help on the antlr-interest
 *		mail list.
 *	  o Thanks to Alan Eliasen for testing the grammar over his
 *		Fink source base
 *
 * Version 1.22 (July, 2004)
 *	  Changes by Michael Studman to support Java 1.5 language extensions
 *	  Notes:
 *	  o Added support for annotations types
 *	  o Finished off Matt Quail's generics enhancements to support bound type arguments
 *	  o Added support for new for statement syntax
 *	  o Added support for static import syntax
 *	  o Added support for enum types
 *	  o Tested against JDK 1.5 source base and source base of jdigraph project
 *	  o Thanks to Matt Quail for doing the hard part by doing most of the generics work
 *
 * Version 1.22.1 (July 28, 2004)
 *	  Bug/omission fixes for Java 1.5 language support
 *	  o Fixed tree structure bug with classOrInterface - thanks to Pieter Vangorpto for
 *		spotting this
 *	  o Fixed bug where incorrect handling of SR and BSR tokens would cause type
 *		parameters to be recognised as type arguments.
 *	  o Enabled type parameters on constructors, annotations on enum constants
 *		and package definitions
 *	  o Fixed problems when parsing if ((char.class.equals(c))) {} - solution by Matt Quail at Cenqua
 *
 * Version 1.22.2 (July 28, 2004)
 *	  Slight refactoring of Java 1.5 language support
 *	  o Refactored for/"foreach" productions so that original literal "for" literal
 *	    is still used but the for sub-clauses vary by token type
 *	  o Fixed bug where type parameter was not included in generic constructor's branch of AST
 *
 * Version 1.22.3 (August 26, 2004)
 *	  Bug fixes as identified by Michael Stahl; clean up of tabs/spaces
 *        and other refactoringsH
 *	  o Fixed typeParameters omission in identPrimary and newStatement
 *	  o Replaced GT reconcilliation code with simple semantic predicate
 *	  o Adapted enum/assert keyword checking support from Michael Stahl's java15 grammar
 *	  o Refactored typeDefinition production and field productions to reduce duplication
 *
 * Version 1.22.4 (October 21, 2004)
 *    Small bux fixes
 *    o Added typeArguments to explicitConstructorInvocation, e.g. new <String>MyParameterised()
 *    o Added typeArguments to postfixExpression productions for anonymous inner class super
 *      constructor invocation, e.g. new Outer().<String>super()
 *    o Fixed bug in array declarations identified by Geoff Roy
 *
 * Version 1.22.5 (January 03, 2005)
 *    Small change to tree structure
 *    o Flattened classOrInterfaceType tree so IDENT no longer has children. TYPE_ARGUMENTS are now
 *      always siblings of IDENT rather than children. Fully.qualified.names trees now
 *      look a little less clean when TYPE_ARGUMENTS are present though.
 *
 * Version 1.22.51 (for MASU) (December 10, 2006)
 *    Fixed to MASU ast analyzer by Koji Taniguchi
 *    o Added FIELD_DEF token which is used field definition of classes and interfaces instead of VARIABLE_DEF.
 *    o Modified to use BLOCK token when parsing '{' instead of SLIST.
 *    o Added NAME token used to declaration's of a name of the class, interface, method, enum and all variables. 
 *    o VARIABLE_DEF was renamed to LOCAL_VARIABLE_DEF
 *    o PARAMETER_DEF token was separated in METHOD_PARAMETER_DEF and LOCAL_PARAMETER_DEF. 
 *    o Line and column registration actions were inserted.
 *    o Modified to a node of CTOR_CALL and SUPER_CTOR_CALL into EXPR node.
 *
 * Version 1.22.52 (for MASU) (February 22, 2007)
 *    Small bux fix
 *    o Fixed bug that a const of HEXADECIMAL float and double could not be parsed.
 *    o Fixed bug that the lexer loops infinitely when an eof is encountered in a line comment.
 * This grammar is in the PUBLIC DOMAIN
 */
public class Java15Parser extends antlr.LLkParser       implements Java15TokenTypes
 {

	/**
	 * Counts the number of LT seen in the typeArguments production.
	 * It is used in semantic predicates to ensure we have seen
	 * enough closing '>' characters; which actually may have been
	 * either GT, SR or BSR tokens.
	 */
	//private int ltCounter = 0;
	
	private boolean useNameTag = true;
	
	private final Stack<Integer> lineStack = new Stack<Integer>();
	private final Stack<Integer> columnStack = new Stack<Integer>();
	
	private PositionManager positionManager = new DefaultPositionManager();
	
	private Java15Lexer lexer;

	public Java15Parser(Java15Lexer lexer){
		this(lexer,2);
		this.lexer = lexer;
	}
	
	public PositionManager getPositionManger(){
		return positionManager;
	}
	
	public void setPositionManager(PositionManager manager){
		this.positionManager = manager;
	}
	
	private void pushStartLineColumn() throws TokenStreamException {
		if (null != lexer){
		    Token token = LT(1);
    		lineStack.push(token.getLine());
   // 		System.out.println(token.getLine() + " ");
    		columnStack.push(token.getColumn());
	//		System.out.println(token.getColumn() + " ");
		}	
	}
	
	private void registLineColumn(AST node) throws TokenStreamException {
		if (null != lexer && !lineStack.isEmpty() && !columnStack.isEmpty()){
		    final Token lastToken = LT(0);
		    final int fromLine = this.lineStack.pop();
		    final int fromColumn = this.columnStack.pop();
		    final int toLine = lastToken.getLine();
            final int toColumn = lastToken.getColumn() + lastToken.getText().length();

    		if(node instanceof CommonASTWithLineNumber) {
    		    ((CommonASTWithLineNumber) node).setPosition(fromLine, fromColumn, toLine, toColumn);
    		}
		}
	}
	
	private void registLineColumn(AST node, int toLine, int toColumn) throws TokenStreamException {
		if (null != lexer && !lineStack.isEmpty() && !columnStack.isEmpty()){
		    final int fromLine = this.lineStack.pop();
		    final int fromColumn = this.columnStack.pop();
    		if(node instanceof CommonASTWithLineNumber) {
    		    ((CommonASTWithLineNumber) node).setPosition(fromLine, fromColumn, toLine, toColumn);
    		}
		}
	}

protected Java15Parser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public Java15Parser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected Java15Parser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public Java15Parser(TokenStream lexer) {
  this(lexer,2);
}

public Java15Parser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void compilationUnit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compilationUnit_AST = null;
		
		{
		boolean synPredMatched1369 = false;
		if (((LA(1)==LITERAL_package||LA(1)==AT) && (LA(2)==IDENT))) {
			int _m1369 = mark();
			synPredMatched1369 = true;
			inputState.guessing++;
			try {
				{
				annotations();
				match(LITERAL_package);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1369 = false;
			}
			rewind(_m1369);
inputState.guessing--;
		}
		if ( synPredMatched1369 ) {
			packageDefinition();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		{
		_loop1371:
		do {
			if ((LA(1)==LITERAL_import)) {
				importDefinition();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1371;
			}
			
		} while (true);
		}
		{
		_loop1373:
		do {
			if ((_tokenSet_2.member(LA(1)))) {
				typeDefinition();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1373;
			}
			
		} while (true);
		}
		match(Token.EOF_TYPE);
		compilationUnit_AST = (AST)currentAST.root;
		returnAST = compilationUnit_AST;
	}
	
	public final void annotations() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotations_AST = null;
		
		{
		_loop1433:
		do {
			if ((LA(1)==AT)) {
				annotation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1433;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			annotations_AST = (AST)currentAST.root;
			annotations_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ANNOTATIONS,"ANNOTATIONS")).add(annotations_AST));
			currentAST.root = annotations_AST;
			currentAST.child = annotations_AST!=null &&annotations_AST.getFirstChild()!=null ?
				annotations_AST.getFirstChild() : annotations_AST;
			currentAST.advanceChildToEnd();
		}
		annotations_AST = (AST)currentAST.root;
		returnAST = annotations_AST;
	}
	
	public final void packageDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST packageDefinition_AST = null;
		Token  p = null;
		AST p_AST = null;
		
		try {      // for error handling
			annotations();
			astFactory.addASTChild(currentAST, returnAST);
			p = LT(1);
			p_AST = astFactory.create(p);
			astFactory.makeASTRoot(currentAST, p_AST);
			match(LITERAL_package);
			if ( inputState.guessing==0 ) {
				p_AST.setType(PACKAGE_DEF);
			}
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMI);
			packageDefinition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
		returnAST = packageDefinition_AST;
	}
	
	public final void importDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST importDefinition_AST = null;
		AST h1_AST = null;
		AST h2_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		boolean synPredMatched1378 = false;
		if (((LA(1)==LITERAL_import) && (LA(2)==LITERAL_static))) {
			int _m1378 = mark();
			synPredMatched1378 = true;
			inputState.guessing++;
			try {
				{
				match(LITERAL_import);
				match(LITERAL_static);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1378 = false;
			}
			rewind(_m1378);
inputState.guessing--;
		}
		if ( synPredMatched1378 ) {
			memberImportDefinition();
			h1_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((LA(1)==LITERAL_import) && (LA(2)==IDENT)) {
			classImportDefinition();
			h2_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			importDefinition_AST = (AST)currentAST.root;
			
						if(null != h1_AST){
							importDefinition_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MEMBER_IMPORT,"import")).add(h1_AST));
						}else{
							importDefinition_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CLASS_IMPORT,"import")).add(h2_AST));
						}
						registLineColumn(importDefinition_AST);
					
			currentAST.root = importDefinition_AST;
			currentAST.child = importDefinition_AST!=null &&importDefinition_AST.getFirstChild()!=null ?
				importDefinition_AST.getFirstChild() : importDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		importDefinition_AST = (AST)currentAST.root;
		returnAST = importDefinition_AST;
	}
	
	public final void typeDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDefinition_AST = null;
		AST m_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				if ( inputState.guessing==0 ) {
					pushStartLineColumn();
				}
				modifiers();
				m_AST = (AST)returnAST;
				typeDefinitionInternal(m_AST);
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					typeDefinition_AST = (AST)currentAST.root;
					registLineColumn(typeDefinition_AST);
				}
				typeDefinition_AST = (AST)currentAST.root;
				break;
			}
			case SEMI:
			{
				match(SEMI);
				typeDefinition_AST = (AST)currentAST.root;
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
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
		returnAST = typeDefinition_AST;
	}
	
	public final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;
		
		AST tmp4_AST = null;
		tmp4_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp4_AST);
		match(IDENT);
		{
		_loop1419:
		do {
			if ((LA(1)==DOT)) {
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp5_AST);
				match(DOT);
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(IDENT);
			}
			else {
				break _loop1419;
			}
			
		} while (true);
		}
		identifier_AST = (AST)currentAST.root;
		returnAST = identifier_AST;
	}
	
	public final void memberImportDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST memberImportDefinition_AST = null;
		Token  i = null;
		AST i_AST = null;
		Token  s = null;
		AST s_AST = null;
		
		i = LT(1);
		i_AST = astFactory.create(i);
		astFactory.makeASTRoot(currentAST, i_AST);
		match(LITERAL_import);
		s = LT(1);
		s_AST = astFactory.create(s);
		match(LITERAL_static);
		identifierStar();
		astFactory.addASTChild(currentAST, returnAST);
		match(SEMI);
		memberImportDefinition_AST = (AST)currentAST.root;
		returnAST = memberImportDefinition_AST;
	}
	
	public final void classImportDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classImportDefinition_AST = null;
		Token  i = null;
		AST i_AST = null;
		
		i = LT(1);
		i_AST = astFactory.create(i);
		astFactory.makeASTRoot(currentAST, i_AST);
		match(LITERAL_import);
		identifierStar();
		astFactory.addASTChild(currentAST, returnAST);
		match(SEMI);
		classImportDefinition_AST = (AST)currentAST.root;
		returnAST = classImportDefinition_AST;
	}
	
	public final void identifierStar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifierStar_AST = null;
		
		AST tmp9_AST = null;
		tmp9_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp9_AST);
		match(IDENT);
		{
		_loop1422:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT)) {
				AST tmp10_AST = null;
				tmp10_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp10_AST);
				match(DOT);
				AST tmp11_AST = null;
				tmp11_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(IDENT);
			}
			else {
				break _loop1422;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case DOT:
		{
			AST tmp12_AST = null;
			tmp12_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp12_AST);
			match(DOT);
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp13_AST);
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
		identifierStar_AST = (AST)currentAST.root;
		returnAST = identifierStar_AST;
	}
	
	public final void modifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifiers_AST = null;
		
		{
		_loop1426:
		do {
			if ((_tokenSet_4.member(LA(1)))) {
				modifier();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if (((LA(1)==AT) && (LA(2)==IDENT))&&(LA(1)==AT && !LT(2).getText().equals("interface"))) {
				annotation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1426;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			modifiers_AST = (AST)currentAST.root;
			modifiers_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MODIFIERS,"MODIFIERS")).add(modifiers_AST));
			currentAST.root = modifiers_AST;
			currentAST.child = modifiers_AST!=null &&modifiers_AST.getFirstChild()!=null ?
				modifiers_AST.getFirstChild() : modifiers_AST;
			currentAST.advanceChildToEnd();
		}
		modifiers_AST = (AST)currentAST.root;
		returnAST = modifiers_AST;
	}
	
	protected final void typeDefinitionInternal(
		AST mods
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeDefinitionInternal_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_class:
		{
			classDefinition(mods);
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_interface:
		{
			interfaceDefinition(mods);
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_enum:
		{
			enumDefinition(mods);
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		case AT:
		{
			annotationDefinition(mods);
			astFactory.addASTChild(currentAST, returnAST);
			typeDefinitionInternal_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeDefinitionInternal_AST;
	}
	
	public final void classDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classDefinition_AST = null;
		AST n_AST = null;
		AST tp_AST = null;
		AST sc_AST = null;
		AST ic_AST = null;
		AST cb_AST = null;
		
		match(LITERAL_class);
		name();
		n_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case LT:
		{
			typeParameters();
			tp_AST = (AST)returnAST;
			break;
		}
		case LITERAL_extends:
		case LCURLY:
		case LITERAL_implements:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		superClassClause();
		sc_AST = (AST)returnAST;
		implementsClause();
		ic_AST = (AST)returnAST;
		classBlock();
		cb_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			classDefinition_AST = (AST)currentAST.root;
			classDefinition_AST = (AST)astFactory.make( (new ASTArray(7)).add(astFactory.create(CLASS_DEF,"CLASS_DEF")).add(modifiers).add(n_AST).add(tp_AST).add(sc_AST).add(ic_AST).add(cb_AST));
			currentAST.root = classDefinition_AST;
			currentAST.child = classDefinition_AST!=null &&classDefinition_AST.getFirstChild()!=null ?
				classDefinition_AST.getFirstChild() : classDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = classDefinition_AST;
	}
	
	public final void interfaceDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceDefinition_AST = null;
		AST n_AST = null;
		AST tp_AST = null;
		AST ie_AST = null;
		AST ib_AST = null;
		
		match(LITERAL_interface);
		name();
		n_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case LT:
		{
			typeParameters();
			tp_AST = (AST)returnAST;
			break;
		}
		case LITERAL_extends:
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
		interfaceExtends();
		ie_AST = (AST)returnAST;
		interfaceBlock();
		ib_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			interfaceDefinition_AST = (AST)currentAST.root;
			interfaceDefinition_AST = (AST)astFactory.make( (new ASTArray(6)).add(astFactory.create(INTERFACE_DEF,"INTERFACE_DEF")).add(modifiers).add(n_AST).add(tp_AST).add(ie_AST).add(ib_AST));
			currentAST.root = interfaceDefinition_AST;
			currentAST.child = interfaceDefinition_AST!=null &&interfaceDefinition_AST.getFirstChild()!=null ?
				interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = interfaceDefinition_AST;
	}
	
	public final void enumDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumDefinition_AST = null;
		AST n_AST = null;
		AST ic_AST = null;
		AST eb_AST = null;
		
		match(LITERAL_enum);
		name();
		n_AST = (AST)returnAST;
		implementsClause();
		ic_AST = (AST)returnAST;
		enumBlock();
		eb_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			enumDefinition_AST = (AST)currentAST.root;
			enumDefinition_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ENUM_DEF,"ENUM_DEF")).add(modifiers).add(n_AST).add(ic_AST).add(eb_AST));
			currentAST.root = enumDefinition_AST;
			currentAST.child = enumDefinition_AST!=null &&enumDefinition_AST.getFirstChild()!=null ?
				enumDefinition_AST.getFirstChild() : enumDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = enumDefinition_AST;
	}
	
	public final void annotationDefinition(
		AST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationDefinition_AST = null;
		AST n_AST = null;
		AST ab_AST = null;
		
		AST tmp17_AST = null;
		tmp17_AST = astFactory.create(LT(1));
		match(AT);
		match(LITERAL_interface);
		name();
		n_AST = (AST)returnAST;
		annotationBlock();
		ab_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			annotationDefinition_AST = (AST)currentAST.root;
			annotationDefinition_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(ANNOTATION_DEF,"ANNOTATION_DEF")).add(modifiers).add(n_AST).add(ab_AST));
			currentAST.root = annotationDefinition_AST;
			currentAST.child = annotationDefinition_AST!=null &&annotationDefinition_AST.getFirstChild()!=null ?
				annotationDefinition_AST.getFirstChild() : annotationDefinition_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = annotationDefinition_AST;
	}
	
	public final void declaration(
		boolean isParameter
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaration_AST = null;
		AST m_AST = null;
		AST t_AST = null;
		AST v_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		modifiers();
		m_AST = (AST)returnAST;
		typeSpec(false);
		t_AST = (AST)returnAST;
		variableDefinitions(m_AST,t_AST,isParameter);
		v_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			declaration_AST = (AST)currentAST.root;
			declaration_AST = v_AST;
			currentAST.root = declaration_AST;
			currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
				declaration_AST.getFirstChild() : declaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			declaration_AST = (AST)currentAST.root;
			registLineColumn(declaration_AST);
		}
		returnAST = declaration_AST;
	}
	
	public final void typeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeSpec_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		{
			classTypeSpec(addImagNode);
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec_AST = (AST)currentAST.root;
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
		{
			builtInTypeSpec(addImagNode);
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeSpec_AST;
	}
	
	public final void variableDefinitions(
		AST mods, AST t, boolean isParameter
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDefinitions_AST = null;
		
		variableDeclarator(getASTFactory().dupTree(mods),
							getASTFactory().dupList(t),isParameter);
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1536:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				variableDeclarator(getASTFactory().dupTree(mods),
							getASTFactory().dupList(t),isParameter);
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1536;
			}
			
		} while (true);
		}
		variableDefinitions_AST = (AST)currentAST.root;
		returnAST = variableDefinitions_AST;
	}
	
	public final void classTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classTypeSpec_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		classOrInterfaceType(false);
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1387:
		do {
			if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(ARRAY_DECLARATOR);
				}
				match(RBRACK);
			}
			else {
				break _loop1387;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			classTypeSpec_AST = (AST)currentAST.root;
			
						if ( addImagNode ) {
							classTypeSpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(classTypeSpec_AST));
						}
						
					
			currentAST.root = classTypeSpec_AST;
			currentAST.child = classTypeSpec_AST!=null &&classTypeSpec_AST.getFirstChild()!=null ?
				classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
			currentAST.advanceChildToEnd();
		}
		classTypeSpec_AST = (AST)currentAST.root;
		returnAST = classTypeSpec_AST;
	}
	
	public final void builtInTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeSpec_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		builtInType();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1413:
		do {
			if ((LA(1)==LBRACK)) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(ARRAY_DECLARATOR);
				}
				match(RBRACK);
			}
			else {
				break _loop1413;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			builtInTypeSpec_AST = (AST)currentAST.root;
			
						if ( addImagNode ) {
							builtInTypeSpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(builtInTypeSpec_AST));
						}
					
			currentAST.root = builtInTypeSpec_AST;
			currentAST.child = builtInTypeSpec_AST!=null &&builtInTypeSpec_AST.getFirstChild()!=null ?
				builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
			currentAST.advanceChildToEnd();
		}
		builtInTypeSpec_AST = (AST)currentAST.root;
		returnAST = builtInTypeSpec_AST;
	}
	
	public final void classOrInterfaceType(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classOrInterfaceType_AST = null;
		
		AST tmp22_AST = null;
		tmp22_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp22_AST);
		match(IDENT);
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case SEMI:
		case LBRACK:
		case RBRACK:
		case IDENT:
		case DOT:
		case QUESTION:
		case LITERAL_extends:
		case LITERAL_super:
		case COMMA:
		case GT:
		case SR:
		case BSR:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case RPAREN:
		case LCURLY:
		case RCURLY:
		case BAND:
		case LITERAL_implements:
		case LITERAL_this:
		case ASSIGN:
		case TRIPLE_DOT:
		case COLON:
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
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case NOT_EQUAL:
		case EQUAL:
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
		_loop1392:
		do {
			if ((LA(1)==DOT) && (LA(2)==IDENT)) {
				AST tmp23_AST = null;
				tmp23_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp23_AST);
				match(DOT);
				AST tmp24_AST = null;
				tmp24_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp24_AST);
				match(IDENT);
				{
				switch ( LA(1)) {
				case LT:
				{
					typeArguments();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				case LBRACK:
				case RBRACK:
				case IDENT:
				case DOT:
				case QUESTION:
				case LITERAL_extends:
				case LITERAL_super:
				case COMMA:
				case GT:
				case SR:
				case BSR:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case BAND:
				case LITERAL_implements:
				case LITERAL_this:
				case ASSIGN:
				case TRIPLE_DOT:
				case COLON:
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
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case NOT_EQUAL:
				case EQUAL:
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
				break _loop1392;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			classOrInterfaceType_AST = (AST)currentAST.root;
			
						if ( addImagNode ) {
							classOrInterfaceType_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(classOrInterfaceType_AST));
						}
					
			currentAST.root = classOrInterfaceType_AST;
			currentAST.child = classOrInterfaceType_AST!=null &&classOrInterfaceType_AST.getFirstChild()!=null ?
				classOrInterfaceType_AST.getFirstChild() : classOrInterfaceType_AST;
			currentAST.advanceChildToEnd();
		}
		classOrInterfaceType_AST = (AST)currentAST.root;
		returnAST = classOrInterfaceType_AST;
	}
	
	public final void typeArguments() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArguments_AST = null;
		
		match(LT);
		typeArgument();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1402:
		do {
			if ((LA(1)==COMMA) && (_tokenSet_5.member(LA(2)))) {
				match(COMMA);
				typeArgument();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1402;
			}
			
		} while (true);
		}
		{
		if (((LA(1) >= GT && LA(1) <= BSR)) && (_tokenSet_6.member(LA(2)))) {
			typeArgumentsOrParametersEnd();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_6.member(LA(1))) && (_tokenSet_7.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			typeArguments_AST = (AST)currentAST.root;
			typeArguments_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_ARGUMENTS,"TYPE_ARGUMENTS")).add(typeArguments_AST));
			currentAST.root = typeArguments_AST;
			currentAST.child = typeArguments_AST!=null &&typeArguments_AST.getFirstChild()!=null ?
				typeArguments_AST.getFirstChild() : typeArguments_AST;
			currentAST.advanceChildToEnd();
		}
		typeArguments_AST = (AST)currentAST.root;
		returnAST = typeArguments_AST;
	}
	
	public final void typeArgumentSpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentSpec_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		{
			classTypeSpec(true);
			astFactory.addASTChild(currentAST, returnAST);
			typeArgumentSpec_AST = (AST)currentAST.root;
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
		{
			builtInTypeArraySpec(true);
			astFactory.addASTChild(currentAST, returnAST);
			typeArgumentSpec_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeArgumentSpec_AST;
	}
	
	public final void builtInTypeArraySpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInTypeArraySpec_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		builtInType();
		astFactory.addASTChild(currentAST, returnAST);
		{
		int _cnt1410=0;
		_loop1410:
		do {
			if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(ARRAY_DECLARATOR);
				}
				match(RBRACK);
			}
			else {
				if ( _cnt1410>=1 ) { break _loop1410; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt1410++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			builtInTypeArraySpec_AST = (AST)currentAST.root;
			
						if ( addImagNode ) {
							builtInTypeArraySpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(builtInTypeArraySpec_AST));
						}
					
			currentAST.root = builtInTypeArraySpec_AST;
			currentAST.child = builtInTypeArraySpec_AST!=null &&builtInTypeArraySpec_AST.getFirstChild()!=null ?
				builtInTypeArraySpec_AST.getFirstChild() : builtInTypeArraySpec_AST;
			currentAST.advanceChildToEnd();
		}
		builtInTypeArraySpec_AST = (AST)currentAST.root;
		returnAST = builtInTypeArraySpec_AST;
	}
	
	public final void typeArgument() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgument_AST = null;
		
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
		{
			typeArgumentSpec();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case QUESTION:
		{
			wildcardType();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			typeArgument_AST = (AST)currentAST.root;
			typeArgument_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_ARGUMENT,"TYPE_ARGUMENT")).add(typeArgument_AST));
			currentAST.root = typeArgument_AST;
			currentAST.child = typeArgument_AST!=null &&typeArgument_AST.getFirstChild()!=null ?
				typeArgument_AST.getFirstChild() : typeArgument_AST;
			currentAST.advanceChildToEnd();
		}
		typeArgument_AST = (AST)currentAST.root;
		returnAST = typeArgument_AST;
	}
	
	public final void wildcardType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST wildcardType_AST = null;
		Token  q = null;
		AST q_AST = null;
		
		q = LT(1);
		q_AST = astFactory.create(q);
		astFactory.makeASTRoot(currentAST, q_AST);
		match(QUESTION);
		if ( inputState.guessing==0 ) {
			q_AST.setType(WILDCARD_TYPE);
		}
		{
		boolean synPredMatched1399 = false;
		if (((LA(1)==LITERAL_extends||LA(1)==LITERAL_super) && (LA(2)==IDENT))) {
			int _m1399 = mark();
			synPredMatched1399 = true;
			inputState.guessing++;
			try {
				{
				switch ( LA(1)) {
				case LITERAL_extends:
				{
					match(LITERAL_extends);
					break;
				}
				case LITERAL_super:
				{
					match(LITERAL_super);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1399 = false;
			}
			rewind(_m1399);
inputState.guessing--;
		}
		if ( synPredMatched1399 ) {
			typeArgumentBounds();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_6.member(LA(1))) && (_tokenSet_7.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		wildcardType_AST = (AST)currentAST.root;
		returnAST = wildcardType_AST;
	}
	
	public final void typeArgumentBounds() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentBounds_AST = null;
		boolean isUpperBounds = false;
		
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			match(LITERAL_extends);
			if ( inputState.guessing==0 ) {
				isUpperBounds=true;
			}
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		classTypeSpec(true);
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			typeArgumentBounds_AST = (AST)currentAST.root;
			
						if (isUpperBounds)
						{
							typeArgumentBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_UPPER_BOUNDS,"TYPE_UPPER_BOUNDS")).add(typeArgumentBounds_AST));
						}
						else
						{
							typeArgumentBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_LOWER_BOUNDS,"TYPE_LOWER_BOUNDS")).add(typeArgumentBounds_AST));
						}
					
			currentAST.root = typeArgumentBounds_AST;
			currentAST.child = typeArgumentBounds_AST!=null &&typeArgumentBounds_AST.getFirstChild()!=null ?
				typeArgumentBounds_AST.getFirstChild() : typeArgumentBounds_AST;
			currentAST.advanceChildToEnd();
		}
		typeArgumentBounds_AST = (AST)currentAST.root;
		returnAST = typeArgumentBounds_AST;
	}
	
	protected final void typeArgumentsOrParametersEnd() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeArgumentsOrParametersEnd_AST = null;
		
				int mark = this.mark();
				int gtCount = 0; 
			
		
		{
		switch ( LA(1)) {
		case GT:
		{
			match(GT);
			if ( inputState.guessing==0 ) {
				gtCount = 1;
			}
			break;
		}
		case SR:
		{
			match(SR);
			if ( inputState.guessing==0 ) {
				gtCount = 2;
			}
			break;
		}
		case BSR:
		{
			match(BSR);
			if ( inputState.guessing==0 ) {
				gtCount = 3;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			
					
					Token nextToken;
					switch (gtCount) {
						case 1:
							break;
						case 2:
							this.rewind(mark);
							nextToken = this.LT(1);
							nextToken.setType(GT);
							nextToken.setText(">");
							break;
						case 3:
							this.rewind(mark);
							nextToken = this.LT(1);
							nextToken.setType(SR);
							nextToken.setText(">>");
							break;
						default :
							break;
					}
				
		}
		typeArgumentsOrParametersEnd_AST = (AST)currentAST.root;
		returnAST = typeArgumentsOrParametersEnd_AST;
	}
	
	public final void builtInType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST builtInType_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_void:
		{
			AST tmp33_AST = null;
			tmp33_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp33_AST);
			match(LITERAL_void);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_boolean:
		{
			AST tmp34_AST = null;
			tmp34_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp34_AST);
			match(LITERAL_boolean);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_byte:
		{
			AST tmp35_AST = null;
			tmp35_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp35_AST);
			match(LITERAL_byte);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_char:
		{
			AST tmp36_AST = null;
			tmp36_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp36_AST);
			match(LITERAL_char);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_short:
		{
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(LITERAL_short);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_int:
		{
			AST tmp38_AST = null;
			tmp38_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp38_AST);
			match(LITERAL_int);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_float:
		{
			AST tmp39_AST = null;
			tmp39_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp39_AST);
			match(LITERAL_float);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_long:
		{
			AST tmp40_AST = null;
			tmp40_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(LITERAL_long);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_double:
		{
			AST tmp41_AST = null;
			tmp41_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
			match(LITERAL_double);
			builtInType_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = builtInType_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;
		
		{
		switch ( LA(1)) {
		case IDENT:
		{
			classOrInterfaceType(true);
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
		{
			builtInType();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			type_AST = (AST)currentAST.root;
			type_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(type_AST));
			currentAST.root = type_AST;
			currentAST.child = type_AST!=null &&type_AST.getFirstChild()!=null ?
				type_AST.getFirstChild() : type_AST;
			currentAST.advanceChildToEnd();
		}
		type_AST = (AST)currentAST.root;
		returnAST = type_AST;
	}
	
	public final void modifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST modifier_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_private:
		{
			AST tmp42_AST = null;
			tmp42_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp42_AST);
			match(LITERAL_private);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_public:
		{
			AST tmp43_AST = null;
			tmp43_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp43_AST);
			match(LITERAL_public);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_protected:
		{
			AST tmp44_AST = null;
			tmp44_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp44_AST);
			match(LITERAL_protected);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp45_AST);
			match(LITERAL_static);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_transient:
		{
			AST tmp46_AST = null;
			tmp46_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp46_AST);
			match(LITERAL_transient);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case FINAL:
		{
			AST tmp47_AST = null;
			tmp47_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp47_AST);
			match(FINAL);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case ABSTRACT:
		{
			AST tmp48_AST = null;
			tmp48_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(ABSTRACT);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_native:
		{
			AST tmp49_AST = null;
			tmp49_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp49_AST);
			match(LITERAL_native);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_threadsafe:
		{
			AST tmp50_AST = null;
			tmp50_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp50_AST);
			match(LITERAL_threadsafe);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_synchronized:
		{
			AST tmp51_AST = null;
			tmp51_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp51_AST);
			match(LITERAL_synchronized);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_volatile:
		{
			AST tmp52_AST = null;
			tmp52_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(LITERAL_volatile);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		case STRICTFP:
		{
			AST tmp53_AST = null;
			tmp53_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp53_AST);
			match(STRICTFP);
			modifier_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = modifier_AST;
	}
	
	public final void annotation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotation_AST = null;
		AST i_AST = null;
		AST args_AST = null;
		
		match(AT);
		identifier();
		i_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			{
			if (((LA(1) >= BLOCK && LA(1) <= FLOAT_SUFFIX)) && ((LA(2) >= BLOCK && LA(2) <= FLOAT_SUFFIX))) {
				annotationString();
				args_AST = (AST)returnAST;
			}
			else if ((LA(1)==RPAREN) && (_tokenSet_8.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(RPAREN);
			break;
		}
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_package:
		case SEMI:
		case LITERAL_static:
		case IDENT:
		case LT:
		case COMMA:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case AT:
		case RCURLY:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
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
			annotation_AST = (AST)currentAST.root;
			annotation_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(ANNOTATION,"ANNOTATION")).add(i_AST).add(args_AST));
			currentAST.root = annotation_AST;
			currentAST.child = annotation_AST!=null &&annotation_AST.getFirstChild()!=null ?
				annotation_AST.getFirstChild() : annotation_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = annotation_AST;
	}
	
	public final void annotationString() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationString_AST = null;
		
		{
		_loop1443:
		do {
			if ((LA(1)==CHAR_LITERAL) && ((LA(2) >= BLOCK && LA(2) <= FLOAT_SUFFIX))) {
				AST tmp57_AST = null;
				tmp57_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp57_AST);
				match(CHAR_LITERAL);
			}
			else if ((LA(1)==STRING_LITERAL) && ((LA(2) >= BLOCK && LA(2) <= FLOAT_SUFFIX))) {
				AST tmp58_AST = null;
				tmp58_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(STRING_LITERAL);
			}
			else if ((LA(1)==AT) && (LA(2)==IDENT)) {
				innerAnnotation();
				astFactory.addASTChild(currentAST, returnAST);
				annotationString();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double))) {
				annotationCast();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_9.member(LA(1))) && ((LA(2) >= BLOCK && LA(2) <= FLOAT_SUFFIX))) {
				AST tmp59_AST = null;
				tmp59_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp59_AST);
				matchNot(RPAREN);
			}
			else {
				break _loop1443;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			annotationString_AST = (AST)currentAST.root;
			annotationString_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ANNOTATION_STRING,"ANNOTATION_STRING")).add(annotationString_AST));
			currentAST.root = annotationString_AST;
			currentAST.child = annotationString_AST!=null &&annotationString_AST.getFirstChild()!=null ?
				annotationString_AST.getFirstChild() : annotationString_AST;
			currentAST.advanceChildToEnd();
		}
		annotationString_AST = (AST)currentAST.root;
		returnAST = annotationString_AST;
	}
	
	public final void annotationMemberValueInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberValueInitializer_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			if ( inputState.guessing==0 ) {
				annotationMemberValueInitializer_AST = (AST)currentAST.root;
				annotationMemberValueInitializer_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ANNOTATION_MEMBER,"ANNOTATION_MEMBER")).add(annotationMemberValueInitializer_AST));
				currentAST.root = annotationMemberValueInitializer_AST;
				currentAST.child = annotationMemberValueInitializer_AST!=null &&annotationMemberValueInitializer_AST.getFirstChild()!=null ?
					annotationMemberValueInitializer_AST.getFirstChild() : annotationMemberValueInitializer_AST;
				currentAST.advanceChildToEnd();
			}
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		case AT:
		{
			annotation();
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		case LCURLY:
		{
			annotationMemberArrayInitializer();
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = annotationMemberValueInitializer_AST;
	}
	
	public final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalExpression_AST = null;
		
		logicalOrExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case QUESTION:
		{
			AST tmp60_AST = null;
			tmp60_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp60_AST);
			match(QUESTION);
			assignmentExpression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COLON);
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case SEMI:
		case RBRACK:
		case COMMA:
		case RPAREN:
		case RCURLY:
		case ASSIGN:
		case COLON:
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
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		conditionalExpression_AST = (AST)currentAST.root;
		returnAST = conditionalExpression_AST;
	}
	
	public final void annotationMemberArrayInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberArrayInitializer_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		
		lc = LT(1);
		lc_AST = astFactory.create(lc);
		astFactory.makeASTRoot(currentAST, lc_AST);
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			lc_AST.setType(ANNOTATION_ARRAY_INIT);
		}
		{
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case AT:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			annotationMemberArrayValueInitializer();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1438:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_10.member(LA(2)))) {
					match(COMMA);
					annotationMemberArrayValueInitializer();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1438;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
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
		match(RCURLY);
		annotationMemberArrayInitializer_AST = (AST)currentAST.root;
		returnAST = annotationMemberArrayInitializer_AST;
	}
	
	public final void annotationMemberArrayValueInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationMemberArrayValueInitializer_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		case AT:
		{
			annotation();
			astFactory.addASTChild(currentAST, returnAST);
			annotationMemberArrayValueInitializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = annotationMemberArrayValueInitializer_AST;
	}
	
	public final void innerAnnotation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST innerAnnotation_AST = null;
		
		AST tmp65_AST = null;
		tmp65_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp65_AST);
		match(AT);
		identifier();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp66_AST = null;
		tmp66_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp66_AST);
		match(LPAREN);
		annotationString();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp67_AST = null;
		tmp67_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp67_AST);
		match(RPAREN);
		innerAnnotation_AST = (AST)currentAST.root;
		returnAST = innerAnnotation_AST;
	}
	
	public final void annotationCast() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationCast_AST = null;
		
		AST tmp68_AST = null;
		tmp68_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp68_AST);
		match(LPAREN);
		builtInType();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp69_AST = null;
		tmp69_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp69_AST);
		match(RPAREN);
		annotationCast_AST = (AST)currentAST.root;
		returnAST = annotationCast_AST;
	}
	
	public final void superClassClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superClassClause_AST = null;
		AST c_AST = null;
		
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			match(LITERAL_extends);
			classOrInterfaceType(true);
			c_AST = (AST)returnAST;
			break;
		}
		case LCURLY:
		case LITERAL_implements:
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
			superClassClause_AST = (AST)currentAST.root;
			superClassClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(c_AST));
			currentAST.root = superClassClause_AST;
			currentAST.child = superClassClause_AST!=null &&superClassClause_AST.getFirstChild()!=null ?
				superClassClause_AST.getFirstChild() : superClassClause_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = superClassClause_AST;
	}
	
	public final void name() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST name_AST = null;
		
		AST tmp71_AST = null;
		tmp71_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp71_AST);
		match(IDENT);
		if ( inputState.guessing==0 ) {
			name_AST = (AST)currentAST.root;
			
			if ( useNameTag ){
				        {name_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(NAME,"NAME")).add(name_AST));}
			}
				
			currentAST.root = name_AST;
			currentAST.child = name_AST!=null &&name_AST.getFirstChild()!=null ?
				name_AST.getFirstChild() : name_AST;
			currentAST.advanceChildToEnd();
		}
		name_AST = (AST)currentAST.root;
		returnAST = name_AST;
	}
	
	public final void typeParameters() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameters_AST = null;
		
		match(LT);
		typeParameter();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1457:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				typeParameter();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1457;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case GT:
		case SR:
		case BSR:
		{
			typeArgumentsOrParametersEnd();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case IDENT:
		case LITERAL_extends:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LCURLY:
		case LITERAL_implements:
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
			typeParameters_AST = (AST)currentAST.root;
			typeParameters_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_PARAMETERS,"TYPE_PARAMETERS")).add(typeParameters_AST));
			currentAST.root = typeParameters_AST;
			currentAST.child = typeParameters_AST!=null &&typeParameters_AST.getFirstChild()!=null ?
				typeParameters_AST.getFirstChild() : typeParameters_AST;
			currentAST.advanceChildToEnd();
		}
		typeParameters_AST = (AST)currentAST.root;
		returnAST = typeParameters_AST;
	}
	
	public final void implementsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implementsClause_AST = null;
		Token  i = null;
		AST i_AST = null;
		
		{
		switch ( LA(1)) {
		case LITERAL_implements:
		{
			i = LT(1);
			i_AST = astFactory.create(i);
			match(LITERAL_implements);
			classOrInterfaceType(true);
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1508:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					classOrInterfaceType(true);
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1508;
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
			implementsClause_AST = (AST)currentAST.root;
			implementsClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE")).add(implementsClause_AST));
			currentAST.root = implementsClause_AST;
			currentAST.child = implementsClause_AST!=null &&implementsClause_AST.getFirstChild()!=null ?
				implementsClause_AST.getFirstChild() : implementsClause_AST;
			currentAST.advanceChildToEnd();
		}
		implementsClause_AST = (AST)currentAST.root;
		returnAST = implementsClause_AST;
	}
	
	public final void classBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classBlock_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		match(LCURLY);
		{
		_loop1468:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case IDENT:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case AT:
			case LCURLY:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				classField();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				break _loop1468;
			}
			}
		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			classBlock_AST = (AST)currentAST.root;
			classBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(classBlock_AST));
			currentAST.root = classBlock_AST;
			currentAST.child = classBlock_AST!=null &&classBlock_AST.getFirstChild()!=null ?
				classBlock_AST.getFirstChild() : classBlock_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			classBlock_AST = (AST)currentAST.root;
			registLineColumn(classBlock_AST);
		}
		classBlock_AST = (AST)currentAST.root;
		returnAST = classBlock_AST;
	}
	
	public final void interfaceExtends() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceExtends_AST = null;
		Token  e = null;
		AST e_AST = null;
		
		{
		switch ( LA(1)) {
		case LITERAL_extends:
		{
			e = LT(1);
			e_AST = astFactory.create(e);
			match(LITERAL_extends);
			classOrInterfaceType(true);
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1504:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					classOrInterfaceType(true);
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1504;
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
			interfaceExtends_AST = (AST)currentAST.root;
			interfaceExtends_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXTENDS_CLAUSE,"EXTENDS_CLAUSE")).add(interfaceExtends_AST));
			currentAST.root = interfaceExtends_AST;
			currentAST.child = interfaceExtends_AST!=null &&interfaceExtends_AST.getFirstChild()!=null ?
				interfaceExtends_AST.getFirstChild() : interfaceExtends_AST;
			currentAST.advanceChildToEnd();
		}
		interfaceExtends_AST = (AST)currentAST.root;
		returnAST = interfaceExtends_AST;
	}
	
	public final void interfaceBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceBlock_AST = null;
		
		match(LCURLY);
		{
		_loop1471:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case IDENT:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				interfaceField();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				break _loop1471;
			}
			}
		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			interfaceBlock_AST = (AST)currentAST.root;
			interfaceBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(interfaceBlock_AST));
			currentAST.root = interfaceBlock_AST;
			currentAST.child = interfaceBlock_AST!=null &&interfaceBlock_AST.getFirstChild()!=null ?
				interfaceBlock_AST.getFirstChild() : interfaceBlock_AST;
			currentAST.advanceChildToEnd();
		}
		interfaceBlock_AST = (AST)currentAST.root;
		returnAST = interfaceBlock_AST;
	}
	
	public final void enumBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumBlock_AST = null;
		
		match(LCURLY);
		{
		switch ( LA(1)) {
		case IDENT:
		case AT:
		{
			enumConstant();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1478:
			do {
				if ((LA(1)==COMMA) && (LA(2)==IDENT||LA(2)==AT)) {
					match(COMMA);
					enumConstant();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1478;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case SEMI:
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
		case SEMI:
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
		{
		switch ( LA(1)) {
		case SEMI:
		{
			match(SEMI);
			{
			_loop1482:
			do {
				switch ( LA(1)) {
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_static:
				case IDENT:
				case LT:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_threadsafe:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case AT:
				case LCURLY:
				case LITERAL_class:
				case LITERAL_interface:
				case LITERAL_enum:
				{
					classField();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					match(SEMI);
					break;
				}
				default:
				{
					break _loop1482;
				}
				}
			} while (true);
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
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			enumBlock_AST = (AST)currentAST.root;
			enumBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(enumBlock_AST));
			currentAST.root = enumBlock_AST;
			currentAST.child = enumBlock_AST!=null &&enumBlock_AST.getFirstChild()!=null ?
				enumBlock_AST.getFirstChild() : enumBlock_AST;
			currentAST.advanceChildToEnd();
		}
		enumBlock_AST = (AST)currentAST.root;
		returnAST = enumBlock_AST;
	}
	
	public final void annotationBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationBlock_AST = null;
		
		match(LCURLY);
		{
		_loop1474:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
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
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				annotationField();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				break _loop1474;
			}
			}
		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			annotationBlock_AST = (AST)currentAST.root;
			annotationBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(annotationBlock_AST));
			currentAST.root = annotationBlock_AST;
			currentAST.child = annotationBlock_AST!=null &&annotationBlock_AST.getFirstChild()!=null ?
				annotationBlock_AST.getFirstChild() : annotationBlock_AST;
			currentAST.advanceChildToEnd();
		}
		annotationBlock_AST = (AST)currentAST.root;
		returnAST = annotationBlock_AST;
	}
	
	public final void typeParameter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameter_AST = null;
		AST id_AST = null;
		
		{
		name();
		id_AST = (AST)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		}
		{
		if ((LA(1)==LITERAL_extends) && (LA(2)==IDENT)) {
			typeParameterBounds();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_11.member(LA(1))) && (_tokenSet_12.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			typeParameter_AST = (AST)currentAST.root;
			typeParameter_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_PARAMETER,"TYPE_PARAMETER")).add(typeParameter_AST));
			currentAST.root = typeParameter_AST;
			currentAST.child = typeParameter_AST!=null &&typeParameter_AST.getFirstChild()!=null ?
				typeParameter_AST.getFirstChild() : typeParameter_AST;
			currentAST.advanceChildToEnd();
		}
		typeParameter_AST = (AST)currentAST.root;
		returnAST = typeParameter_AST;
	}
	
	public final void typeParameterBounds() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeParameterBounds_AST = null;
		
		match(LITERAL_extends);
		classOrInterfaceType(true);
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1464:
		do {
			if ((LA(1)==BAND)) {
				typeAdditionalBounds();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1464;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			typeParameterBounds_AST = (AST)currentAST.root;
			typeParameterBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_UPPER_BOUNDS,"TYPE_UPPER_BOUNDS")).add(typeParameterBounds_AST));
			currentAST.root = typeParameterBounds_AST;
			currentAST.child = typeParameterBounds_AST!=null &&typeParameterBounds_AST.getFirstChild()!=null ?
				typeParameterBounds_AST.getFirstChild() : typeParameterBounds_AST;
			currentAST.advanceChildToEnd();
		}
		typeParameterBounds_AST = (AST)currentAST.root;
		returnAST = typeParameterBounds_AST;
	}
	
	public final void typeAdditionalBounds() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typeAdditionalBounds_AST = null;
		
		match(BAND);
		classOrInterfaceType(true);
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			typeAdditionalBounds_AST = (AST)currentAST.root;
			typeAdditionalBounds_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE_ADDITIONAL_BOUNDS,"TYPE_ADDITIONAL_BOUNDS")).add(typeAdditionalBounds_AST));
			currentAST.root = typeAdditionalBounds_AST;
			currentAST.child = typeAdditionalBounds_AST!=null &&typeAdditionalBounds_AST.getFirstChild()!=null ?
				typeAdditionalBounds_AST.getFirstChild() : typeAdditionalBounds_AST;
			currentAST.advanceChildToEnd();
		}
		typeAdditionalBounds_AST = (AST)currentAST.root;
		returnAST = typeAdditionalBounds_AST;
	}
	
	public final void classField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST tp_AST = null;
		AST h_AST = null;
		AST s_AST = null;
		AST t_AST = null;
		AST n_AST = null;
		AST param_AST = null;
		AST rt_AST = null;
		AST tc_AST = null;
		AST s2_AST = null;
		AST f_AST = null;
		AST s3_AST = null;
		AST s4_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		if ((_tokenSet_13.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
			modifiers();
			mods_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				typeDefinitionInternal(mods_AST);
				td_AST = (AST)returnAST;
				if ( inputState.guessing==0 ) {
					classField_AST = (AST)currentAST.root;
					classField_AST = td_AST;
					currentAST.root = classField_AST;
					currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
						classField_AST.getFirstChild() : classField_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			case IDENT:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				{
				switch ( LA(1)) {
				case LT:
				{
					typeParameters();
					tp_AST = (AST)returnAST;
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
				if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
					ctorHead();
					h_AST = (AST)returnAST;
					constructorBody();
					s_AST = (AST)returnAST;
					if ( inputState.guessing==0 ) {
						classField_AST = (AST)currentAST.root;
						classField_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(CTOR_DEF,"CTOR_DEF")).add(mods_AST).add(tp_AST).add(h_AST).add(s_AST));
						currentAST.root = classField_AST;
						currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
							classField_AST.getFirstChild() : classField_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_16.member(LA(2)))) {
					typeSpec(false);
					t_AST = (AST)returnAST;
					{
					if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
						name();
						n_AST = (AST)returnAST;
						match(LPAREN);
						parameterDeclarationList();
						param_AST = (AST)returnAST;
						match(RPAREN);
						declaratorBrackets(t_AST);
						rt_AST = (AST)returnAST;
						{
						switch ( LA(1)) {
						case LITERAL_throws:
						{
							throwsClause();
							tc_AST = (AST)returnAST;
							break;
						}
						case SEMI:
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
						{
						switch ( LA(1)) {
						case LCURLY:
						{
							compoundStatement();
							s2_AST = (AST)returnAST;
							break;
						}
						case SEMI:
						{
							AST tmp95_AST = null;
							tmp95_AST = astFactory.create(LT(1));
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
							classField_AST = (AST)currentAST.root;
							classField_AST = (AST)astFactory.make( (new ASTArray(8)).add(astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods_AST).add(tp_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(rt_AST))).add(n_AST).add(param_AST).add(tc_AST).add(s2_AST));
							currentAST.root = classField_AST;
							currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
								classField_AST.getFirstChild() : classField_AST;
							currentAST.advanceChildToEnd();
						}
					}
					else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
						fieldDefinitions(mods_AST,t_AST);
						f_AST = (AST)returnAST;
						AST tmp96_AST = null;
						tmp96_AST = astFactory.create(LT(1));
						match(SEMI);
						if ( inputState.guessing==0 ) {
							classField_AST = (AST)currentAST.root;
							classField_AST = f_AST;
							currentAST.root = classField_AST;
							currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
								classField_AST.getFirstChild() : classField_AST;
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
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
			match(LITERAL_static);
			compoundStatement();
			s3_AST = (AST)returnAST;
			if ( inputState.guessing==0 ) {
				classField_AST = (AST)currentAST.root;
				classField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(STATIC_INIT,"STATIC_INIT")).add(s3_AST));
				currentAST.root = classField_AST;
				currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
					classField_AST.getFirstChild() : classField_AST;
				currentAST.advanceChildToEnd();
			}
		}
		else if ((LA(1)==LCURLY)) {
			compoundStatement();
			s4_AST = (AST)returnAST;
			if ( inputState.guessing==0 ) {
				classField_AST = (AST)currentAST.root;
				classField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(s4_AST));
				currentAST.root = classField_AST;
				currentAST.child = classField_AST!=null &&classField_AST.getFirstChild()!=null ?
					classField_AST.getFirstChild() : classField_AST;
				currentAST.advanceChildToEnd();
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			classField_AST = (AST)currentAST.root;
			registLineColumn(classField_AST);
		}
		returnAST = classField_AST;
	}
	
	public final void interfaceField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interfaceField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST tp_AST = null;
		AST t_AST = null;
		AST n_AST = null;
		AST param_AST = null;
		AST rt_AST = null;
		AST tc_AST = null;
		AST f_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		modifiers();
		mods_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case AT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		{
			typeDefinitionInternal(mods_AST);
			td_AST = (AST)returnAST;
			if ( inputState.guessing==0 ) {
				interfaceField_AST = (AST)currentAST.root;
				interfaceField_AST = td_AST;
				currentAST.root = interfaceField_AST;
				currentAST.child = interfaceField_AST!=null &&interfaceField_AST.getFirstChild()!=null ?
					interfaceField_AST.getFirstChild() : interfaceField_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case IDENT:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		{
			{
			switch ( LA(1)) {
			case LT:
			{
				typeParameters();
				tp_AST = (AST)returnAST;
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
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			typeSpec(false);
			t_AST = (AST)returnAST;
			{
			if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
				name();
				n_AST = (AST)returnAST;
				match(LPAREN);
				parameterDeclarationList();
				param_AST = (AST)returnAST;
				match(RPAREN);
				declaratorBrackets(t_AST);
				rt_AST = (AST)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_throws:
				{
					throwsClause();
					tc_AST = (AST)returnAST;
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
				AST tmp100_AST = null;
				tmp100_AST = astFactory.create(LT(1));
				match(SEMI);
				if ( inputState.guessing==0 ) {
					interfaceField_AST = (AST)currentAST.root;
					interfaceField_AST = (AST)astFactory.make( (new ASTArray(7)).add(astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods_AST).add(tp_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(rt_AST))).add(n_AST).add(param_AST).add(tc_AST));
					currentAST.root = interfaceField_AST;
					currentAST.child = interfaceField_AST!=null &&interfaceField_AST.getFirstChild()!=null ?
						interfaceField_AST.getFirstChild() : interfaceField_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
				fieldDefinitions(mods_AST,t_AST);
				f_AST = (AST)returnAST;
				AST tmp101_AST = null;
				tmp101_AST = astFactory.create(LT(1));
				match(SEMI);
				if ( inputState.guessing==0 ) {
					interfaceField_AST = (AST)currentAST.root;
					interfaceField_AST = f_AST;
					currentAST.root = interfaceField_AST;
					currentAST.child = interfaceField_AST!=null &&interfaceField_AST.getFirstChild()!=null ?
						interfaceField_AST.getFirstChild() : interfaceField_AST;
					currentAST.advanceChildToEnd();
				}
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
		}
		if ( inputState.guessing==0 ) {
			interfaceField_AST = (AST)currentAST.root;
			registLineColumn(interfaceField_AST);
		}
		returnAST = interfaceField_AST;
	}
	
	public final void annotationField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST annotationField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST t_AST = null;
		AST i_AST = null;
		AST rt_AST = null;
		AST amvi_AST = null;
		AST v_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		modifiers();
		mods_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case AT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		{
			typeDefinitionInternal(mods_AST);
			td_AST = (AST)returnAST;
			if ( inputState.guessing==0 ) {
				annotationField_AST = (AST)currentAST.root;
				annotationField_AST = td_AST;
				currentAST.root = annotationField_AST;
				currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
					annotationField_AST.getFirstChild() : annotationField_AST;
				currentAST.advanceChildToEnd();
			}
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
		{
			typeSpec(false);
			t_AST = (AST)returnAST;
			{
			if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
				name();
				i_AST = (AST)returnAST;
				match(LPAREN);
				match(RPAREN);
				declaratorBrackets(t_AST);
				rt_AST = (AST)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_default:
				{
					match(LITERAL_default);
					annotationMemberValueInitializer();
					amvi_AST = (AST)returnAST;
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
				AST tmp105_AST = null;
				tmp105_AST = astFactory.create(LT(1));
				match(SEMI);
				if ( inputState.guessing==0 ) {
					annotationField_AST = (AST)currentAST.root;
					annotationField_AST =
										(AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ANNOTATION_FIELD_DEF,"ANNOTATION_FIELD_DEF")).add(mods_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(rt_AST))).add(i_AST).add(amvi_AST));
					currentAST.root = annotationField_AST;
					currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
						annotationField_AST.getFirstChild() : annotationField_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
				variableDefinitions(mods_AST,t_AST,false);
				v_AST = (AST)returnAST;
				AST tmp106_AST = null;
				tmp106_AST = astFactory.create(LT(1));
				match(SEMI);
				if ( inputState.guessing==0 ) {
					annotationField_AST = (AST)currentAST.root;
					annotationField_AST = v_AST;
					currentAST.root = annotationField_AST;
					currentAST.child = annotationField_AST!=null &&annotationField_AST.getFirstChild()!=null ?
						annotationField_AST.getFirstChild() : annotationField_AST;
					currentAST.advanceChildToEnd();
				}
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
		}
		if ( inputState.guessing==0 ) {
			annotationField_AST = (AST)currentAST.root;
			registLineColumn(annotationField_AST);
		}
		returnAST = annotationField_AST;
	}
	
	public final void enumConstant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstant_AST = null;
		AST an_AST = null;
		AST i_AST = null;
		AST a_AST = null;
		AST b_AST = null;
		
		annotations();
		an_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		name();
		i_AST = (AST)returnAST;
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			match(LPAREN);
			argList();
			a_AST = (AST)returnAST;
			match(RPAREN);
			break;
		}
		case SEMI:
		case COMMA:
		case LCURLY:
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
		{
		switch ( LA(1)) {
		case LCURLY:
		{
			enumConstantBlock();
			b_AST = (AST)returnAST;
			break;
		}
		case SEMI:
		case COMMA:
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
		if ( inputState.guessing==0 ) {
			enumConstant_AST = (AST)currentAST.root;
			enumConstant_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(ENUM_CONSTANT_DEF,"ENUM_CONSTANT_DEF")).add(an_AST).add(i_AST).add(a_AST).add(b_AST));
			currentAST.root = enumConstant_AST;
			currentAST.child = enumConstant_AST!=null &&enumConstant_AST.getFirstChild()!=null ?
				enumConstant_AST.getFirstChild() : enumConstant_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			enumConstant_AST = (AST)currentAST.root;
			registLineColumn(enumConstant_AST);
		}
		returnAST = enumConstant_AST;
	}
	
	public final void declaratorBrackets(
		AST typ
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST declaratorBrackets_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		if ( inputState.guessing==0 ) {
			declaratorBrackets_AST = (AST)currentAST.root;
			declaratorBrackets_AST=typ;
			currentAST.root = declaratorBrackets_AST;
			currentAST.child = declaratorBrackets_AST!=null &&declaratorBrackets_AST.getFirstChild()!=null ?
				declaratorBrackets_AST.getFirstChild() : declaratorBrackets_AST;
			currentAST.advanceChildToEnd();
		}
		{
		_loop1540:
		do {
			if ((LA(1)==LBRACK)) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(ARRAY_DECLARATOR);
				}
				match(RBRACK);
			}
			else {
				break _loop1540;
			}
			
		} while (true);
		}
		declaratorBrackets_AST = (AST)currentAST.root;
		returnAST = declaratorBrackets_AST;
	}
	
	public final void argList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST argList_AST = null;
		
		{
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
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
			if ( inputState.guessing==0 ) {
				argList_AST = (AST)currentAST.root;
				argList_AST = astFactory.create(ELIST,"ELIST");
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
		argList_AST = (AST)currentAST.root;
		returnAST = argList_AST;
	}
	
	public final void enumConstantBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantBlock_AST = null;
		
		match(LCURLY);
		{
		_loop1493:
		do {
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_static:
			case IDENT:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_threadsafe:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case AT:
			case LCURLY:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				enumConstantField();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				match(SEMI);
				break;
			}
			default:
			{
				break _loop1493;
			}
			}
		} while (true);
		}
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			enumConstantBlock_AST = (AST)currentAST.root;
			enumConstantBlock_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(OBJBLOCK,"OBJBLOCK")).add(enumConstantBlock_AST));
			currentAST.root = enumConstantBlock_AST;
			currentAST.child = enumConstantBlock_AST!=null &&enumConstantBlock_AST.getFirstChild()!=null ?
				enumConstantBlock_AST.getFirstChild() : enumConstantBlock_AST;
			currentAST.advanceChildToEnd();
		}
		enumConstantBlock_AST = (AST)currentAST.root;
		returnAST = enumConstantBlock_AST;
	}
	
	public final void enumConstantField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST enumConstantField_AST = null;
		AST mods_AST = null;
		AST td_AST = null;
		AST tp_AST = null;
		AST t_AST = null;
		AST n_AST = null;
		AST param_AST = null;
		AST rt_AST = null;
		AST tc_AST = null;
		AST s2_AST = null;
		AST f_AST = null;
		AST s4_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		switch ( LA(1)) {
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case LITERAL_static:
		case IDENT:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case AT:
		case LITERAL_class:
		case LITERAL_interface:
		case LITERAL_enum:
		{
			modifiers();
			mods_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case AT:
			case LITERAL_class:
			case LITERAL_interface:
			case LITERAL_enum:
			{
				typeDefinitionInternal(mods_AST);
				td_AST = (AST)returnAST;
				if ( inputState.guessing==0 ) {
					enumConstantField_AST = (AST)currentAST.root;
					enumConstantField_AST = td_AST;
					currentAST.root = enumConstantField_AST;
					currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
						enumConstantField_AST.getFirstChild() : enumConstantField_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			case IDENT:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				{
				switch ( LA(1)) {
				case LT:
				{
					typeParameters();
					tp_AST = (AST)returnAST;
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
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				typeSpec(false);
				t_AST = (AST)returnAST;
				{
				if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
					name();
					n_AST = (AST)returnAST;
					match(LPAREN);
					parameterDeclarationList();
					param_AST = (AST)returnAST;
					match(RPAREN);
					declaratorBrackets(t_AST);
					rt_AST = (AST)returnAST;
					{
					switch ( LA(1)) {
					case LITERAL_throws:
					{
						throwsClause();
						tc_AST = (AST)returnAST;
						break;
					}
					case SEMI:
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
					{
					switch ( LA(1)) {
					case LCURLY:
					{
						compoundStatement();
						s2_AST = (AST)returnAST;
						break;
					}
					case SEMI:
					{
						AST tmp115_AST = null;
						tmp115_AST = astFactory.create(LT(1));
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
						enumConstantField_AST = (AST)currentAST.root;
						enumConstantField_AST = (AST)astFactory.make( (new ASTArray(8)).add(astFactory.create(METHOD_DEF,"METHOD_DEF")).add(mods_AST).add(tp_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(rt_AST))).add(n_AST).add(param_AST).add(tc_AST).add(s2_AST));
						currentAST.root = enumConstantField_AST;
						currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
							enumConstantField_AST.getFirstChild() : enumConstantField_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
					fieldDefinitions(mods_AST,t_AST);
					f_AST = (AST)returnAST;
					AST tmp116_AST = null;
					tmp116_AST = astFactory.create(LT(1));
					match(SEMI);
					if ( inputState.guessing==0 ) {
						enumConstantField_AST = (AST)currentAST.root;
						enumConstantField_AST = f_AST;
						currentAST.root = enumConstantField_AST;
						currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
							enumConstantField_AST.getFirstChild() : enumConstantField_AST;
						currentAST.advanceChildToEnd();
					}
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
			break;
		}
		case LCURLY:
		{
			compoundStatement();
			s4_AST = (AST)returnAST;
			if ( inputState.guessing==0 ) {
				enumConstantField_AST = (AST)currentAST.root;
				enumConstantField_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(s4_AST));
				currentAST.root = enumConstantField_AST;
				currentAST.child = enumConstantField_AST!=null &&enumConstantField_AST.getFirstChild()!=null ?
					enumConstantField_AST.getFirstChild() : enumConstantField_AST;
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
		if ( inputState.guessing==0 ) {
			enumConstantField_AST = (AST)currentAST.root;
			registLineColumn(enumConstantField_AST);
		}
		returnAST = enumConstantField_AST;
	}
	
	public final void parameterDeclarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclarationList_AST = null;
		
		{
		boolean synPredMatched1557 = false;
		if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
			int _m1557 = mark();
			synPredMatched1557 = true;
			inputState.guessing++;
			try {
				{
				parameterDeclaration();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1557 = false;
			}
			rewind(_m1557);
inputState.guessing--;
		}
		if ( synPredMatched1557 ) {
			parameterDeclaration();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1561:
			do {
				boolean synPredMatched1560 = false;
				if (((LA(1)==COMMA) && (_tokenSet_18.member(LA(2))))) {
					int _m1560 = mark();
					synPredMatched1560 = true;
					inputState.guessing++;
					try {
						{
						match(COMMA);
						parameterDeclaration();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched1560 = false;
					}
					rewind(_m1560);
inputState.guessing--;
				}
				if ( synPredMatched1560 ) {
					match(COMMA);
					parameterDeclaration();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1561;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				variableLengthParameterDeclaration();
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
		}
		else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_20.member(LA(2)))) {
			variableLengthParameterDeclaration();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((LA(1)==RPAREN)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			parameterDeclarationList_AST = (AST)currentAST.root;
			parameterDeclarationList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(PARAMETERS,"PARAMETERS")).add(parameterDeclarationList_AST));
			currentAST.root = parameterDeclarationList_AST;
			currentAST.child = parameterDeclarationList_AST!=null &&parameterDeclarationList_AST.getFirstChild()!=null ?
				parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
			currentAST.advanceChildToEnd();
		}
		parameterDeclarationList_AST = (AST)currentAST.root;
		returnAST = parameterDeclarationList_AST;
	}
	
	public final void throwsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST throwsClause_AST = null;
		Token  t = null;
		AST t_AST = null;
		
		t = LT(1);
		t_AST = astFactory.create(t);
		match(LITERAL_throws);
		classOrInterfaceType(true);
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1553:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				classOrInterfaceType(true);
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1553;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			throwsClause_AST = (AST)currentAST.root;
			throwsClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(THROWS_CLAUSE,"THROWS_CLAUSE")).add(throwsClause_AST));
			currentAST.root = throwsClause_AST;
			currentAST.child = throwsClause_AST!=null &&throwsClause_AST.getFirstChild()!=null ?
				throwsClause_AST.getFirstChild() : throwsClause_AST;
			currentAST.advanceChildToEnd();
		}
		throwsClause_AST = (AST)currentAST.root;
		returnAST = throwsClause_AST;
	}
	
	public final void compoundStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundStatement_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		
		lc = LT(1);
		lc_AST = astFactory.create(lc);
		astFactory.makeASTRoot(currentAST, lc_AST);
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			lc_AST.setType(BLOCK);
		}
		{
		_loop1573:
		do {
			if ((_tokenSet_21.member(LA(1)))) {
				statement();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1573;
			}
			
		} while (true);
		}
		match(RCURLY);
		compoundStatement_AST = (AST)currentAST.root;
		returnAST = compoundStatement_AST;
	}
	
	public final void fieldDefinitions(
		AST mods, AST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fieldDefinitions_AST = null;
		
		fieldDeclarator(getASTFactory().dupTree(mods),
							getASTFactory().dupList(t));
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1532:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				fieldDeclarator(getASTFactory().dupTree(mods),
							getASTFactory().dupList(t));
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1532;
			}
			
		} while (true);
		}
		fieldDefinitions_AST = (AST)currentAST.root;
		returnAST = fieldDefinitions_AST;
	}
	
	public final void ctorHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ctorHead_AST = null;
		
		name();
		astFactory.addASTChild(currentAST, returnAST);
		match(LPAREN);
		parameterDeclarationList();
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
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
		ctorHead_AST = (AST)currentAST.root;
		returnAST = ctorHead_AST;
	}
	
	public final void constructorBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructorBody_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		
		lc = LT(1);
		lc_AST = astFactory.create(lc);
		astFactory.makeASTRoot(currentAST, lc_AST);
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			lc_AST.setType(BLOCK);
		}
		{
		if ((_tokenSet_22.member(LA(1))) && (_tokenSet_23.member(LA(2)))) {
			explicitConstructorInvocation();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_25.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		{
		_loop1526:
		do {
			if ((_tokenSet_21.member(LA(1)))) {
				statement();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1526;
			}
			
		} while (true);
		}
		match(RCURLY);
		constructorBody_AST = (AST)currentAST.root;
		returnAST = constructorBody_AST;
	}
	
/** Catch obvious constructor calls, but not the expr.super(...) calls */
	public final void explicitConstructorInvocation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST explicitConstructorInvocation_AST = null;
		Token  lp1 = null;
		AST lp1_AST = null;
		Token  lp2 = null;
		AST lp2_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_super:
		case LITERAL_this:
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
		case LITERAL_this:
		{
			match(LITERAL_this);
			lp1 = LT(1);
			lp1_AST = astFactory.create(lp1);
			astFactory.makeASTRoot(currentAST, lp1_AST);
			match(LPAREN);
			argList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				lp1_AST.setType(CTOR_CALL);
			}
			if ( inputState.guessing==0 ) {
				registLineColumn(lp1_AST);
			}
			break;
		}
		case LITERAL_super:
		{
			match(LITERAL_super);
			lp2 = LT(1);
			lp2_AST = astFactory.create(lp2);
			astFactory.makeASTRoot(currentAST, lp2_AST);
			match(LPAREN);
			argList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				lp2_AST.setType(SUPER_CTOR_CALL);
			}
			if ( inputState.guessing==0 ) {
				registLineColumn(lp2_AST);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			explicitConstructorInvocation_AST = (AST)currentAST.root;
			explicitConstructorInvocation_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR,"EXPR")).add(explicitConstructorInvocation_AST));
			currentAST.root = explicitConstructorInvocation_AST;
			currentAST.child = explicitConstructorInvocation_AST!=null &&explicitConstructorInvocation_AST.getFirstChild()!=null ?
				explicitConstructorInvocation_AST.getFirstChild() : explicitConstructorInvocation_AST;
			currentAST.advanceChildToEnd();
		}
		explicitConstructorInvocation_AST = (AST)currentAST.root;
		returnAST = explicitConstructorInvocation_AST;
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		AST dec_AST = null;
		AST ex_AST = null;
		AST m_AST = null;
		Token  c = null;
		AST c_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST e_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		Token  s = null;
		AST s_AST = null;
		
				boolean isIf = false;
				int ifToLine = 0;
				int ifToColumn = 0;
			
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		switch ( LA(1)) {
		case LCURLY:
		{
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_if:
		{
			i = LT(1);
			i_AST = astFactory.create(i);
			astFactory.makeASTRoot(currentAST, i_AST);
			match(LITERAL_if);
			conditionalClause();
			astFactory.addASTChild(currentAST, returnAST);
			statement();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				
							final Token lastToken = LT(0);
							ifToLine = lastToken.getLine();
							ifToColumn = lastToken.getColumn() + lastToken.getText().length();
							isIf = true;
						
			}
			{
			if ((LA(1)==LITERAL_else) && (_tokenSet_21.member(LA(2)))) {
				if ( inputState.guessing==0 ) {
					pushStartLineColumn();
				}
				elseStatement();
				e_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					registLineColumn(e_AST);
				}
			}
			else if ((_tokenSet_26.member(LA(1))) && (_tokenSet_27.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			break;
		}
		case LITERAL_for:
		{
			forStatement();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_while:
		{
			AST tmp131_AST = null;
			tmp131_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp131_AST);
			match(LITERAL_while);
			conditionalClause();
			astFactory.addASTChild(currentAST, returnAST);
			statement();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_do:
		{
			AST tmp132_AST = null;
			tmp132_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp132_AST);
			match(LITERAL_do);
			statement();
			astFactory.addASTChild(currentAST, returnAST);
			match(LITERAL_while);
			conditionalClause();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMI);
			break;
		}
		case LITERAL_break:
		{
			AST tmp135_AST = null;
			tmp135_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp135_AST);
			match(LITERAL_break);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				AST tmp136_AST = null;
				tmp136_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp136_AST);
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
			break;
		}
		case LITERAL_continue:
		{
			AST tmp138_AST = null;
			tmp138_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp138_AST);
			match(LITERAL_continue);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				AST tmp139_AST = null;
				tmp139_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp139_AST);
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
			break;
		}
		case LITERAL_return:
		{
			AST tmp141_AST = null;
			tmp141_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp141_AST);
			match(LITERAL_return);
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_super:
			case LT:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LPAREN:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case LITERAL_this:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
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
			break;
		}
		case LITERAL_switch:
		{
			AST tmp143_AST = null;
			tmp143_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp143_AST);
			match(LITERAL_switch);
			conditionalClause();
			astFactory.addASTChild(currentAST, returnAST);
			lc = LT(1);
			lc_AST = astFactory.create(lc);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(BLOCK);
			}
			{
			_loop1583:
			do {
				if ((LA(1)==LITERAL_default||LA(1)==LITERAL_case)) {
					casesGroup();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1583;
				}
				
			} while (true);
			}
			match(RCURLY);
			break;
		}
		case LITERAL_try:
		{
			tryBlock();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_throw:
		{
			AST tmp145_AST = null;
			tmp145_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp145_AST);
			match(LITERAL_throw);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(SEMI);
			break;
		}
		case LITERAL_assert:
		{
			AST tmp147_AST = null;
			tmp147_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp147_AST);
			match(LITERAL_assert);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COLON:
			{
				match(COLON);
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
			break;
		}
		case SEMI:
		{
			s = LT(1);
			s_AST = astFactory.create(s);
			astFactory.addASTChild(currentAST, s_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				s_AST.setType(EMPTY_STAT);
			}
			break;
		}
		default:
			boolean synPredMatched1577 = false;
			if (((_tokenSet_28.member(LA(1))) && (_tokenSet_29.member(LA(2))))) {
				int _m1577 = mark();
				synPredMatched1577 = true;
				inputState.guessing++;
				try {
					{
					declaration(false);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched1577 = false;
				}
				rewind(_m1577);
inputState.guessing--;
			}
			if ( synPredMatched1577 ) {
				declaration(false);
				dec_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					statement_AST = (AST)currentAST.root;
					statement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(LOCAL_VARIABLE_DEF_STATE,"LOCAL_VARIABLE_DEF_STATE")).add(dec_AST));
					currentAST.root = statement_AST;
					currentAST.child = statement_AST!=null &&statement_AST.getFirstChild()!=null ?
						statement_AST.getFirstChild() : statement_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((_tokenSet_30.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
				expression();
				ex_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					statement_AST = (AST)currentAST.root;
					statement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR_STATE,"EXPR_STATE")).add(ex_AST));
					currentAST.root = statement_AST;
					currentAST.child = statement_AST!=null &&statement_AST.getFirstChild()!=null ?
						statement_AST.getFirstChild() : statement_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((_tokenSet_32.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
				modifiers();
				m_AST = (AST)returnAST;
				classDefinition(m_AST);
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
				AST tmp152_AST = null;
				tmp152_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp152_AST);
				match(IDENT);
				c = LT(1);
				c_AST = astFactory.create(c);
				astFactory.makeASTRoot(currentAST, c_AST);
				match(COLON);
				if ( inputState.guessing==0 ) {
					c_AST.setType(LABELED_STAT);
				}
				statement();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
				AST tmp153_AST = null;
				tmp153_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp153_AST);
				match(LITERAL_synchronized);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			statement_AST = (AST)currentAST.root;
			
					if(isIf) {
						registLineColumn(statement_AST, ifToLine, ifToColumn);
					} else {
						registLineColumn(statement_AST);
					}
				
		}
		statement_AST = (AST)currentAST.root;
		returnAST = statement_AST;
	}
	
/** Declaration of a field. This can be a class/instance field.
 *  It can also include possible initialization.
 */
	public final void fieldDeclarator(
		AST mods, AST t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fieldDeclarator_AST = null;
		AST id_AST = null;
		AST d_AST = null;
		AST v_AST = null;
		
		name();
		id_AST = (AST)returnAST;
		declaratorBrackets(t);
		d_AST = (AST)returnAST;
		varInitializer();
		v_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			fieldDeclarator_AST = (AST)currentAST.root;
			fieldDeclarator_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(FIELD_DEF,"FIELD_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(d_AST))).add(id_AST).add(v_AST));
			currentAST.root = fieldDeclarator_AST;
			currentAST.child = fieldDeclarator_AST!=null &&fieldDeclarator_AST.getFirstChild()!=null ?
				fieldDeclarator_AST.getFirstChild() : fieldDeclarator_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = fieldDeclarator_AST;
	}
	
	public final void varInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varInitializer_AST = null;
		
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			AST tmp156_AST = null;
			tmp156_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp156_AST);
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
		varInitializer_AST = (AST)currentAST.root;
		returnAST = varInitializer_AST;
	}
	
/** Declaration of a variable. This can be a class/instance variable,
 *  or a local variable in a method
 *  It can also include possible initialization.
 */
	public final void variableDeclarator(
		AST mods, AST t, boolean isParameter
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableDeclarator_AST = null;
		AST id_AST = null;
		AST d_AST = null;
		AST v_AST = null;
		
		name();
		id_AST = (AST)returnAST;
		declaratorBrackets(t);
		d_AST = (AST)returnAST;
		varInitializer();
		v_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			variableDeclarator_AST = (AST)currentAST.root;
			
						if (isParameter){
							variableDeclarator_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(LOCAL_PARAMETER_DEF,"LOCAL_PARAMETER_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(d_AST))).add(id_AST).add(v_AST));									
					} else {
						variableDeclarator_AST = (AST)astFactory.make( (new ASTArray(5)).add(astFactory.create(LOCAL_VARIABLE_DEF,"LOCAL_VARIABLE_DEF")).add(mods).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(d_AST))).add(id_AST).add(v_AST));
					}
					
			currentAST.root = variableDeclarator_AST;
			currentAST.child = variableDeclarator_AST!=null &&variableDeclarator_AST.getFirstChild()!=null ?
				variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = variableDeclarator_AST;
	}
	
	public final void initializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST initializer_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			initializer_AST = (AST)currentAST.root;
			break;
		}
		case LCURLY:
		{
			arrayInitializer();
			astFactory.addASTChild(currentAST, returnAST);
			initializer_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = initializer_AST;
	}
	
	public final void arrayInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST arrayInitializer_AST = null;
		Token  lc = null;
		AST lc_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		lc = LT(1);
		lc_AST = astFactory.create(lc);
		astFactory.makeASTRoot(currentAST, lc_AST);
		match(LCURLY);
		if ( inputState.guessing==0 ) {
			lc_AST.setType(ARRAY_INIT);
		}
		{
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case LCURLY:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			initializer();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1546:
			do {
				if ((LA(1)==COMMA) && (_tokenSet_34.member(LA(2)))) {
					match(COMMA);
					initializer();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop1546;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
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
		match(RCURLY);
		if ( inputState.guessing==0 ) {
			arrayInitializer_AST = (AST)currentAST.root;
			registLineColumn(arrayInitializer_AST);
		}
		arrayInitializer_AST = (AST)currentAST.root;
		returnAST = arrayInitializer_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;
		
		assignmentExpression();
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			expression_AST = (AST)currentAST.root;
			expression_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR,"EXPR")).add(expression_AST));
			currentAST.root = expression_AST;
			currentAST.child = expression_AST!=null &&expression_AST.getFirstChild()!=null ?
				expression_AST.getFirstChild() : expression_AST;
			currentAST.advanceChildToEnd();
		}
		expression_AST = (AST)currentAST.root;
		returnAST = expression_AST;
	}
	
	public final void parameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterDeclaration_AST = null;
		AST pm_AST = null;
		AST t_AST = null;
		AST id_AST = null;
		AST pd_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		parameterModifier();
		pm_AST = (AST)returnAST;
		typeSpec(false);
		t_AST = (AST)returnAST;
		name();
		id_AST = (AST)returnAST;
		declaratorBrackets(t_AST);
		pd_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (AST)currentAST.root;
			parameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(METHOD_PARAMETER_DEF,"METHOD_PARAMETER_DEF")).add(pm_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(pd_AST))).add(id_AST));
			currentAST.root = parameterDeclaration_AST;
			currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
				parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (AST)currentAST.root;
			registLineColumn(parameterDeclaration_AST);
		}
		returnAST = parameterDeclaration_AST;
	}
	
	public final void variableLengthParameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variableLengthParameterDeclaration_AST = null;
		AST pm_AST = null;
		AST t_AST = null;
		AST id_AST = null;
		AST pd_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		parameterModifier();
		pm_AST = (AST)returnAST;
		typeSpec(false);
		t_AST = (AST)returnAST;
		match(TRIPLE_DOT);
		name();
		id_AST = (AST)returnAST;
		declaratorBrackets(t_AST);
		pd_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			variableLengthParameterDeclaration_AST = (AST)currentAST.root;
			variableLengthParameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(VARIABLE_PARAMETER_DEF,"VARIABLE_PARAMETER_DEF")).add(pm_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(pd_AST))).add(id_AST));
			currentAST.root = variableLengthParameterDeclaration_AST;
			currentAST.child = variableLengthParameterDeclaration_AST!=null &&variableLengthParameterDeclaration_AST.getFirstChild()!=null ?
				variableLengthParameterDeclaration_AST.getFirstChild() : variableLengthParameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			variableLengthParameterDeclaration_AST = (AST)currentAST.root;
			registLineColumn(variableLengthParameterDeclaration_AST);
		}
		returnAST = variableLengthParameterDeclaration_AST;
	}
	
	public final void parameterModifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameterModifier_AST = null;
		Token  f = null;
		AST f_AST = null;
		
		{
		_loop1567:
		do {
			if ((LA(1)==AT) && (LA(2)==IDENT)) {
				annotation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1567;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case FINAL:
		{
			f = LT(1);
			f_AST = astFactory.create(f);
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
		case AT:
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
		_loop1570:
		do {
			if ((LA(1)==AT)) {
				annotation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1570;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			parameterModifier_AST = (AST)currentAST.root;
			parameterModifier_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(MODIFIERS,"MODIFIERS")).add(parameterModifier_AST));
			currentAST.root = parameterModifier_AST;
			currentAST.child = parameterModifier_AST!=null &&parameterModifier_AST.getFirstChild()!=null ?
				parameterModifier_AST.getFirstChild() : parameterModifier_AST;
			currentAST.advanceChildToEnd();
		}
		parameterModifier_AST = (AST)currentAST.root;
		returnAST = parameterModifier_AST;
	}
	
	public final void conditionalClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalClause_AST = null;
		AST ex_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		match(LPAREN);
		expression();
		ex_AST = (AST)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			conditionalClause_AST = (AST)currentAST.root;
			conditionalClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(COND_CLAUSE,"COND_CLAUSE")).add(ex_AST));
			currentAST.root = conditionalClause_AST;
			currentAST.child = conditionalClause_AST!=null &&conditionalClause_AST.getFirstChild()!=null ?
				conditionalClause_AST.getFirstChild() : conditionalClause_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			conditionalClause_AST = (AST)currentAST.root;
			registLineColumn(conditionalClause_AST);
		}
		conditionalClause_AST = (AST)currentAST.root;
		returnAST = conditionalClause_AST;
	}
	
	public final void elseStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST elseStatement_AST = null;
		
		AST tmp163_AST = null;
		tmp163_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp163_AST);
		match(LITERAL_else);
		statement();
		astFactory.addASTChild(currentAST, returnAST);
		elseStatement_AST = (AST)currentAST.root;
		returnAST = elseStatement_AST;
	}
	
	public final void forStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forStatement_AST = null;
		Token  f = null;
		AST f_AST = null;
		AST h1_AST = null;
		AST h2_AST = null;
		AST s_AST = null;
		
		f = LT(1);
		f_AST = astFactory.create(f);
		match(LITERAL_for);
		match(LPAREN);
		{
		boolean synPredMatched1590 = false;
		if (((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2))))) {
			int _m1590 = mark();
			synPredMatched1590 = true;
			inputState.guessing++;
			try {
				{
				forInit();
				match(SEMI);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1590 = false;
			}
			rewind(_m1590);
inputState.guessing--;
		}
		if ( synPredMatched1590 ) {
			traditionalForClause();
			h1_AST = (AST)returnAST;
		}
		else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
			foreachClause();
			h2_AST = (AST)returnAST;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		match(RPAREN);
		statement();
		s_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			forStatement_AST = (AST)currentAST.root;
			
					  if (null != h1_AST){
						//#forStatement = #(#[FOR,"for"], #h1,#(#[BLOCK,"{"], #s));
						forStatement_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(FOR,"for")).add(h1_AST).add(s_AST));		  
						}
					  else {
					//#forStatement = #(#[FOR,"for"], #h2, #(#[BLOCK,"{"], #s));
					  	forStatement_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(FOREACH,"foreach")).add(h2_AST).add(s_AST));
					  }
					
			currentAST.root = forStatement_AST;
			currentAST.child = forStatement_AST!=null &&forStatement_AST.getFirstChild()!=null ?
				forStatement_AST.getFirstChild() : forStatement_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = forStatement_AST;
	}
	
	public final void casesGroup() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST casesGroup_AST = null;
		
		{
		int _cnt1598=0;
		_loop1598:
		do {
			if ((LA(1)==LITERAL_default||LA(1)==LITERAL_case) && (_tokenSet_37.member(LA(2)))) {
				aCase();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt1598>=1 ) { break _loop1598; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt1598++;
		} while (true);
		}
		caseSList();
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			casesGroup_AST = (AST)currentAST.root;
			casesGroup_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CASE_GROUP,"CASE_GROUP")).add(casesGroup_AST));
			currentAST.root = casesGroup_AST;
			currentAST.child = casesGroup_AST!=null &&casesGroup_AST.getFirstChild()!=null ?
				casesGroup_AST.getFirstChild() : casesGroup_AST;
			currentAST.advanceChildToEnd();
		}
		casesGroup_AST = (AST)currentAST.root;
		returnAST = casesGroup_AST;
	}
	
	public final void tryBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST tryBlock_AST = null;
		
		AST tmp166_AST = null;
		tmp166_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp166_AST);
		match(LITERAL_try);
		compoundStatement();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1614:
		do {
			if ((LA(1)==LITERAL_catch)) {
				handler();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1614;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case LITERAL_finally:
		{
			finallyClause();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case FINAL:
		case ABSTRACT:
		case STRICTFP:
		case SEMI:
		case LITERAL_static:
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LITERAL_private:
		case LITERAL_public:
		case LITERAL_protected:
		case LITERAL_transient:
		case LITERAL_native:
		case LITERAL_threadsafe:
		case LITERAL_synchronized:
		case LITERAL_volatile:
		case AT:
		case LPAREN:
		case LCURLY:
		case RCURLY:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_class:
		case LITERAL_default:
		case LITERAL_this:
		case LITERAL_if:
		case LITERAL_while:
		case LITERAL_do:
		case LITERAL_break:
		case LITERAL_continue:
		case LITERAL_return:
		case LITERAL_switch:
		case LITERAL_throw:
		case LITERAL_assert:
		case LITERAL_else:
		case LITERAL_for:
		case LITERAL_case:
		case LITERAL_try:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
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
		tryBlock_AST = (AST)currentAST.root;
		returnAST = tryBlock_AST;
	}
	
	public final void forInit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forInit_AST = null;
		
		{
		boolean synPredMatched1607 = false;
		if (((_tokenSet_28.member(LA(1))) && (_tokenSet_29.member(LA(2))))) {
			int _m1607 = mark();
			synPredMatched1607 = true;
			inputState.guessing++;
			try {
				{
				declaration(true);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched1607 = false;
			}
			rewind(_m1607);
inputState.guessing--;
		}
		if ( synPredMatched1607 ) {
			declaration(true);
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_30.member(LA(1))) && (_tokenSet_38.member(LA(2)))) {
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
			forInit_AST = (AST)currentAST.root;
			forInit_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_INIT,"FOR_INIT")).add(forInit_AST));
			currentAST.root = forInit_AST;
			currentAST.child = forInit_AST!=null &&forInit_AST.getFirstChild()!=null ?
				forInit_AST.getFirstChild() : forInit_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			forInit_AST = (AST)currentAST.root;
			registLineColumn(forInit_AST);
		}
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		forInit_AST = (AST)currentAST.root;
		returnAST = forInit_AST;
	}
	
	public final void traditionalForClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST traditionalForClause_AST = null;
		
		forInit();
		astFactory.addASTChild(currentAST, returnAST);
		match(SEMI);
		forCond();
		astFactory.addASTChild(currentAST, returnAST);
		match(SEMI);
		forIter();
		astFactory.addASTChild(currentAST, returnAST);
		traditionalForClause_AST = (AST)currentAST.root;
		returnAST = traditionalForClause_AST;
	}
	
	public final void foreachClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST foreachClause_AST = null;
		
		foreachVariable();
		astFactory.addASTChild(currentAST, returnAST);
		match(COLON);
		foreachExpression();
		astFactory.addASTChild(currentAST, returnAST);
		foreachClause_AST = (AST)currentAST.root;
		returnAST = foreachClause_AST;
	}
	
	public final void forCond() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forCond_AST = null;
		
		{
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
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
			forCond_AST = (AST)currentAST.root;
			forCond_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_CONDITION,"FOR_CONDITION")).add(forCond_AST));
			currentAST.root = forCond_AST;
			currentAST.child = forCond_AST!=null &&forCond_AST.getFirstChild()!=null ?
				forCond_AST.getFirstChild() : forCond_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			forCond_AST = (AST)currentAST.root;
			registLineColumn(forCond_AST);
		}
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		forCond_AST = (AST)currentAST.root;
		returnAST = forCond_AST;
	}
	
	public final void forIter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST forIter_AST = null;
		
		{
		switch ( LA(1)) {
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case PLUS:
		case MINUS:
		case INC:
		case DEC:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
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
			forIter_AST = (AST)currentAST.root;
			forIter_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOR_ITERATOR,"FOR_ITERATOR")).add(forIter_AST));
			currentAST.root = forIter_AST;
			currentAST.child = forIter_AST!=null &&forIter_AST.getFirstChild()!=null ?
				forIter_AST.getFirstChild() : forIter_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			forIter_AST = (AST)currentAST.root;
			registLineColumn(forIter_AST);
		}
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		forIter_AST = (AST)currentAST.root;
		returnAST = forIter_AST;
	}
	
	public final void foreachVariable() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST foreachVariable_AST = null;
		AST p_AST = null;
		
		localParameterDeclaration();
		p_AST = (AST)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			foreachVariable_AST = (AST)currentAST.root;
			foreachVariable_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOREACH_VARIABLE,"FOREACH_VARIABLE")).add(foreachVariable_AST));
			currentAST.root = foreachVariable_AST;
			currentAST.child = foreachVariable_AST!=null &&foreachVariable_AST.getFirstChild()!=null ?
				foreachVariable_AST.getFirstChild() : foreachVariable_AST;
			currentAST.advanceChildToEnd();
		}
		foreachVariable_AST = (AST)currentAST.root;
		returnAST = foreachVariable_AST;
	}
	
	public final void foreachExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST foreachExpression_AST = null;
		AST e_AST = null;
		
		expression();
		e_AST = (AST)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			foreachExpression_AST = (AST)currentAST.root;
			foreachExpression_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FOREACH_EXPRESSION,"FOREACH_EXPRESSION")).add(foreachExpression_AST));
			currentAST.root = foreachExpression_AST;
			currentAST.child = foreachExpression_AST!=null &&foreachExpression_AST.getFirstChild()!=null ?
				foreachExpression_AST.getFirstChild() : foreachExpression_AST;
			currentAST.advanceChildToEnd();
		}
		foreachExpression_AST = (AST)currentAST.root;
		returnAST = foreachExpression_AST;
	}
	
	public final void localParameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST localParameterDeclaration_AST = null;
		AST pm_AST = null;
		AST t_AST = null;
		AST id_AST = null;
		AST pd_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		parameterModifier();
		pm_AST = (AST)returnAST;
		typeSpec(false);
		t_AST = (AST)returnAST;
		name();
		id_AST = (AST)returnAST;
		declaratorBrackets(t_AST);
		pd_AST = (AST)returnAST;
		if ( inputState.guessing==0 ) {
			localParameterDeclaration_AST = (AST)currentAST.root;
			localParameterDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(LOCAL_PARAMETER_DEF,"LOCAL_PARAMETER_DEF")).add(pm_AST).add((AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(TYPE,"TYPE")).add(pd_AST))).add(id_AST));
			currentAST.root = localParameterDeclaration_AST;
			currentAST.child = localParameterDeclaration_AST!=null &&localParameterDeclaration_AST.getFirstChild()!=null ?
				localParameterDeclaration_AST.getFirstChild() : localParameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		if ( inputState.guessing==0 ) {
			localParameterDeclaration_AST = (AST)currentAST.root;
			registLineColumn(localParameterDeclaration_AST);
		}
		returnAST = localParameterDeclaration_AST;
	}
	
	public final void aCase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aCase_AST = null;
		
		{
		switch ( LA(1)) {
		case LITERAL_case:
		{
			AST tmp170_AST = null;
			tmp170_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp170_AST);
			match(LITERAL_case);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case LITERAL_default:
		{
			AST tmp171_AST = null;
			tmp171_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp171_AST);
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
		aCase_AST = (AST)currentAST.root;
		returnAST = aCase_AST;
	}
	
	public final void caseSList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseSList_AST = null;
		
		{
		_loop1603:
		do {
			if ((_tokenSet_21.member(LA(1)))) {
				statement();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1603;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			caseSList_AST = (AST)currentAST.root;
			caseSList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(SLIST,"SLIST")).add(caseSList_AST));
			currentAST.root = caseSList_AST;
			currentAST.child = caseSList_AST!=null &&caseSList_AST.getFirstChild()!=null ?
				caseSList_AST.getFirstChild() : caseSList_AST;
			currentAST.advanceChildToEnd();
		}
		caseSList_AST = (AST)currentAST.root;
		returnAST = caseSList_AST;
	}
	
	public final void expressionList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionList_AST = null;
		
		expression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1621:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1621;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			expressionList_AST = (AST)currentAST.root;
			expressionList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ELIST,"ELIST")).add(expressionList_AST));
			currentAST.root = expressionList_AST;
			currentAST.child = expressionList_AST!=null &&expressionList_AST.getFirstChild()!=null ?
				expressionList_AST.getFirstChild() : expressionList_AST;
			currentAST.advanceChildToEnd();
		}
		expressionList_AST = (AST)currentAST.root;
		returnAST = expressionList_AST;
	}
	
	public final void handler() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST handler_AST = null;
		AST lpd_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		AST tmp174_AST = null;
		tmp174_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp174_AST);
		match(LITERAL_catch);
		match(LPAREN);
		localParameterDeclaration();
		lpd_AST = (AST)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
		compoundStatement();
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			handler_AST = (AST)currentAST.root;
			registLineColumn(handler_AST);
		}
		handler_AST = (AST)currentAST.root;
		returnAST = handler_AST;
	}
	
	public final void finallyClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST finallyClause_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		AST tmp177_AST = null;
		tmp177_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp177_AST);
		match(LITERAL_finally);
		compoundStatement();
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			finallyClause_AST = (AST)currentAST.root;
			registLineColumn(finallyClause_AST);
		}
		finallyClause_AST = (AST)currentAST.root;
		returnAST = finallyClause_AST;
	}
	
	public final void assignmentExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignmentExpression_AST = null;
		
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
				AST tmp178_AST = null;
				tmp178_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp178_AST);
				match(ASSIGN);
				break;
			}
			case PLUS_ASSIGN:
			{
				AST tmp179_AST = null;
				tmp179_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp179_AST);
				match(PLUS_ASSIGN);
				break;
			}
			case MINUS_ASSIGN:
			{
				AST tmp180_AST = null;
				tmp180_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp180_AST);
				match(MINUS_ASSIGN);
				break;
			}
			case STAR_ASSIGN:
			{
				AST tmp181_AST = null;
				tmp181_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp181_AST);
				match(STAR_ASSIGN);
				break;
			}
			case DIV_ASSIGN:
			{
				AST tmp182_AST = null;
				tmp182_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp182_AST);
				match(DIV_ASSIGN);
				break;
			}
			case MOD_ASSIGN:
			{
				AST tmp183_AST = null;
				tmp183_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp183_AST);
				match(MOD_ASSIGN);
				break;
			}
			case SR_ASSIGN:
			{
				AST tmp184_AST = null;
				tmp184_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp184_AST);
				match(SR_ASSIGN);
				break;
			}
			case BSR_ASSIGN:
			{
				AST tmp185_AST = null;
				tmp185_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp185_AST);
				match(BSR_ASSIGN);
				break;
			}
			case SL_ASSIGN:
			{
				AST tmp186_AST = null;
				tmp186_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp186_AST);
				match(SL_ASSIGN);
				break;
			}
			case BAND_ASSIGN:
			{
				AST tmp187_AST = null;
				tmp187_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp187_AST);
				match(BAND_ASSIGN);
				break;
			}
			case BXOR_ASSIGN:
			{
				AST tmp188_AST = null;
				tmp188_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp188_AST);
				match(BXOR_ASSIGN);
				break;
			}
			case BOR_ASSIGN:
			{
				AST tmp189_AST = null;
				tmp189_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp189_AST);
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
		case SEMI:
		case RBRACK:
		case COMMA:
		case RPAREN:
		case RCURLY:
		case COLON:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		assignmentExpression_AST = (AST)currentAST.root;
		returnAST = assignmentExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;
		
		logicalAndExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1629:
		do {
			if ((LA(1)==LOR)) {
				AST tmp190_AST = null;
				tmp190_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp190_AST);
				match(LOR);
				logicalAndExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1629;
			}
			
		} while (true);
		}
		logicalOrExpression_AST = (AST)currentAST.root;
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;
		
		inclusiveOrExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1632:
		do {
			if ((LA(1)==LAND)) {
				AST tmp191_AST = null;
				tmp191_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp191_AST);
				match(LAND);
				inclusiveOrExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1632;
			}
			
		} while (true);
		}
		logicalAndExpression_AST = (AST)currentAST.root;
		returnAST = logicalAndExpression_AST;
	}
	
	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression_AST = null;
		
		exclusiveOrExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1635:
		do {
			if ((LA(1)==BOR)) {
				AST tmp192_AST = null;
				tmp192_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp192_AST);
				match(BOR);
				exclusiveOrExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1635;
			}
			
		} while (true);
		}
		inclusiveOrExpression_AST = (AST)currentAST.root;
		returnAST = inclusiveOrExpression_AST;
	}
	
	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression_AST = null;
		
		andExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1638:
		do {
			if ((LA(1)==BXOR)) {
				AST tmp193_AST = null;
				tmp193_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp193_AST);
				match(BXOR);
				andExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1638;
			}
			
		} while (true);
		}
		exclusiveOrExpression_AST = (AST)currentAST.root;
		returnAST = exclusiveOrExpression_AST;
	}
	
	public final void andExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression_AST = null;
		
		equalityExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1641:
		do {
			if ((LA(1)==BAND)) {
				AST tmp194_AST = null;
				tmp194_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp194_AST);
				match(BAND);
				equalityExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop1641;
			}
			
		} while (true);
		}
		andExpression_AST = (AST)currentAST.root;
		returnAST = andExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;
		
		relationalExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1645:
		do {
			if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
				{
				switch ( LA(1)) {
				case NOT_EQUAL:
				{
					AST tmp195_AST = null;
					tmp195_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp195_AST);
					match(NOT_EQUAL);
					break;
				}
				case EQUAL:
				{
					AST tmp196_AST = null;
					tmp196_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp196_AST);
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
				break _loop1645;
			}
			
		} while (true);
		}
		equalityExpression_AST = (AST)currentAST.root;
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;
		
		shiftExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case SEMI:
		case RBRACK:
		case QUESTION:
		case LT:
		case COMMA:
		case GT:
		case RPAREN:
		case RCURLY:
		case BAND:
		case ASSIGN:
		case COLON:
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
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case NOT_EQUAL:
		case EQUAL:
		case LE:
		case GE:
		{
			{
			_loop1650:
			do {
				if ((_tokenSet_39.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LT:
					{
						AST tmp197_AST = null;
						tmp197_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp197_AST);
						match(LT);
						break;
					}
					case GT:
					{
						AST tmp198_AST = null;
						tmp198_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp198_AST);
						match(GT);
						break;
					}
					case LE:
					{
						AST tmp199_AST = null;
						tmp199_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp199_AST);
						match(LE);
						break;
					}
					case GE:
					{
						AST tmp200_AST = null;
						tmp200_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp200_AST);
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
					break _loop1650;
				}
				
			} while (true);
			}
			break;
		}
		case LITERAL_instanceof:
		{
			AST tmp201_AST = null;
			tmp201_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp201_AST);
			match(LITERAL_instanceof);
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
		relationalExpression_AST = (AST)currentAST.root;
		returnAST = relationalExpression_AST;
	}
	
	public final void shiftExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression_AST = null;
		
		additiveExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1654:
		do {
			if ((_tokenSet_40.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case SL:
				{
					AST tmp202_AST = null;
					tmp202_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp202_AST);
					match(SL);
					break;
				}
				case SR:
				{
					AST tmp203_AST = null;
					tmp203_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp203_AST);
					match(SR);
					break;
				}
				case BSR:
				{
					AST tmp204_AST = null;
					tmp204_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp204_AST);
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
				break _loop1654;
			}
			
		} while (true);
		}
		shiftExpression_AST = (AST)currentAST.root;
		returnAST = shiftExpression_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;
		
		multiplicativeExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1658:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS)) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					AST tmp205_AST = null;
					tmp205_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp205_AST);
					match(PLUS);
					break;
				}
				case MINUS:
				{
					AST tmp206_AST = null;
					tmp206_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp206_AST);
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
				break _loop1658;
			}
			
		} while (true);
		}
		additiveExpression_AST = (AST)currentAST.root;
		returnAST = additiveExpression_AST;
	}
	
	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression_AST = null;
		
		unaryExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1662:
		do {
			if ((_tokenSet_41.member(LA(1)))) {
				{
				switch ( LA(1)) {
				case STAR:
				{
					AST tmp207_AST = null;
					tmp207_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp207_AST);
					match(STAR);
					break;
				}
				case DIV:
				{
					AST tmp208_AST = null;
					tmp208_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp208_AST);
					match(DIV);
					break;
				}
				case MOD:
				{
					AST tmp209_AST = null;
					tmp209_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp209_AST);
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
				break _loop1662;
			}
			
		} while (true);
		}
		multiplicativeExpression_AST = (AST)currentAST.root;
		returnAST = multiplicativeExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;
		
		switch ( LA(1)) {
		case INC:
		{
			AST tmp210_AST = null;
			tmp210_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp210_AST);
			match(INC);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case DEC:
		{
			AST tmp211_AST = null;
			tmp211_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp211_AST);
			match(DEC);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST tmp212_AST = null;
			tmp212_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp212_AST);
			match(MINUS);
			if ( inputState.guessing==0 ) {
				tmp212_AST.setType(UNARY_MINUS);
			}
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case PLUS:
		{
			AST tmp213_AST = null;
			tmp213_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp213_AST);
			match(PLUS);
			if ( inputState.guessing==0 ) {
				tmp213_AST.setType(UNARY_PLUS);
			}
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case BNOT:
		case LNOT:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			unaryExpressionNotPlusMinus();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpressionNotPlusMinus_AST = null;
		Token  lpb = null;
		AST lpb_AST = null;
		Token  lp = null;
		AST lp_AST = null;
		
		switch ( LA(1)) {
		case BNOT:
		{
			AST tmp214_AST = null;
			tmp214_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp214_AST);
			match(BNOT);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case LNOT:
		{
			AST tmp215_AST = null;
			tmp215_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp215_AST);
			match(LNOT);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
			break;
		}
		case IDENT:
		case LITERAL_super:
		case LT:
		case LITERAL_void:
		case LITERAL_boolean:
		case LITERAL_byte:
		case LITERAL_char:
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_float:
		case LITERAL_long:
		case LITERAL_double:
		case LPAREN:
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case LITERAL_this:
		case LITERAL_true:
		case LITERAL_false:
		case LITERAL_null:
		case LITERAL_new:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			{
			boolean synPredMatched1667 = false;
			if (((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double)))) {
				int _m1667 = mark();
				synPredMatched1667 = true;
				inputState.guessing++;
				try {
					{
					match(LPAREN);
					builtInTypeSpec(true);
					match(RPAREN);
					unaryExpression();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched1667 = false;
				}
				rewind(_m1667);
inputState.guessing--;
			}
			if ( synPredMatched1667 ) {
				lpb = LT(1);
				lpb_AST = astFactory.create(lpb);
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
				boolean synPredMatched1669 = false;
				if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
					int _m1669 = mark();
					synPredMatched1669 = true;
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
						synPredMatched1669 = false;
					}
					rewind(_m1669);
inputState.guessing--;
				}
				if ( synPredMatched1669 ) {
					lp = LT(1);
					lp_AST = astFactory.create(lp);
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
				else if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
					postfixExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}
		
	public final void postfixExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST postfixExpression_AST = null;
		Token  lp = null;
		AST lp_AST = null;
		Token  lp3 = null;
		AST lp3_AST = null;
		Token  lps = null;
		AST lps_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		Token  in = null;
		AST in_AST = null;
		Token  de = null;
		AST de_AST = null;
		
		primaryExpression();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop1678:
		do {
			if ((LA(1)==DOT) && (_tokenSet_44.member(LA(2)))) {
				AST tmp218_AST = null;
				tmp218_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp218_AST);
				match(DOT);
				if ( inputState.guessing==0 ) {
					pushStartLineColumn();
				}
				{
				switch ( LA(1)) {
				case LT:
				{
					typeArguments();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case IDENT:
				case LITERAL_super:
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
				case IDENT:
				{
					AST tmp219_AST = null;
					tmp219_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp219_AST);
					match(IDENT);
					{
					switch ( LA(1)) {
					case LPAREN:
					{
						lp = LT(1);
						lp_AST = astFactory.create(lp);
						astFactory.makeASTRoot(currentAST, lp_AST);
						match(LPAREN);
						if ( inputState.guessing==0 ) {
							lp_AST.setType(METHOD_CALL);
						}
						argList();
						astFactory.addASTChild(currentAST, returnAST);
						match(RPAREN);
						break;
					}
					case SEMI:
					case LBRACK:
					case RBRACK:
					case DOT:
					case QUESTION:
					case LT:
					case COMMA:
					case GT:
					case SR:
					case BSR:
					case STAR:
					case RPAREN:
					case RCURLY:
					case BAND:
					case ASSIGN:
					case COLON:
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
					case LOR:
					case LAND:
					case BOR:
					case BXOR:
					case NOT_EQUAL:
					case EQUAL:
					case LE:
					case GE:
					case LITERAL_instanceof:
					case SL:
					case PLUS:
					case MINUS:
					case DIV:
					case MOD:
					case INC:
					case DEC:
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
				case LITERAL_super:
				{
					if ( inputState.guessing==0 ) {
						pushStartLineColumn();
					}
					AST tmp221_AST = null;
					tmp221_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp221_AST);
					match(LITERAL_super);
					{
					switch ( LA(1)) {
					case LPAREN:
					{
						lp3 = LT(1);
						lp3_AST = astFactory.create(lp3);
						astFactory.makeASTRoot(currentAST, lp3_AST);
						match(LPAREN);
						argList();
						astFactory.addASTChild(currentAST, returnAST);
						match(RPAREN);
						if ( inputState.guessing==0 ) {
							lp3_AST.setType(SUPER_CTOR_CALL);
						}
						break;
					}
					case DOT:
					{
						AST tmp223_AST = null;
						tmp223_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp223_AST);
						match(DOT);
						if ( inputState.guessing==0 ) {
							pushStartLineColumn();
						}
						{
						switch ( LA(1)) {
						case LT:
						{
							typeArguments();
							astFactory.addASTChild(currentAST, returnAST);
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
						AST tmp224_AST = null;
						tmp224_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp224_AST);
						match(IDENT);
						{
						switch ( LA(1)) {
						case LPAREN:
						{
							lps = LT(1);
							lps_AST = astFactory.create(lps);
							astFactory.makeASTRoot(currentAST, lps_AST);
							match(LPAREN);
							if ( inputState.guessing==0 ) {
								lps_AST.setType(METHOD_CALL);
							}
							argList();
							astFactory.addASTChild(currentAST, returnAST);
							match(RPAREN);
							break;
						}
						case SEMI:
						case LBRACK:
						case RBRACK:
						case DOT:
						case QUESTION:
						case LT:
						case COMMA:
						case GT:
						case SR:
						case BSR:
						case STAR:
						case RPAREN:
						case RCURLY:
						case BAND:
						case ASSIGN:
						case COLON:
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
						case LOR:
						case LAND:
						case BOR:
						case BXOR:
						case NOT_EQUAL:
						case EQUAL:
						case LE:
						case GE:
						case LITERAL_instanceof:
						case SL:
						case PLUS:
						case MINUS:
						case DIV:
						case MOD:
						case INC:
						case DEC:
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
							registLineColumn(lps_AST);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						registLineColumn(lp3_AST);
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					registLineColumn(lp_AST);
				}
			}
			else if ((LA(1)==DOT) && (LA(2)==LITERAL_this)) {
				AST tmp226_AST = null;
				tmp226_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp226_AST);
				match(DOT);
				AST tmp227_AST = null;
				tmp227_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp227_AST);
				match(LITERAL_this);
			}
			else if ((LA(1)==DOT) && (LA(2)==LITERAL_new)) {
				AST tmp228_AST = null;
				tmp228_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp228_AST);
				match(DOT);
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==LBRACK)) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(INDEX_OP);
				}
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				match(RBRACK);
			}
			else {
				break _loop1678;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case INC:
		{
			in = LT(1);
			in_AST = astFactory.create(in);
			astFactory.makeASTRoot(currentAST, in_AST);
			match(INC);
			if ( inputState.guessing==0 ) {
				in_AST.setType(POST_INC);
			}
			break;
		}
		case DEC:
		{
			de = LT(1);
			de_AST = astFactory.create(de);
			astFactory.makeASTRoot(currentAST, de_AST);
			match(DEC);
			if ( inputState.guessing==0 ) {
				de_AST.setType(POST_DEC);
			}
			break;
		}
		case SEMI:
		case RBRACK:
		case QUESTION:
		case LT:
		case COMMA:
		case GT:
		case SR:
		case BSR:
		case STAR:
		case RPAREN:
		case RCURLY:
		case BAND:
		case ASSIGN:
		case COLON:
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
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case NOT_EQUAL:
		case EQUAL:
		case LE:
		case GE:
		case LITERAL_instanceof:
		case SL:
		case PLUS:
		case MINUS:
		case DIV:
		case MOD:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		postfixExpression_AST = (AST)currentAST.root;
		returnAST = postfixExpression_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;
		Token  lbt = null;
		AST lbt_AST = null;
		
		switch ( LA(1)) {
		case IDENT:
		case LT:
		{
			identPrimary();
			astFactory.addASTChild(currentAST, returnAST);
			{
			if ((LA(1)==DOT) && (LA(2)==LITERAL_class)) {
				AST tmp230_AST = null;
				tmp230_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp230_AST);
				match(DOT);
				AST tmp231_AST = null;
				tmp231_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp231_AST);
				match(LITERAL_class);
			}
			else if ((_tokenSet_45.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case CHAR_LITERAL:
		case STRING_LITERAL:
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		{
			constant();
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_true:
		{
			AST tmp232_AST = null;
			tmp232_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp232_AST);
			match(LITERAL_true);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_false:
		{
			AST tmp233_AST = null;
			tmp233_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp233_AST);
			match(LITERAL_false);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_null:
		{
			AST tmp234_AST = null;
			tmp234_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp234_AST);
			match(LITERAL_null);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_new:
		{
			newExpression();
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_this:
		{
			AST tmp235_AST = null;
			tmp235_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp235_AST);
			match(LITERAL_this);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LITERAL_super:
		{
			AST tmp236_AST = null;
			tmp236_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp236_AST);
			match(LITERAL_super);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		case LPAREN:
		{
			parenthesesExpression();
			astFactory.addASTChild(currentAST, returnAST);
			primaryExpression_AST = (AST)currentAST.root;
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
		{
			builtInType();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop1683:
			do {
				if ((LA(1)==LBRACK)) {
					lbt = LT(1);
					lbt_AST = astFactory.create(lbt);
					astFactory.makeASTRoot(currentAST, lbt_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lbt_AST.setType(ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop1683;
				}
				
			} while (true);
			}
			AST tmp238_AST = null;
			tmp238_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp238_AST);
			match(DOT);
			AST tmp239_AST = null;
			tmp239_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp239_AST);
			match(LITERAL_class);
			primaryExpression_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = primaryExpression_AST;
	}
	
/** object instantiation.
 *  Trees are built as illustrated by the following input/tree pairs:
 *
 *  new T()
 *
 *  new
 *   |
 *   T --  ELIST
 *		   |
 *		  arg1 -- arg2 -- .. -- argn
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
 *								  |
 *								EXPR -- EXPR
 *								  |	  |
 *								  1	  2
 *
 *  new int[3]
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *				|
 *			  EXPR
 *				|
 *				3
 *
 *  new int[1][2]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *			   |
 *		 ARRAY_DECLARATOR -- EXPR
 *			   |			  |
 *			 EXPR			 1
 *			   |
 *			   2
 *
 */
	public final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		AST tmp240_AST = null;
		tmp240_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp240_AST);
		match(LITERAL_new);
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			astFactory.addASTChild(currentAST, returnAST);
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
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
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
			switch ( LA(1)) {
			case LCURLY:
			{
				classBlock();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case LBRACK:
			case RBRACK:
			case DOT:
			case QUESTION:
			case LT:
			case COMMA:
			case GT:
			case SR:
			case BSR:
			case STAR:
			case RPAREN:
			case RCURLY:
			case BAND:
			case ASSIGN:
			case COLON:
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
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case NOT_EQUAL:
			case EQUAL:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case INC:
			case DEC:
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
		case LBRACK:
		{
			newArrayDeclarator();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LCURLY:
			{
				arrayInitializer();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case LBRACK:
			case RBRACK:
			case DOT:
			case QUESTION:
			case LT:
			case COMMA:
			case GT:
			case SR:
			case BSR:
			case STAR:
			case RPAREN:
			case RCURLY:
			case BAND:
			case ASSIGN:
			case COLON:
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
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case NOT_EQUAL:
			case EQUAL:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case INC:
			case DEC:
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
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			newExpression_AST = (AST)currentAST.root;
			registLineColumn(newExpression_AST);;
		}
		newExpression_AST = (AST)currentAST.root;
		returnAST = newExpression_AST;
	}
	
/** Match a, a.b.c refs, a.b.c(...) refs, a.b.c[], a.b.c[].class,
 *  and a.b.c.class refs. Also this(...) and super(...). Match
 *  this or super.
 */
	public final void identPrimary() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identPrimary_AST = null;
		AST ta1_AST = null;
		AST ta2_AST = null;
		Token  lp = null;
		AST lp_AST = null;
		Token  lbc = null;
		AST lbc_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		{
		switch ( LA(1)) {
		case LT:
		{
			typeArguments();
			ta1_AST = (AST)returnAST;
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
		AST tmp243_AST = null;
		tmp243_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp243_AST);
		match(IDENT);
		{
		_loop1692:
		do {
			boolean synPredMatched1690 = false;
			if (((LA(1)==DOT) && (LA(2)==IDENT||LA(2)==LT))) {
				int _m1690 = mark();
				synPredMatched1690 = true;
				inputState.guessing++;
				try {
					{
					match(DOT);
					if ( inputState.guessing==0 ) {
						registLineColumn(null); pushStartLineColumn();
					}
					{
					switch ( LA(1)) {
					case LT:
					{
						typeArguments();
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
					match(IDENT);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched1690 = false;
				}
				rewind(_m1690);
inputState.guessing--;
			}
			if ( synPredMatched1690 ) {
				AST tmp244_AST = null;
				tmp244_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp244_AST);
				match(DOT);
				if ( inputState.guessing==0 ) {
					registLineColumn(null); pushStartLineColumn();
				}
				{
				switch ( LA(1)) {
				case LT:
				{
					typeArguments();
					ta2_AST = (AST)returnAST;
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
				AST tmp245_AST = null;
				tmp245_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp245_AST);
				match(IDENT);
			}
			else if (((_tokenSet_47.member(LA(1))) && (_tokenSet_46.member(LA(2))))&&(false)) {
			}
			else {
				break _loop1692;
			}
			
		} while (true);
		}
		{
		if ((LA(1)==LPAREN)) {
			{
			lp = LT(1);
			lp_AST = astFactory.create(lp);
			astFactory.makeASTRoot(currentAST, lp_AST);
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				lp_AST.setType(METHOD_CALL);
			}
			if ( inputState.guessing==0 ) {
				if (ta2_AST != null) astFactory.addASTChild(currentAST, ta2_AST);
			}
			if ( inputState.guessing==0 ) {
				if (ta2_AST == null) astFactory.addASTChild(currentAST, ta1_AST);
			}
			argList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			}
		}
		else if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
			{
			int _cnt1696=0;
			_loop1696:
			do {
				if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
					lbc = LT(1);
					lbc_AST = astFactory.create(lbc);
					astFactory.makeASTRoot(currentAST, lbc_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lbc_AST.setType(ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					if ( _cnt1696>=1 ) { break _loop1696; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt1696++;
			} while (true);
			}
		}
		else if ((_tokenSet_45.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			registLineColumn(lp_AST);
		}
		identPrimary_AST = (AST)currentAST.root;
		returnAST = identPrimary_AST;
	}
	
	public final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;
		
		switch ( LA(1)) {
		case NUM_INT:
		{
			AST tmp248_AST = null;
			tmp248_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp248_AST);
			match(NUM_INT);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case CHAR_LITERAL:
		{
			AST tmp249_AST = null;
			tmp249_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp249_AST);
			match(CHAR_LITERAL);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case STRING_LITERAL:
		{
			AST tmp250_AST = null;
			tmp250_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp250_AST);
			match(STRING_LITERAL);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case NUM_FLOAT:
		{
			AST tmp251_AST = null;
			tmp251_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp251_AST);
			match(NUM_FLOAT);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case NUM_LONG:
		{
			AST tmp252_AST = null;
			tmp252_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp252_AST);
			match(NUM_LONG);
			constant_AST = (AST)currentAST.root;
			break;
		}
		case NUM_DOUBLE:
		{
			AST tmp253_AST = null;
			tmp253_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp253_AST);
			match(NUM_DOUBLE);
			constant_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = constant_AST;
	}
	
	public final void parenthesesExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parenthesesExpression_AST = null;
		Token  lp = null;
		AST lp_AST = null;
		
		if ( inputState.guessing==0 ) {
			pushStartLineColumn();
		}
		lp = LT(1);
		lp_AST = astFactory.create(lp);
		astFactory.makeASTRoot(currentAST, lp_AST);
		match(LPAREN);
		if ( inputState.guessing==0 ) {
			lp_AST.setType(PAREN_EXPR);
		}
		assignmentExpression();
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			parenthesesExpression_AST = (AST)currentAST.root;
			registLineColumn(parenthesesExpression_AST);
		}
		parenthesesExpression_AST = (AST)currentAST.root;
		returnAST = parenthesesExpression_AST;
	}
	
	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newArrayDeclarator_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		{
		int _cnt1707=0;
		_loop1707:
		do {
			if ((LA(1)==LBRACK) && (_tokenSet_48.member(LA(2)))) {
				lb = LT(1);
				lb_AST = astFactory.create(lb);
				astFactory.makeASTRoot(currentAST, lb_AST);
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					lb_AST.setType(ARRAY_INSTANTIATION);
				}
				{
				switch ( LA(1)) {
				case IDENT:
				case LITERAL_super:
				case LT:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case LPAREN:
				case CHAR_LITERAL:
				case STRING_LITERAL:
				case LITERAL_this:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_null:
				case LITERAL_new:
				case NUM_INT:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_DOUBLE:
				{
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
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
				match(RBRACK);
			}
			else {
				if ( _cnt1707>=1 ) { break _loop1707; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt1707++;
		} while (true);
		}
		newArrayDeclarator_AST = (AST)currentAST.root;
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
		"LOCAL_VARIABLE_DEF",
		"FIELD_DEF",
		"INSTANCE_INIT",
		"STATIC_INIT",
		"TYPE",
		"NAME",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"PACKAGE_DEF",
		"ARRAY_DECLARATOR",
		"ARRAY_INSTANTIATION",
		"EXTENDS_CLAUSE",
		"IMPLEMENTS_CLAUSE",
		"PARAMETERS",
		"METHOD_PARAMETER_DEF",
		"LOCAL_PARAMETER_DEF",
		"LABELED_STAT",
		"TYPECAST",
		"INDEX_OP",
		"POST_INC",
		"POST_DEC",
		"METHOD_CALL",
		"EXPR",
		"EXPR_STATE",
		"ARRAY_INIT",
		"CLASS_IMPORT",
		"MEMBER_IMPORT",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"CASE_GROUP",
		"ELIST",
		"FOR",
		"FOREACH",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"EMPTY_STAT",
		"\"final\"",
		"\"abstract\"",
		"\"strictfp\"",
		"SUPER_CTOR_CALL",
		"CTOR_CALL",
		"VARIABLE_PARAMETER_DEF",
		"STATIC_IMPORT",
		"ENUM_DEF",
		"ENUM_CONSTANT_DEF",
		"FOREACH_CLAUSE",
		"FOREACH_VARIABLE",
		"FOREACH_EXPRESSION",
		"ANNOTATION_DEF",
		"ANNOTATION_MEMBER",
		"ANNOTATIONS",
		"ANNOTATION",
		"ANNOTATION_STRING",
		"ANNOTATION_MEMBER_VALUE_PAIR",
		"ANNOTATION_FIELD_DEF",
		"ANNOTATION_ARRAY_INIT",
		"TYPE_ARGUMENTS",
		"TYPE_ARGUMENT",
		"TYPE_PARAMETERS",
		"TYPE_PARAMETER",
		"WILDCARD_TYPE",
		"TYPE_UPPER_BOUNDS",
		"TYPE_LOWER_BOUNDS",
		"TYPE_ADDITIONAL_BOUNDS",
		"COND_CLAUSE",
		"LOCAL_VARIABLE_DEF_STATE",
		"PAREN_EXPR",
		"THROWS_CLAUSE",
		"\"package\"",
		"SEMI",
		"\"import\"",
		"\"static\"",
		"LBRACK",
		"RBRACK",
		"IDENT",
		"DOT",
		"QUESTION",
		"\"extends\"",
		"\"super\"",
		"LT",
		"COMMA",
		"GT",
		"SR",
		"BSR",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"STAR",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"transient\"",
		"\"native\"",
		"\"threadsafe\"",
		"\"synchronized\"",
		"\"volatile\"",
		"AT",
		"LPAREN",
		"RPAREN",
		"LCURLY",
		"RCURLY",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"\"class\"",
		"\"interface\"",
		"\"enum\"",
		"BAND",
		"\"default\"",
		"\"implements\"",
		"\"this\"",
		"ASSIGN",
		"\"throws\"",
		"TRIPLE_DOT",
		"COLON",
		"\"if\"",
		"\"while\"",
		"\"do\"",
		"\"break\"",
		"\"continue\"",
		"\"return\"",
		"\"switch\"",
		"\"throw\"",
		"\"assert\"",
		"\"else\"",
		"\"for\"",
		"\"case\"",
		"\"try\"",
		"\"finally\"",
		"\"catch\"",
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
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"NOT_EQUAL",
		"EQUAL",
		"LE",
		"GE",
		"\"instanceof\"",
		"SL",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"BNOT",
		"LNOT",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"\"new\"",
		"NUM_INT",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"DOC_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"HEXADECIMAL_FP",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 985162418487298L, 505526859149541376L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 985162418487298L, 505526859151507456L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 985162418487296L, 505526859149410304L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 985162418487298L, 505526859149410304L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 985162418487296L, 560750930427904L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 0L, 1097374629888L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 0L, -1712492658863570944L, 34359476230L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 985162418487296L, -196608L, 36028797018767358L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 985162418487296L, 514535155971751936L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = new long[8];
		data[0]=-16L;
		data[1]=-57983845202395137L;
		data[2]=-1L;
		data[3]=1L;
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 0L, 4667419161283002368L, 36021650193383424L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 0L, 2310347708237348864L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 985162418487296L, 2826007664606904320L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 985162418487296L, 505527956582694912L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 985162418487296L, 506653856494256128L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 0L, 1097366241280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 0L, 73924608L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 0L, -9223372036719968256L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 140737488355328L, 564047319662592L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 0L, 564047391490048L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 0L, 564047391490048L, 2L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 985162418487296L, 4744541105878794240L, 36021650193428472L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 0L, 4611686018528051200L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 0L, 1126997281472512L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 985162418487296L, 4753548305133535232L, 36021650193428472L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 985162418487296L, -3461016313786466304L, 36028797018746876L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 985162418487296L, 5906469809740382208L, 36021650193448952L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 985162418487296L, -2308094809179619328L, 36028797018963964L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 985162418487296L, 1124798250090496L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 985162418487296L, 1124798321917952L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 0L, 4666856211329581056L, 36021650193383424L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 0L, -3980053971817922560L, 36028797018701824L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 985162418487296L, 73181294921777152L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 985162418487296L, 73181294923874304L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 0L, 4671359810956951552L, 36021650193383424L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 985162418487296L, 4667979912213495808L, 36021650193383424L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 985162418487296L, -3978930270799855616L, 36028797018701824L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 0L, 4666856211329581056L, 36021650193383428L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 0L, -3980053971683704832L, 36028797018701824L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 0L, 335544320L, 103079215104L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 0L, 1610612736L, 274877906944L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 0L, 1099511627776L, 6597069766656L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 0L, 4666856211329581056L, 35888059530608640L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 0L, -3968794972614230016L, 36028797018701828L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 0L, 102760448L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { 0L, -8635651183876702208L, 35184371826692L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = { 985162418487296L, -2305843009230667776L, 36028797018767356L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 0L, -8634525283969859584L, 35184371826692L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { 0L, 4666856211330629632L, 36021650193383424L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	
	}
