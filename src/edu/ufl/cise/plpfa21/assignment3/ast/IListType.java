package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IListType extends IType {
	
	IType getElementType();
	void setElementType(IType type);

}
