////////////////////////////////////////////////////////////////////////////////
// This grammar file for cSharp
// Copyright (C) 2003  Virtusa Corporation
//
// Author : K.Rajendra Kumar 
//
////////////////////////////////////////////////////////////////////////////////
header {
package jp.ac.osaka_u.ist.sel.metricstool.main.parse;

import antlr.CommonAST;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp.CSharpASTNodeFactory;
}

/** cSharp Recognizer
 *
 * Based heavily on the Grammer example that comes with ANTLR. See
 * http://www.antlr.org.
 *
 * This grammar is in the PUBLIC DOMAIN
 */
class CSharpParser extends Parser;

options {
	ASTLabelType=CommonAST;
	k = 2;                           	// two token lookahead
	exportVocab=CSharp;       	// Call its vocabulary "GeneratedCSharp"
	codeGenMakeSwitchThreshold = 2;  	// Some optimizations
	codeGenBitsetTestThreshold = 3;
	defaultErrorHandler = true;     	// Don't generate parser error handlers
	buildAST = true;
}

tokens {
	BLOCK; MODIFIERS; OBJBLOCK; SLIST; CTOR_DEF; METHOD_DEF; VARIABLE_DEF;
	INSTANCE_INIT; STATIC_INIT; TYPE; CLASS_DEF; INTERFACE_DEF;
	PACKAGE_DEF; ARRAY_DECLARATOR; EXTENDS_CLAUSE; IMPLEMENTS_CLAUSE;
	PARAMETERS; PARAMETER_DEF; LABELED_STAT; TYPECAST; INDEX_OP;
	POST_INC; POST_DEC; METHOD_CALL; EXPR; ARRAY_INIT;
	IMPORT; UNARY_MINUS; UNARY_PLUS; CASE_GROUP; ELIST; FOR_INIT; FOR_CONDITION;
	FOR_ITERATOR; FOR_EACH_CLAUSE; EMPTY_STAT; FINAL="final"; ABSTRACT="abstract";
	STRICTFP="strictfp"; SUPER_CTOR_CALL; CTOR_CALL; PROPERTY_DEF; ENUM_DEF; STRUCT_DEF;
	SCTOR_DEF; EXPR_STATE; FIELD_DEF; NAME; ENUM_CONSTANT_DEF; LOCAL_PARAMETER_DEF;
	ARRAY_INSTANTIATION; COND_CLAUSE; LOCAL_VARIABLE_DEF;
	PROPERTY_SET_BODY; PROPERTY_GET_BODY;
	IF; ELSE; OPER_OVERLOAD_DEF;
}

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
}
// Compilation Unit: In cSharp, this is a single file.  This is the start
//   rule for this parser
compilationUnit
	:	
	
		( symbolDefinition )*
		(region!)*
		// Next we have a series of zero or more import statements
		( usingDefinition )*
		(region!)*
		// A compilation unit starts with an optional package definition
		(customAttribute!)*
		(region!)*
		(	packageDefinition
		|	typeDefinition
		)*
		(region!)*
		
		EOF!
	;

symbolDefinition
	:
		HASH! "define"^ identifier
	; 

// Package statement: "package" followed by an identifier.
packageDefinition
	options {defaultErrorHandler = true;} // let ANTLR handle errors
	:	packageHead LCURLY! (typeDefinition)* RCURLY!
	
	;
	
packageHead
	: 
		p:"namespace"^ {#p.setType(PACKAGE_DEF);} identifier
	;

// Using statement: using followed by a package or class name
usingDefinition
	options {defaultErrorHandler = true;}
	:	i:"using"^ (IDENT ASSIGN)? {#i.setType(IMPORT);} identifierStar SEMI!
	;

// A type definition in a file is either a class or interface definition.
typeDefinition
	options {defaultErrorHandler = true;}
	:
		(region!)*
		(customAttribute!)*
		m:modifiers!
		( classDefinition[#m]
		| interfaceDefinition[#m]
		| structDefinition[#m]
		| enumDefinition[#m]
		)
		(region!)*
	|	SEMI
		
	
	;

/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
declaration!
	:	m:modifiers t:typeSpec[false] v:variableDefinitions[#m,#t]
		{#declaration = #v;}
	;

// A type specification is a type name with possible brackets afterwards
//   (which would make it an array type).
typeSpec[boolean addImagNode]
	: (classTypeSpec[addImagNode]
	| builtInTypeSpec[addImagNode])
	;

// A class type specification is a class type with possible brackets afterwards
//   (which would make it an array type).
classTypeSpec[boolean addImagNode]
	:	identifier (lb:LBRACK^ (COMMA)* {#lb.setType(ARRAY_DECLARATOR);} RBRACK!)*
		(STAR!)?
		{
			if ( addImagNode ) {
				#classTypeSpec = #(#[TYPE,"TYPE"], #classTypeSpec);
			}
		}
		
	;

// A builtin type specification is a builtin type with possible brackets
// afterwards (which would make it an array type).
builtInTypeSpec[boolean addImagNode]
	:	builtInType (lb:LBRACK^ (COMMA)* {#lb.setType(ARRAY_DECLARATOR);} RBRACK!)*
		(STAR!)?
		{
			if ( addImagNode ) {
				#builtInTypeSpec = #(#[TYPE,"TYPE"], #builtInTypeSpec);
			}
		}
		
	;

// A type name. which is either a (possibly qualified) class name or
//   a primitive (builtin) type
type
	:	i:identifier {#type = #(#[TYPE,"TYPE"], #type);}
	|	builtInType
	;

// The primitive types.
builtInType
	:	"void"
	|	"boolean"
	|	"byte"
	|	"char"
	|	"short"
	|	"int"
	|	"float"
	|	"long"
	|	"double"
	|	"bool"
	|	"sbyte"
	|	"uint"
	|	"ulong"
	|	"ushort"
	|	"decimal"
	;

// A (possibly-qualified) csharp identifier.  We start with the first IDENT
//   and expand its name by adding dots and following IDENTS
identifier
	:	IDENT  ( DOT^ IDENT )*
	;

identifierStar
	:	IDENT
		( DOT^ IDENT )*
		( DOT^ STAR  )?
	;

// A list of zero or more modifiers.  We could have used (modifier)* in
//   place of a call to modifiers, but I thought it was a good idea to keep
//   this rule separate so they can easily be collected in a Vector if
//   someone so desires
modifiers
	:	( modifier )*
		{#modifiers = #([MODIFIERS, "MODIFIERS"], #modifiers);}
	;

// modifiers for cSharp classes, interfaces, class/instance vars and methods
modifier
	:	"private"
	|	"public"
	|	"protected"
	|	"static"
	|	"transient"
	|	"final"
	|	"abstract"
	|	"native"
	|	"synchronized"
	|	"const"
	|	"volatile"
	|	"strictfp"
	|	"override"
	|	"sealed"
	|	"virtual"
	|	"internal"
	|	"internal protected"
	|	"extern"
	|	"readonly"
	|	"event"
	|	"delegate"
	|	"new"
	|	"unsafe"
	;




region 
	options {defaultErrorHandler=true;}
	: HASH (IDENT | ("if" IDENT) | "elif" | "else" | "endif")
;

regionNotIf
	options {defaultErrorHandler=true;}
	: HASH IDENT
;

//directiveIf
//	: HASH "if"^ (expression)?
//;
/*
endregion 
	options {defaultErrorHandler = true;}	
	: // ("#endregion") => er: (PREPROC)* 
;
*/


enumDefinition![CommonAST modifiers]
	:	"enum"^ n:name (COLON type)?
		LCURLY!
		  ( name (COMMA)? )* 
		RCURLY! SEMI
	;

structDefinition![CommonAST modifiers]
	:	"struct" n:name
		// it might implement some interfaces...
		sc:superClassClause
		// now parse the body of the class
		sb:structBlock
		{#structDefinition = #(#[CLASS_DEF,"STRUCT_DEF"],
					   modifiers,n,sc,sb);}
	;

// Definition of a cSharp class
classDefinition![CommonAST modifiers]
	:	"class" 
		n:name
		// it _might_ have a superclass...
		sc:superClassClause
		// it might implement some interfaces...
		ic:implementsClause
		// now parse the body of the class
		cb:classBlock
		{#classDefinition = #(#[CLASS_DEF,"CLASS_DEF"],
							   modifiers,n,sc,ic,cb);}
	;


superClassClause!
	:	( (COLON^)? (id:identifier (COMMA)? )* )
		{#superClassClause = #(#[EXTENDS_CLAUSE,"EXTENDS_CLAUSE"],id);}
	;

// Definition of a cSharp Interface
interfaceDefinition![CommonAST modifiers]
	:	"interface"
		n:name
		// it might extend some other interfaces
		ie:interfaceExtends
		// now parse the body of the interface (looks like a class...)
		cb:classBlock
		{#interfaceDefinition = #(#[INTERFACE_DEF,"INTERFACE_DEF"],
									modifiers,n,ie,cb);}
	;

// This is the body of a class.  You can have fields and extra semicolons,
// That's about it (until you see what a field is...)
classBlock
	:	LCURLY!
			(((region!)*  field (region!)*) | SEMI! )*
		RCURLY!
		{#classBlock = #([OBJBLOCK, "OBJBLOCK"], #classBlock);}
	;

// This is the body of a struct.  You can have fields and extra semicolons,
structBlock
	:	LCURLY!
			(((region!)*  field (region!)*  ) | SEMI )*
		RCURLY!
		{#structBlock = #([OBJBLOCK, "OBJBLOCK"], #structBlock);}
	;

// An interface can extend several other interfaces...
interfaceExtends
	:	(
		e:"extends"!
		identifier ( COMMA identifier )*
		)?
		{#interfaceExtends = #(#[EXTENDS_CLAUSE,"EXTENDS_CLAUSE"],
							#interfaceExtends);}
	;

// A class can implement several interfaces...
implementsClause
	:	(
			i:":"! identifier ( COMMA identifier )*
		)?
		{#implementsClause = #(#[IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE"],
								 #implementsClause);}
	;
	


// Now the various things that can be defined inside a class or interface...
// Note that not all of these are really valid in an interface (constructors,
//   for example), and if this grammar were used for a compiler there would
//   need to be some semantic checks to make sure we're doing the right thing...
field!
	:	// method, constructor, or variable declaration
		(customAttribute!)*
		mods:modifiers
		(	(ctorHead constructorBody) => h:ctorHead s:constructorBody // constructor
			{#field = #(#[CTOR_DEF,"CTOR_DEF"], mods, h, s);}
		
		|	(structHead structBody) => sh:structHead sb:structBody // struct constructor
			{#field = (#(#[SCTOR_DEF,"SCTOR_DEF"], mods, sh, sb));}
		
		|	(enumHead enumBody) => enh:enumHead enb:enumBody
			{#field = (#(#[ENUM_DEF, "ENUM_DEF"], mods, enh, enb));}
			
		|	(type propertyHead propertyBody ) => pt:type p:propertyHead pb:propertyBody //PropertyHeader
			{#field =(#(#[PROPERTY_DEF, "PROPERTY_DEF"], mods, #(#[TYPE,"TYPE"],pt), p, pb));}

		//|	(type propertyHead propertyBody ) => pt:type (LBRACK RBRACK)? pn:name pb:propertyBody2[#mods, #(#[TYPE,"TYPE"],pt), #pn] //PropertyHeader
		//	{#field =(#(#[PROPERTY_DEF, "PROPERTY_DEF"], mods, pb));}
		|	cd:classDefinition[#mods]       // inner class
			{#field = #cd;}

		|	id:interfaceDefinition[#mods]   // inner interface
			{#field = #id;}
		
		|
			("implicit"! | "explicit"!)? "operator"
			t2:typeSpec[true]
			LPAREN! pr:parameterDeclarationList RPAREN!
			cs:compoundStatement
			{#field = (#(#[OPER_OVERLOAD_DEF,"OPER_OVERLOAD_DEF"], mods, t2, pr, cs));}
		| 
			t:typeSpec[false]  // method or variable declaration(s)
			(
				modifiers
			
				n:name   // the name of the method

				// parse the formal parameter declarations.
				LPAREN! param:parameterDeclarationList RPAREN!

				rt:declaratorBrackets[#t] 

				// get the list of exceptions that this method is
				// declared to throw
				(tc:throwsClause)?

				( s2: compoundStatement | SEMI )
				{#field = (#(#[METHOD_DEF,"METHOD_DEF"], mods, #(#[TYPE,"TYPE"],rt),n, param, tc, s2));}
				
    				
			|	f:fieldDefinitions[#mods,#t] SEMI
//				{#field = #(#[VARIABLE_DEF,"VARIABLE_DEF"], v);}
				{#field = #f;}
				
				
			)
		
		
		
		)

    // "static { ... }" class initializer
	|	"static" s3:compoundStatement
		{#field = #(#[STATIC_INIT,"STATIC_INIT"], s3);}

    // "{ ... }" instance initializer
	|	s4:compoundStatement
		{#field = #(#[INSTANCE_INIT,"INSTANCE_INIT"], s4);}
	;
	
constructorBody
    :   lc:LCURLY^ {#lc.setType(BLOCK);}
		// Predicate might be slow but only checked once per constructor def
		// not for general methods.
		(	(explicitConstructorInvocation) => explicitConstructorInvocation
		|
		)
        (statement)*
        RCURLY!
    ;

structBody
    :   lc:LCURLY^ {#lc.setType(BLOCK);}
		// Predicate might be slow but only checked once per constructor def
		// not for general methods.
		(	(explicitConstructorInvocation) => explicitConstructorInvocation
		|
		)
        (statement)*
        RCURLY!
    ;
    
propertyBody
    :   pc: LCURLY!
    		(   
    		   (propinnerBody)*
   		)
	    RCURLY!
	;
	
propertyBody2[CommonAST modifiers, CommonAST type, CommonAST propertyName]
   :   pc: LCURLY!
    		(   
    		   (propinnerBody2[#modifiers, #type, #propertyName])*
   		)
	    RCURLY!
	;

propinnerBody
   : pib :IDENT^
   {
       if(#pib.getText().equals("set")){
       		#pib.setType(PROPERTY_SET_BODY);
       } else if(#pib.getText().equals("get")) {
       		#pib.setType(PROPERTY_GET_BODY);
       }
   }
   (LCURLY! (statement)* RCURLY!)?  (SEMI)?
   ;
   
setOrget
	: IDENT
	;

propinnerBody2[CommonAST modifiers, CommonAST type, CommonAST propertyName]
   :   	i:IDENT!
		{
			if(#i.getText().equals("set")){
				#propinnerBody2 = (CommonAST)this.astNode.createPropertySetterHeadNode(#modifiers, #type, #propertyName);
			} else if(#i.getText().equals("get")) {
				#propinnerBody2 = (CommonAST)this.astNode.createPropertyGetterHeadNode(#modifiers, #type, #propertyName);
			}
		}
		propinnerBlock
   ;
   
propinnerBlock
	:	(lc:LCURLY^ {#lc.setType(BLOCK);} (statement)* RCURLY!)?  (SEMI)?
	;
   
enumBody
   :	en: LCURLY!
	   	( enumConstant (COMMA)? )* 
	   RCURLY! SEMI!
   ;
   
enumConstant!
	:	i:name
		{#enumConstant = #([ENUM_CONSTANT_DEF, "ENUM_CONSTANT_DEF"], i);}
	;
   
name
    :   IDENT
    	{#name = #(#[NAME,"NAME"],#name);}
    ;   

explicitConstructorInvocation
    :   (	options {
				
				generateAmbigWarnings=false;
			}
		:	"this"! lp1:LPAREN^ argList RPAREN! SEMI!
			{#lp1.setType(CTOR_CALL);}

	    |   "base"! lp2:LPAREN^ argList RPAREN! SEMI!
			{#lp2.setType(SUPER_CTOR_CALL);}

			// (new Outer()).super()  (create enclosing instance)
		|	primaryExpression DOT! "base"! lp3:LPAREN^ argList RPAREN! SEMI!
			{#lp3.setType(SUPER_CTOR_CALL);}
		)
    ;
    
fieldDefinitions[CommonAST mods, CommonAST t]
	:	fieldDeclarator[(CommonAST) getASTFactory().dupTree(mods),
						   (CommonAST) getASTFactory().dupTree(t)]
		(	COMMA
			fieldDeclarator[(CommonAST) getASTFactory().dupTree(mods),
							   (CommonAST) getASTFactory().dupTree(t)]
		)*
	;
	
fieldDeclarator![CommonAST mods, CommonAST t]
	:	id:name d:declaratorBrackets[t] v:varInitializer
		{#fieldDeclarator = #(#[FIELD_DEF,"FIELD_DEF"], mods, #(#[TYPE,"TYPE"],d), id, v);}
	;

variableDefinitions[CommonAST mods, CommonAST t]
	:	variableDeclarator[(CommonAST) getASTFactory().dupTree(mods),
						   (CommonAST) getASTFactory().dupTree(t)]
		(	COMMA
			variableDeclarator[(CommonAST) getASTFactory().dupTree(mods),
							   (CommonAST) getASTFactory().dupTree(t)]
		)*
	;
	
/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
variableDeclarator![CommonAST mods, CommonAST t]
	:	id:name d:declaratorBrackets[t] v:varInitializer
		{#variableDeclarator = #(#[LOCAL_VARIABLE_DEF,"LOCAL_VARIABLE_DEF"], mods, #(#[TYPE,"TYPE"],d), id, v);}
	;

declaratorBrackets[CommonAST typ]
	:	{#declaratorBrackets=typ;}
		(lb:LBRACK^ {#lb.setType(ARRAY_DECLARATOR);} (COMMA)* RBRACK!)*
		
	;

varInitializer
	:	( ASSIGN^ initializer )?
	;

// This is an initializer used to set up an array.
arrayInitializer
	:	lc:LCURLY^ {#lc.setType(ARRAY_INIT);}
			(	initializer
				(
					// CONFLICT: does a COMMA after an initializer start a new
					//           initializer or start the option ',' at end?
					//           ANTLR generates proper code by matching
					//			 the comma as soon as possible.
					options {
						warnWhenFollowAmbig = false;
					}
				:
					COMMA initializer
				)*
				(COMMA)?
			)?
		RCURLY
	;


// The two "things" that can initialize an array element are an expression
//   and another (nested) array initializer.
initializer
	:	expression
	|	arrayInitializer
	;



// This is the header of a method.  It includes the name and parameters
//   for the method.
//   This also watches for a list of exception classes in a "throws" clause.
ctorHead
	:	(BNOT!)?
		name  // the name of the method

		// parse the formal parameter declarations.
		LPAREN! parameterDeclarationList RPAREN!
		
//		(COLON^ ("base" | "this") LPAREN! parameterDeclarationList RPAREN!)?
		(explicitCtorInvocation)?

		// get the list of exceptions that this method is declared to throw
		(throwsClause)?
	;
	
explicitCtorInvocation
	:
		COLON!
		(b:"base"^ {#b.setType(SUPER_CTOR_CALL);}| t:"this"^ {#t.setType(CTOR_CALL);})
		LPAREN! argList RPAREN!
		{#explicitCtorInvocation = #(#[EXPR_STATE,"EXPR_STATE"],
									#explicitCtorInvocation);}
	;

// This is the header of a method.  It includes the name and parameters
//   for the method.
structHead
	:	n:name  // the name of the method

		// parse the formal parameter declarations.
		LPAREN! parameterDeclarationList RPAREN!
	;

propertyHead
	:	(LBRACK RBRACK)? n:name // name of the method
	;

enumHead
	: "enum"^ n:name (COLON type)? 
	;

// This is a list of exception classes that the method is declared to throw
throwsClause
	:	"throws"^ identifier ( COMMA identifier )*
	;


// A list of formal parameters
parameterDeclarationList
	:	( ("params")* ("ref")* ("out")* parameterDeclaration ( COMMA ("params")* ("ref")* ("out")* parameterDeclaration )* )?
		{#parameterDeclarationList = #(#[PARAMETERS,"PARAMETERS"],
									#parameterDeclarationList);}
	;

// A formal parameter.
parameterDeclaration!
	:	(customAttribute!)? 
		pm:parameterModifier t:typeSpec[false] id:name
		pd:declaratorBrackets[#t]
		{#parameterDeclaration = #(#[PARAMETER_DEF,"PARAMETER_DEF"],
									pm, #([TYPE,"TYPE"],pd), id);}
	;

parameterModifier
	:	(f:"final")?
		{#parameterModifier = #(#[MODIFIERS,"MODIFIERS"], f);}
	;

// Compound statement.  This is used in many contexts:
//   Inside a class definition prefixed with "static":
//      it is a class initializer
//   Inside a class definition without "static":
//      it is an instance initializer
//   As the body of a method
//   As a completely indepdent braced block of code inside a method
//      it starts a new scope for variable definitions

compoundStatement
	: 
		
		lc:LCURLY^  {#lc.setType(BLOCK);} 		
			// include the (possibly-empty) list of statements
			((regionNotIf)*  statement)* 			
		RCURLY!
		
		
	;

// This production provides a slot for adding additional statement productions.
// It is used to simplify an inherited grammar that includes assert statements
statement
	:
		traditionalStatement
	;

traditionalStatement
	// A list of statements in curly braces -- start a new scope!
	:
		
		compoundStatement

	// declarations are ambiguous with "ID DOT" relative to expression
	// statements.  Must backtrack to be sure.  Could use a semantic
	// predicate to test symbol table to see what the type was coming
	// up, but that's pretty hard without a symbol table ;)
	|	(declaration)=> declaration SEMI!

	// An expression statement.  This could be a method call,
	// assignment statement, or any other expression evaluated for
	// side-effects.
	|	(expression)=> ex:expression SEMI!
		{#traditionalStatement = #(#[EXPR_STATE,"EXPR_STATE"], ex);}

	// class definition
	|	m:modifiers! classDefinition[#m]

	// Attach a label to the front of a statement
	|	IDENT c:COLON^ {#c.setType(LABELED_STAT);} statement

	// If-else statement
	|	i1:"if"^ conditionalClause statement
		(
			// CONFLICT: the old "dangling-else" problem...
			//           ANTLR generates proper code matching
			//			 as soon as possible.  Hush warning.
			options {
				warnWhenFollowAmbig = false;
			}
		:
			elseStatement
		)?
		
	|	HASH! i2:"if"^ {#i2.setType(IF);} d:directiveConditionalClause (statement)*
		
		(
			directiveElifStatement | directiveElseStatement[#i2]
		)?
		
		{#i2 = #(#i2, #d);}
//		(
//			d1:directiveElifStatement
//		)*
//		
//		(
//			d2:directiveElseStatement
//			//{if(null != #d1) #d1 = #(#d2); }
//		)?
//	
		HASH! "endif"!

	// For statement
	|	"for"^
			LPAREN!
				forInit SEMI!   // initializer
				forCond	SEMI!   // condition test
				forIter         // updater
			RPAREN!
			statement                     // statement to loop over
			
	// Foreach statement
	|	"foreach"^
			forEachClause
			statement

	// While statement
	|	"while"^ conditionalClause statement

	// do-while statement
	|	"do"^ statement "while"! conditionalClause SEMI!

	// get out of a loop (or switch)
	|	"break"^ (IDENT)? SEMI!

	// do next iteration of a loop
	|	"continue"^ (IDENT)? SEMI!

	// Return an expression
	|	"return"^ (expression)? SEMI!

	// switch/case statement
	|	"switch"^ conditionalClause lc:LCURLY^ {#lc.setType(BLOCK);}
			( casesGroup )*
		RCURLY!

	// exception try-catch block
	|	tryBlock

	// throw an exception
	|	"throw"^ expression SEMI!

	// synchronize a statement
	|	"synchronized"^ LPAREN! expression RPAREN! compoundStatement

	// empty statement
	|	s:SEMI! {#s.setType(EMPTY_STAT);}
	
	// goto statement
	|	"goto"^ IDENT SEMI!
	
	 
	;
	
conditionalClause
	:	LPAREN! ex:expression RPAREN!
		{#conditionalClause = #(#[COND_CLAUSE,"COND_CLAUSE"], ex);}
	;
	
forEachClause
	:
		LPAREN!
				forEachParameter "in" ex:expression
				//t:type v:variableDeclarator[null, #t] "in" ex:expression
		RPAREN!
		{#forEachClause = #(#[FOR_EACH_CLAUSE,"FOR_EACH_CLAUSE"], forEachClause);}
	;
	
forEachParameter
	:
		modifiers type name
		{#forEachParameter = #(#[LOCAL_VARIABLE_DEF,"LOCAL_VARIABLE_DEF"], #forEachParameter);}
	;
	
directiveConditionalClause
	:	ex:expression
		{#directiveConditionalClause = #(#[COND_CLAUSE,"COND_CLAUSE"], ex);}
	;
	
directiveElseStatement[AST parentIf]
	:	HASH! e:"else"^ (statement)*
		{#parentIf = #(#parentIf, #e);}
	;
	
directiveElifStatement
	:	HASH! i:"elif"^ {#i.setType(IF);} d:directiveConditionalClause (statement)*
		(
				d1:directiveElifStatement
			|
				d2:directiveElseStatement[#i]
		)?
			
		{#i = #(#i, #d);}
		{#directiveElifStatement = #(#[ELSE,"ELSE"], i);}
	;


//directiveElseStatement
//	:	HASH! "else"^ (statement)*
//	;
//	
//directiveElifStatement
//	:	HASH! i:"elif"^ {#i.setType(IF);} directiveConditionalClause (statement)*
//		{#directiveElifStatement = #(#[ELSE,"ELSE"], i);}
//	;

elseStatement
    : "else"^ statement
    ;

casesGroup
	:	(	// CONFLICT: to which case group do the statements bind?
			//           ANTLR generates proper code: it groups the
			//           many "case"/"default" labels together then
			//           follows them with the statements
			options {
				warnWhenFollowAmbig = false;
			}
			:
			aCase
		)+
		caseSList
		{#casesGroup = #([CASE_GROUP, "CASE_GROUP"], #casesGroup);}
	;

aCase
	:	("case"^ expression | "default") COLON!
	;

caseSList
	:	(statement)*
		{#caseSList = #(#[SLIST,"SLIST"],#caseSList);}
	;

// The initializer for a for loop
forInit
		// if it looks like a declaration, it is
	:	(	(declaration)=> declaration
		// otherwise it could be an expression list...
		|	expressionList
		)?
		{#forInit = #(#[FOR_INIT,"FOR_INIT"],#forInit);}
	;

forCond
	:	(expression)?
		{#forCond = #(#[FOR_CONDITION,"FOR_CONDITION"],#forCond);}
	;

forIter
	:	(expressionList)?
		{#forIter = #(#[FOR_ITERATOR,"FOR_ITERATOR"],#forIter);}
	;

// an exception handler try/catch block
tryBlock
	:	"try"^ compoundStatement
		(handler)*
		( finallyHandler )?
	;


// an exception handler
handler
	:	"catch"^ LPAREN! pd:parameterDeclaration RPAREN! compoundStatement
		{#pd.setType(LOCAL_PARAMETER_DEF);}
	;

finallyHandler
    : "finally"^ compoundStatement
    ;


// expressions
// Note that most of these expressions follow the pattern
//   thisLevelExpression :
//       nextHigherPrecedenceExpression
//           (OPERATOR nextHigherPrecedenceExpression)*
// which is a standard recursive definition for a parsing an expression.
// The operators in csharp have the following precedences:
//    lowest  (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
//            (12)  ?:
//            (11)  ||
//            (10)  &&
//            ( 9)  |
//            ( 8)  ^
//            ( 7)  &
//            ( 6)  == !=
//            ( 5)  < <= > >=
//            ( 4)  << >>
//            ( 3)  +(binary) -(binary)
//            ( 2)  * / %
//            ( 1)  ++ -- +(unary) -(unary)  ~  !  (type)
//                  []   () (method call)  . (dot -- identifier qualification)
//                  new   ()  (explicit parenthesis)
//
// the last two are not usually on a precedence chart; I put them in
// to point out that new has a higher precedence than '.', so you
// can validy use
//     new Frame().show()
//
// Note that the above precedence levels map to the rules below...
// Once you have a precedence chart, writing the appropriate rules as below
//   is usually very straightfoward



// the mother of all expressions
expression
	:	assignmentExpression
		{#expression = #(#[EXPR,"EXPR"],#expression);}
	;


// This is a list of expressions.
expressionList
	:	expression (COMMA expression)*
		{#expressionList = #(#[ELIST,"ELIST"], expressionList);}
	;


// assignment expression (level 13)
assignmentExpression
	:	conditionalExpression
		(	(	ASSIGN^
            |   PLUS_ASSIGN^
            |   MINUS_ASSIGN^
            |   STAR_ASSIGN^
            |   DIV_ASSIGN^
            |   MOD_ASSIGN^
            |   SR_ASSIGN^
            |   BSR_ASSIGN^
            |   SL_ASSIGN^
            |   BAND_ASSIGN^
            |   BXOR_ASSIGN^
            |   BOR_ASSIGN^
            )
			assignmentExpression
		)?
	;


// conditional test (level 12)
conditionalExpression
	:	logicalOrExpression
		( QUESTION^ assignmentExpression COLON conditionalExpression )?
	;


// logical or (||)  (level 11)
logicalOrExpression
	:	logicalAndExpression (LOR^ logicalAndExpression)*
	;


// logical and (&&)  (level 10)
logicalAndExpression
	:	inclusiveOrExpression (LAND^ inclusiveOrExpression)*
	;


// bitwise or non-short-circuiting or (|)  (level 9)
inclusiveOrExpression
	:	exclusiveOrExpression (BOR^ exclusiveOrExpression)*
	;


// exclusive or (^)  (level 8)
exclusiveOrExpression
	:	andExpression (BXOR^ andExpression)*
	;


// bitwise or non-short-circuiting and (&)  (level 7)
andExpression
	:	equalityExpression (BAND^ equalityExpression)*
	;


// equality/inequality (==/!=) (level 6)
equalityExpression
	:	relationalExpression ((NOT_EQUAL^ | EQUAL^) relationalExpression)*
	;


// boolean relational expressions (level 5)
relationalExpression
	:	s:shiftExpression
		(	(	(	LT^
				|	GT^
				|	LE^
				|	GE^
				)
				shiftExpression
			)*
		|	"instanceof"^ typeSpec[true]
		|	a:as[#s]!	{#relationalExpression = #a;}
		//a:"as"^ t:typeSpec[true]  {#relationalExpression = (#(#[TYPECAST,"TYPECAST"], #t, #s));}
		//{#field = (#(#[METHOD_DEF,"METHOD_DEF"], mods, #(#[TYPE,"TYPE"],rt),n, param, tc, s2));}
		|	"is"^	typeSpec[true]
		)
	;
	
as[AST e]
	:
		"as"! t:typeSpec[true] 
		{#as = (#(#[TYPECAST,"TYPECAST"], #t, #e));}
	;


// bit shift expressions (level 4)
shiftExpression
	:	additiveExpression ((SL^ | SR^ | BSR^) additiveExpression)*
	;


// binary addition/subtraction (level 3)
additiveExpression
	:	multiplicativeExpression ((PLUS^ | MINUS^) multiplicativeExpression)*
	;


// multiplication/division/modulo (level 2)
multiplicativeExpression
	:	unaryExpression ((STAR^ | DIV^ | MOD^ ) unaryExpression)*
	;

unaryExpression
	:	INC^ unaryExpression
	|	DEC^ unaryExpression
	|	MINUS^ {#MINUS.setType(UNARY_MINUS);} unaryExpression
	|	PLUS^  {#PLUS.setType(UNARY_PLUS);} unaryExpression
	|	unaryExpressionNotPlusMinus
	;

unaryExpressionNotPlusMinus
	:	BNOT^ unaryExpression
	|	LNOT^ unaryExpression

	|	(	// subrule allows option to shut off warnings
			options {
				// "(int" ambig with postfixExpr due to lack of sequence
				// info in linear approximate LL(k).  It's ok.  Shut up.
				generateAmbigWarnings=false;
			}
		:	// If typecast is built in type, must be numeric operand
			// Also, no reason to backtrack if type keyword like int, float...
			lpb:LPAREN^ {#lpb.setType(TYPECAST);} builtInTypeSpec[true] RPAREN!
			unaryExpression

			// Have to backtrack to see if operator follows.  If no operator
			// follows, it's a typecast.  No semantic checking needed to parse.
			// if it _looks_ like a cast, it _is_ a cast; else it's a "(expr)"
		|	(LPAREN classTypeSpec[true] RPAREN unaryExpressionNotPlusMinus)=>
			lp:LPAREN^ {#lp.setType(TYPECAST);} classTypeSpec[true] RPAREN!
			unaryExpressionNotPlusMinus

		|	postfixExpression
		)
	;

// qualified names, array expressions, method invocation, post inc/dec
postfixExpression
	:	primaryExpression // start with a primary

		(	// qualified id (id.id.id.id...) -- build the name
			DOT^ ( IDENT
				| "this"
				| "class"
				| newExpression
				| "base" // ClassName.super.field
				)
			// the above line needs a semantic check to make sure "class"
			// is the _last_ qualifier.

			// allow ClassName[].class
		|	( lbc:LBRACK^ {#lbc.setType(ARRAY_DECLARATOR);} RBRACK! )+
			DOT^ "class"

/*
			// an array indexing operation
		|	lb:LBRACK^ {#lb.setType(INDEX_OP);} 
			   (  (identifier QUOTE) (COMMA)?)+ 
			   RBRACK!
			   */
		
		|	(LBRACK identifier) => pb:LBRACK^ {#pb.setType(INDEX_OP);}  (identifier (COMMA)?)+ RBRACK!
		
		|	(LBRACK expression) => pbs:LBRACK^ {#pbs.setType(INDEX_OP);} expression (COMMA expression)* RBRACK!
		
		|	lp:LPAREN^ {#lp.setType(METHOD_CALL);}
				argList
			RPAREN!
		
		
		)*

		// possibly add on a post-increment or post-decrement.
		// allows INC/DEC on too much, but semantics can check
		(	in:INC^ {#in.setType(POST_INC);}
	 	|	de:DEC^ {#de.setType(POST_DEC);}
		|	// nothing
		)
	;

// the basic element of an expression
primaryExpression
	:	IDENT
	|	constant
	|	"true"
	|	"false"
	|	"this"
	|	"null"
	|	newExpression
	|	LPAREN! assignmentExpression RPAREN!
	|	"base"
		// look for int.class and int[].class
	|	builtInType
		( lbt:LBRACK^ {#lbt.setType(ARRAY_DECLARATOR);} RBRACK! )*
		DOT^ "class"
	;

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
newExpression
	:	"new"^ type
		(	LPAREN! argList RPAREN! (classBlock)?

			

		|	newArrayDeclarator (arrayInitializer)?
		)
	;

argList
	:	(	expressionList
		|	/*nothing*/
			("params")* ("ref")* ("out")* {#argList = #[ELIST,"ELIST"];}
		)
	;

newArrayDeclarator
	:	(
			// CONFLICT:
			// newExpression is a primaryExpression which can be
			// followed by an array index reference.  This is ok,
			// as the generated code will stay in this loop as
			// long as it sees an LBRACK (proper behavior)
			options {
				warnWhenFollowAmbig = false;
			}
		:
			lb:LBRACK^ {#lb.setType(ARRAY_INSTANTIATION);}
				(expression)? ((COMMA) (expression)?)*
			RBRACK!
		)+
	;

constant
	:	NUM_INT
	|	CHAR_LITERAL
	|	STRING_LITERAL
	|	NUM_FLOAT
	|	NUM_LONG
	|	NUM_DOUBLE
	;
	
customAttribute
	: 
		(region!)*
		LBRACK^ (IDENT COLON! )? expression RBRACK!
	;


//----------------------------------------------------------------------------
// The csharp Scanner
//----------------------------------------------------------------------------
class CSharpLexer extends Lexer;

options {
	exportVocab=CSharp; // call the vocabulary "cSharp"
	testLiterals=false;        // don't automatically test for literals
	k=4;                       // four characters of lookahead
	charVocabulary='\u0003'..'\uFFFF';
	
	codeGenBitsetTestThreshold=20;
}

// cSharpLexer verbatim source code
{

    @Override
    public void tab()
    {
        setColumn( getColumn() + 1 );
    }

   // private FileContents mFileContents = null;

    // TODO: Check visibility of this method one parsing is done in central
    // utility method
   /* public void setFileContents(FileContents aContents)
    { 
        mFileContents = aContents;
    }
	*/
}


// OPERATORS
QUESTION		:	'?'		;
LPAREN			:	'('		;
RPAREN			:	')'		;
LBRACK			:	'['		;
RBRACK			:	']'		;
LCURLY			:	'{'		;
RCURLY			:	'}'		;
COLON			:	':'		;
COMMA			:	','		;
//DOT			:	'.'		;
ASSIGN			:	'='		;
EQUAL			:	"=="	;
LNOT			:	'!'		;
BNOT			:	'~'		;
NOT_EQUAL		:	"!="	;
DIV			:	'/'		;
DIV_ASSIGN		:	"/="	;
PLUS			:	'+'		;
PLUS_ASSIGN		:	"+="	;
INC			:	"++"	;
MINUS			:	'-'		;
MINUS_ASSIGN	:	"-="	;
DEC			:	"--"	;
STAR			:	'*'		;
STAR_ASSIGN		:	"*="	;
MOD			:	'%'		;
MOD_ASSIGN		:	"%="	;
SR			:	">>"	;
SR_ASSIGN		:	">>="	;
BSR			:	">>>"	;
BSR_ASSIGN		:	">>>="	;
GE				:	">="	;
GT				:	">"		;
SL				:	"<<"	;
SL_ASSIGN		:	"<<="	;
LE				:	"<="	;
LT				:	'<'		;
BXOR			:	'^'		;
BXOR_ASSIGN		:	"^="	;
BOR				:	'|'		;
BOR_ASSIGN		:	"|="	;
LOR				:	"||"	;
BAND			:	'&'		;
BAND_ASSIGN		:	"&="	;
LAND			:	"&&"	;
SEMI			:	';'		;
HASH			:	"#"     ;
QUOTE			:	"\""    ;
//SPACE			:	" "	;

// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
			// handle newlines
		|	(	options {generateAmbigWarnings=false;}
			:	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)+
		{ _ttype = Token.SKIP; }
	;

// Single-line comments
SL_COMMENT
	:	"//" 
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
		{$setType(Token.SKIP); newline();}
	;

// multiple-line comments
ML_COMMENT
{
   int startLine;
   int startCol;
}
	:	"/*"  { startLine = getLine(); startCol = getColumn() - 3; }
		(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*/"
      {
        /* mFileContents.reportCComment(startLine, startCol,
                            getLine(), getColumn() - 2);*/
         $setType(Token.SKIP);
      }
	;


// character literals
CHAR_LITERAL
	:	'\'' ( ESC | ~'\'' ) '\''
	;

// string literals
STRING_LITERAL
	:	'"' (ESC|~('"'|'\\'))* '"'
	;


// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected
ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
		|	('0'..'3')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'7')
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	('4'..'7')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'9')
			)?
		)
	;


// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;


// a dummy rule to force vocabulary to be all characters (except special
//   ones that ANTLR uses internally (0 to 2)
protected
VOCAB
	:	'\3'..'\377'
	;


// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT
	options {testLiterals=true;}
	:	('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$')*
	;
/*
PREPROCREGION
	options {testLiterals=true;}
	:       '#'("region"|'_'|'$'|' ')*
	;
	
PREPROCENDREGION
	options {testLiterals=true;}
	:       '#'("endregion"|'_'|'$'|' ')*
	;
*/	

// a numeric literal
NUM_INT
	{boolean isDecimal=false; Token t=null;}
    :   '.' {_ttype = DOT;}
            (	('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
                {
				if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
                	_ttype = NUM_FLOAT;
				}
				else {
                	_ttype = NUM_DOUBLE; // assume double
				}
				}
            )?

	|	(	'0' {isDecimal = true;} // special case for just '0'
			(	('x'|'X')
				(											// hex
					// the 'e'|'E' and float suffix stuff look
					// like hex digits, hence the (...)+ doesn't
					// know when to stop: ambig.  ANTLR resolves
					// it correctly by matching immediately.  It
					// is therefor ok to hush warning.
					options {
						warnWhenFollowAmbig=false;
					}
				:	HEX_DIGIT
				)+
			|	('0'..'7')+									// octal
			)?
		|	('1'..'9') ('0'..'9')*  {isDecimal=true;}		// non-zero decimal
		)
		(	('l'|'L') { _ttype = NUM_LONG; }

		// only check to see if it's a float if looks like decimal so far
		|	{isDecimal}?
            (   '.' ('0'..'9')* (EXPONENT)? (f2:FLOAT_SUFFIX {t=f2;})?
            |   EXPONENT (f3:FLOAT_SUFFIX {t=f3;})?
            |   f4:FLOAT_SUFFIX {t=f4;}
            )
            {
			if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0) {
                _ttype = NUM_FLOAT;
			}
            else {
	           	_ttype = NUM_DOUBLE; // assume double
			}
			}
        )?
	;


// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
	:	('e'|'E') ('+'|'-')? ('0'..'9')+
	;


protected
FLOAT_SUFFIX
	:	'f'|'F'|'d'|'D'
	;

