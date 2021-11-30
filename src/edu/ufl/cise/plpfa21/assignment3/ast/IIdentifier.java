package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IIdentifier extends IASTNode {
	
	String getName();
	IDeclaration getDec();
	void setDec(IDeclaration dec);
	void setSlot(int i);
	int getSlot();
	
}
