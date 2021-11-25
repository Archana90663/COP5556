package edu.ufl.cise.plpfa21.assignment2;

import java.util.*;

import edu.ufl.cise.plpfa21.assignment1.IPLPLexer;
import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.Lexer;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment1.Token;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
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
	
	HashMap<String, IExpression> map = new HashMap<>();
	
	
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
		IExpression ex = null;
		IExpression ex1 = null;
		BinaryExpression__ b = null;
		ex = trm();
			while(this.token.getKind() == Kind.LT || this.token.getKind() == Kind.GT || this.token.getKind() == Kind.EQUALS || this.token.getKind() == Kind.NOT_EQUALS) {
				IPLPToken t = this.token;
//				nextOne();
//				equal(Kind.LT);
//				equal(Kind.GT);
//				equal(Kind.EQUALS);
//				equal(Kind.NOT_EQUALS);
				ex1 = trm();
				b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, ex1, t.getKind());
				if(t.getKind() == Kind.EQUALS) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
					
					boolean leftDigit = (left.getText().equals("TRUE") || left.getText().equals("FALSE"));
					boolean rightDigit = (right.getText().equals("TRUE") || right.getText().equals("FALSE"));
					if(leftDigit && rightDigit) {
						String s = Boolean.toString(left.getText().equals(right.getText()));
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.EQUALS);

					}
					else {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), map.get(left.getText()).getText() + map.get(right.getText()).getText(), ex, ex1, t.getKind());
					}
				}
				else if(t.getKind() == Kind.NOT_EQUALS) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
					
					boolean leftDigit = (left.getText().equals("TRUE") || left.getText().equals("FALSE"));
					boolean rightDigit = (right.getText().equals("TRUE") || right.getText().equals("FALSE"));
					if(leftDigit && rightDigit) {
						String s = "";
						if(left.getText().equals(right.getText())) {
							s = "false";
						}
						else {
							s = "true";
						}
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.NOT_EQUALS);

					}
					else {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), map.get(left.getText()).getText() + map.get(right.getText()).getText(), ex, ex1, t.getKind());
					}
				}
				return b;
			}
			return ex;
		} 
		
	
	IExpression trm() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex = null;
		IExpression ex1 = null;
		ex = element();
		BinaryExpression__ b = null;
		
		
			while(this.token.getKind() == Kind.PLUS || this.token.getKind() == Kind.MINUS || this.token.getKind() == Kind.OR) {
				IPLPToken t = this.token;
//				nextOne();
//				equal(Kind.PLUS);
//				equal(Kind.MINUS);
//				equal(Kind.OR);
				ex1 = element();
				b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, ex1, t.getKind());
				if(t.getKind() == Kind.PLUS) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText()) && map.containsKey(right.getText())) {
						left = map.get(left.getText());
						right = map.get(right.getText());
					}
//					boolean leftDigit = Character.isDigit(map.get(left.getText()).getText().charAt(0));
//					boolean rightDigit = Character.isDigit(map.get(right.getText()).getText().charAt(0));
					boolean leftDigit = Character.isDigit(left.getText().charAt(0));
					boolean rightDigit = Character.isDigit(right.getText().charAt(0));
					if(leftDigit && rightDigit) {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Integer.toString(Integer.parseInt(left.getText()) + Integer.parseInt(right.getText())), left, right, Kind.PLUS);

					}
					else {
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), map.get(left.getText()).getText() + map.get(right.getText()).getText(), ex, ex1, t.getKind());
				}
			}
				else if(t.getKind() == Kind.MINUS) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), left.getText() + "-" + right.getText(), ex, ex1, t.getKind());
				}
				else if(t.getKind() == Kind.DIV) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), left.getText() + "/" + right.getText(), ex, ex1, t.getKind());
				}
				return b;
			}
			return ex;
		} 
		
	
	
	public IExpression element() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex=null;
		IExpression ex1=null;
		ex = factor();
				while(this.token.getKind() == Kind.TIMES || this.token.getKind() == Kind.DIV || this.token.getKind() == Kind.AND) {
					IPLPToken t = this.token;
//					equal(Kind.TIMES);
//					equal(Kind.DIV);
//					equal(Kind.AND);
					ex1 = factor();
					ex = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, ex1, t.getKind());
			}
		
		return ex;
	}
	
	public IExpression factor() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex = null;
		Kind kind = this.token.getKind();
		switch(kind) {
			case STRING_LITERAL:
				ex = new StringLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				nextOne();
				break;
			case IDENTIFIER:
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				System.out.println(first.getStringValue() + " " + first.getText());
//				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), this.token.getText(), ident);
//				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);	
				ex = new StringLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
				nextOne();
				break;
			case INT_LITERAL:
				ex = new IntLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getIntValue());	
				nextOne();
				break;
			case KW_TRUE:
				ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), Boolean.parseBoolean(first.getText()));
				nextOne();
				break;
			case KW_FALSE:
				ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), Boolean.parseBoolean(first.getText()));
				nextOne();
				break;
			case KW_DO:
				nextOne();
				
			case LPAREN:
				nextOne();
				ex = expr();
				equal(Kind.RPAREN);
				break;
			case LSQUARE:
				nextOne();
				ex = expr();
				equal(Kind.RSQUARE);
				break;
			case PLUS:
				nextOne();
				ex = expr();
				break;
			case TIMES:
				nextOne();
				ex = expr();
				break;
			case DIV:
				nextOne();
				ex = expr();
				break;
			case MINUS:
				nextOne();
				ex = expr();
				break;
			case LT:
				nextOne();
				ex = expr();
				break;
			case GT:
				nextOne();
				ex = expr();
				break;
			case EQUALS:
				nextOne();
				ex = expr();
				break;
			case NOT_EQUALS:
				nextOne();
				ex = expr();
				break;
			case AND:
				nextOne();
				ex = expr();
				break;
			case OR:
				nextOne();
				ex = expr();
				break;
			case ASSIGN:
				nextOne();
				ex = expr();
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
					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
				}
			}
			equal(Kind.KW_END);
			b = new Block__(first.getLine(), first.getCharPositionInLine(), first.getText(), statementList);
		} catch(SyntaxException e) {
			throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
			}
		}
//		else {
//			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//		}
		return b;
		
	}
	
	public IDeclaration declarationMethod() throws SyntaxException{
		IPLPToken first = this.token;
		IPLPToken t = this.token;
		IPLPToken begin = this.token;
		IDeclaration d = null;
		if(this.token.getKind() == Kind.KW_VAL) {
			nextOne();
			IExpression ex = null;
			if(this.token.getKind() == Kind.IDENTIFIER) {
				first = this.token;
				nextOne();
				if(this.token.getKind() == Kind.ASSIGN) {
//					t= this.token;
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER) {
//						nextOne();
						t = this.token;
						ex = expr();
					}
				}
				else {
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER) {
						t = this.token;
						nextOne();
						if(this.token.getKind() == Kind.ASSIGN) {
							ex = expr();
						}
					}
				}
			}
			TypeKind type = IType.TypeKind.STRING;
			if(t.getKind() == Kind.KW_INT || t.getKind() == Kind.INT_LITERAL) {
				type = IType.TypeKind.INT;
			}
			if(t.getKind() == Kind.KW_BOOLEAN || t.getKind() == Kind.KW_TRUE || t.getKind() == Kind.KW_FALSE) {
				type = IType.TypeKind.BOOLEAN;
			}
			if(t.getKind() == Kind.KW_LIST) {
				type = IType.TypeKind.LIST;
			}
			Identifier__ ident = new Identifier__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), first.getText());
			PrimitiveType__ tokenType = new PrimitiveType__(this.token.getLine(),this.token.getCharPositionInLine() , this.token.getText(), type);
			NameDef__ nameDef = new NameDef__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), ident, tokenType);
			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
			
//			try {
//				nextOne();
//				first = this.token;
////				while(this.token.getKind() != Kind.KW_INT && this.token.getKind() != Kind.KW_STRING && this.token.getKind() != Kind.KW_BOOLEAN && this.token.getKind() != Kind.KW_LIST) {
////					if(this.token.getKind() == Kind.EOF) {
////						return null;
////					}
////					nextOne();
////				}
//				while(this.token.getKind() != Kind.IDENTIFIER) {
//					nextOne();
//				}
//				nextOne();
////				t = this.token;
////				this.token.getKind() != Kind.IDENTIFIER && this.token.getKind() != Kind.INT_LITERAL && this.token.getKind() != Kind.STRING_LITERAL && this.token.getKind() != Kind.KW_TRUE && this.token.getKind() != Kind.KW_FALSE && 
//				while(this.token.getKind() != Kind.EOF && this.token.getKind() != Kind.KW_INT && this.token.getKind() != Kind.KW_STRING && this.token.getKind() != Kind.KW_BOOLEAN && this.token.getKind() != Kind.IDENTIFIER && this.token.getKind() != Kind.INT_LITERAL && this.token.getKind() != Kind.STRING_LITERAL && this.token.getKind() != Kind.KW_TRUE && this.token.getKind() != Kind.KW_FALSE){
////					if(this.token.getKind() == Kind.EOF) {
////						return null;
////					}
//					nextOne();
//				}
//				t = this.token;
//				TypeKind type = IType.TypeKind.STRING;
//				if(t.getKind() == Kind.KW_INT || t.getKind() == Kind.INT_LITERAL) {
//					type = IType.TypeKind.INT;
//				}
//				if(t.getKind() == Kind.KW_BOOLEAN || t.getKind() == Kind.KW_TRUE || t.getKind() == Kind.KW_FALSE) {
//					type = IType.TypeKind.BOOLEAN;
//				}
//				if(t.getKind() == Kind.KW_LIST) {
//					type = IType.TypeKind.LIST;
//				}
//				while(this.token.getKind() != Kind.EOF && this.token.getKind() != Kind.INT_LITERAL && this.token.getKind() != Kind.STRING_LITERAL && this.token.getKind() != Kind.KW_TRUE && this.token.getKind() != Kind.KW_FALSE) {
//					nextOne();
//				}
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), first.getText());
//				PrimitiveType__ tokenType = new PrimitiveType__(this.token.getLine(),this.token.getCharPositionInLine() , this.token.getText(), type);
//				NameDef__ nameDef = new NameDef__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), ident, tokenType);
////				d = new MutableGlobal__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), nameDef, ex);
////				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
////				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), type);
////				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//
//			}
		}
		else if(this.token.getKind() == Kind.KW_VAR) {
			nextOne();
			IExpression ex = null;
			if(this.token.getKind() == Kind.IDENTIFIER) {
				first = this.token;
				nextOne();
				if(this.token.getKind() == Kind.ASSIGN) {
//					t= this.token;
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER) {
//						nextOne();
						t = this.token;
						ex = expr();
					}
					else {
						nextOne();
						if(this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
							ex = expr();
						}
					}
				}
				else {
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER) {
						t = this.token;
						nextOne();
						if(this.token.getKind() == Kind.ASSIGN) {
							ex = expr();
						}
					}
				}
			}
			TypeKind type = IType.TypeKind.STRING;
			if(Character.isDigit(ex.getText().charAt(0))) {
				type = IType.TypeKind.INT;
			}
			if(ex.getText().equals("TRUE") || ex.getText().equals("FALSE") || ex.getText().equals("true") || ex.getText().equals("false")) {
				type = IType.TypeKind.BOOLEAN;
			}
//			if(t.getKind() == Kind.KW_INT || t.getKind() == Kind.INT_LITERAL) {
//				type = IType.TypeKind.INT;
//			}
//			if(t.getKind() == Kind.KW_BOOLEAN || t.getKind() == Kind.KW_TRUE || t.getKind() == Kind.KW_FALSE) {
//				type = IType.TypeKind.BOOLEAN;
//			}
//			if(t.getKind() == Kind.KW_LIST) {
//				type = IType.TypeKind.LIST;
//			}
			
			Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
			PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), type);
			NameDef__ nameDef = new NameDef__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident, tokenType);
			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
			map.put(first.getText(), ex);
			
//			Identifier__ ident = new Identifier__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), first.getText());
//			PrimitiveType__ tokenType = new PrimitiveType__(this.token.getLine(),this.token.getCharPositionInLine() , this.token.getText(), type);
//			NameDef__ nameDef = new NameDef__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), ident, tokenType);
//			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
		}
		
		
//		else if(this.token.getKind() == Kind.KW_FUN) {
//			nextOne();
//			IExpression ex = null;
//			if(this.token.getKind() == Kind.IDENTIFIER) {
//				first = this.token;
//				nextOne();
//				if(this.token.getKind() == Kind.ASSIGN) {
////					t= this.token;
//					nextOne();
//					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER) {
////						nextOne();
//						t = this.token;
//						ex = expr();
//					}
//				}
//				else {
//					nextOne();
//					if(this.token.getKind() == Kind.LPAREN) {
//						while(this.token.getKind() != Kind.RPAREN) {
//							nextOne();
//						}
//						nextOne();
//						if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_STRING) {
//							t = this.token;
//							ex = expr();
//						}
//					}
//				}
//			}
//			TypeKind type = IType.TypeKind.STRING;
//			if(t.getKind() == Kind.KW_INT || t.getKind() == Kind.INT_LITERAL) {
//				type = IType.TypeKind.INT;
//			}
//			if(t.getKind() == Kind.KW_BOOLEAN || t.getKind() == Kind.KW_TRUE || t.getKind() == Kind.KW_FALSE) {
//				type = IType.TypeKind.BOOLEAN;
//			}
//			if(t.getKind() == Kind.KW_LIST) {
//				type = IType.TypeKind.LIST;
//			}
//			Identifier__ ident = new Identifier__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), first.getText());
//			PrimitiveType__ tokenType = new PrimitiveType__(this.token.getLine(),this.token.getCharPositionInLine() , this.token.getText(), type);
//			NameDef__ nameDef = new NameDef__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), ident, tokenType);
//			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//		}
//			try {
//				nextOne();
//				first = this.token;
////				nextOne();
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//			}
//		else if(this.token.getKind() == Kind.KW_INT) {
//			try {
//				nextOne();
//				first = this.token;
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//			}
//		}
		
//		else if(this.token.getKind() == Kind.KW_BOOLEAN) {
//			try {
//				nextOne();
//				first = this.token;
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.BOOLEAN);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//			}
//		}
//		else if(this.token.getKind() == Kind.KW_STRING) {
//			try {
//				nextOne();
//				first = this.token;
//				IExpression ex = expr();
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.STRING);
//				NameDef__ nameDef = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
//				d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
//				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//			}
//		}
//		else if(this.token.getKind() == Kind.KW_FUN) {
//			IExpression e0 = null;
//			nextOne();
//			if(this.token.getKind() == Kind.LPAREN) {
//				nextOne();
//				e0 = expr();
//				equal(Kind.RPAREN);
//				TypeKind type = IType.TypeKind.STRING;
//				if(this.token.getKind() == Kind.KW_INT) {
//					type = IType.TypeKind.INT;
//				}
//				if(this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
//					type = IType.TypeKind.BOOLEAN;
//				}
//				if(this.token.getKind() == Kind.KW_LIST) {
//					type = IType.TypeKind.LIST;
//				}
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
//				List<INameDef> list = new ArrayList<>();
//				list.add(args());
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), type);
//				IBlock b = blck();
//				d = new FunctionDeclaration___(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, list, tokenType, b);
////				d = new FunctionDeclaration___(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, list, tokenType, b);				
//			}
//		}
			
//			try {
//				nextOne();
//				first = this.token;
//				while(this.token.getKind() != Kind.LPAREN) {
//					nextOne();
//				}
//				List<INameDef> list = new ArrayList<>();
//				list.add(args());
//				while(this.token.getKind() != Kind.KW_DO) {
//					nextOne();
//				}
//				IBlock b = blck();
//				TypeKind type = IType.TypeKind.STRING;
//				if(this.token.getKind() == Kind.INT_LITERAL) {
//					type = IType.TypeKind.INT;
//				}
//				if(this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
//					type = IType.TypeKind.BOOLEAN;
//				}
//				if(this.token.getKind() == Kind.KW_LIST) {
//					type = IType.TypeKind.LIST;
//				}
//				
//				IIdentifier ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), type);
//				d = new FunctionDeclaration___(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, list, tokenType, b);				
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//
//			}
//		}
//			try {
//				first = this.token;
//				nextOne();
//				List<INameDef> list = new ArrayList<>();
//				list.add(args());
//				IIdentifier ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				IBlock b = blck();
//				PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.INT);
//				d = new FunctionDeclaration___(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, list, tokenType, b);
//			} catch(SyntaxException e) {
//				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//			}
//		}
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
			if(this.token.getKind() == Kind.KW_VAL || this.token.getKind() == Kind.KW_VAR || this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.KW_FUN) {	
				dec = declarationMethod();
				list.add(dec);
				while(this.token.getKind() != Kind.EOF) {
					nextOne();
					dec = declarationMethod();
					if(dec != null) {
						list.add(dec);
					}
				}	
			}
			
//			Block__ b = blck();
			p = new Program__(first.getLine(), first.getCharPositionInLine(), first.getText(), list);
			
		} catch(SyntaxException e) {
			throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());

		}
		return p;
	}
	
	public INameDef args() throws SyntaxException{
		NameDef__ elements = null;
		IExpression ex = null;
		IPLPToken first = this.token;
		if(this.token.getKind() == Kind.LPAREN) {
			nextOne();
			ex = expr();
			while(this.token.getKind() != Kind.RPAREN) {
				nextOne();
				ex = expr();
			}
			equal(Kind.RPAREN);
		}
		Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
		PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), TypeKind.STRING);
		elements = new NameDef__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, tokenType);
		return elements;
	}
	
	
	public IStatement stmt() throws SyntaxException{
		IPLPToken first = this.token;
		IStatement s=null;
		Kind kind = this.token.getKind();
		switch(kind) {
			case KW_WHILE:
				try {
					s = whileStatementMethod();
				} catch(SyntaxException e) {
					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
				}
				break;
			case KW_IF:
				try {
					s = ifStatementMethod();
				} catch(SyntaxException e) {
					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
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
					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());

				}
				break;
			default:
				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
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
				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());

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
				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
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
				throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
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
	
