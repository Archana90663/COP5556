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
	
	
	
	public Token setKind(){
		char c ='\0';
		int position=0;
		int start=0;
		int newLength = 1;
		State state = State.start;
		if(Lexer.globalInput.length() != 0) {
			int start_line = 0;
			int line = 0;
			while(Lexer.globalInput.length() > position) {
				c = Lexer.globalInput.charAt(position);
				if(state==State.start) {
					start = position;
					Token t = new Token(null, 0,0);
					if(c == '\n' || c == '\r' || c == '\t' || c=='\f' || c==' ') {
						startLineArray.add(start_line);
						line = line+1;
						start_line = position+1;
						position = position+1;
					}
					else if(c==';') {
						t.kind = Kind.SEMI;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c==',') {
						t.kind = Kind.COMMA;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c == '(') {
						t.kind = Kind.LPAREN;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c == ')') {
						t.kind = Kind.RPAREN;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
						
					}
					else if(c == '0') {
						t.kind = Kind.INT_LITERAL;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c=='&') {
						t.kind = Kind.AND;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c=='*') {
						t.kind = Kind.TIMES;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c == '+') {
						t.kind = Kind.PLUS;
						t.currentPosition = start;
						t.tokenLength = newLength;
						position = position+1;
					}
					else if(c=='!') {
						state = State.not;
						position = position+1;
					}
					else if(c == '-') {
						state = State.minus;
						position = position+1;
					}
					else if(c == '=') {
						state = State.equal_sign;
						position = position+1;
					}
					else if(c=='|') {
						state = State.or;
						position = position+1;
					}
					else if(c == '/') {
						state = State.div;
						position = position+1;
					}
					else if(c == '<') {
						state = State.less_than;
						position = position+1;
					}
					else if(c =='>') {
						state = State.greater_than;
						position = position+1;
					}
					else if(Character.isDigit(c)) {
						state = State.integer_literal;
						position = position+1;
					}
					else if(Character.isWhitespace(c)) {
						position = position+1;
					}
					else if((c == '$') || Character.isDigit(c) || Character.isLetter(c) || (c=='_') == true) {
						state = State.identity;
						position = position+1;
					}
				}
				else if(state == State.integer_literal) {
					if(!Character.isDigit(c)) {
						Token t2 = new Token(null, 0, 0);
						t2.kind = Kind.INT_LITERAL;
						t2.currentPosition = start;
						int length = position - start;
						t2.tokenLength = length;
						int v = t2.getIntValue();
						tokensList.add(t2);
						state = State.start;
					}
					else {
						position = position+1;
					}
				}
				else if(state == State.not) {
					if(c == '=') {
						Token t2 = new Token(null, 0, 0);
						t2.kind = Kind.NOT_EQUALS;
						t2.currentPosition = start;
						t2.tokenLength = newLength+1;
						tokensList.add(t2);
					}
					state = State.start;
					
				}
				else if(state == State.minus) {
					Token t2 = new Token(null, 0, 0);
					t2.kind = Kind.MINUS;
					t2.currentPosition = start;
					t2.tokenLength = newLength;
					tokensList.add(t2);
					state = State.start;
				}
				else if(state == State.less_than) {
					if(c != '=') {
						Token t2 = new Token(null ,0,0);
						t2.kind = Kind.LT;
						t2.currentPosition = start;
						int len = position - start;
						t2.tokenLength = len;
						tokensList.add(t2);
			
					}
					state = State.start;
				}
				else if(state == State.equal_sign) {
					if(c == '=') {
						position = position+1;
						Token t2 = new Token(null,0,0);
						t2.kind = Kind.EQUALS;
						t2.currentPosition = start;
						t2.tokenLength = newLength+1;
						tokensList.add(t2);
						state = State.start;
					}
				}
				else if(state == State.greater_than) {
					if(c != '=') {
						Token t2 = new Token(null, 0,0);
						t2.kind = Kind.GT;
						t2.currentPosition = start;
						int len = position - start;
						t2.tokenLength = len;
						tokensList.add(t2);
					}
					state = State.start;
				}
				else if(state == State.or) {
					Token t2 = new Token(null,0,0);
					t2.kind = Kind.OR;
					t2.currentPosition = start;
					t2.tokenLength = newLength;
					tokensList.add(t2);
					state = State.start;
				}
				else if(state == State.div) {
					Token t2 = new Token(null, 0,0);
					t2.kind = Kind.DIV;
					t2.currentPosition = start;
					t2.tokenLength = newLength;
					tokensList.add(t2);
					state = State.start;
				}
				else if(state == State.identity) {
					if((c == '$') || Character.isLetter(c) || Character.isDigit(c) || (c=='_') == false) {
						String str = Lexer.globalInput.substring(start, position);
						Kind k;				
						if(str.equals("do")) {
							k = Kind.KW_DO;
						}
						else if(str.equals("end")) {
							k = Kind.KW_END;
						}
						else if(str.equals("let")) {
							k = Kind.KW_LET;
						}
						else if(str.equals("switch")) {
							k = Kind.KW_SWITCH;
						}
						else if(str.equals("case")) {
							k = Kind.KW_CASE;
						}
						else if(str.equals("default")) {
							k = Kind.KW_DEFAULT;
						}
						else if(str.equals("if")) {
							k = Kind.KW_IF;
						}
						else if(str.equals("else")) {
							k = Kind.KW_ELSE;
						}
						else if(str.equals("while")) {
							k = Kind.KW_WHILE;
						}
						else if(str.equals("return")) {
							k = Kind.KW_RETURN;
						}
						else if(str.equals("list")) {
							k = Kind.KW_LIST;
						}
						else if(str.equals("var")) {
							k = Kind.KW_VAR;
						}
						else if(str.equals("val")) {
							k = Kind.KW_VAL;
						}
						else if(str.equals("nil")) {
							k = Kind.KW_NIL;
						}
						else if(str.equals("true")) {
							k = Kind.KW_TRUE;
						}
						else if(str.equals("false")) {
							k = Kind.KW_FALSE;
						}
						else if(str.equals("int")) {
							k = Kind.KW_INT;
						}
						else if(str.equals("String")) {
							k = Kind.KW_STRING;
						}
						else if(str.equals("float")) {
							k = Kind.KW_FLOAT;
						}
						else if(str.equals("boolean")) {
							k = Kind.KW_BOOLEAN;
						}
						else {
							k = Kind.IDENTIFIER;
						}
						Token t2 = new Token(null,0,0);
						t2.kind = k;
						t2.currentPosition = start;
						int len = position - start;
						t2.tokenLength = len;
						tokensList.add(t2);
						state = State.start;
					}
					else {
						position = position+1;
					}
					
					
				}
				
				
	}
			startLineArray.add(start_line);
			
			
}
		else {
			startLineArray.add(0);
		}
		
		if(state == State.integer_literal) {
			Token t = new Token(null,0,0);
			t.kind = Kind.INT_LITERAL;
			t.currentPosition = start;
			int len = position - start;
			t.tokenLength = len;
			tokensList.add(t);
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t2);
		}
		else if(state == State.not) {
			Token t = new Token(null,0,0);
			t.kind = Kind.NOT_EQUALS;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.minus) {
			Token t = new Token(null,0,0);
			t.kind = Kind.MINUS;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.less_than) {
			Token t = new Token(null,0,0);
			t.kind = Kind.LT;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.equal_sign) {
			Token t = new Token(null,0,0);
			t.kind = Kind.EQUALS;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.greater_than) {
			Token t = new Token(null,0,0);
			t.kind = Kind.GT;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.or) {
			Token t = new Token(null,0,0);
			t.kind = Kind.OR;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.div) {
			Token t = new Token(null,0,0);
			t.kind = Kind.DIV;
			t.currentPosition = start;
			t.tokenLength = 1;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
			tokensList.add(t);
			tokensList.add(t2);
		}
		else if(state == State.start) {
			Token t = new Token(null,0,0);
			t.kind = Kind.EOF;
			t.currentPosition = position;
			t.tokenLength = 0;
		}
		else if(state == State.identity) {
			String str = Lexer.globalInput.substring(start, position);
			Kind k;				
			if(str.equals("do")) {
				k = Kind.KW_DO;
			}
			else if(str.equals("end")) {
				k = Kind.KW_END;
			}
			else if(str.equals("let")) {
				k = Kind.KW_LET;
			}
			else if(str.equals("switch")) {
				k = Kind.KW_SWITCH;
			}
			else if(str.equals("case")) {
				k = Kind.KW_CASE;
			}
			else if(str.equals("default")) {
				k = Kind.KW_DEFAULT;
			}
			else if(str.equals("if")) {
				k = Kind.KW_IF;
			}
			else if(str.equals("else")) {
				k = Kind.KW_ELSE;
			}
			else if(str.equals("while")) {
				k = Kind.KW_WHILE;
			}
			else if(str.equals("return")) {
				k = Kind.KW_RETURN;
			}
			else if(str.equals("list")) {
				k = Kind.KW_LIST;
			}
			else if(str.equals("var")) {
				k = Kind.KW_VAR;
			}
			else if(str.equals("val")) {
				k = Kind.KW_VAL;
			}
			else if(str.equals("nil")) {
				k = Kind.KW_NIL;
			}
			else if(str.equals("true")) {
				k = Kind.KW_TRUE;
			}
			else if(str.equals("false")) {
				k = Kind.KW_FALSE;
			}
			else if(str.equals("int")) {
				k = Kind.KW_INT;
			}
			else if(str.equals("String")) {
				k = Kind.KW_STRING;
			}
			else if(str.equals("float")) {
				k = Kind.KW_FLOAT;
			}
			else if(str.equals("boolean")) {
				k = Kind.KW_BOOLEAN;
			}
			else {
				k = Kind.IDENTIFIER;
			}
			Token t = new Token(null,0,0);
			t.kind = k;
			t.currentPosition = start;
			int len = position-start;
			t.tokenLength = len;
			Token t2 = new Token(null,0,0);
			t2.kind = Kind.EOF;
			t2.currentPosition = position;
			t2.tokenLength = 0;
		}
		this.startLineArray = Collections.unmodifiableList(startLineArray);
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


