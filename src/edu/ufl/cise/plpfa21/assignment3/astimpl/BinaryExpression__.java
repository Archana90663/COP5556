package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBinaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;

public class BinaryExpression__ extends Expression__ implements IBinaryExpression {
	
	public BinaryExpression__(int line, int posInLine, String text, IExpression left, IExpression right, Kind op) {
		super(line, posInLine, text);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	final IExpression left;
	final IExpression right;
	final Kind op;
	
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIBinaryExpression(this, arg);
	}

	public IExpression getLeft() {
		return left;
	}

	public IExpression getRight() {
		return right;
	}

	public Kind getOp() {
		return op;
	}

	@Override
	public String toString() {
		return "BinaryExpression__ [left=" + left + ", right=" + right + ", op=" + op + "]";
	}


	
	

}
