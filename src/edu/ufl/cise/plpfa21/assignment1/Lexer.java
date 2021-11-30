package edu.ufl.cise.plpfa21.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ufl.cise.plpfa21.assignment1.PLPStateKinds.State;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class Lexer implements IPLPLexer {
	
	public static String globalInput;
	int numberOfTokens;
	public static ArrayList<Token> tokensList;
	static List<Integer> startLineArray;
	
	public Lexer(String input) {
		this.globalInput = input;
		this.numberOfTokens = 0;
		tokensList = new ArrayList<>();
		this.startLineArray = new ArrayList<>();
		
	}
	
	public void scanToken() throws LexicalException{
		char c ='\0';
		int position=0;
		int start=0;
		int newLength = 1;
		State state = State.start;
		Token t = new Token(null,0,0);
		if(globalInput.length() != 0) {
			int start_line = 0;
			while(globalInput.length() > position) {
				c = globalInput.charAt(position);
				switch(state) {
					case start:
						start = position;
						switch(c) {
						case '\r':
							startLineArray.add(start_line);
							start_line = position+1;
							position = position+1;
							break;
						case '\n':
							startLineArray.add(start_line);
							start_line = position+1;
							position = position+1;
							break;
						case '\t':
							startLineArray.add(start_line);
							start_line = position+1;
							position = position+1;
							break;
						case '\"':
							startLineArray.add(start_line);
							start_line = position+1;
							position = position+1;
							break;
						case ';':
							tokensList.add(new Token(Kind.SEMI, start, newLength));
							position = position+1;
							break;
						case ':':
							tokensList.add(new Token(Kind.COLON, start, newLength));
							position = position+1;
							break;
						case ',':
							tokensList.add(new Token(Kind.COMMA, start, newLength));
							position = position+1;
							break;
						case '(':
							tokensList.add(new Token(Kind.LPAREN, start, newLength));
							position = position+1;
							break;
						case ')':
							tokensList.add(new Token(Kind.RPAREN, start, newLength));
							position = position+1;
							break;
						case '[':
							tokensList.add(new Token(Kind.LSQUARE, start, newLength));
							position = position+1;
							break;
						case ']':
							tokensList.add(new Token(Kind.RSQUARE, start, newLength));
							position = position+1;
							break;
						case '<':
							tokensList.add(new Token(Kind.LT, start, newLength));
							position = position+1;
							break;
						case '>':
							tokensList.add(new Token(Kind.GT, start, newLength));
							position = position+1;
							break;
//						case '0':
//							tokensList.add(new Token(Kind.INT_LITERAL, start, newLength));
//							position = position+1;
//							break;
//						case '&':
//							tokensList.add(new Token(Kind.AND, start, newLength));
//							position = position+1;
//							break;
						case '&':
							state = State.and;
							position = position+1;
							break;
//						case '\"':
//							state = State.string_start;
//							position = position+1;
//							break;
						case '=':
							state = State.equal_sign;
							position = position+1;
							break;
						case '-':
							tokensList.add(new Token(Kind.MINUS, start, newLength));
							position = position+1;
							break;
						case '*':
							tokensList.add(new Token(Kind.TIMES, start, newLength));
							position = position+1;
							break;
						case '/':
							state = State.div;
							position = position+1;
							break;
						case '+':
							tokensList.add(new Token(Kind.PLUS, start, newLength));
							position = position+1;
							break;
//						case '|':
//							tokensList.add(new Token(Kind.OR, start, newLength));
//							position = position+1;
//							break;
						case '|':
							state = State.or;
							position = position+1;
							break;
						case '!':
							state = State.not;
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
							else if(Character.isJavaIdentifierPart(c)){
								state = State.identity;
								position = position+1;
							}
							else {
								tokensList.add(new Token(null,0,0));
								position = position+1;
							}
						
						}
						break;
					case integer_literal:
						if(!Character.isDigit(c)) {
							Token token = new Token(Kind.INT_LITERAL, start, position-start);
							try {
								int num = Integer.parseInt(token.getText());
							} catch(Exception e) {
								throw new LexicalException("Invalid token", token.getLine(), token.getCharPositionInLine());							}
							state = State.start;
							tokensList.add(token);

						}
						else {
							position = position+1;
						}
						break;
					case div:
						if(c != '*') {
							tokensList.add(new Token(Kind.DIV, start, newLength));
							state = State.start;	
						}
						else {
							state = State.comment;
							position = position+1;
						}					
						break;
					case string_start:
						if(c == '\"') {
							position = position+1;
							tokensList.add(new Token(Kind.STRING_LITERAL, start, position-start));
							state = State.start;
						}
						else if(Character.isJavaIdentifierPart(c)){
							state = State.string_literal;
							position = position+1;
						}
						else {
							state = State.start;
						}
						break;
					case and:
						if(c == '&') {
							position = position+1;
							tokensList.add(new Token(Kind.AND, start, newLength+1));
							state = State.start;
						}
						break;
					case or:
						if(c == '|') {
							position = position+1;
							tokensList.add(new Token(Kind.OR, start, newLength+1));
							state = State.start;
						}
						break;
					case equal_sign:
						if(c == '=') {
							position = position+1;
							tokensList.add(new Token(Kind.EQUALS, start, newLength+1));
							state = State.start;
						}
						else {
							tokensList.add(new Token(Kind.ASSIGN, start, position-start));
							position = position+1;
							state = State.start;
//							tokensList.add(t);
						}
						break;									
					case not:
						if(c == '=') {
							tokensList.add(new Token(Kind.NOT_EQUALS, start, newLength+1));
							position = position+1;
						}
						state = State.start;
						break;
					case comment:
						if(c == '\n') {
							startLineArray.add(start_line);
							start_line = position+1;
							
						}
						else if(c == '*') {
							state = State.close_comment;
						}
						position = position+1;
						break;
					case close_comment:
						if(c=='*') {
							state = State.close_comment;
						}
						else if(c == '/') {
							state = State.start;
						}
						else {
							if(c == '\n') {
								startLineArray.add(start_line);
								start_line = position+1;
							}
							state = State.comment;
						}
						position = position+1;
						break;
					case string_literal:
						if(!Character.isJavaIdentifierPart(c)) {
							String str = globalInput.substring(start, position);
							tokensList.add(new Token(Kind.STRING_LITERAL, start, position-start));
							state = State.start;
						}
						else {
							position = position+1;
						}
						break;
						
					case identity:
						if(!Character.isJavaIdentifierPart(c)) {
							String str = globalInput.substring(start, position);
							Kind kind;
							if(str.equals("DO")) {
								kind = Kind.KW_DO;
							}
							else if(str.equals("FUN")) {
								kind = Kind.KW_FUN;
							}
							else if(str.equals("END")) {
								kind = Kind.KW_END;
							}
							else if(str.equals("LET")) {
								kind = Kind.KW_LET;
							}
							else if(str.equals("SWITCH")) {
								kind = Kind.KW_SWITCH;
							}
							else if(str.equals("CASE")) {
								kind = Kind.KW_CASE;
							}
							else if(str.equals("DEFAULT")) {
								kind = Kind.KW_DEFAULT;
							}
							else if(str.equals("IF")) {
								kind = Kind.KW_IF;
							}
							else if(str.equals("ELSE")) {
								kind = Kind.KW_ELSE;
							}
							else if(str.equals("WHILE")) {
								kind = Kind.KW_WHILE;
							}
							else if(str.equals("RETURN")) {
								kind = Kind.KW_RETURN;
							}
							else if(str.equals("LIST")) {
								kind = Kind.KW_LIST;
							}
							else if(str.equals("VAR")) {
								kind = Kind.KW_VAR;
							}
							else if(str.equals("VAL")) {
								kind = Kind.KW_VAL;
							}
							else if(str.equals("NIL")) {
								kind = Kind.KW_NIL;
							}
							else if(str.equals("TRUE")) {
								kind = Kind.KW_TRUE;
							}
							else if(str.equals("FALSE")) {
								kind = Kind.KW_FALSE;
							}
							else if(str.equals("INT")) {
								kind = Kind.KW_INT;
							}
							else if(str.equals("STRING")) {
								kind = Kind.KW_STRING;
							}
							else if(str.equals("FLOAT")) {
								kind = Kind.KW_FLOAT;
							}
							else if(str.equals("BOOLEAN")) {
								kind = Kind.KW_BOOLEAN;
							}
							else {
								kind = Kind.IDENTIFIER;
//								kind = Kind.STRING_LITERAL;
							}
							tokensList.add(new Token(kind, start, position-start));
//							t.currentPosition = start;
//							t.tokenLength = position-start;
//							tokensList.add(t);
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
			case start:
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				break;
			case integer_literal:
				Token token = new Token(Kind.INT_LITERAL, start, position-start);
				try {
					int num = token.getIntValue();
				} catch(Exception e) {
					throw new LexicalException("Invalid token", token.getLine(), token.getCharPositionInLine());
				}
				tokensList.add(token);
				tokensList.add(new Token(Kind.EOF, position, newLength-1));

				break;
			case not:
				tokensList.add(new Token(Kind.NOT_EQUALS, start, newLength));
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				break;
			case close_comment:
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				break;
			case div:
				tokensList.add(new Token(Kind.DIV, start, newLength));
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
				break;
			case identity:
				String str = globalInput.substring(start, position-start);
				Kind kind;
				if(str.equals("DO")) {
					kind = Kind.KW_DO;
				}
				else if(str.equals("FUN")) {
					kind = Kind.KW_FUN;
				}
				else if(str.equals("END")) {
					kind = Kind.KW_END;
				}
				else if(str.equals("LET")) {
					kind = Kind.KW_LET;
				}
				else if(str.equals("SWITCH")) {
					kind = Kind.KW_SWITCH;
				}
				else if(str.equals("CASE")) {
					kind = Kind.KW_CASE;
				}
				else if(str.equals("DEFAULT")) {
					kind = Kind.KW_DEFAULT;
				}
				else if(str.equals("IF")) {
					kind = Kind.KW_IF;
				}
				else if(str.equals("ELSE")) {
					kind = Kind.KW_ELSE;
				}
				else if(str.equals("WHILE")) {
					kind = Kind.KW_WHILE;
				}
				else if(str.equals("RETURN")) {
					kind = Kind.KW_RETURN;
				}
				else if(str.equals("LIST")) {
					kind = Kind.KW_LIST;
				}
				else if(str.equals("VAR")) {
					kind = Kind.KW_VAR;
				}
				else if(str.equals("VAL")) {
					kind = Kind.KW_VAL;
				}
				else if(str.equals("NIL")) {
					kind = Kind.KW_NIL;
				}
				else if(str.equals("TRUE")) {
					kind = Kind.KW_TRUE;
				}
				else if(str.equals("FALSE")) {
					kind = Kind.KW_FALSE;
				}
				else if(str.equals("INT")) {
					kind = Kind.KW_INT;
				}
				else if(str.equals("STRING")) {
					kind = Kind.KW_STRING;
				}
				else if(str.equals("FLOAT")) {
					kind = Kind.KW_FLOAT;
				}
				else if(str.equals("BOOLEAN")) {
					kind = Kind.KW_BOOLEAN;
				}
				else {
					kind = Kind.IDENTIFIER;
				}
				tokensList.add(new Token(kind, start, position-start));
				tokensList.add(new Token(Kind.EOF, position, newLength-1));
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
				if(t.kind == null) {
					throw new Exception();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new LexicalException("Invalid token", 0, 0);
			}
			
//			System.out.println(t.getKind());
//			for(int i=0; i<tokensList.size(); i++) {
//				System.out.println("Token items: " + tokensList.get(i).getKind());
//			}
//			System.out.println("\n");
//			System.out.println(t.kind);
			numberOfTokens++;
			return t;
		
	}
			
	
}