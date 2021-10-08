package edu.ufl.cise.plpfa21.assignment3.astimpl;

import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IMutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;

public class MutableGlobal__ extends Declaration__ implements IMutableGlobal {

	INameDef varDef;
	IExpression expression;
	
	public MutableGlobal__(int line, int posInLine, String text, INameDef varDef, IExpression expr) {
		super(line, posInLine, text);
		this.varDef = varDef;
		this.expression = expr;
	}

	@Override
	public INameDef getVarDef() {
		return varDef;
	}
	
	@Override
	public IExpression getExpression() {
		return expression;
	}


	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIMutableGlobal(this, arg);
		
	}

	@Override
	public String toString() {
		return "MutableGlobal__ [varDef=" + varDef + ", expression=" + expression + "]";
	}


	
	
}
