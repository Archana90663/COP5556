package edu.ufl.cise.plpfa21.assignment3.ast;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;

public interface IBinaryExpression extends IExpression, PLPTokenKinds {
	
	IExpression getLeft();
	IExpression getRight();
	Kind getOp();

}
