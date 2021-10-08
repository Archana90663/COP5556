package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IAssignmentStatement extends IStatement {
	
	IExpression getLeft();
	IExpression getRight();

}
