package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IUnaryExpression;

public class UnaryExpression__ extends Expression__ implements IUnaryExpression {

	IExpression expression;
	Kind op;
	
	public UnaryExpression__(int line, int posInLine, String text, IExpression expression, Kind op) {
		super(line, posInLine, text);
		this.expression = expression;
		this.op = op;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIUnaryExpression(this, arg);
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Kind getOp() {
		return op;
	}

	@Override
	public String toString() {
		return "UnaryExpression__ [expression=" + expression + ", op=" + op + "]";
	}
	
	

}
