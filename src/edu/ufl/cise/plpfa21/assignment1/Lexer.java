package edu.ufl.cise.plpfa21.assignment1;


public class Lexer implements IPLPLexer {
	
	static String globalInput;
	int numberOfTokens;
	
	public Lexer(String input) {
		this.globalInput = input;
		this.numberOfTokens = 0;
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
			Token t = new Token(null,0,0);
			try {
				t = Token.tokensList.get(numberOfTokens).setKind();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			numberOfTokens++;
			return t;
		
	}
			
	
}