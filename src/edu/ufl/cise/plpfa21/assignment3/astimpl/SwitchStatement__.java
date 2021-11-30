package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.List;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.ISwitchStatement;

public class SwitchStatement__ extends Statement__ implements ISwitchStatement {
	
	IExpression switchExpression;
	List<IExpression> branchExpressions;
	List<IBlock> blocks;
	IBlock defaultBlock;
	
	
	
	public SwitchStatement__(int line, int posInLine, String text, IExpression switchExpression,
			List<IExpression> branchExpressions, List<IBlock> blocks, IBlock defaultBlock) {
		super(line, posInLine, text);
		this.switchExpression = switchExpression;
		this.branchExpressions = branchExpressions;
		this.blocks = blocks;
		this.defaultBlock = defaultBlock;
	}
	
	@Override
	public IExpression getSwitchExpression() {
		return switchExpression;
	}
	@Override
	public List<IExpression> getBranchExpressions() {
		return branchExpressions;
	}
	@Override
	public List<IBlock> getBlocks() {
		return blocks;
	}
	@Override
	public IBlock getDefaultBlock() {
		return defaultBlock;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitISwitchStatement(this,arg);
	}

	@Override
	public String toString() {
		return "SwitchStatement__ [switchExpression=" + switchExpression + ", branchExpressions=" + branchExpressions
				+ ", blocks=" + blocks + ", defaultBlock=" + defaultBlock + "]";
	}
	
	
}
