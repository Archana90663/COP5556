package edu.ufl.cise.plpfa21.assignment1;


public class CompilerComponentFactory{


	static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		Lexer lexer = new Lexer(input);
		return lexer;
	}

}
