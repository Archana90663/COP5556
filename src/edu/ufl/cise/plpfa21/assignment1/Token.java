package edu.ufl.cise.plpfa21.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ufl.cise.plpfa21.assignment1.PLPStateKinds.State;
	
	
public class Token implements IPLPToken{
	
	public Kind kind;
	int tokenLength;
	int currentPosition;
	
	
	public Token(Kind kind, int currentPosition, int tokenLength) {
		this.kind = kind;
		this.tokenLength = tokenLength;
		this.currentPosition = currentPosition;
	}
	
	static ArrayList<Token> tokensList = new ArrayList<>();
	List<Integer> startLineArray = new ArrayList<>();
	
	
	
	public Token setKind() throws Exception{
		char c ='\0';
		int position=0;
		int start=0;
		int newLength = 1;
		State state = State.start;
		Token t = new Token(null,0,0);
		if(Lexer.globalInput.length() != 0) {
			int start_line = 0;
			int line = 0;
			while(Lexer.globalInput.length() > position) {
				c = Lexer.globalInput.charAt(position);
				switch(state) {
					case start:
						start = position;
						switch(c) {
						case '\n':
							startLineArray.add(start);
							line++;
							start_line = position+1;
							position = position+1;
							break;
						case '\t':
							startLineArray.add(start);
							line++;
							start_line = position+1;
							position = position+1;
							break;
						case '\r':
							startLineArray.add(start);
							line++;
							start_line = position+1;
							position = position+1;
							break;
						case ' ':
							startLineArray.add(start);
							line++;
							start_line = position+1;
							position = position+1;
							break;
						case ';':
							t.kind = Kind.SEMI;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case ',':
							t.kind = Kind.COMMA;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '(':
							t.kind = Kind.LPAREN;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case ')':
							t.kind = Kind.RPAREN;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '0':
							t.kind = Kind.INT_LITERAL;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '&':
							t.kind = Kind.AND;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '*':
							t.kind = Kind.TIMES;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '+':
							t.kind = Kind.PLUS;
							t.currentPosition = start;
							t.tokenLength = newLength;
							tokensList.add(t);
							position = position+1;
							break;
						case '!':
							state = State.not;
							position = position+1;
							break;
						case '-':
							state = State.minus;
							position = position+1;
							break;
						case '=':
							state = State.equal_sign;
							position = position+1;
							break;
						case '<':
							state = State.greater_than;
							position = position+1;
							break;
						case '>':
							state = State.less_than;
							position = position+1;
							break;
						case '|':
							state = State.or;
							position = position+1;
							break;
						case '/':
							state = State.div;
							position = position+1;
							break;
						default:
							if(Character.isDigit(c)) {
								state = State.integer_literal;
								position = position+1;
							}
							else if(Character.isWhitespace(c)) {
								position = position+1;
							}
							else if(Character.isLetter(c)) {
								state = State.identity;
								position = position+1;
							}
							else {
								throw new Exception("Char not accepted: " + c);
							}
						
						}
						break;
					case integer_literal:
						if(!Character.isDigit(c)) {
							t.kind = Kind.INT_LITERAL;
							t.currentPosition = start;
							t.tokenLength = position-start;
							tokensList.add(t);
							state = State.start;
						}
						else {
							position = position+1;
						}
						break;
					case not:
						if(c == '=') {
							t.kind = Kind.NOT_EQUALS;
							t.currentPosition= start;
							t.tokenLength = newLength+1;
							tokensList.add(t);
							position = position+1;
						}
						state = State.start;
						break;
					case minus:
						t.kind = Kind.MINUS;
						t.currentPosition = start;
						t.tokenLength = 1;
						tokensList.add(t);
						state = State.start;
						break;
					case less_than:
						t.kind = Kind.LT;
						t.currentPosition = start;
						t.tokenLength = position - start;
						tokensList.add(t);
						state = State.start;
						break;
					case equal_sign:
						if(c != '=') {
							throw new Exception("Char not accepted: " + c);
							
						}
						else {
							t.kind = Kind.EQUALS;
							t.currentPosition = start;
							t.tokenLength = newLength+1;
							position = position+1;
							state = State.start;
							tokensList.add(t);
						}
						break;
					case greater_than:
						t.kind = Kind.GT;
						t.currentPosition = start;
						t.tokenLength = position - start;
						tokensList.add(t);
						state = State.start;
						break;
					case or:
						t.kind = Kind.OR;
						t.currentPosition = start;
						t.tokenLength = newLength;
						tokensList.add(t);
						state = State.start;
						break;
					case div:
						t.kind = Kind.DIV;
						t.currentPosition = start;
						t.tokenLength = newLength;
						tokensList.add(t);
						state = State.start;
						break;
					case identity:
						if(!Character.isLetter(c)) {
							String str = Lexer.globalInput.substring(start, position);
							if(str.equals("do")) {
								t.kind = Kind.KW_DO;
							}
							else if(str.equals("end")) {
								t.kind = Kind.KW_END;
							}
							else if(str.equals("let")) {
								t.kind = Kind.KW_LET;
							}
							else if(str.equals("switch")) {
								t.kind = Kind.KW_SWITCH;
							}
							else if(str.equals("case")) {
								t.kind = Kind.KW_CASE;
							}
							else if(str.equals("default")) {
								t.kind = Kind.KW_DEFAULT;
							}
							else if(str.equals("if")) {
								t.kind = Kind.KW_IF;
							}
							else if(str.equals("else")) {
								t.kind = Kind.KW_ELSE;
							}
							else if(str.equals("while")) {
								t.kind = Kind.KW_WHILE;
							}
							else if(str.equals("return")) {
								t.kind = Kind.KW_RETURN;
							}
							else if(str.equals("list")) {
								t.kind = Kind.KW_LIST;
							}
							else if(str.equals("var")) {
								t.kind = Kind.KW_VAR;
							}
							else if(str.equals("val")) {
								t.kind = Kind.KW_VAL;
							}
							else if(str.equals("nil")) {
								t.kind = Kind.KW_NIL;
							}
							else if(str.equals("true")) {
								t.kind = Kind.KW_TRUE;
							}
							else if(str.equals("false")) {
								t.kind = Kind.KW_FALSE;
							}
							else if(str.equals("int")) {
								t.kind = Kind.KW_INT;
							}
							else if(str.equals("String")) {
								t.kind = Kind.KW_STRING;
							}
							else if(str.equals("float")) {
								t.kind = Kind.KW_FLOAT;
							}
							else if(str.equals("boolean")) {
								t.kind = Kind.KW_BOOLEAN;
							}
							else {
								t.kind = Kind.IDENTIFIER;
							}
							t.currentPosition = start;
							t.tokenLength = position-start;
							tokensList.add(t);
							state = State.start;
							
						}
						else {
							position = position+1;
						}
						break;
					
					
				}
			}
			
			startLineArray.add(start_line);
		}
		else {
			startLineArray.add(0);
		}
		
		switch(state) {
			case integer_literal:
				t.kind = Kind.INT_LITERAL;
				t.currentPosition = start;
				t.tokenLength = position-start;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case not:
				t.kind = Kind.NOT_EQUALS;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case minus:
				t.kind = Kind.MINUS;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case less_than:
				t.kind = Kind.LT;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case equal_sign:
				t.kind = Kind.EQUALS;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case greater_than:
				t.kind = Kind.GT;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case or:
				t.kind = Kind.OR;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case div:
				t.kind = Kind.DIV;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = newLength-1;
				tokensList.add(t);
				break;
			case start:
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = 0;
				tokensList.add(t);
				break;
			case identity:
				String str = Lexer.globalInput.substring(start, position);
				if(str.equals("do")) {
					t.kind = Kind.KW_DO;
				}
				else if(str.equals("end")) {
					t.kind = Kind.KW_END;
				}
				else if(str.equals("let")) {
					t.kind = Kind.KW_LET;
				}
				else if(str.equals("switch")) {
					t.kind = Kind.KW_SWITCH;
				}
				else if(str.equals("case")) {
					t.kind = Kind.KW_CASE;
				}
				else if(str.equals("default")) {
					t.kind = Kind.KW_DEFAULT;
				}
				else if(str.equals("if")) {
					t.kind = Kind.KW_IF;
				}
				else if(str.equals("else")) {
					t.kind = Kind.KW_ELSE;
				}
				else if(str.equals("while")) {
					t.kind = Kind.KW_WHILE;
				}
				else if(str.equals("return")) {
					t.kind = Kind.KW_RETURN;
				}
				else if(str.equals("list")) {
					t.kind = Kind.KW_LIST;
				}
				else if(str.equals("var")) {
					t.kind = Kind.KW_VAR;
				}
				else if(str.equals("val")) {
					t.kind = Kind.KW_VAL;
				}
				else if(str.equals("nil")) {
					t.kind = Kind.KW_NIL;
				}
				else if(str.equals("true")) {
					t.kind = Kind.KW_TRUE;
				}
				else if(str.equals("false")) {
					t.kind = Kind.KW_FALSE;
				}
				else if(str.equals("int")) {
					t.kind = Kind.KW_INT;
				}
				else if(str.equals("String")) {
					t.kind = Kind.KW_STRING;
				}
				else if(str.equals("float")) {
					t.kind = Kind.KW_FLOAT;
				}
				else if(str.equals("boolean")) {
					t.kind = Kind.KW_BOOLEAN;
				}
				else {
					t.kind = Kind.IDENTIFIER;
				}
				t.currentPosition = start;
				t.tokenLength = position-start;
				tokensList.add(t);
				t.kind = Kind.EOF;
				t.currentPosition = position;
				t.tokenLength = 0;
				tokensList.add(t);
				break;
			default:
				throw new Exception("Char not accepted: " + c);
		}
		return this;
		
	}
	@Override
	public Kind getKind() {
		if(kind != null)
			return kind;
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
		if(startLineArray.size() == 0) {
			return 0;
		}
		int index = Collections.binarySearch(startLineArray, currentPosition);
		if(index < 0) {
			if(index != -1) {
				index = (-index-1)-1;
			}
			else {
				index=0;
			}
		}
		return index;
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
		return currentPosition - startLineArray.get(getLine());
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


