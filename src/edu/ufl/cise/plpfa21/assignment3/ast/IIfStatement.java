package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IIfStatement extends IStatement {
	
	IExpression getGuardExpression();
	IBlock  getBlock();

}
