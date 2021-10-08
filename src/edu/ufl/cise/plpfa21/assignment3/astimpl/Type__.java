package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public abstract class Type__ extends ASTNode__ implements IType {
	
	public Type__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}

}
