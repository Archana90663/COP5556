package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIfStatement;

public class IfStatement__ extends Statement__ implements IIfStatement {
	
	IExpression guardExpression;
	IBlock block;
	public IfStatement__(int line, int posInLine, String text, IExpression guardExpression, IBlock ifBlock) {
		super(line, posInLine, text);
		this.guardExpression = guardExpression;
		this.block = ifBlock;
	}
	
	@Override
	public IExpression getGuardExpression() {
		return guardExpression;
	}
	@Override
	public IBlock getBlock() {
		return block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIfStatement(this, arg);
	}

	@Override
	public String toString() {
		return "IfStatement__ [guardExpression=" + guardExpression + ", block=" + block + "]";
	}
	
	

}
