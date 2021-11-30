package edu.ufl.cise.plpfa21.assignment3.ast;

public interface ILetStatement extends IStatement {
	
	IBlock getBlock();
	IExpression getExpression();
	INameDef getLocalDef();

}
