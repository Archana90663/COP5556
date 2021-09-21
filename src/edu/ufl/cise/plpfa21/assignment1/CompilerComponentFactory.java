package edu.ufl.cise.plpfa21.assignment1;

import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment2.Parser;

public class CompilerComponentFactory{


	static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		Lexer lexer = new Lexer(input);
		return lexer;
	}
	
	public static IPLPParser getParser(String input) {
		Parser parser = new Parser(input);
		return parser;
	   	 //Implement this in Assignment 2
	   	 //Your parser will create a lexer.
		
	    }


}
