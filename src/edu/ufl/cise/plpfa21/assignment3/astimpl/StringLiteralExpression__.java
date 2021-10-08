package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IStringLiteralExpression;

public class StringLiteralExpression__ extends Expression__ implements IStringLiteralExpression {
	
	String value;

	public StringLiteralExpression__(int line, int posInLine, String text, String value) {
		super(line, posInLine, text);
		this.value = value;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIStringLiteralExpression(this, arg);
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "StringLiteralExpression__ [value=" + value + "]";
	}
	
	

}
