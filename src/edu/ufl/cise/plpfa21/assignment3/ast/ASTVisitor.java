package edu.ufl.cise.plpfa21.assignment3.ast;


public interface ASTVisitor {
	
    Object visitIBinaryExpression(IBinaryExpression n, Object arg)throws Exception;
    Object visitIBlock(IBlock n, Object arg)throws Exception;
    Object visitIBooleanLiteralExpression(IBooleanLiteralExpression n, Object arg)throws Exception;
    Object visitIExpressionStatement(IExpressionStatement n, Object arg)throws Exception;
    Object visitIFunctionDeclaration(IFunctionDeclaration n, Object arg)throws Exception;
    Object visitIFunctionCallExpression(IFunctionCallExpression n, Object arg)throws Exception;
    Object visitIIdentExpression(IIdentExpression n, Object arg)throws Exception;
    Object visitIIdentifier(IIdentifier n, Object arg)throws Exception;
    Object visitIIfStatement(IIfStatement n, Object arg)throws Exception;
    Object visitIImmutableGlobal(IImmutableGlobal n, Object arg)throws Exception;
    Object visitIIntLiteralExpression(IIntLiteralExpression n, Object arg)throws Exception;
    Object visitILetStatement(ILetStatement n, Object arg)throws Exception;
    Object visitIListSelectorExpression(IListSelectorExpression n, Object arg)throws Exception;
    Object visitIListType(IListType n, Object arg)throws Exception;
    Object visitINameDef(INameDef n, Object arg)throws Exception;
    Object visitINilConstantExpression(INilConstantExpression n, Object arg)throws Exception;
	Object visitIProgram(IProgram n, Object arg) throws Exception;
    Object visitIReturnStatement(IReturnStatement n, Object arg)throws Exception;
	Object visitIStringLiteralExpression(IStringLiteralExpression n, Object arg)throws Exception;
	Object visitISwitchStatement(ISwitchStatement n, Object arg)throws Exception;
	Object visitIUnaryExpression(IUnaryExpression n, Object arg)throws Exception;
	Object visitIWhileStatement(IWhileStatement n, Object arg)throws Exception;
	Object visitIMutableGlobal(IMutableGlobal n, Object arg) throws Exception;
	Object visitIPrimitiveType(IPrimitiveType n, Object arg) throws Exception;
	Object visitIAssignmentStatement(IAssignmentStatement n, Object arg) throws Exception;

}
