package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IListType;
import edu.ufl.cise.plpfa21.assignment3.ast.IPrimitiveType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public class Type__ extends ASTNode__ implements IType {
	
	public Type__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}


	public static final Type__ undefinedType = new Type__(0,0,"undefinedType");
	public static final Type__ voidType = new Type__(0,0,"voidType");
	public static final Type__ nilType = new ListType__(-1,-1,"nilType",undefinedType);

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return this;
	}

	@Override
	public boolean isInt() {
		return (this instanceof IPrimitiveType)  && ((IPrimitiveType)this).getType()==TypeKind.INT;
	}

	@Override
	public boolean isBoolean() {
		return (this instanceof IPrimitiveType)  && ((IPrimitiveType)this).getType()==TypeKind.BOOLEAN;
	}

	@Override
	public boolean isString() {
		return (this instanceof IPrimitiveType)  && ((IPrimitiveType)this).getType()==TypeKind.STRING;
	}

	@Override
	public boolean isList() {
		return (this instanceof IListType);
	}

	@Override
	public boolean isKind(TypeKind kind) {
		return switch(kind) {
		case INT -> isInt();
		case BOOLEAN -> isBoolean();
		case STRING -> isString();
		case LIST -> isList();
		};
	}

	@Override
	public String toString() {
		return "Type__ [text=" + text + "]";
	}

	@Override
	public String getDesc() throws Exception {
		if (this.equals(voidType)) return "V";
		throw new Exception("Type not defined");
	}

	@Override
	public String getClassName() throws Exception {
		throw new UnsupportedOperationException("should not invoke getClassName in Type__");
	}
	
	
}
