package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.IListSelectorExpression;

public class ListSelectorExpression__ extends Expression__ implements IListSelectorExpression {
	
	IIdentifier name;
	IExpression index;
	public ListSelectorExpression__(int line, int posInLine, String text, IIdentifier name, IExpression index) {
		super(line, posInLine, text);
		this.name = name;
		this.index = index;
	}
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIListSelectorExpression(this, arg);
	}
	@Override
	public IIdentifier getName() {
		return name;
	}
	@Override
	public IExpression getIndex() {
		return index;
	}
	@Override
	public String toString() {
		return "ListSelectorExpression__ [name=" + name + ", index=" + index + "]";
	}
	
	

}
