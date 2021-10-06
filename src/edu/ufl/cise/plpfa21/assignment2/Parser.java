package edu.ufl.cise.plpfa21.assignment2;

import java.util.*;

import edu.ufl.cise.plpfa21.assignment1.IPLPLexer;
import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.Lexer;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment1.Token;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.IStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType.TypeKind;
import edu.ufl.cise.plpfa21.assignment3.astimpl.*;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Block__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.BooleanLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Declaration__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Expression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IdentExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Identifier__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IntLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Program__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Statement__;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;


public class Parser implements IPLPParser{

	public IPLPToken token;
	public IPLPLexer lexer;
	
	HashSet<Kind> declarations = new HashSet<>();
	HashSet<Kind> statements = new HashSet<>();
	
	
	public Parser(IPLPLexer lexer) {
		this.lexer = lexer;
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		declarations.add(Kind.KW_INT);
		declarations.add(Kind.KW_BOOLEAN);
		declarations.add(Kind.KW_STRING);
		statements.add(Kind.KW_WHILE);
		statements.add(Kind.KW_IF);
		statements.add(Kind.IDENTIFIER);
		
	}

	@Override
	public IASTNode parse() throws Exception {
		Program__ p = prgm();
		ReachedEOF();
		return p;
		
	}
	
	public IExpression expr() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex;
		IExpression ex2;
		IExpression ex3;
		
		try {
			ex2 = trm();
			while(this.token.getKind() == Kind.LT || this.token.getKind() == Kind.GT || this.token.getKind() == Kind.EQUALS || this.token.getKind() == Kind.NOT_EQUALS) {
				IPLPToken t = this.token;
				nextOne();
				ex3 = trm();
				//int line, int posInLine, String text, IExpression left, IExpression right, Kind op
				ex2 = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex2, ex3, first.getKind());
			}
			ex = ex2;
			
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

		}
		
		return ex;
	}
	
	IExpression trm() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex;
		
		IExpression ex2;
		IExpression ex3;
		
		try {
			ex2 = element();
			while(this.token.getKind() == Kind.PLUS || this.token.getKind() == Kind.MINUS || this.token.getKind() == Kind.OR) {
				IPLPToken t = this.token;
				nextOne();
				ex3 = element();
				ex2 = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex2, ex3, first.getKind());
			}
			ex = ex2;
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return ex;
	}
	
	public IExpression element() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex;
		IExpression ex2;
		IExpression ex3;
		try {
			ex2 = factor();
			while(this.token.getKind() == Kind.TIMES || this.token.getKind() == Kind.DIV || this.token.getKind() == Kind.AND) {
				IPLPToken t = this.token;
				nextOne();
				ex3 = factor();
				ex2 = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex2, ex3, first.getKind());
			}
			ex = ex2;
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return ex;
	}
	
	public IExpression factor() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex = null;
		Kind kind = this.token.getKind();
		switch(kind) {
			case IDENTIFIER:
				nextOne();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getStringValue(), first.getText());
				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);	
				break;
			case INT_LITERAL:
				nextOne();
				ex = new IntLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getIntValue());	
				break;
			case KW_TRUE:
			case KW_FALSE:
				nextOne();
				ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), Boolean.parseBoolean(first.getStringValue()));
				break;
			case LPAREN:
				try {
					nextOne();
					ex = expr();
					equal(Kind.RPAREN);
				} catch(SyntaxException e) {
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

				}
				break;				
				
//				default:
//					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return ex;
			
	}
	
	public Block__ blck() throws SyntaxException{
		IPLPToken first = this.token;
		Block__ b = null;
		List<IDeclaration> declarationList = new ArrayList<>();
		List<IStatement> statementList = new ArrayList<>();
		
		if(this.token.getKind() == Kind.KW_DO) {
		try {
			nextOne();
			while(declarations.contains(this.token.getKind()) || statements.contains(this.token.getKind())) {
				if(declarations.contains(this.token.getKind())) {
					IDeclaration declaration = declarationMethod();
					declarationList.add(declaration);
					
				}
				else if(statements.contains(this.token.getKind())) {
					IStatement s = stmt();
					statementList.add(s);
				}
				else {
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
				}
			}
			equal(Kind.KW_END);
			b = new Block__(first.getLine(), first.getCharPositionInLine(), first.getText(), statementList);
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
		return b;
		
	}
	
	public IDeclaration declarationMethod() throws SyntaxException{
		IPLPToken first = this.token;
		IDeclaration d = null;
		if(this.token.getKind() == Kind.KW_VAL) {
			try {
				nextOne();
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
				d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

			}
		}
		else if(this.token.getKind() == Kind.KW_VAR) {
			try {
				nextOne();
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
		else if(this.token.getKind() == Kind.KW_INT) {
			try {
				nextOne();
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
		
		else if(this.token.getKind() == Kind.KW_BOOLEAN) {
			try {
				nextOne();
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.BOOLEAN);
				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
		else if(this.token.getKind() == Kind.KW_STRING) {
			try {
				nextOne();
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.STRING);
				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
				d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
//		IPLPToken first = this.token;
//		IDeclaration d;
//		if(this.token.getKind()==Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN) {
//			try {
//				nextOne();
//				IPLPToken i = equal(Kind.IDENTIFIER);
//				d = new Declaration__(first.getLine(), first.getCharPositionInLine(), first.getText());
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//			}
//		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
//		
		return d;
	}
	
	public Program__ prgm() throws SyntaxException{
		IPLPToken first = this.token;
		Program__ p;
		List<IDeclaration> list = new ArrayList<>();
		try {
			//equal(Kind.IDENTIFIER);
			IDeclaration dec;
//			if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN) {
			if(this.token.getKind() == Kind.KW_VAL || this.token.getKind() == Kind.KW_VAR || this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_STRING) {	
				dec = declarationMethod();
				list.add(dec);
				while(this.token.getKind() != Kind.EOF) {
					nextOne();
					dec = declarationMethod();
					list.add(dec);
				}	
			}
			Block__ b = blck();
			p = new Program__(first.getLine(), first.getCharPositionInLine(), first.getText(), list);
			
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

		}
		return p;
	}
	
//	public IDeclaration prmDec() throws SyntaxException{
//		IPLPToken first = this.token;
//		IDeclaration d = null;
//		if(this.token.getKind() == Kind.KW_VAL) {
//			try {
//				nextOne();
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//
//			}
//		}
//		else if(this.token.getKind() == Kind.KW_VAR) {
//			try {
//				nextOne();
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//			}
//		
//		}
//		
//		else if(this.token.getKind() == Kind.KW_INT) {
//			try {
//				nextOne();
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//			}
//		}
//		
//		else if(this.token.getKind() == Kind.KW_BOOLEAN) {
//			try {
//				nextOne();
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.BOOLEAN);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//			}
//		}
////		else {
////			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
////		}
////		IPLPToken first = this.token;
////		IDeclaration d;
////		if(this.token.getKind()==Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN) {
////			try {
////				nextOne();
////				IPLPToken i = equal(Kind.IDENTIFIER);
////				d = new Declaration__(first.getLine(), first.getCharPositionInLine(), first.getText());
////			} catch(SyntaxException e) {
////				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
////			}
////		}
////		else {
////			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
////		}
////		
//		return d;
//	}
	
	public IStatement stmt() throws SyntaxException{
		IPLPToken first = this.token;
		IStatement s=null;
		Kind kind = this.token.getKind();
		switch(kind) {
			case KW_WHILE:
				try {
					s = whileStatementMethod();
				} catch(SyntaxException e) {
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
				}
				break;
			case KW_IF:
				try {
					s = ifStatementMethod();
				} catch(SyntaxException e) {
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
				}
				break;
			case IDENTIFIER:
				try {
					IPLPToken t = lexer.nextToken();
					if(t.getKind() == Kind.ASSIGN) {
						s = assignStatementMethod();
						equal(Kind.SEMI);
						
					}
//					else {
//						throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//					}
					
				} catch(SyntaxException | LexicalException e) {
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

				}
				break;
			default:
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return s;
	}
	
	public AssignmentStatement__ assignStatementMethod() throws SyntaxException{
		IPLPToken first = this.token;
		AssignmentStatement__ as = null;
		if(this.token.getKind() == Kind.IDENTIFIER) {
			try {
				IPLPToken i = this.token;
				nextOne();
				equal(Kind.ASSIGN);
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getStringValue(), first.getText());
				IdentExpression__ identValue = new IdentExpression__(i.getLine(), i.getCharPositionInLine(), i.getText(), ident);
				as = new AssignmentStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), identValue, ex);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
		return as;
	}
	
	public WhileStatement__ whileStatementMethod() throws SyntaxException{
		IPLPToken first = this.token;
		WhileStatement__ ws=null;
		if(this.token.getKind() == Kind.KW_WHILE) {
			try {
				nextOne();
				equal(Kind.LPAREN);
				IExpression ex = expr();
				equal(Kind.RPAREN);
				Block__ b = blck();
				ws = new WhileStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, b);
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//
//		}
		return ws;
	}
	
	public IfStatement__ ifStatementMethod() throws SyntaxException{
		IPLPToken first = this.token;
		IfStatement__ is=null;
		if(this.token.getKind() == Kind.KW_IF) {
			try {
				nextOne();
				equal(Kind.LPAREN);
				IExpression ex = expr();
				equal(Kind.RPAREN);
				Block__ b = blck();
				is = new IfStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, b);
				
			} catch(SyntaxException e) {
				throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
		return is;
	}
	
	
	
	
	public IPLPToken nextOne() throws SyntaxException{
		IPLPToken t = this.token;
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	public IPLPToken ReachedEOF() throws SyntaxException{
		if(this.token.getKind() == Kind.EOF) {
			return this.token;
		}
		throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
	}
	
	public IPLPToken equal(Kind kind) throws SyntaxException{
		if(this.token.getKind() == kind) {
			return nextOne();
		}
		throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

	}
	
	}
	
