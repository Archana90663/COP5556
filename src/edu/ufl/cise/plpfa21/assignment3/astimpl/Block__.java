package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.List;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IStatement;

public class Block__ extends ASTNode__ implements IBlock {
	

	public Block__(int line, int posInLine, String text, List<IStatement> statements) {
		super(line, posInLine, text);
		this.statements = statements;
	}

	final List<IStatement> statements;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIBlock(this, arg);
	}

	@Override
	public List<IStatement> getStatements() {
		return statements;
	}

	@Override
	public String toString() {
		return "Block__ [statements=" + statements + "]";
	}
	
	

}
