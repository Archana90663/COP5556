package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IIntLiteralExpression;

public class IntLiteralExpression__ extends Expression__ implements IIntLiteralExpression {
	int value;

	public IntLiteralExpression__(int line, int posInLine, String text, int value) {
		super(line, posInLine, text);
		this.value = value;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIntLiteralExpression(this, arg);
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "IntLiteralExpression__ [value=" + value + "]";
	}
	
	
	
}
