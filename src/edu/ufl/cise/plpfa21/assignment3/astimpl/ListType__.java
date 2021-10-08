package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IListType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public class ListType__ extends Type__ implements IListType {
	
	IType elementType;

	public ListType__(int line, int posInLine, String text, IType elementType) {
		super(line, posInLine, text);
		this.elementType = elementType;
	}

	public IType getElementType() {
		return elementType;
	}

	public void setElementType(IType elementType) {
		this.elementType = elementType;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIListType(this, arg);
		}

	@Override
	public String toString() {
		return "ListType__ [elementType=" + elementType + "]";
	}
	
	
	

}
