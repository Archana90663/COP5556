package edu.ufl.cise.plpfa21.assignment3.ast;

import java.util.List;

public interface IBlock extends IASTNode {

	List<IStatement> getStatements();
	
}
