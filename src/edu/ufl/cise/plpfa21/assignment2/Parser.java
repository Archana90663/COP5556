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

	public Token token;
	public Lexer lexer;
	public String globalInput;
	
	public Parser(String globalInput) {
		this.globalInput = globalInput;
	}
	
	@Override	
	public void parse() throws Exception {
		Kind kind = this.token.kind;
		switch(kind) {
			case EOF:
				return;
			case IDENTIFIER:
				Token t = this.token;
				this.token = (Token)lexer.nextToken();
				break;
			case INT_LITERAL:
				Token t2 = this.token;
				this.token = (Token)lexer.nextToken();
				break;
			case KW_TRUE:
				Token t3 = this.token;
				this.token = (Token)lexer.nextToken();
				break;
			case LPAREN:
				Token t4 = this.token;
				this.token = (Token)lexer.nextToken();
				for(Kind k : Kind.values()) {
					if(this.token.kind.equals(k)) {
						Token t5 = this.token;
						this.token = (Token)lexer.nextToken();
					}
				}
				break;
			default:
				throw new SyntaxException(this.globalInput, this.token.getLine(), this.token.getCharPositionInLine());
		}
	}
	
//	public Token parserToken() throws SyntaxException {
//		Token t = this.token;
//		this.token = lexer.nextToken();
//		return t;
//	}

}
