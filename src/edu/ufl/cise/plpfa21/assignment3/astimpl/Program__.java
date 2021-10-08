package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.List;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IProgram;

public class Program__ extends ASTNode__ implements IProgram {

	
	final List<IDeclaration> declarations;



	public Program__(int line, int posInLine, String text, List<IDeclaration> declarations) {
		super(line, posInLine, text);
		this.declarations = declarations;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIProgram(this, arg);
	}

	@Override
	public List<IDeclaration> getDeclarations() {
		return declarations;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IProgram decs=[\n");
		for (IDeclaration d: declarations) {
			sb.append(d.toString()).append('\n');
		}
		sb.append(']');
		return sb.toString();
	}



}
