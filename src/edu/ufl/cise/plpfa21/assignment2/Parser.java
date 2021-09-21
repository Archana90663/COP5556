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
	public HashMap<Token, Token> tokensMap = new HashMap<>();
	
	public Parser(Token token, Lexer lexer) {
		this.token = token;
		this.lexer = lexer;
	}
	
	@Override	
	public void parse() throws Exception {
		Kind kind = this.token.kind;
		switch(kind) {
			case 
		}
	}

}
