package edu.ufl.cise.plpfa21.assignment1;


public class Lexer implements IPLPLexer {
	
	String globalInput;
	int numberOfTokens;
	
	public Lexer(String input) {
		this.globalInput = input;
		this.numberOfTokens = 0;
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
			Token t = Token.tokensList.get(numberOfTokens);
			numberOfTokens++;
			return t;
		
	}
			
	
}