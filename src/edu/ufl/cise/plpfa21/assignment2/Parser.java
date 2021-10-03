package edu.ufl.cise.plpfa21.assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.Lexer;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment1.Token;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.astimpl.BinaryExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.BooleanLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Expression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IdentExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Identifier__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IntLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Program__;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;


public class Parser implements IPLPParser{

	public IPLPToken token;
	public Lexer lexer;
	
	
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
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
				ex2 = new BinaryExpression__(t.getLine(), t.getCharPositionInLine(), t.getText(), ex2, ex3, t.getKind());
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
				ex2 = new BinaryExpression__(t.getLine(), t.getCharPositionInLine(), t.getText(), ex2, ex3, t.getKind());
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
				ex2 = new BinaryExpression__(t.getLine(), t.getCharPositionInLine(), t.getText(), ex2, ex3, t.getKind());
			}
			ex = ex2;
		} catch(SyntaxException e) {
			throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return ex;
	}
	
	public IExpression factor() throws SyntaxException{
		IPLPToken first = this.token;
		IExpression ex;
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
				default:
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
		}
		return ex;
			
	}
	
	
	
	
	public void nextOne() throws SyntaxException{
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IPLPToken ReachedEOF() throws SyntaxException{
		if(this.token.getKind() == Kind.EOF) {
			return this.token;
		}
		throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
	}
	
	public IPLPToken equal(Kind kind) throws SyntaxException{
		if(this.token.getKind() == kind) {
			nextOne();
			return this.token;
		}
		throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());

	}
	
	}
	
