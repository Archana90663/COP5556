package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public abstract class Expression__ extends ASTNode__ implements IExpression{

	public Expression__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}
	
	IType type;
	
	
	@Override
	public 
	IType getType() {
		return type;
	}
	

	@Override
	public
	void setType(IType type) {
		this.type = type;
	}

	


}
