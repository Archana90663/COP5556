package edu.ufl.cise.plpfa21.assignment3.ast;

import java.util.List;

public interface ISwitchStatement extends IStatement {
	
	IExpression getSwitchExpression();
	List<IExpression> getBranchExpressions();
	List<IBlock> getBlocks();
	IBlock getDefaultBlock();


}
