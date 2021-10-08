package edu.ufl.cise.plpfa21.assignment3.ast;

public interface IType extends IASTNode {

	public static enum TypeKind {
		INT,
		BOOLEAN,
		STRING,
		LIST
	}
	
}
