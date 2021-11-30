package edu.ufl.cise.plpfa21.assignment3.ast;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;

public interface IUnaryExpression extends IExpression, PLPTokenKinds{
	
	IExpression getExpression();
	Kind getOp();
}
