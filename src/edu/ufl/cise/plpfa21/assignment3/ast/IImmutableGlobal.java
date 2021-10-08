package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IImmutableGlobal extends IDeclaration {

	
	INameDef getVarDef();
	IExpression getExpression();

}
