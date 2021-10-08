package edu.ufl.cise.plpfa21.assignment3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.ufl.cise.plpfa21.assignment1.CompilerComponentFactory;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds;
import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment3.ast.IBinaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IBooleanLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionCallExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.IImmutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.IIntLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IListType;
import edu.ufl.cise.plpfa21.assignment3.ast.IMutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.IPrimitiveType;
import edu.ufl.cise.plpfa21.assignment3.ast.IProgram;
import edu.ufl.cise.plpfa21.assignment3.ast.IStringLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType.TypeKind;

class ExampleASTParserTests implements PLPTokenKinds {

	private IASTNode getAST(String input) throws Exception {
		IPLPParser parser = CompilerComponentFactory.getParser(input);
		return parser.parse();
	}

	@Test
	public void test0() throws Exception {
		String input = """

				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
	}

	@Test
	public void test1() throws Exception {
		String input = """
				VAL a: INT = 0;
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "a");
		IType n8 = n5.getType();
		assertTrue(n8 instanceof IPrimitiveType);
		IPrimitiveType n9 = (IPrimitiveType) n8;
		assertEquals(n9.getType(), TypeKind.INT);
		IExpression n10 = n3.getExpression();
		assertTrue(n10 instanceof IIntLiteralExpression);
		IIntLiteralExpression n11 = (IIntLiteralExpression) n10;
		assertEquals(n11.getValue(), 0);
	}

	@Test
	public void test2() throws Exception {
		String input = """
				VAL a: STRING = "hello";
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "a");
		IType n8 = n5.getType();
		assertTrue(n8 instanceof IPrimitiveType);
		IPrimitiveType n9 = (IPrimitiveType) n8;
		assertEquals(n9.getType(), TypeKind.STRING);
		IExpression n10 = n3.getExpression();
		assertTrue(n10 instanceof IStringLiteralExpression);
		IStringLiteralExpression n11 = (IStringLiteralExpression) n10;
		assertEquals("hello", n11.getValue());
	}

	@Test
	public void test3() throws Exception {
		String input = """
				VAL b: BOOLEAN = TRUE;
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "b");
		IType n8 = n5.getType();
		assertTrue(n8 instanceof IPrimitiveType);
		IPrimitiveType n9 = (IPrimitiveType) n8;
		assertEquals(n9.getType(), TypeKind.BOOLEAN);
		IExpression n10 = n3.getExpression();
		assertTrue(n10 instanceof IBooleanLiteralExpression);
		IBooleanLiteralExpression n11 = (IBooleanLiteralExpression) n10;
		assertEquals(n11.getValue(), true);
	}

	@Test
	public void test4() throws Exception {
		String input = """
				VAR b: LIST[];
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IMutableGlobal);
		IMutableGlobal n3 = (IMutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "b");
		IType n8 = n5.getType();
		assertTrue(n8 instanceof IListType);
		IListType n9 = (IListType) n8;
	}



	@Test
	public void test5() throws Exception {
		String input = """
				VAR c = f();
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IMutableGlobal);
		IMutableGlobal n3 = (IMutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "c");
		IExpression n8 = n3.getExpression();
		assertTrue(n8 instanceof IFunctionCallExpression);
		IFunctionCallExpression n9 = (IFunctionCallExpression) n8;
		IIdentifier n10 = n9.getName();
		assertTrue(n10 instanceof IIdentifier);
		IIdentifier n11 = (IIdentifier) n10;
		assertEquals(n11.getName(), "f");
	}

	@Test
	public void test6() throws Exception {
		String input = """
				VAL c = a+b;
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "c");
		IExpression n8 = n3.getExpression();
		assertTrue(n8 instanceof IBinaryExpression);
		IBinaryExpression n9 = (IBinaryExpression) n8;
		IExpression n10 = n9.getLeft();
		assertTrue(n10 instanceof IIdentExpression);
		IIdentExpression n12 = (IIdentExpression) n10;
		IIdentifier n13 = n12.getName();
		assertTrue(n13 instanceof IIdentifier);
		IIdentifier n14 = (IIdentifier) n13;
		assertEquals(n14.getName(), "a");
		IExpression n11 = n9.getRight();
		assertTrue(n11 instanceof IIdentExpression);
		IIdentExpression n15 = (IIdentExpression) n11;
		IIdentifier n16 = n15.getName();
		assertTrue(n16 instanceof IIdentifier);
		IIdentifier n17 = (IIdentifier) n16;
		assertEquals(n17.getName(), "b");
		assertEquals(n9.getOp(), Kind.PLUS);
	}



	@Test
	public void test7() throws Exception {
		String input = """
				VAL d = ((2));
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "d");
		IExpression n8 = n3.getExpression();
		assertTrue(n8 instanceof IIntLiteralExpression);
		IIntLiteralExpression n9 = (IIntLiteralExpression) n8;
		assertEquals(n9.getValue(), 2);
	}

	@Test
	public void test8() throws Exception {
		String input = """
				VAL d = ((a+b)/(c+f()));
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IImmutableGlobal);
		IImmutableGlobal n3 = (IImmutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "d");
		IExpression n8 = n3.getExpression();
		assertTrue(n8 instanceof IBinaryExpression);
		IBinaryExpression n9 = (IBinaryExpression) n8;
		IExpression n10 = n9.getLeft();
		assertTrue(n10 instanceof IBinaryExpression);
		IBinaryExpression n12 = (IBinaryExpression) n10;
		IExpression n13 = n12.getLeft();
		assertTrue(n13 instanceof IIdentExpression);
		IIdentExpression n15 = (IIdentExpression) n13;
		IIdentifier n16 = n15.getName();
		assertTrue(n16 instanceof IIdentifier);
		IIdentifier n17 = (IIdentifier) n16;
		assertEquals(n17.getName(), "a");
		IExpression n14 = n12.getRight();
		assertTrue(n14 instanceof IIdentExpression);
		IIdentExpression n18 = (IIdentExpression) n14;
		IIdentifier n19 = n18.getName();
		assertTrue(n19 instanceof IIdentifier);
		IIdentifier n20 = (IIdentifier) n19;
		assertEquals(n20.getName(), "b");
		assertEquals(n12.getOp(), Kind.PLUS);
		IExpression n11 = n9.getRight();
		assertTrue(n11 instanceof IBinaryExpression);
		IBinaryExpression n21 = (IBinaryExpression) n11;
		IExpression n22 = n21.getLeft();
		assertTrue(n22 instanceof IIdentExpression);
		IIdentExpression n24 = (IIdentExpression) n22;
		IIdentifier n25 = n24.getName();
		assertTrue(n25 instanceof IIdentifier);
		IIdentifier n26 = (IIdentifier) n25;
		assertEquals(n26.getName(), "c");
		IExpression n23 = n21.getRight();
		assertTrue(n23 instanceof IFunctionCallExpression);
		IFunctionCallExpression n27 = (IFunctionCallExpression) n23;
		IIdentifier n28 = n27.getName();
		assertTrue(n28 instanceof IIdentifier);
		IIdentifier n29 = (IIdentifier) n28;
		assertEquals(n29.getName(), "f");
		assertEquals(n21.getOp(), Kind.PLUS);
		assertEquals(n9.getOp(), Kind.DIV);
	}


	@Test
	public void test9() throws Exception {
		String input = """
				VAR A:LIST[LIST[INT]];
				""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0 = (IProgram) ast;
		List<IDeclaration> n1 = n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IMutableGlobal);
		IMutableGlobal n3 = (IMutableGlobal) n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5 = (INameDef) n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7 = (IIdentifier) n6;
		assertEquals(n7.getName(), "A");
		IType n8 = n5.getType();
		assertTrue(n8 instanceof IListType);
		IListType n9 = (IListType) n8;
		IType n10 = n9.getElementType();
		assertTrue(n10 instanceof IListType);
		IListType n11 = (IListType) n10;
		IType n12 = n11.getElementType();
		assertTrue(n12 instanceof IPrimitiveType);
		IPrimitiveType n13 = (IPrimitiveType) n12;
		assertEquals(n13.getType(), TypeKind.INT);
	}
	
	@Test public void test10() throws Exception{
		String input = """
		VAR A;
		VAL B=0;

		""";
		IASTNode ast = getAST(input);
		assertTrue(ast instanceof IProgram);
		IProgram n0=(IProgram)ast;
		List<IDeclaration> n1=n0.getDeclarations();
		IDeclaration n2 = n1.get(0);
		assertTrue(n2 instanceof IMutableGlobal);
		IMutableGlobal n3=(IMutableGlobal)n2;
		INameDef n4 = n3.getVarDef();
		assertTrue(n4 instanceof INameDef);
		INameDef n5=(INameDef)n4;
		IIdentifier n6 = n5.getIdent();
		assertTrue(n6 instanceof IIdentifier);
		IIdentifier n7=(IIdentifier)n6;
		assertEquals(n7.getName(),"A");
		IDeclaration n8 = n1.get(1);
		assertTrue(n8 instanceof IImmutableGlobal);
		IImmutableGlobal n9=(IImmutableGlobal)n8;
		INameDef n10 = n9.getVarDef();
		assertTrue(n10 instanceof INameDef);
		INameDef n11=(INameDef)n10;
		IIdentifier n12 = n11.getIdent();
		assertTrue(n12 instanceof IIdentifier);
		IIdentifier n13=(IIdentifier)n12;
		assertEquals(n13.getName(),"B");
		IExpression n14 = n9.getExpression();
		assertTrue(n14 instanceof IIntLiteralExpression);
		IIntLiteralExpression n15=(IIntLiteralExpression)n14;
		assertEquals(n15.getValue(),0);
		}


}