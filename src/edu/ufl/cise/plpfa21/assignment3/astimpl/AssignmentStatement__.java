package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IAssignmentStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;

public class AssignmentStatement__ extends Statement__ implements IAssignmentStatement {
	
	IExpression left;
	IExpression right;
	public AssignmentStatement__(int line, int posInLine, String text, IExpression left, IExpression right) {
		super(line, posInLine, text);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIAssignmentStatement(this,arg);
	}

	@Override
	public IExpression getLeft() {
		return left;
	}

	@Override
	public IExpression getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "AssignmentStatement__ [left=" + left + ", right=" + right + "]";
	}


	

}
