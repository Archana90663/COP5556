package edu.ufl.cise.plpfa21.assignment3.ast;

import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public interface IExpression extends IASTNode {

	IType getType();
	void setType(IType type);

}
