package edu.ufl.cise.plpfa21.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ufl.cise.plpfa21.assignment1.PLPStateKinds.State;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class Lexer implements IPLPLexer {
	
	static String globalInput;
	int numberOfTokens;
	static ArrayList<Token> tokensList;
	static List<Integer> startLineArray;
	
	public Lexer(String input) {
		this.globalInput = input;
		this.numberOfTokens = 0;
		tokensList = new ArrayList<>();
		this.startLineArray = new ArrayList<>();
		
	}
	
	public void scanToken() throws LexicalException{
		for(int i=0; i<tokensList.size(); i++) {
			System.out.println("Before method starts: " + tokensList.get(i).kind);
		}
		char c ='\0';
		int position=0;
		int start=0;
		int newLength = 1;
		State state = State.start;
		Token t = new Token(null,0,0);
		if(globalInput.length() != 0) {
			int start_line = 0;
			int line = 0;
			while(globalInput.length() > position) {
				c = globalInput.charAt(position);
				switch(state) {
					case start:
						start = position;
						switch(c) {
						case '\n':
							startLineArray.add(start_line);
							line++;
							start_line = position+1;
							position = position+1;
							break;
//						case '\t':
//							startLineArray.add(start_line);
//							line++;
//							start_line = position+1;
//							position = position+1;
//							break;
//						case '\r':
//							startLineArray.add(start_line);
//							line++;
//							start_line = position+1;
//							position = position+1;
//							break;
//						case ' ':
//							startLineArray.add(start_line);
//							line++;
//							start_line = position+1;
//							position = position+1;
//							break;
						case ';':
//							t.kind = Kind.SEMI;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.SEMI, start, newLength));
							position = position+1;
							break;
						case ',':
//							t.kind = Kind.COMMA;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.COMMA, start, newLength));
							position = position+1;
							break;
						case '(':
							tokensList.add(new Token(Kind.LPAREN, start, newLength));
							position = position+1;
							break;
						case ')':
//							t.kind = Kind.RPAREN;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.RPAREN, start, newLength));
							position = position+1;
							break;
						case '0':
//							t.kind = Kind.INT_LITERAL;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.INT_LITERAL, start, newLength));
							position = position+1;
							break;
						case '&':
//							t.kind = Kind.AND;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.AND, start, newLength));

							position = position+1;
							break;
						case '*':
//							t.kind = Kind.TIMES;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.TIMES, start, newLength));

							position = position+1;
							break;
						case '+':
//							t.kind = Kind.PLUS;
//							t.currentPosition = start;
//							t.tokenLength = newLength;
//							tokensList.add(t);
							tokensList.add(new Token(Kind.PLUS, start, newLength));

							position = position+1;
							break;
//						case '!':
//							state = State.not;
//							position = position+1;
//							break;
//						case '-':
//							state = State.minus;
//							position = position+1;
//							break;
						case '=':
							state = State.equal_sign;
							position = position+1;
							break;
//						case '<':
//							state = State.greater_than;
//							position = position+1;
//							break;
//						case '>':
//							state = State.less_than;
//							position = position+1;
//							break;
//						case '|':
//							state = State.or;
//							position = position+1;
//							break;
//						case '/':
//							state = State.div;
//							position = position+1;
//							break;
						default:
							if(Character.isDigit(c)) {
								state = State.integer_literal;
								position = position+1;
							}
							else if(Character.isWhitespace(c)) {
								position = position+1;
							}
							else if(Character.isLetter(c)){
								state = State.identity;
								position = position+1;
							}
							else {
								throw new LexicalException("Invalid token", line, position-start_line);
							}
						
						}
						break;
					case integer_literal:
						if(!Character.isDigit(c)) {
							Token token = new Token(Kind.INT_LITERAL, start, position-start);
//							t.kind = Kind.INT_LITERAL;
//							t.currentPosition = start;
//							t.tokenLength = position-start;
							try {
								int num = token.getIntValue();
							} catch(Exception e) {
								throw new LexicalException("Invalid token", token.getLine(), token.getCharPositionInLine());							}
							tokensList.add(token);
							state = State.start;
						}
						else {
							position = position+1;
						}
						break;
//					case not:
//						if(c == '=') {
//							t.kind = Kind.NOT_EQUALS;
//							t.currentPosition= start;
//							t.tokenLength = newLength+1;
//							tokensList.add(t);
//							position = position+1;
//						}
//						state = State.start;
//						break;
//					case minus:
//						t.kind = Kind.MINUS;
//						t.currentPosition = start;
//						t.tokenLength = 1;
//						tokensList.add(t);
//						state = State.start;
//						break;
//					case less_than:
//						t.kind = Kind.LT;
//						t.currentPosition = start;
//						t.tokenLength = position - start;
//						tokensList.add(t);
//						state = State.start;
//						break;
					case equal_sign:
						if(c == '=') {
//							Token t6 = new Token(null,0,0);
//							t6.kind = Kind.EQUALS;
//							t6.currentPosition = start;
//							t6.tokenLength = newLength+1;
							tokensList.add(new Token(Kind.EQUALS, start, newLength+1));
							position = position+1;
//							tokensList.add(t6);
						}
						else {
//							t.kind = Kind.ASSIGN;
//							t.currentPosition = start;
//							t.tokenLength = newLength+1;
							tokensList.add(new Token(Kind.ASSIGN, start, newLength+1));
							position = position+1;
							state = State.start;
//							tokensList.add(t);
						}
						break;
//					case greater_than:
//						t.kind = Kind.GT;
//						t.currentPosition = start;
//						t.tokenLength = position - start;
//						tokensList.add(t);
//						state = State.start;
//						break;
//					case or:
//						t.kind = Kind.OR;
//						t.currentPosition = start;
//						t.tokenLength = newLength;
//						tokensList.add(t);
//						state = State.start;
//						break;
//					case div:
//						t.kind = Kind.DIV;
//						t.currentPosition = start;
//						t.tokenLength = newLength;
//						tokensList.add(t);
//						state = State.start;
//						break;
					case identity:
						if(!Character.isLetter(c) && !Character.isDigit(c)) {
							String str = globalInput.substring(start, position);
							Kind kind;
							if(str.equals("do")) {
								kind = Kind.KW_DO;
							}
							else if(str.equals("end")) {
								kind = Kind.KW_END;
							}
							else if(str.equals("let")) {
								kind = Kind.KW_LET;
							}
							else if(str.equals("switch")) {
								kind = Kind.KW_SWITCH;
							}
							else if(str.equals("case")) {
								kind = Kind.KW_CASE;
							}
							else if(str.equals("default")) {
								kind = Kind.KW_DEFAULT;
							}
							else if(str.equals("if")) {
								kind = Kind.KW_IF;
							}
							else if(str.equals("else")) {
								kind = Kind.KW_ELSE;
							}
							else if(str.equals("while")) {
								kind = Kind.KW_WHILE;
							}
							else if(str.equals("return")) {
								kind = Kind.KW_RETURN;
							}
							else if(str.equals("list")) {
								kind = Kind.KW_LIST;
							}
							else if(str.equals("var")) {
								kind = Kind.KW_VAR;
							}
							else if(str.equals("val")) {
								kind = Kind.KW_VAL;
							}
							else if(str.equals("nil")) {
								kind = Kind.KW_NIL;
							}
							else if(str.equals("true")) {
								kind = Kind.KW_TRUE;
							}
							else if(str.equals("false")) {
								kind = Kind.KW_FALSE;
							}
							else if(str.equals("int")) {
								kind = Kind.KW_INT;
							}
							else if(str.equals("String")) {
								kind = Kind.KW_STRING;
							}
							else if(str.equals("float")) {
								kind = Kind.KW_FLOAT;
							}
							else if(str.equals("boolean")) {
								kind = Kind.KW_BOOLEAN;
							}
							else {
								kind = Kind.IDENTIFIER;
							}
							tokensList.add(new Token(kind, start, position-start));
//							t.currentPosition = start;
//							t.tokenLength = position-start;
//							tokensList.add(t);
							state = State.start;
							System.out.println("\n");
							
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
				Token token2 = new Token(Kind.INT_LITERAL, start, position-start);
//				t.kind = Kind.INT_LITERAL;
//				t.currentPosition = start;
//				t.tokenLength = position-start;
//				tokensList.add(t);
				try {
					int num = token2.getIntValue();
				} catch(Exception e) {
					throw new LexicalException("Invalid token", token2.getLine(), token2.getCharPositionInLine());
				}
				tokensList.add(token2);
//				Token t2 = new Token(null,0,0);
//				t2.kind = Kind.EOF;
//				t2.currentPosition = position;
//				t2.tokenLength = newLength-1;
//				tokensList.add(t2);
				tokensList.add(new Token(Kind.EOF, position, newLength-1));

				break;
//			case not:
//				t.kind = Kind.NOT_EQUALS;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
//			case minus:
//				t.kind = Kind.MINUS;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
//			case less_than:
//				t.kind = Kind.LT;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
			case equal_sign:
				tokensList.add(new Token(Kind.EQUALS, start, newLength));
//				t.kind = Kind.EQUALS;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				Token t3 = new Token(null,0,0);
//				t3.kind = Kind.EOF;
//				t3.currentPosition = position;
//				t3.tokenLength = newLength-1;
//				tokensList.add(t3);
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				break;
//			case greater_than:
//				t.kind = Kind.GT;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
//			case or:
//				t.kind = Kind.OR;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
//			case div:
//				t.kind = Kind.DIV;
//				t.currentPosition = start;
//				t.tokenLength = newLength;
//				tokensList.add(t);
//				t.kind = Kind.EOF;
//				t.currentPosition = position;
//				t.tokenLength = newLength-1;
//				tokensList.add(t);
//				break;
			case start:
//				Token t4 = new Token(null,0,0);
//				t4.kind = Kind.EOF;
//				t4.currentPosition = position;
//				t4.tokenLength = newLength;
//				tokensList.add(t4);
				tokensList.add(new Token(Kind.EOF, position, newLength));
				break;
			case identity:
				String str = globalInput.substring(start, position);
				Kind kind;
				if(str.equals("do")) {
					kind = Kind.KW_DO;
				}
				else if(str.equals("end")) {
					kind = Kind.KW_END;
				}
				else if(str.equals("let")) {
					kind = Kind.KW_LET;
				}
				else if(str.equals("switch")) {
					kind = Kind.KW_SWITCH;
				}
				else if(str.equals("case")) {
					kind = Kind.KW_CASE;
				}
				else if(str.equals("default")) {
					kind = Kind.KW_DEFAULT;
				}
				else if(str.equals("if")) {
					kind = Kind.KW_IF;
				}
				else if(str.equals("else")) {
					kind = Kind.KW_ELSE;
				}
				else if(str.equals("while")) {
					kind = Kind.KW_WHILE;
				}
				else if(str.equals("return")) {
					kind = Kind.KW_RETURN;
				}
				else if(str.equals("list")) {
					kind = Kind.KW_LIST;
				}
				else if(str.equals("var")) {
					kind = Kind.KW_VAR;
				}
				else if(str.equals("val")) {
					kind = Kind.KW_VAL;
				}
				else if(str.equals("nil")) {
					kind = Kind.KW_NIL;
				}
				else if(str.equals("true")) {
					kind = Kind.KW_TRUE;
				}
				else if(str.equals("false")) {
					kind = Kind.KW_FALSE;
				}
				else if(str.equals("int")) {
					kind = Kind.KW_INT;
				}
				else if(str.equals("String")) {
					kind = Kind.KW_STRING;
				}
				else if(str.equals("float")) {
					kind = Kind.KW_FLOAT;
				}
				else if(str.equals("boolean")) {
					kind = Kind.KW_BOOLEAN;
				}
				else {
					kind = Kind.IDENTIFIER;
				}
				tokensList.add(new Token(kind, start, position-start));
//				t.currentPosition = start;
//				t.tokenLength = position-start;
//				tokensList.add(t);
				System.out.println("Before: " + tokensList.get(tokensList.size()-1));
//				Token t5 = new Token(null,0,0);
//				t5.kind = Kind.EOF;
//				t5.currentPosition = position;
//				t5.tokenLength = newLength-1;
//				tokensList.add(t5);
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				System.out.println("After: " + tokensList.get(tokensList.size()-1));
				break;
			default:
				throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());
		}
				
		
	}

	@Override
	public IPLPToken nextToken() throws LexicalException {
			Token t = new Token(null,0,0);
			try {
				if(numberOfTokens==0) {
					this.scanToken();
				}
				t = tokensList.get(numberOfTokens);
//				for(int i=0; i<tokensList.size(); i++) {
//					System.out.println("Token from list: " + tokensList.get(i).kind + " Line: " + t.getLine() + " Char in position Line: " + t.getCharPositionInLine());
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());
			}
			numberOfTokens++;
			return t;
		
	}
			
	
}