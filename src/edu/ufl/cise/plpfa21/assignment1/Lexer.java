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
							else{
								state = State.identity;
								position = position+1;
							}
//							else {
//								throw new Exception("Char not accepted: " + c);
//							}
						
						}
						break;
					case integer_literal:
						if(!Character.isDigit(c)) {
							t.kind = Kind.INT_LITERAL;
							t.currentPosition = start;
							t.tokenLength = position-start;
							try {
								int num = t.getIntValue();
							} catch(Exception e) {
								throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());							}
							tokensList.add(t);
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
							Token t6 = new Token(null,0,0);
							t6.kind = Kind.EQUALS;
							t6.currentPosition = start;
							t6.tokenLength = newLength+1;
							position = position+1;
							tokensList.add(t6);
						}
						else {
							t.kind = Kind.ASSIGN;
							t.currentPosition = start;
							t.tokenLength = newLength+1;
							position = position+1;
							state = State.start;
							tokensList.add(t);
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
				t.kind = Kind.INT_LITERAL;
				t.currentPosition = start;
				t.tokenLength = position-start;
				tokensList.add(t);
				try {
					int num = t.getIntValue();
				} catch(Exception e) {
					throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());
				}
				Token t2 = new Token(null,0,0);
				t2.kind = Kind.EOF;
				t2.currentPosition = position;
				t2.tokenLength = newLength-1;
				tokensList.add(t2);
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
				t.kind = Kind.EQUALS;
				t.currentPosition = start;
				t.tokenLength = newLength;
				tokensList.add(t);
				Token t3 = new Token(null,0,0);
				t3.kind = Kind.EOF;
				t3.currentPosition = position;
				t3.tokenLength = newLength-1;
				tokensList.add(t3);
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
				Token t4 = new Token(null,0,0);
				t4.kind = Kind.EOF;
				t4.currentPosition = position;
				t4.tokenLength = newLength;
				tokensList.add(t4);
				break;
			case identity:
				String str = globalInput.substring(start, position);
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
				System.out.println("Before: " + tokensList.get(tokensList.size()-1));
				Token t5 = new Token(null,0,0);
				t5.kind = Kind.EOF;
				t5.currentPosition = position;
				t5.tokenLength = newLength-1;
				tokensList.add(t5);
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
				this.scanToken();
				t = tokensList.get(numberOfTokens);
				for(int i=0; i<tokensList.size(); i++) {
					System.out.println("Token from list: " + tokensList.get(i).kind);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new LexicalException("Invalid token", t.getLine(), t.getCharPositionInLine());
			}
			numberOfTokens++;
			return t;
		
	}
			
	
}