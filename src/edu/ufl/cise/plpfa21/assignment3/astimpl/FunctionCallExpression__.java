package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.List;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionCallExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;

public class FunctionCallExpression__ extends Expression__ implements IFunctionCallExpression {

	final IIdentifier name;
	final List<IExpression> args;
	
	public IIdentifier getName() {
		return name;
	}
	public List<IExpression> getArgs() {
		return args;
	}
	
	public FunctionCallExpression__(int line, int posInLine, String text, IIdentifier name, List<IExpression> args) {
		super(line, posInLine, text);
		this.name = name;
		this.args = args;
	}
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIFunctionCallExpression(this, arg);
	}
	@Override
	public String toString() {
		return "FunctionCallExpression__ [name=" + name + ", args=" + args + "]";
	}

	
	

	
}
