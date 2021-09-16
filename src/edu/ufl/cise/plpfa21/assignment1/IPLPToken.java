package edu.ufl.cise.plpfa21.assignment1;


public interface IPLPToken extends PLPTokenKinds {
	
	
	Kind getKind();

	/**
	 * Returns the actual text of token.
	 * 
	 * For string literals, this includes the delimiters and unprocessed escape sequences.
	 * For example, the text corresponding to this token "abc\'"  would have 7 characters.
	 * 
	 * @return text of token
	 */
	String getText();

	/**
	 * @return  The line number of first character of this token in the input.  Counting starts at 1.
	 */
	int getLine();

	/**
	 * 
	 * @return  The position of the first character of this token in the line.  Counting starts at 0.
	 */
	int getCharPositionInLine();
	
	/**
	 * This routine is only defined for string literals, i.e. kind = STRING_LITERAL
	 * It returns a String representing the token after stripping delimiters and handling escape sequences.
	 * For example, the string value of the token with text "abc\'" would have four characters, abc'
	 * 
	 * @return the string value of this token
	 */
	String getStringValue();
	
	/**
	 * This routine is only defined for integer literals, i.e. kind = INTEGER_LITERAL.
	 * It returns the int value represented by the token.  
	 * @return
	 */
	int getIntValue();
}
