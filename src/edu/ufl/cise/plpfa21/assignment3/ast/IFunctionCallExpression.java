package edu.ufl.cise.plpfa21.assignment3.ast;

import java.util.List;

public interface IFunctionCallExpression extends IExpression {
	
	IIdentifier getName();
	
	List<IExpression> getArgs();

}
