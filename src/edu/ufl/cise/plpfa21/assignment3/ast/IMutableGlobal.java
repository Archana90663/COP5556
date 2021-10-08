
package edu.ufl.cise.plpfa21.assignment3.ast;



public interface IMutableGlobal extends IDeclaration
{
  INameDef getVarDef();
  IExpression getExpression();

} 
