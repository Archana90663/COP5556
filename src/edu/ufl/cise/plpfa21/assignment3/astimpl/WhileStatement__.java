package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IWhileStatement;

public class WhileStatement__ extends Statement__ implements IWhileStatement {
	
	IExpression guardExpression;
	IBlock block;
	public WhileStatement__(int line, int posInLine, String text, IExpression guardExpression, IBlock block) {
		super(line, posInLine, text);
		this.guardExpression = guardExpression;
		this.block = block;
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
		return v.visitIWhileStatement(this,arg);
	}

	@Override
	public String toString() {
		return "WhileStatement__ [guardExpression=" + guardExpression + ", block=" + block + "]";
	}
	
	

}
