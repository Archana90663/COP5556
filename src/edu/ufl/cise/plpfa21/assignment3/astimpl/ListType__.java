package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(elementType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListType__ other = (ListType__) obj;
		return Objects.equals(elementType, other.elementType);
	}
	
//	@Override
//	public String getDesc() throws Exception {
//		return "[" + elementType.getDesc();
//	}
	
	@Override
	public String getDesc() throws Exception {
		return "Ljava/util/ArrayList;";
	}
	
	@Override
	public String getClassName() throws Exception {
		return "java/util/ArrayList";
	}
	

}
