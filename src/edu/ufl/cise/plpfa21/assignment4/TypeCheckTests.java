package edu.ufl.cise.plpfa21.assignment4;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.ufl.cise.plpfa21.assignment1.CompilerComponentFactory;
import edu.ufl.cise.plpfa21.assignment2.IPLPParser;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment4.TypeCheckVisitor.TypeCheckException;

class TypeCheckTests {
	private static final boolean VERBOSE = false;

	IASTNode checkTypes(IASTNode ast) throws Exception {
		TypeCheckVisitor v = new TypeCheckVisitor();
		ast.visit(v, null);
		return ast;
	}

	IASTNode getAST(String input) throws Exception {
		IPLPParser parser = CompilerComponentFactory.getParser(input);
		return parser.parse();
	}

	void parseAndCheckTypes(String input) throws Exception {
		show(input);
		IASTNode ast = getAST(input);
		show(ast);
		checkTypes(ast);
		show(ast);
		show("-------------------------");
	}

	void parseAndCheckTypesWithTypeError(String input) throws Exception {
		show(input);
		IASTNode ast = getAST(input);
		show(ast);
		assertThrows(TypeCheckException.class, () -> {
			try {
				checkTypes(ast);
			} catch (TypeCheckException e) {
				show(e);
				throw e;
			}
		});
		show(ast);
		show("-------------------------");
	}

	void show(Object o) {
		if (VERBOSE) {
			System.out.println(o);
		}
	}

	@BeforeAll
	public static void beforeAll() throws FileNotFoundException {
		out = new PrintStream(new BufferedOutputStream(new FileOutputStream("hw3Errors.txt")));
	}

	@AfterAll
	public static void afterAll() {
		out.close();
	}

	static PrintStream out;

	IASTNode noErrorParse(String input, TestInfo testInfo) throws Exception {
		IPLPParser parser = CompilerComponentFactory.getParser(input);
		return assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
			try {
				return parser.parse();
			} catch (Throwable e) {
				out.println(testInfo.getDisplayName() + ": Unexpected exception in error-free input " + e.getClass()
						+ "  " + e.getMessage());
				out.println("input=");
				out.println(input);
				out.println("-----------");
				throw e;
			}
		});
	}

	IASTNode errorParse(String input, TestInfo testInfo) throws Exception {
		IPLPParser parser = CompilerComponentFactory.getParser(input);
		assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
			try {
				assertThrows(Exception.class, () -> {
					parser.parse();
				});
			} catch (Throwable e) {
				out.println(testInfo.getDisplayName() + ": " + e.getClass() + "  " + e.getMessage());
				out.println("input=");
				out.println(input);
				out.println("-----------");
				throw e;
			}
		});
		return null;
	}

	@DisplayName("test0")
	@Test
	public void test0(TestInfo testInfo) throws Exception {
		String input = """

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test1")
	@Test
	public void test1(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: INT = 0;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test2")
	@Test
	public void test2(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: STRING = "hello";

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test3")
	@Test
	public void test3(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: BOOLEAN = TRUE;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test4")
	@Test
	public void test4(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: BOOLEAN = FALSE;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test5")
	@Test
	public void test5(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT = 2+4;
					VAR b: INT = a-1;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test6")
	@Test
	public void test6(TestInfo testInfo) throws Exception {
		String input = """
					VAL  a: INT = 2+4;
				VAL  b: INT = a-1;
				VAR  c: BOOLEAN = a<b;
				VAR  d: BOOLEAN = a>b;
				VAR  e:  BOOLEAN= c == d;
				VAR  f:  BOOLEAN= e != d;
				VAR  g:  BOOLEAN = !f != d;
				VAR  h:  BOOLEAN  = !(f == d);
				VAR  i:  BOOLEAN = g && h;
				VAR  j:  BOOLEAN = g || h;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test7")
	@Test
	public void test7(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: INT = 4;
				VAL b: INT = -a;
				VAL c: INT = -(-a);
				VAL d: INT = -5;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test8")
	@Test
	public void test8(TestInfo testInfo) throws Exception {
		String input = """
					VAL  a: INT = 2*4;
				VAL  b: INT = a/1;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test9")
	@Test
	public void test9(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST [INT];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test10")
	@Test
	public void test10(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST [INT] = NIL;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test11")
	@Test
	public void test11(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST [INT] = NIL;
				   VAR b: INT = a[0];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test12")
	@Test
	public void test12(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST [INT] = NIL;
				   VAR b: INT = a[3+a[2]];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test13")
	@Test
	public void test13(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST [LIST [ INT] ];
				                  VAR b: LIST [INT] = a[0];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test14")
	@Test
	public void test14(TestInfo testInfo) throws Exception {
		String input = """
				FUN a():INT DO END
				                  VAR c: INT = a();

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test15")
	@Test
	public void test15(TestInfo testInfo) throws Exception {
		String input = """
				FUN a(x:INT, y:BOOLEAN, s:STRING):STRING
				DO RETURN "hello"; END
				VAR c:STRING = a(2, TRUE, "hello");

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test16")
	@Test
	public void test16(TestInfo testInfo) throws Exception {
		String input = """
				VAR x:INT;
				VAR y:STRING;
				FUN f(a:INT):STRING
				   DO LET b:INT = 5 DO x=a+b; RETURN "go gators"; END
				   END
				VAR z:STRING = f(3);

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test17")
	@Test
	public void test17(TestInfo testInfo) throws Exception {
		String input = """
				VAR x: INT;
				FUN f(x:INT)
				DO
				SWITCH x
				CASE 1 :
				CASE 2 :
				DEFAULT
				END

				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test18")
	@Test
	public void test18(TestInfo testInfo) throws Exception {
		String input = """
				VAL a = 1;
				VAL b = 2;
				VAR x: INT;
				FUN f(x:INT)
				DO
				SWITCH x
				CASE 0 :
				CASE a :
				CASE b :
				DEFAULT
				END

				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test19")
	@Test
	public void test19(TestInfo testInfo) throws Exception {
		String input = """
				VAR x: INT;
				FUN f(x:STRING)
				DO
				SWITCH x
				CASE "Go" :
				CASE "Gators" :
				DEFAULT
				END
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test20")
	@Test
	public void test20(TestInfo testInfo) throws Exception {
		String input = """
				VAR x: BOOLEAN;
				FUN f(x:INT)
				DO
				SWITCH x
				CASE 1 :
				CASE 2 :
				DEFAULT
				END
				END
				FUN g()
				DO
				f(1);
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test21")
	@Test
	public void test21(TestInfo testInfo) throws Exception {
		String input = """
				VAR b: BOOLEAN;
				FUN f(x:BOOLEAN)
				DO
				SWITCH x
				CASE TRUE :
				CASE FALSE :
				DEFAULT
				END
				END
				FUN g()
				DO
				f(TRUE);
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test22")
	@Test
	public void test22(TestInfo testInfo) throws Exception {
		String input = """
				VAR x: INT;
				FUN f(x:INT)
				DO
				IF x==0 DO END
				END


				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test23")
	@Test
	public void test23(TestInfo testInfo) throws Exception {
		String input = """
				VAR x: INT;
				FUN f(x:INT)
				DO
				WHILE x > 0 DO x=x-1; END
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test24")
	@Test
	public void test24(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[LIST[INT]];
				VAR b: LIST[] = a[0];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test25")
	@Test
	public void test25(TestInfo testInfo) throws Exception {
		String input = """

				VAR a: LIST[LIST[INT]];
				VAR b: LIST[] = a[0];
				VAR c: INT = b[0];

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test26")
	@Test
	public void test26(TestInfo testInfo) throws Exception {
		String input = """
				VAR a:INT = 42;
				FUN f(x:INT, y:BOOLEAN):INT
				DO
				   IF y DO a = x; END
				   IF !y DO a = 0; END
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test27")
	@Test
	public void test27(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT;
				FUN f(x:INT) DO a = x; END
				FUN main() DO
				f(3);
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test28")
	@Test
	public void test28(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT;
				FUN f(x:INT) DO a = x; END
				FUN main() DO
				f(3);
				END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test29")
	@Test
	public void test29(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[INT];
				VAR N = 5;
				FUN f(b: LIST[INT])
				DO
						LET i:INT=0
						DO  LET c:LIST[INT]
						    DO
						    WHILE i < N
						       DO
						        c[i] = b[i] + i;
						       END

						    END
						END
				      END
				   VAR j=0;
				   FUN init() DO
				      WHILE j < N
				      DO
				          a[j] = j;
				      END
				   END
				   FUN main() DO
				       init();
				       f(a);
				   END

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test30")
	@Test
	public void test30(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[LIST[INT]];
				VAR b: LIST[LIST[]] = a;

				""";
		parseAndCheckTypes(input);
	}

	@DisplayName("test31")
	@Test
	public void test31(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: INT = "hello";

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test32")
	@Test
	public void test32(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: STRING = TRUE;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test33")
	@Test
	public void test33(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: BOOLEAN = 0;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test34")
	@Test
	public void test34(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: BOOLEAN = "hello";

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test35")
	@Test
	public void test35(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT = "hello" + "goodbye";
					VAR b: INT = a-1;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test36")
	@Test
	public void test36(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: INT = a+1;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test37")
	@Test
	public void test37(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[LIST[INT]];
				VAR b: LIST[LIST[STRING]] = a[2];


				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test38")
	@Test
	public void test38(TestInfo testInfo) throws Exception {
		String input = """
					VAL  a: STRING = "hello";
				VAL  b: INT = a-1;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test39")
	@Test
	public void test39(TestInfo testInfo) throws Exception {
		String input = """
				VAL  a: STRING = "hello";
				VAL  b: INT = 3;
				VAR  c: BOOLEAN = a<b;
				VAR  e:  BOOLEAN= c == d;
				VAR  f:  BOOLEAN= e != d;
				VAR  g:  BOOLEAN = !f != d;
				VAR  h:  BOOLEAN  = !(f == d);
				VAR  i:  BOOLEAN = g && h;
				VAR  j:  BOOLEAN = g || h;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test40")
	@Test
	public void test40(TestInfo testInfo) throws Exception {
		String input = """
				VAL  a: INT = 2;
				VAL  b: INT = 3;
				VAR  c: BOOLEAN = a<b;
				VAR  e:  BOOLEAN= c == d; /* d not declared */

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test41")
	@Test
	public void test41(TestInfo testInfo) throws Exception {
		String input = """
				VAL  a: INT = 2;
				VAL  b: INT = 3;
				VAR  c: BOOLEAN = a<b;
				VAR  d: BOOLEAN = TRUE;
				VAR  e:  BOOLEAN= c == b;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test42")
	@Test
	public void test42(TestInfo testInfo) throws Exception {
		String input = """
				VAL  a: INT = 2;
				VAL  b: INT = 3;
				VAR  c: BOOLEAN = a<b;
				VAR  d: BOOLEAN = TRUE;
				VAR  e:  BOOLEAN= c == d;
				VAR  f:  BOOLEAN= e != d;
				VAR  g:  BOOLEAN = !b != d;  /*b wrong type*/
				VAR  h:  BOOLEAN  = !(f == d);
				VAR  i:  BOOLEAN = g && h;
				VAR  j:  BOOLEAN = g || h;

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test43")
	@Test
	public void test43(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: INT = 4;
				VAL b: INT = -a;
				VAL c: INT = -(-a);
				VAL d: INT = -5;
				FUN f(x:INT, y:BOOLEAN, s:STRING):STRING
				                  DO RETURN "hello"; END
				VAR e:STRING = f(a,TRUE,d);

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test44")
	@Test
	public void test44(TestInfo testInfo) throws Exception {
		String input = """
				VAL a:INT = 42;
				FUN f(x:INT, y:BOOLEAN):INT
				DO
				   IF y DO a = x; END
				   IF !y DO a = 0; END
				END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test45")
	@Test
	public void test45(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT;
				FUN f(x:INT) DO a = x; END
				FUN main() DO
				f(3) = 4;
				END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test46")
	@Test
	public void test46(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: INT;
				FUN f(x) DO a = x; END
				FUN main() DO
				f(3);
				END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test47")
	@Test
	public void test47(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[INT];
				VAR N = 5;
				FUN f(b: LIST[])
				DO
						LET i:INT=0
						DO  LET c:LIST[INT]
						    DO
						    WHILE i < N
						       DO
						        c[i] = b[i] + i;
						       END

						    END
						END
				      END
				   VAR j=0;
				   FUN init() DO
				      WHILE j < N
				      DO
				          a[j] = j;
				      END
				   END
				   FUN main() DO
				       init();
				       f(a);
				   END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test48")
	@Test
	public void test48(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[INT] = NIL;
				VAR N = 5;
				FUN f(b: LIST[])
				DO
						LET i:INT=0
						DO  LET c:LIST[INT]
						    DO
						    WHILE i < N
						       DO
						        c[i] = i;
						        a[i] = b[i] + c[i];
						       END

						    END
						END
				      END
				   VAR j=0;
				   FUN init() DO
				      WHILE j < N
				      DO
				          a[j] = j;
				      END
				   END
				   FUN main() DO
				       init();
				       f(a);
				   END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test49")
	@Test
	public void test49(TestInfo testInfo) throws Exception {
		String input = """
				VAL a: LIST[INT] = NIL;
				VAR N = 5;
				FUN f(b: LIST[])
				DO
						LET i:INT=0
						DO  LET c:LIST[INT]
						    DO
						    WHILE i < N
						       DO
						        c[i] = i;
						        a[i] = b[i] + c[i];
						       END

						    END
						END
				      END
				   VAR j=0;
				   FUN init() DO
				      WHILE j < N
				      DO
				          a[j] = j;
				      END
				   END
				   FUN main() DO
				       init();
				       f(a);
				   END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test50")
	@Test
	public void test50(TestInfo testInfo) throws Exception {
		String input = """
				VAR a: LIST[INT];
				VAR b: LIST[] = a[0];

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test51")
	@Test
	public void test51(TestInfo testInfo) throws Exception {
		String input = """

				VAR a: LIST[INT];
				VAR b: LIST[] = a[0];
				VAR c: LIST[] = b[0];

				""";
		parseAndCheckTypesWithTypeError(input);
	}

	@DisplayName("test52")
	@Test
	public void test52(TestInfo testInfo) throws Exception {
		String input = """
				VAL a = 1;
				VAR b = 2;
				VAR x: INT;
				FUN f(x:INT)
				DO
				SWITCH x
				CASE 0 :
				CASE a :
				CASE b :
				DEFAULT
				END

				END

				""";
		parseAndCheckTypesWithTypeError(input);
	}

}