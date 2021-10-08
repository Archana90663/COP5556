package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IListSelectorExpression extends IExpression {
	
	IIdentifier getName();
	IExpression getIndex();

}
