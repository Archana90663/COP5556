package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;

public class IdentExpression__ extends Expression__ implements IIdentExpression {
	
	IIdentifier name;

	public IdentExpression__(int line, int posInLine, String text, IIdentifier name) {
		super(line, posInLine, text);
		this.name = name;
	}
	
	@Override
	public IIdentifier getName() {
		return name;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIdentExpression(this,arg);
	}

	@Override
	public String toString() {
		return "IdentExpression__ [name=" + name + "]";
	}

	
	
	

}
