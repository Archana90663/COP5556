package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public class NameDef__ extends ASTNode__ implements INameDef {
	
	final IIdentifier identifier;
	IType type;
	

	public NameDef__(int line, int posInLine, String text, IIdentifier identifier, IType type) {
		super(line, posInLine, text);
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitINameDef(this, arg);
	}

	@Override
	public IIdentifier getIdent() {
		return identifier;
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public void setType(IType type) {
		this.type = type;		
	}

	@Override
	public String toString() {
		return "NameDef__ [identifier=" + identifier + ", type=" + type + "]";
	}



	

}
