package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IPrimitiveType;

public class PrimitiveType__ extends Type__ implements IPrimitiveType {
	
	TypeKind typeKind;

	public PrimitiveType__(int line, int posInLine, String text, TypeKind typeKind) {
		super(line, posInLine, text);
		this.typeKind = typeKind;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIPrimitiveType(this,arg);
	}

	@Override
	public TypeKind getType() {
		return typeKind;
	}

	@Override
	public String toString() {
		return "PrimitiveType__ [typeKind=" + typeKind + "]";
	};
	
	

}
