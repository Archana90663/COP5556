package edu.ufl.cise.plpfa21.assignment1;

import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment3.ReferenceParser;

public class CompilerComponentFactory {

	public static IPLPLexer getLexer(String input) {
		//Replace with whatever is needed for your lexer.
		return ReferenceLexer.createLexer(input);
	}
	
	public static IPLPParser getParser(String input) {
		//Replace this with whatever is needed for your parser.
		return  ReferenceParser.create(input);		
	}
}
