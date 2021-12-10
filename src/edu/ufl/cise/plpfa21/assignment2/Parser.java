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
		IExpression e=null;
		ex = trm();
			while(this.token.getKind() == Kind.LT || this.token.getKind() == Kind.GT || this.token.getKind() == Kind.EQUALS || this.token.getKind() == Kind.NOT_EQUALS || this.token.getKind() == Kind.ASSIGN) {
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
					if(Character.isLetter(left.getText().charAt(0)) || Character.isLetter(right.getText().charAt(0))) {
						String s = "";
						if(left.getText().equals(right.getText())) {
							s = "true";
						}
						else {
							s = "false";
						}
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Boolean.toString(left.getText().equals(right.getText())), left, right, Kind.EQUALS);
//						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
						b.setType(tokenType);
						return b;
					}
					else if(Character.isDigit(left.getText().charAt(0)) || Character.isDigit(right.getText().charAt(0))) {
						int intLeft = Integer.parseInt(left.getText());
						int intRight = Integer.parseInt(right.getText());
						String s="";
						if(intLeft == intRight) {
							s = "true";
						}
						else {
							s ="false";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						return e;
					}
						
//					IExpression left = b.getLeft();
//					IExpression right = b.getRight();
//					if(map.containsKey(left.getText())) {
//						left = map.get(left.getText());
//					}
//					if(map.containsKey(right.getText())) {
//						right = map.get(right.getText());
//					}
//					
//					boolean leftDigit = (left.getText().equals("TRUE") || left.getText().equals("FALSE"));
//					boolean rightDigit = (right.getText().equals("TRUE") || right.getText().equals("FALSE"));
//					if(leftDigit && rightDigit) {
//						String s = Boolean.toString(left.getText().equals(right.getText()));
//						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.EQUALS);
//
//					}
//					else {
//						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), map.get(left.getText()).getText() + map.get(right.getText()).getText(), ex, ex1, t.getKind());
//					}
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
						String s = "";
						if(left.getText().equals(right.getText())) {
							s = "false";
						}
						else {
							s = "true";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						return e;
					}
				else if(t.getKind() == Kind.LT) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
					if(Character.isDigit(left.getText().charAt(0)) || Character.isDigit(right.getText().charAt(0))) {
						int intLeft = Integer.parseInt(left.getText());
						int intRight = Integer.parseInt(right.getText());
						String s = "";
						if(intLeft < intRight) {
							s = "true";
						}
						else {
							s = "false";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
//						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.LT);
						return e;
					}
					else if(Character.isLetter(left.getText().charAt(0)) || Character.isLetter(right.getText().charAt(0))) {
						String s = "";
						if(left.getText().compareTo(right.getText()) <=0) {
							s = "true";
						}
						else {
							s = "false";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						return e;
					}
		
						
				}
				else if(t.getKind() == Kind.GT) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
					if(Character.isDigit(left.getText().charAt(0)) || Character.isDigit(right.getText().charAt(0))) {
						int intLeft = Integer.parseInt(left.getText());
						int intRight = Integer.parseInt(right.getText());
						String s = "";
						if(intLeft > intRight) {
							s = "true";
						}
						else {
							s = "false";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						return e;
					}
					else if(Character.isLetter(left.getText().charAt(0)) || Character.isLetter(right.getText().charAt(0))) {
						String s = "";
						if(left.getText().compareTo(right.getText()) >=0) {
							s = "true";
						}
						else {
							s = "false";
						}
						e = new BooleanLiteralExpression__(left.getLine(), left.getPosInLine(), s, Boolean.parseBoolean(s));
						return e;
					}
		
						
				}
				else if(t.getKind() == Kind.ASSIGN) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(left.getType().isInt() || right.getType().isInt()) {
						left = new IntLiteralExpression__(left.getLine(), left.getPosInLine(), left.getText(), Integer.parseInt(right.getText()));
						left.setType(right.getType());
						map.put(first.getText(), left);
						return left;
					}
						else if(left.getType().isString() || right.getType().isString()){
							left = new StringLiteralExpression__(left.getLine(), left.getPosInLine(), left.getText(), right.getText());
							left.setType(right.getType());
							map.put(first.getText(), left);
							return left;
					}
						else {
							BooleanLiteralExpression__ rightboo = (BooleanLiteralExpression__)b.getRight();
//							Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//							left = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);
							left = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), rightboo.getValue());
							PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
							left.setType(tokenType);
							map.put(first.getText(), left);
							return left;
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
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
//					PrimitiveType__ tokenType = new PrimitiveType__(left.getLine(), left.getPosInLine(), left.getText(), IType.TypeKind.INT);
//					PrimitiveType__ tokenType2 = new PrimitiveType__(right.getLine(), right.getPosInLine(), right.getText(), IType.TypeKind.INT);
//					left.setType(tokenType);
//					right.setType(tokenType2);
//					boolean leftDigit = Character.isDigit(map.get(left.getText()).getText().charAt(0));
//					boolean rightDigit = Character.isDigit(map.get(right.getText()).getText().charAt(0));
					boolean leftDigit = Character.isDigit(left.getText().charAt(0));
					boolean rightDigit = Character.isDigit(right.getText().charAt(0));
					if(leftDigit && rightDigit) {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Integer.toString(Integer.parseInt(left.getText()) + Integer.parseInt(right.getText())), left, right, Kind.PLUS);
						b.setType(right.getType());

					}
					else {
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), left.getText()+right.getText(), ex, ex1, t.getKind());
					b.setType(right.getType());
					System.out.println(left.getType());
				}
			}
				else if(t.getKind() == Kind.MINUS) {
//					IExpression left = b.getLeft();
//					IExpression right = b.getRight();
//					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), left.getText() + "-" + right.getText(), ex, ex1, t.getKind());
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
//					PrimitiveType__ tokenType = new PrimitiveType__(left.getLine(), left.getPosInLine(), left.getText(), IType.TypeKind.INT);
//					PrimitiveType__ tokenType2 = new PrimitiveType__(right.getLine(), right.getPosInLine(), right.getText(), IType.TypeKind.INT);
//					left.setType(tokenType);
//					right.setType(tokenType2);
//					boolean leftDigit = Character.isDigit(map.get(left.getText()).getText().charAt(0));
//					boolean rightDigit = Character.isDigit(map.get(right.getText()).getText().charAt(0));
					boolean leftDigit = Character.isDigit(left.getText().charAt(0));
					boolean rightDigit = Character.isDigit(right.getText().charAt(0));
					if(leftDigit && rightDigit) {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Integer.toString(Integer.parseInt(left.getText()) - Integer.parseInt(right.getText())), left, right, Kind.MINUS);
						b.setType(right.getType());

					}
					else {
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), left.getText()+"-"+right.getText(), ex, ex1, t.getKind());
					b.setType(right.getType());
					System.out.println(left.getType());
				}
				}
				
				else if(t.getKind() == Kind.OR) {
					IExpression left = b.getLeft();
					IExpression right = b.getRight();
					if(map.containsKey(left.getText())) {
						left = map.get(left.getText());
					}
					if(map.containsKey(right.getText())) {
						right = map.get(right.getText());
					}
					String s = "";
					boolean leftBool = Boolean.parseBoolean(left.getText());
					boolean rightBool = Boolean.parseBoolean(right.getText());
					if(leftBool || rightBool) {
						s = "true";
					}
					else {
						s = "false";
					}
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.OR);
					
				}
				return b;
			}
			return ex;
		} 
		
	
	
	public IExpression element() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex=null;
		IExpression ex1=null;
		BinaryExpression__ b = null;
		ex = factor();
				while(this.token.getKind() == Kind.TIMES || this.token.getKind() == Kind.DIV || this.token.getKind() == Kind.AND) {
					IPLPToken t = this.token;
//					equal(Kind.TIMES);
//					equal(Kind.DIV);
//					equal(Kind.AND);
					ex1 = factor();
//					ex = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, ex1, t.getKind());
					b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex, ex1, t.getKind());
					if(t.getKind() == Kind.TIMES) {
						IExpression left = b.getLeft();
						IExpression right = b.getRight();
						if(map.containsKey(left.getText()) && map.containsKey(right.getText())) {
							left = map.get(left.getText());
							right = map.get(right.getText());
						}
//						boolean leftDigit = Character.isDigit(map.get(left.getText()).getText().charAt(0));
//						boolean rightDigit = Character.isDigit(map.get(right.getText()).getText().charAt(0));
						boolean leftDigit = Character.isDigit(left.getText().charAt(0));
						boolean rightDigit = Character.isDigit(right.getText().charAt(0));
						if(leftDigit && rightDigit) {
							b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Integer.toString(Integer.parseInt(left.getText()) * Integer.parseInt(right.getText())), left, right, Kind.TIMES);

						}
						else {
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), map.get(left.getText()).getText() + map.get(right.getText()).getText(), ex, ex1, t.getKind());
					}
				}
					else if(t.getKind() == Kind.AND) {
						IExpression left = b.getLeft();
						IExpression right = b.getRight();
						if(map.containsKey(left.getText())) {
							left = map.get(left.getText());
						}
						if(map.containsKey(right.getText())) {
							right = map.get(right.getText());
						}
						String s = "";
						if(left.getText().equals(right.getText())) {
							s = "true";
						}
						else {
							s = "false";
						}
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), s, left, right, Kind.AND);
						PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
						b.setType(tokenType);
					}
					else if(t.getKind() == Kind.DIV) {
						IExpression left = b.getLeft();
						IExpression right = b.getRight();
						if(map.containsKey(left.getText()) && map.containsKey(right.getText())) {
							left = map.get(left.getText());
							right = map.get(right.getText());
						}
//						boolean leftDigit = Character.isDigit(map.get(left.getText()).getText().charAt(0));
//						boolean rightDigit = Character.isDigit(map.get(right.getText()).getText().charAt(0));
						b = new BinaryExpression__(first.getLine(), first.getCharPositionInLine(), Integer.toString(Integer.parseInt(left.getText()) / Integer.parseInt(right.getText())), left, right, Kind.DIV);

					}
					return b;
			}
		
		return ex;
	}
	
	public IExpression factor() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex = null;
		Kind kind = this.token.getKind();
		PrimitiveType__ tokenType= null;
		switch(kind) {
			case STRING_LITERAL:
				if(first.getText().equals("!")) {
					nextOne();
					if(map.containsKey(this.token.getText())) {
						ex = new BooleanLiteralExpression__(this.token.getLine(), this.token.getCharPositionInLine(), this.token.getText(), !Boolean.parseBoolean(this.token.getStringValue()));
//						if(ex.getText().equals("true")) {
//							ex = new BooleanLiteralExpression__(this.token.getLine(), this.token.getCharPositionInLine(), "false", false);
//						}
//						else {
//							ex = new BooleanLiteralExpression__(this.token.getLine(), this.token.getCharPositionInLine(), "true", true);
//						}
//						else {
//							Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//							ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);
//							tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.STRING);
//							ex.setType(tokenType);
//							nextOne();
//							break;
//						}
						tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
						ex.setType(tokenType);
						nextOne();
						break;
					}
					else {
						if(this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
							if(this.token.getKind() == Kind.KW_TRUE) {
								ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), "FALSE", false);
							}
							else{
								ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), "TRUE", true);
							}
							tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
							ex.setType(tokenType);
							nextOne();
							break;
						}
					}
					
				}
				else {
					if(map.containsKey(first.getText())) {
						ex = map.get(first.getText());
						nextOne();
						break;
					}
				}
				
				ex = new StringLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.STRING);
				ex.setType(tokenType);
				nextOne();
				break;
			case IDENTIFIER:
//				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
//				System.out.println(first.getStringValue() + " " + first.getText());
//				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), this.token.getText(), ident);
//				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);	
				
				if(map.containsKey(first.getText())) {
					ex = map.get(first.getText());
					nextOne();
					break;
				}
//				ex = new StringLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getText());
				Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
				ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), ident);
				tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.STRING);
				ex.setType(tokenType);
				nextOne();
				break;
			case INT_LITERAL:
				ex = new IntLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getIntValue());	
				nextOne();
				tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.INT);
				ex.setType(tokenType);
				break;
			case KW_TRUE:
				ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), Boolean.parseBoolean(first.getText()));
				tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
				ex.setType(tokenType);
				nextOne();
				break;
			case KW_FALSE:
				ex = new BooleanLiteralExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), Boolean.parseBoolean(first.getText()));
				tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), TypeKind.BOOLEAN);
				ex.setType(tokenType);
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
//			while(declarations.contains(this.token.getKind()) || statements.contains(this.token.getKind())) {
//				if(declarations.contains(this.token.getKind())) {
//					IDeclaration declaration = declarationMethod();
//					declarationList.add(declaration);
//					
//				}
//				if(statements.contains(this.token.getKind())) {
//					IStatement s = stmt();
//					statementList.add(s);
//				}
				//if(this.token.getKind() == Kind.KW_RETURN) {
					b = new Block__(first.getLine(), first.getCharPositionInLine(), first.getText(), statementList);
					return b;
				//}
//				while(this.token.getKind() != Kind.IDENTIFIER && this.token.getKind() != Kind.KW_TRUE && this.token.getKind() != Kind.KW_FALSE) {
//					nextOne();
//				}
//				IStatement s = stmt();
//				statementList.add(s);
//				else {
//					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());
//				}
//			}
//			equal(Kind.SEMI);
			//b = new Block__(first.getLine(), first.getCharPositionInLine(), first.getText(), statementList);
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
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.STRING_LITERAL) {
//						nextOne();
						t = this.token;
						ex = expr();
					}
					else {
						nextOne();
						if(this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.STRING_LITERAL) {
							ex = expr();
						}
					}
				}
				else {
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.STRING_LITERAL) {
						t = this.token;
						nextOne();
						if(this.token.getKind() == Kind.ASSIGN) {
							ex = expr();
						}
					}
				}
			}
			TypeKind type = IType.TypeKind.STRING;
			if(ex.getText().equals("TRUE") || ex.getText().equals("FALSE") || ex.getText().equals("true") || ex.getText().equals("false")) {
				type = IType.TypeKind.BOOLEAN;
			}
			if(Character.isDigit(ex.getText().charAt(0))) {
				type = IType.TypeKind.INT;
			}
			
			Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
			PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), type);
			NameDef__ nameDef = new NameDef__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident, tokenType);
			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
			map.put(first.getText(), ex);
		}
		else if(this.token.getKind() == Kind.KW_VAR) {
			nextOne();
			IExpression ex = null;
			TypeKind type = IType.TypeKind.STRING;

			if(this.token.getKind() == Kind.IDENTIFIER) {
				first = this.token;
				nextOne();
				if(this.token.getKind() == Kind.ASSIGN) {
//					t= this.token;
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.STRING_LITERAL) {
//						nextOne();
						t = this.token;
						ex = expr();
					}
					else {
						nextOne();
						if(this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind()==Kind.STRING_LITERAL ) {
							ex = expr();
						}
					}
				}
				else {
					nextOne();
					if(this.token.getKind() == Kind.KW_INT || this.token.getKind() == Kind.KW_BOOLEAN || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE || this.token.getKind() == Kind.INT_LITERAL || this.token.getKind() == Kind.KW_STRING || this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.STRING_LITERAL) {
						t = this.token;
						nextOne();
						if(this.token.getKind() == Kind.ASSIGN) {
							ex = expr();
						}
						else if(this.token.getKind() == Kind.SEMI){
							if(t.getKind() == Kind.KW_INT) {
								type = TypeKind.INT;
//								ex = expr();
//								type= TypeKind.INT;
							}
							else if(t.getKind() == Kind.KW_STRING) {
								type = TypeKind.STRING;
							}
							else {
								type = TypeKind.BOOLEAN;
							}
							Identifier__ identpre = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
							ex = new IdentExpression__(first.getLine(), first.getCharPositionInLine(), first.getText(), identpre);
							Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
							PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), type);
							ex.setType(tokenType);
							NameDef__ nameDef = new NameDef__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident, tokenType);
							d = new MutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
							map.put(first.getText(), ex);
							return d;
						}
					}
				}
			}
			if(ex.getText().equals("TRUE") || ex.getText().equals("FALSE") || ex.getText().equals("true") || ex.getText().equals("false")) {
				type = IType.TypeKind.BOOLEAN;
			}
			if(Character.isDigit(ex.getText().charAt(0))) {
				type = IType.TypeKind.INT;
			}
			
			Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
			PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(),ex.getPosInLine() , ex.getText(), type);
			ex.setType(tokenType);
			NameDef__ nameDef = new NameDef__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident, tokenType);
			d = new ImmutableGlobal__(first.getLine(), first.getCharPositionInLine(), first.getText(), nameDef, ex);
			map.put(first.getText(), ex);

		}
		else if(this.token.getKind() == Kind.KW_FUN) {
			List<INameDef> argList = new ArrayList<>();
			while(this.token.getKind() != Kind.IDENTIFIER) {
				nextOne();
			}
			first = this.token;
			TypeKind type = IType.TypeKind.STRING;
			nextOne();
			nextOne();
			if(this.token.getKind() == Kind.IDENTIFIER) {
//				while(this.token.getKind() != Kind.KW_INT && this.token.getKind() != Kind.KW_STRING &&this.token.getKind() != Kind.KW_BOOLEAN) {
//					nextOne();					
//				}
				argList.add(args());
				nextOne();
				if(this.token.getKind() == Kind.COMMA) {
					nextOne();
					argList.add(args());
				}
//				type = IType.TypeKind.INT;
			}	
			while(this.token.getKind() != Kind.KW_DO) {
				if(this.token.getKind() == Kind.KW_BOOLEAN) {
					type = IType.TypeKind.BOOLEAN;
				}
				else if(this.token.getKind() == Kind.KW_INT) {
					type = IType.TypeKind.INT;
				}
				else {
					type = IType.TypeKind.STRING;
				}
				nextOne();
			}
			IExpression ex = null;
			IBlock block = blck();
			if(this.token.getKind()== Kind.KW_RETURN) {
				nextOne();
				ex = expr();
				ReturnStatement__ s = new ReturnStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex);
				block.getStatements().add(s);
				if(ex.getType().isInt()) {
					type = TypeKind.INT;
				}
			}
			else {
				AssignmentStatement__ as = null;
				if(this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
						IPLPToken i = this.token;
						System.out.println(i.getText());
//						nextOne();
//						equal(Kind.ASSIGN);
						ex = expr();
						Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), i.getText());
						IdentExpression__ identValue = new IdentExpression__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident);
						as = new AssignmentStatement__(i.getLine(), i.getCharPositionInLine(), i.getText(), identValue, ex);
						map.put(i.getText(), ex);
						block.getStatements().add(as);
						if(ex.getType().isInt()) {
							type = TypeKind.INT;
						}
						else if(ex.getType().isBoolean()) {
							type = TypeKind.BOOLEAN;
						}
						while(this.token.getKind() != Kind.KW_RETURN && this.token.getKind() != Kind.EOF) {
							nextOne();
						}
						if(this.token.getKind() == Kind.KW_RETURN) {
							nextOne();
							ex = expr();
							ReturnStatement__ s = new ReturnStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex);
							block.getStatements().add(s);
						}
				}
			}
//			String tt = block.getStatements().get(0).getText();
//			
			if(ex.getText().equals("TRUE") || ex.getText().equals("FALSE") || ex.getText().equals("true") || ex.getText().equals("false")) {
				type = IType.TypeKind.BOOLEAN;
			}
			if(Character.isDigit(ex.getText().charAt(0))) {
				type = IType.TypeKind.INT;
			}
			
//				if(map.containsKey(tt)) {
//				ex = map.get(tt);
//				if(ex.getText().equals("TRUE") || ex.getText().equals("FALSE") || ex.getText().equals("true") || ex.getText().equals("false")) {
//					type = IType.TypeKind.BOOLEAN;
//				}
//				if(Character.isDigit(ex.getText().charAt(0))) {
//					type = IType.TypeKind.INT;
//				}
//			}
//			else {
//				if(tt.equals("TRUE") || tt.equals("FALSE") || tt.equals("true") || tt.equals("false")) {
//					type = IType.TypeKind.BOOLEAN;
//				}
//				if(Character.isDigit(tt.charAt(0))) {
//					type = IType.TypeKind.INT;
//				}
//			}
			
			Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
			PrimitiveType__ tokenType = new PrimitiveType__(ex.getLine(), ex.getPosInLine(), ex.getText(), type);
			ex.setType(tokenType);
			d = new FunctionDeclaration___(first.getLine(), first.getCharPositionInLine(), first.getText(), ident, argList, tokenType, block);
//			map.put(first.getText(), ex);
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
				if(dec!=null) {
					list.add(dec);
				}
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
		TypeKind type = TypeKind.STRING;
		
		if(this.token.getKind() == Kind.IDENTIFIER) {
			ex = expr();
			while(this.token.getKind() != Kind.KW_BOOLEAN && this.token.getKind() != Kind.KW_INT && this.token.getKind() != Kind.KW_STRING) {
				nextOne();
			}
			if(this.token.getKind() == Kind.KW_BOOLEAN) {
				type = TypeKind.BOOLEAN;
			}
			else if(this.token.getKind() == Kind.KW_INT){
				type = TypeKind.INT;
			}
			
//			while(this.token.getKind() != Kind.RPAREN) {
//				nextOne();
////				ex = expr();
//			}
//		
//			equal(Kind.RPAREN);
		}
		Identifier__ ident = new Identifier__(first.getLine(), first.getCharPositionInLine(), first.getText(), first.getStringValue());
		PrimitiveType__ tokenType = new PrimitiveType__(first.getLine(),first.getCharPositionInLine() , first.getText(), type);
		ex.setType(tokenType);
		map.put(first.getText(), ex);
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
					IExpression ex = null;
//					IPLPToken next=null;
//					try {
//						next = lexer.nextToken();
//					} catch (LexicalException e) {
//						e.printStackTrace();
//					}
//					if(next.getKind() == Kind.ASSIGN) {
//						s = assignStatementMethod();
//						return s;
//					}
					s = assignStatementMethod();
//					else {
//						ex = expr();
//					}
//					s = new ReturnStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex);
//					if(t.getKind() == Kind.ASSIGN) {
//						s = assignStatementMethod();
//						equal(Kind.SEMI);
//						
//					}
//					else {
//						throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//					}
					
				} catch(SyntaxException e) {
					throw new SyntaxException("",first.getLine(), first.getCharPositionInLine());

				}
				break;
			case KW_TRUE:
				try {
					IExpression ex = null;
					IPLPToken next=null;
					try {
						next = lexer.nextToken();
					} catch (LexicalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(next.getKind() == Kind.SEMI) {
						ex = expr();
						map.put(first.getText(), ex);

					}
//					else {
//						ex = expr();
//					}
					s = new ReturnStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), ex);
//					if(t.getKind() == Kind.ASSIGN) {
//						s = assignStatementMethod();
//						equal(Kind.SEMI);
//						
//					}
//					else {
//						throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
//					}
					
				} catch(SyntaxException e) {
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
		if(this.token.getKind() == Kind.IDENTIFIER || this.token.getKind() == Kind.KW_TRUE || this.token.getKind() == Kind.KW_FALSE) {
			try {
				IPLPToken i = this.token;
//				nextOne();
//				equal(Kind.ASSIGN);
				IExpression ex = expr();
				Identifier__ ident = new Identifier__(ex.getLine(), ex.getPosInLine(), ex.getText(), first.getText());
				IdentExpression__ identValue = new IdentExpression__(ex.getLine(), ex.getPosInLine(), ex.getText(), ident);
				as = new AssignmentStatement__(first.getLine(), first.getCharPositionInLine(), first.getText(), identValue, ex);
				map.put(first.getText(), ex);
				
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
//				equal(Kind.LPAREN);
				IExpression ex = expr();
//				equal(Kind.RPAREN);
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
	
