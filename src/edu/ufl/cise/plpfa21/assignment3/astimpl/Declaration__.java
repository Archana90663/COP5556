package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;

public abstract class Declaration__ extends ASTNode__ implements IDeclaration{

	public Declaration__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}



}
