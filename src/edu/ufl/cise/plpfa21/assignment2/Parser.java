package edu.ufl.cise.plpfa21.assignment2;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.Lexer;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment1.Token;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;


public class Parser implements IPLPParser{

	public IPLPToken token;
	public Lexer lexer;
	
	public Parser(Lexer lexer) {
		this.lexer = lexer;
		try {
			this.token = lexer.nextToken();
		} catch (LexicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void parse() throws Exception {
			Kind kind = this.token.getKind();
			switch(kind) {
				case EOF:
					return;
				case IDENTIFIER:
					this.token = lexer.nextToken();
					break;
				case INT_LITERAL:
					this.token = lexer.nextToken();
					break;
				case KW_TRUE:
					this.token = lexer.nextToken();
					break;
				case LPAREN:
					this.token = lexer.nextToken();
					for(Kind k : Kind.values()) {
						if(this.token.getKind().equals(k)) {
							this.token = lexer.nextToken();
						}
					}
					break;
				default:
					throw new SyntaxException("",this.token.getLine(), this.token.getCharPositionInLine());
			}
		}
		
	}
	
