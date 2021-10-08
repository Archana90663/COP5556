package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.INilConstantExpression;

public class NilConstantExpression__ extends Expression__ implements INilConstantExpression {

	public NilConstantExpression__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitINilConstantExpression(null, arg);
	}

	@Override
	public String toString() {
		return "NilConstantExpression__ []";
	}
	
	

}
