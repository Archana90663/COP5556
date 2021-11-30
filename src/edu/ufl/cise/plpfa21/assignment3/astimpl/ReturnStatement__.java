package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IReturnStatement;

public class ReturnStatement__ extends Statement__ implements IReturnStatement {
	
	IExpression expression;

	public ReturnStatement__(int line, int posInLine, String text, IExpression returnExpression) {
		super(line, posInLine, text);
		this.expression = returnExpression;
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIReturnStatement(this, arg);
	}

	@Override
	public String toString() {
		return "ReturnStatement__ [expression=" + expression + "]";
	}
	
	

}
