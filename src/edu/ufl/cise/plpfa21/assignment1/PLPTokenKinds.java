package edu.ufl.cise.plpfa21.assignment1;


public interface PLPTokenKinds {

	static enum Kind  {
		INT_LITERAL,
		STRING_LITERAL,
		IDENTIFIER,
		KW_FUN,
		LPAREN,
		COLON,
		COMMA,
		RPAREN,
		KW_DO,
		KW_END,
		KW_LET,
		ASSIGN,
		SEMI,
		KW_SWITCH,
		KW_CASE,
		KW_DEFAULT,
		KW_IF,
		KW_ELSE,
		KW_WHILE,
		KW_RETURN,
		KW_LIST,
		KW_VAR,
		KW_VAL,
		AND,
		OR,
		LT,
		GT,
		EQUALS,
		NOT_EQUALS,
		PLUS,
		MINUS,
		TIMES,
		DIV,
		BANG,
		KW_NIL,
		KW_TRUE,
		KW_FALSE,
		LSQUARE,
		RSQUARE,
		KW_INT,
		KW_STRING,
		KW_FLOAT,
		KW_BOOLEAN,
		EOF, 
		ERROR
}

}
