package edu.ufl.cise.plpfa21.assignment2;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.Lexer;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment1.Token;
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
	public void parse() throws Exception {
			Kind kind = this.token.getKind();
			switch(kind) {
				case EOF:
					return;
				case IDENTIFIER:
					nextOne();
					break;
				case INT_LITERAL:
					nextOne();					
					break;
				case KW_TRUE:
					nextOne();
					break;
				case KW_FALSE:
					nextOne();
					break;
				case LPAREN:
					nextOne();
					equal(Kind.RPAREN);
					break;
				case SEMI:
					nextOne();
					break;
				default:
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
	
	public IPLPToken equal(Kind kind) throws SyntaxException{
			if(this.token.getKind().equals(kind)) {
				IPLPToken x = this.token;
				try {
					this.token = lexer.nextToken();
				} catch (LexicalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return x;
			}
		
		throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
	}
	
	public IPLPToken nextOne() throws SyntaxException {
		IPLPToken token2 = this.token;
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token2;
	}
	
		
	}
	
