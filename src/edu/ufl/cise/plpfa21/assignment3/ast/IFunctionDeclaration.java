package edu.ufl.cise.plpfa21.assignment3.ast;

import java.util.List;

public interface IFunctionDeclaration extends IDeclaration {

	List<INameDef> getArgs();
	IBlock getBlock();
	IIdentifier getName();
	IType getResultType();
}
