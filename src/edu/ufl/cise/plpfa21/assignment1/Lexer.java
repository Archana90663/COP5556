package edu.ufl.cise.plpfa21.assignment1;

import java.util.ArrayList;

public class Lexer implements IPLPLexer {
	
	static String globalInput;
	int numberOfTokens;
	
	public Lexer(String input) {
		this.globalInput = input;
		this.numberOfTokens = 0;
		Token.tokensList = new ArrayList<>();
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
			Token t = new Token(null,0,0);
			try {
				for(int i=0; i<Token.tokensList.size(); i++) {
					System.out.println("Initial: " + Token.tokensList.get(i).kind);
				}
				Token.setKind();
				t = Token.tokensList.get(numberOfTokens);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());
			}
			numberOfTokens++;
			return t;
		
	}
			
	
}