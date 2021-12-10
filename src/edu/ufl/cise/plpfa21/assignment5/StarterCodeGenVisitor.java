package edu.ufl.cise.plpfa21.assignment5;


import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;
import edu.ufl.cise.plpfa21.assignment3.ast.ASTVisitor;
import edu.ufl.cise.plpfa21.assignment3.ast.IAssignmentStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IBinaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IBooleanLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpressionStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionCallExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.IIfStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IImmutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.IIntLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.ILetStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IListSelectorExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IListType;
import edu.ufl.cise.plpfa21.assignment3.ast.IMutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.INilConstantExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IPrimitiveType;
import edu.ufl.cise.plpfa21.assignment3.ast.IProgram;
import edu.ufl.cise.plpfa21.assignment3.ast.IReturnStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IStringLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.ISwitchStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType.TypeKind;
import edu.ufl.cise.plpfa21.assignment3.ast.IUnaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IWhileStatement;
import edu.ufl.cise.plpfa21.assignment3.astimpl.MutableGlobal__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.NameDef__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Type__;
import edu.ufl.cise.plpfa21.assignment4.TypeCheckVisitor;
import edu.ufl.cise.plpfa21.assignment5.StarterCodeGenVisitor.LocalVarInfo;
import edu.ufl.cise.plpfa21.assignment5.StarterCodeGenVisitor.MethodVisitorLocalVarTable;
//import edu.ufl.cise.plpfa21.assignment5.ReferenceCodeGenVisitor.LocalVarInfo;
//import edu.ufl.cise.plpfa21.assignment5.ReferenceCodeGenVisitor.MethodVisitorLocalVarTable;
//import edu.ufl.cise.plpfa21.pLP.ListSelectorExpression;


public class StarterCodeGenVisitor implements ASTVisitor, Opcodes {
	
	public StarterCodeGenVisitor(String className, String packageName, String sourceFileName){
		this.className = className;
		this.packageName = packageName;	
		this.sourceFileName = sourceFileName;
	}
	

	ClassWriter cw;
	String className;
	String packageName;
	String classDesc;
	String sourceFileName; //



	public static final String stringClass = "java/lang/String";
	public static final String stringDesc = "Ljava/lang/String;";
	public static final String listClass = "java/util/ArrayList";
	public static final String listDesc = "Ljava/util/ArrayList;";
	public static final String runtimeClass = "edu/ufl/cise/plpfa21/assignment5/Runtime";
	
	int slot=0;
	int pslot=0;
	
	/* Records for information passed to children, namely the methodVisitor and information about current methods Local Variables */
	record LocalVarInfo(String name, String typeDesc, Label start, Label end) {}
	record MethodVisitorLocalVarTable(MethodVisitor mv, List<LocalVarInfo> localVars) {};	

	/*  Adds local variables to a method
	 *  The information about local variables to add has been collected in the localVars table.  
	 *  This method should be invoked after all instructions for the method have been generated, immediately before invoking mv.visitMaxs.
	 */
	private void addLocals(MethodVisitorLocalVarTable arg, Label start, Label end) {
		MethodVisitor mv = arg.mv;
		List<LocalVarInfo> localVars = arg.localVars();
		for (int slot = 0; slot < localVars.size(); slot++) {
			LocalVarInfo varInfo = localVars.get(slot);
			String varName = varInfo.name;
			String localVarDesc = varInfo.typeDesc;
			Label range0 = varInfo.start == null ? start : varInfo.start;
		    Label range1 = varInfo.end == null ? end : varInfo.end;
		    mv.visitLocalVariable(varName, localVarDesc, null, range0, range1, slot);
		}
	}

	@Override
	public Object visitIBinaryExpression(IBinaryExpression n, Object arg) throws Exception {
////		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		Label exTrue = new Label();
		Label exFalse = new Label();
//		if(n.getLeft().getType().isInt() && n.getRight().getType().isInt()) {
		System.out.println("LEFT: " + n.getLeft().getType() + " " + n.getLeft().getText());
		System.out.println("RIGHT: " + n.getRight().getType());
		if((n.getLeft().getType().isInt() && n.getRight().getType().isInt())) {
			n.getLeft().visit(this, arg);
			n.getRight().visit(this, arg);
			Kind kind = n.getOp();
			switch(kind) {
				case PLUS:
					mv.visitInsn(IADD);
					break;
				case MINUS:
					mv.visitInsn(ISUB);
					break;
				case TIMES:
					mv.visitInsn(IMUL);
					break;
				case DIV:
					mv.visitInsn(IDIV);
					break;
				case AND:
					mv.visitInsn(IAND);
					break;
				case OR:
					mv.visitInsn(IOR);
					break;
				case NOT_EQUALS:
					mv.visitJumpInsn(IF_ICMPNE, exTrue);
					mv.visitLdcInsn(false);
					break;
				case EQUALS:
					mv.visitJumpInsn(IF_ICMPEQ, exTrue);
					mv.visitLdcInsn(false);
					break;
				case LT:
					mv.visitJumpInsn(IF_ICMPLT, exTrue);
					mv.visitLdcInsn(false);
					break;
				case GT:
					mv.visitJumpInsn(IF_ICMPGT, exTrue);
					mv.visitLdcInsn(false);
					break;
			}
//			mv.visitJumpInsn(GOTO, exFalse);
//			mv.visitLabel(exTrue);
//			mv.visitLdcInsn(true);
//			mv.visitLabel(exFalse);
		}
		else if(n.getLeft().getType().isBoolean() && n.getRight().getType().isBoolean()) {
			n.getLeft().visit(this, arg);
			n.getRight().visit(this, arg);
			Kind kind = n.getOp();
			switch(kind) {
				case AND:
					mv.visitInsn(IAND);
					break;
				case OR:
					mv.visitInsn(IOR);
					break;
				case NOT_EQUALS:
					mv.visitJumpInsn(IF_ICMPNE, exTrue);
					mv.visitLdcInsn(false);
					break;
				case EQUALS:
					mv.visitJumpInsn(IF_ICMPEQ, exTrue);
					mv.visitLdcInsn(false);
					break;
				case LT:
					mv.visitJumpInsn(IF_ICMPLT, exTrue);
					mv.visitLdcInsn(false);
					break;
				case GT:
					mv.visitJumpInsn(IF_ICMPGT, exTrue);
					mv.visitLdcInsn(false);
					break;
			}
			mv.visitJumpInsn(GOTO, exFalse);
			mv.visitLabel(exTrue);
			mv.visitLdcInsn(true);
			mv.visitLabel(exFalse);
			
		}
		else if(n.getLeft().getType().isString() && n.getRight().getType().isString()) {
//			n.getLeft().visit(this, arg);
//			n.getRight().visit(this, arg);
			Kind kind = n.getOp();
			switch(kind) {
				case NOT_EQUALS:
					n.getLeft().visit(this, arg);
					n.getRight().visit(this, arg);
//					mv.visitInsn(IFEQ);
					mv.visitJumpInsn(IF_ACMPNE, exTrue);
					mv.visitLdcInsn(false);
					mv.visitJumpInsn(GOTO, exFalse);
					mv.visitLabel(exTrue);
					mv.visitLdcInsn(true);
					mv.visitLabel(exFalse);
//					mv.visitJumpInsn(IF_ACMPNE, exTrue);
//					mv.visitLdcInsn(false);
					break;
				case EQUALS:
					n.getLeft().visit(this, arg);
					n.getRight().visit(this, arg);
//					mv.visitInsn(IFEQ);
					mv.visitJumpInsn(IF_ACMPEQ, exTrue);
					mv.visitLdcInsn(false);
					mv.visitJumpInsn(GOTO, exFalse);
					mv.visitLabel(exTrue);
					mv.visitLdcInsn(true);
					mv.visitLabel(exFalse);
					break;
				case PLUS:
					final String stringPath = "java/lang/String";
			        final String stringBPath = "java/lang/StringBuilder";
			        mv.visitTypeInsn(Opcodes.NEW,"java/lang/StringBuilder");
			        mv.visitInsn(Opcodes.DUP);
			        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,"java/lang/StringBuilder","<init>","()V",false);
			        n.getLeft().visit(this, arg);
		            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,stringBPath,"append",
		                    "(L"+stringPath+";)L"+stringBPath+";",false);
//			        mv.visitLdcInsn(n.getLeft().getText());
//			        mv.visitLdcInsn("null");
		            n.getRight().visit(this, arg);
		            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,stringBPath,"append",
		                    "(L"+stringPath+";)L"+stringBPath+";",false);
			        //turn builder to string and throw on stack to be processed
//			        mv.visitLdcInsn(n.getRight().getText());
//		            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,stringBPath,"append",
//		                    "(L"+stringPath+";)L"+stringBPath+";",false);
		            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,stringBPath,
			                "toString","()L"+stringPath+";",false);
				default:
					break;
			}
//			mv.visitJumpInsn(GOTO, exFalse);
//			mv.visitLabel(exTrue);
//			mv.visitLdcInsn(true);
//			mv.visitLabel(exFalse);
		}
		return null;

	}


	@Override
	public Object visitIBlock(IBlock n, Object arg) throws Exception {
		List<IStatement> statements = n.getStatements();
		for(IStatement statement: statements) {
			statement.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitIBooleanLiteralExpression(IBooleanLiteralExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		mv.visitLdcInsn(n.getValue());
		return null;
	}


	
	@Override
	public Object visitIFunctionDeclaration(IFunctionDeclaration n, Object arg) throws Exception {
		String name = n.getName().getName();

		//Local var table
		List<LocalVarInfo> localVars = new ArrayList<LocalVarInfo>();
		//Add args to local var table while constructing type desc.
		List<INameDef> args = n.getArgs();
		System.out.println("ARG SIZE: " + args.size());

		//Iterate over the parameter list and build the function descriptor
		//Also assign and store slot numbers for parameters
		StringBuilder sb = new StringBuilder();	
		sb.append("(");
		for( INameDef def: args) {
			String desc = def.getType().getDesc();
			sb.append(desc);
			def.getIdent().setSlot(localVars.size());
			localVars.add(new LocalVarInfo(def.getIdent().getName(), desc, null, null));
		}
		sb.append(")");
		sb.append(n.getResultType().getDesc());
		String desc = sb.toString();
		
		// get method visitor
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, name, desc, null, null);
		// initialize
		mv.visitCode();
		// mark beginning of instructions for method
		Label funcStart = new Label();
		mv.visitLabel(funcStart);
		MethodVisitorLocalVarTable context = new MethodVisitorLocalVarTable(mv, localVars);
		//visit block to generate code for statements
		n.getBlock().visit(this, context);
		
		//add return instruction if Void return type
		if(n.getResultType().equals(Type__.voidType)) {
			mv.visitInsn(RETURN);
		}
		//add label after last instruction
		Label funcEnd = new Label();
		mv.visitLabel(funcEnd);
				
		addLocals(context, funcStart, funcEnd);

		mv.visitMaxs(0, 0);
		
		//terminate construction of method
		mv.visitEnd();
		return null;

	}




	@Override
	public Object visitIFunctionCallExpression(IFunctionCallExpression n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		if(n.getArgs() != null) {
			for(IExpression list : n.getArgs()) {
				list.visit(this, arg);
			}
			mv.visitMethodInsn(INVOKESTATIC, runtimeClass, className, classDesc, false);
		}
		return null;
		
		
	}

	@Override
	public Object visitIIdentExpression(IIdentExpression n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
//		NameDef__ dec = (NameDef__)n.getName().getDec();
		IDeclaration dec = n.getName().getDec();
		IIdentifier name = null;
		if(dec == null) {
			return null;
		}
		if(dec instanceof NameDef__) {
			name = ((NameDef__) dec).getIdent();
		}
		else if(dec instanceof MutableGlobal__) {
			name = ((MutableGlobal__) dec).getVarDef().getIdent();
		}
//		IIdentifier name = dec.getIdent();
		IType type = TypeCheckVisitor.getType(dec);
		System.out.println("SLOT: " + name.getSlot());
		if(name.getSlot() == -1) {
			name.setSlot(pslot);
			pslot++;
		}
		if(type.isInt() || type.isBoolean()) {
			mv.visitVarInsn(ILOAD, name.getSlot());
		}
		else {
			mv.visitVarInsn(ALOAD, name.getSlot());
		}
		return null;
	}

	@Override
	public Object visitIIdentifier(IIdentifier n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
//		NameDef__ dec = (NameDef__)n.getDec();
		IIdentifier name = null;
		IDeclaration dec = n.getDec();
		if(dec instanceof NameDef__) {
			name = ((NameDef__) dec).getIdent();
		}
		else if(dec instanceof MutableGlobal__) {
			name = ((MutableGlobal__) dec).getVarDef().getIdent();
		}
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
//		IIdentifier name = dec.getIdent();
		IType type = TypeCheckVisitor.getType(dec);
		if(name.getSlot() == -1) {
			name.setSlot(pslot);
			pslot++;
		}
		if(type.isInt() || type.isBoolean()) {
			mv.visitVarInsn(ISTORE, name.getSlot());
	}
		else {
			mv.visitVarInsn(ASTORE, name.getSlot());
		}
		return dec;
	}

	@Override
	public Object visitIIfStatement(IIfStatement n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		Label ifLabel = new Label();
		n.getGuardExpression().visit(this, arg);
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		mv.visitJumpInsn(IFEQ, ifLabel);
		Label ifLabel2 = new Label();
		mv.visitLabel(ifLabel2);
		n.getBlock().visit(this, arg);
		Label ifLabel3 = new Label();
		mv.visitLabel(ifLabel3);
		mv.visitLabel(ifLabel);
		Label ifLabel4 = new Label();
		mv.visitLabel(ifLabel4);
		return null;
	}

	@Override
	public Object visitIImmutableGlobal(IImmutableGlobal n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;				
		INameDef nameDef = n.getVarDef();
		String varName = nameDef.getIdent().getName();
		String typeDesc = nameDef.getType().getDesc();
		FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, varName, typeDesc, null, null);
		fieldVisitor.visitEnd();
		//generate code to initialize field.  
		IExpression e = n.getExpression();
		e.visit(this, arg);  //generate code to leave value of expression on top of stack
		mv.visitFieldInsn(PUTSTATIC, className, varName, typeDesc);	
		return null;
	}

	@Override
	public Object visitIIntLiteralExpression(IIntLiteralExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		mv.visitLdcInsn(n.getValue());
		return null;
	}

	@Override
	public Object visitILetStatement(ILetStatement n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		String s_type = "I";
		IType type = n.getExpression().getType();
		if(type.isInt()) {
			s_type = "I";
			cw.visitField(ACC_STATIC, n.getText(), "I", null, null);
		}
		else if(type.isBoolean()) {
			s_type = "Z";
			cw.visitField(ACC_STATIC, n.getText(), "Z", null, null);
		}
		if(n.getExpression()!= null) {
			n.getExpression().visit(this, arg);
			mv.visitFieldInsn(PUTSTATIC, className, n.getText(), s_type);
		}
		return null;
	}
		


	@Override
	public Object visitIListSelectorExpression(IListSelectorExpression n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");
	}

	@Override
	public Object visitIListType(IListType n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("SKIP THIS!!");
		return null;
	}

	@Override
	public Object visitINameDef(INameDef n, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitINilConstantExpression(INilConstantExpression n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");
	}

	@Override
	public Object visitIProgram(IProgram n, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//		cw = new ClassWriter(0);
		/*
		 * If the call to mv.visitMaxs(1, 1) crashes, it is sometime helpful to temporarily try it without COMPUTE_FRAMES. You won't get a runnable class file
		 * but you can at least see the bytecode that is being generated. 
		 */
//	    cw = new ClassWriter(0); 
		classDesc = "L" + className + ";";
		cw.visit(V16, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		if (sourceFileName != null) cw.visitSource(sourceFileName, null);
		
		// create MethodVisitor for <clinit>  
		//  This method is the static initializer for the class and contains code to initialize global variables.
		// get a MethodVisitor
		MethodVisitor clmv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "<clinit>", "()V", null, null);
		// visit the code first
		clmv.visitCode();
		//mark the beginning of the code
		Label clinitStart = new Label();
		clmv.visitLabel(clinitStart);
		//create a list to hold local var info.  This will remain empty for <clinit> but is shown for completeness.  Methods with local variable need this.
		List<LocalVarInfo> initializerLocalVars = new ArrayList<LocalVarInfo>();
		//pair the local var infor and method visitor to pass into visit routines
		MethodVisitorLocalVarTable clinitArg = new MethodVisitorLocalVarTable(clmv,initializerLocalVars);
		//visit all the declarations. 
		List<IDeclaration> decs = n.getDeclarations();
		for (IDeclaration dec : decs) {
			dec.visit(this, clinitArg);  //argument contains local variable info and the method visitor.  
		}
		//add a return method
		clmv.visitInsn(RETURN);
		//mark the end of the bytecode for <clinit>
		Label clinitEnd = new Label();
		clmv.visitLabel(clinitEnd);
		//add the locals to the class
		addLocals(clinitArg, clinitStart, clinitEnd);  //shown for completeness.  There shouldn't be any local variables in clinit.
		//required call of visitMaxs.  Since we created the ClassWriter with  COMPUTE_FRAMES, the parameter values don't matter. 
		clmv.visitMaxs(0, 0);
		//finish the method
		clmv.visitEnd();
	
		//finish the clas
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

	@Override
	public Object visitIReturnStatement(IReturnStatement n, Object arg) throws Exception {
		//get the method visitor from the arg
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		IExpression e = n.getExpression();
		if (e != null) {  //the return statement has an expression
			e.visit(this, arg);  //generate code to leave value of expression on top of stack.
			//use type of expression to determine which return instruction to use
			IType type = e.getType();
			if (type.isInt() || type.isBoolean()) {mv.visitInsn(IRETURN);}
			else  {mv.visitInsn(ARETURN);}
		}
		else { //there is no argument, (and we have verified duirng type checking that function has void return type) so use this return statement.  
			mv.visitInsn(RETURN);
		}
		return null;
	}

	@Override
	public Object visitIStringLiteralExpression(IStringLiteralExpression n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		mv.visitLdcInsn(n.getValue());
		return null;
	}

	@Override
	public Object visitISwitchStatement(ISwitchStatement n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");

	}

	@Override
	public Object visitIUnaryExpression(IUnaryExpression n, Object arg) throws Exception {
		//get method visitor from arg
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		//generate code to leave value of expression on top of stack
		n.getExpression().visit(this, arg);
		//get the operator and types of operand and result
		Kind op = n.getOp();
		IType resultType = n.getType();
		IType operandType = n.getExpression().getType();
		switch(op) {
		case MINUS -> {
			mv.visitInsn(INEG);
//			throw new UnsupportedOperationException("IMPLEMENT unary minus");
		}
		case BANG -> {
			if (operandType.isBoolean()) {
				//this is complicated.  Use a Java method instead
//				Label brLabel = new Label();
//				Label after = new Label();
//				mv.visitJumpInsn(IFEQ,brLabel);
//				mv.visitLdcInsn(0);
//				mv.visitJumpInsn(GOTO,after);
//				mv.visitLabel(brLabel);
//				mv.visitLdcInsn(1);
//				mv.visitLabel(after);
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "not", "(Z)Z",false);
			}
			else { //argument is List
				throw new UnsupportedOperationException("SKIP THIS");
		}
		}
		default -> throw new UnsupportedOperationException("compiler error");
		}
		return null;
	}

	@Override
	public Object visitIWhileStatement(IWhileStatement n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		Label whileLabel = new Label();
		Label bodyLabel = new Label();
		mv.visitJumpInsn(GOTO, whileLabel);
		mv.visitLabel(bodyLabel);
		n.getBlock().visit(this, arg);
		Label bodyLabel2 = new Label();
		mv.visitLabel(bodyLabel2);
		mv.visitLabel(whileLabel);
		n.getGuardExpression().visit(this, arg);
		Label whileLabel2 = new Label();
		mv.visitLabel(whileLabel2);
		mv.visitJumpInsn(IFNE, bodyLabel);
		return null;
	}


	@Override
	public Object visitIMutableGlobal(IMutableGlobal n, Object arg) throws Exception {
		//		throw new UnsupportedOperationException("TO IMPLEMENT");

		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;				
		INameDef nameDef = n.getVarDef();
		String varName = nameDef.getIdent().getName();
		String typeDesc = nameDef.getType().getDesc();
		FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC, varName, typeDesc, null, null);
		fieldVisitor.visitEnd();
		//generate code to initialize field.  
		IExpression e = n.getExpression();
		e.visit(this, arg);  //generate code to leave value of expression on top of stack
		mv.visitFieldInsn(PUTFIELD, className, varName, typeDesc);	
		return null;
	}

	@Override
	public Object visitIPrimitiveType(IPrimitiveType n, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}


	@Override
	public Object visitIAssignmentStatement(IAssignmentStatement n, Object arg) throws Exception {
//		throw new UnsupportedOperationException("TO IMPLEMENT");
		IIdentExpression ex = (IIdentExpression)n.getLeft();
		IIdentifier name = ex.getName();
		ex.visit(this, arg);
		name.visit(this, arg);
//		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
//		IIdentExpression ex1 = (IIdentExpression)n.getRight();
//		IIdentifier name1 = ex1.getName();
//		name1.visit(this, arg);
		n.getRight().visit(this, arg);
		
		return null;

	}

	@Override
	public Object visitIExpressionStatement(IExpressionStatement n, Object arg) throws Exception {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}
}
