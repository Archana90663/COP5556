package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;

public class Identifier__ extends ASTNode__ implements IIdentifier {

	String name;

	public Identifier__(int line, int posInLine, String text, String name) {
		super(line, posInLine, text);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIdentifier(this, arg);
	}

	@Override
	public String toString() {
		return "Identifier__ [name=" + name + "]";
	}
	
	

}
