package edu.ufl.cise.plpfa21.assignment3.astimpl;

import java.util.List;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;

public class FunctionDeclaration___ extends Declaration__ implements IFunctionDeclaration {
	
	IIdentifier name;
	List<INameDef> args;
	IType resultType;
	IBlock block;
	
	public FunctionDeclaration___(int line, int posInLine, String text, IIdentifier name, List<INameDef> args,
			IType resultType, IBlock block) {
		super(line, posInLine, text);
		this.name = name;
		this.args = args;
		this.resultType = resultType;
		this.block = block;
	}
	public List<INameDef> getArgs() {
		return args;
	}
	public IBlock getBlock() {
		return block;
	}
	public IIdentifier getName() {
		return name;
	}
	public IType getResultType() {
		return resultType;
	}
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIFunctionDeclaration(this, arg);
	}
	@Override
	public String toString() {
		return "FunctionDeclaration___ [args=" + args + ", block=" + block + ", name=" + name + ", resultType="
				+ resultType + "]";
	}
	
	

}
