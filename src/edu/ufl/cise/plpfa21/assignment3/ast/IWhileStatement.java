package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IWhileStatement extends IStatement {
	
	IExpression getGuardExpression();
	IBlock getBlock();

}
