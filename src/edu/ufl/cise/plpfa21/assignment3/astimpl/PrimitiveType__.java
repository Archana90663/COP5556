package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.Objects;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IPrimitiveType;

public class PrimitiveType__ extends Type__ implements IPrimitiveType {

	TypeKind typeKind;

	public PrimitiveType__(int line, int posInLine, String text, TypeKind typeKind) {
		super(line, posInLine, text);
		this.typeKind = typeKind;
	}

	public PrimitiveType__(TypeKind typeKind) {
		super(0, 0, "");
		this.typeKind = typeKind;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIPrimitiveType(this, arg);
	}

	@Override
	public TypeKind getType() {
		return typeKind;
	}

	@Override
	public String toString() {
		return "PrimitiveType__ [typeKind=" + typeKind + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeKind);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimitiveType__ other = (PrimitiveType__) obj;
		return typeKind == other.typeKind;
	};

	@Override
	public boolean isInt() {
		return typeKind == TypeKind.INT;
	}

	@Override
	public boolean isString() {
		return typeKind == TypeKind.STRING;
	}

	@Override
	public boolean isBoolean() {
		return typeKind == TypeKind.BOOLEAN;
	}
	
	public static final PrimitiveType__ intType = new PrimitiveType__(0,0,"INT",TypeKind.INT);
	public static final PrimitiveType__ booleanType = new PrimitiveType__(0,0,"BOOLEAN",TypeKind.BOOLEAN);
	public static final PrimitiveType__ stringType = new PrimitiveType__(0,0,"STRING",TypeKind.STRING);

}
