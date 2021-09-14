package edu.ufl.cise.plpfa21.assignment1;

import java.util.Collections;

	
	
public class Token implements IPLPToken{
	
	public Kind kind;
	int tokenLength;
	int currentPosition;
	
	
	public Token(Kind kind, int currentPosition, int tokenLength) {
		this.kind = kind;
		this.tokenLength = tokenLength;
		this.currentPosition = currentPosition;
	}
	
	
	
	@Override
	public Kind getKind() {
		for(int i=0; i<Lexer.tokensList.size(); i++) {
			System.out.println(Lexer.tokensList.get(i).kind);
		}
		if(this.kind != null)
			return this.kind;
		
		return null;
		
	}

	@Override
	public String getText() {
		/**
		 * Returns the actual text of token.
		 * 
		 * For string literals, this includes the delimiters and unprocessed escape sequences.
		 * For example, the text corresponding to this token "abc\'"  would have 7 characters.
		 * 
		 * @return text of token
		 */
		// TODO Auto-generated method stub
		String s = Lexer.globalInput.substring(currentPosition, currentPosition + tokenLength);
		if(s != null)
			return s;
		return null;
	}

	@Override
	public int getLine() {
		int index=0;
		index = Collections.binarySearch(Lexer.startLineArray, currentPosition)+1;
		int line = 0;
		line = index < 0 ? Math.abs(index+2) : index;
		return line;
		/**
		 * @return  The line number of first character of this token in the input.  Counting starts at 1.
		 */
		
}

	@Override
	public int getCharPositionInLine() {
		/**
		 * 
		 * @return  The position of the first character of this token in the line.  Counting starts at 0.
		 */
		return currentPosition - Lexer.startLineArray.get(this.getLine()-1);
	}

	@Override
	public String getStringValue() {
		/**
		 * This routine is only defined for string literals, i.e. kind = STRING_LITERAL
		 * It returns a String representing the token after stripping delimiters and handling escape sequences.
		 * For example, the string value of the token with text "abc\'" would have four characters, abc'
		 * 
		 * @return the string value of this token
		 */
		if(getKind() == Kind.STRING_LITERAL) {
			String s = getText();
			StringBuilder sb = new StringBuilder();
			for(char c : s.toCharArray()) {
				if(Character.isLetter(c)) {
					sb.append(c);
				}
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return Integer.parseInt(getText());
		// TODO Auto-generated method stub
		/**
		 * This routine is only defined for integer literals, i.e. kind = INTEGER_LITERAL.
		 * It returns the int value represented by the token.  
		 * @return
		 */
	}
	
	
}


