/* This file was generated by SableCC (http://www.sablecc.org/).
   Then later modified.
   
   This visitor/visit class redefines the visit/visit methods for each AST 
   node (e.g. inPlusExp, etc) so that the AST is visited in a 
   left-to-right depth-first fashion.
   This class also provides in and out method hooks (e.g. inPlusExp, outPlusExp,
   etc.) so that concrete visitors/switches that extend this class 
   only need to implement in and out methods and do not need to worry about
   traversing the AST.
*/

package ast_visitors;

import ast.node.*;
import ast.visitor.DepthFirstVisitor;

import java.util.*;
import java.io.PrintWriter;
import symtable.*;

public class AVRgenVisitor extends DepthFirstVisitor {

   private static int labNum = 0;

   private PrintWriter out;
   private SymTable currentST;

   public AVRgenVisitor(PrintWriter out, SymTable symT)
   {
	   this.out = out;
	   this.currentST = symT;
   }

   /** A helper to return the current label and then increment it
    */
   private String getLabel()
   {
	   String currLabel = "MJ_L" + labNum;
	   labNum++;
	   return currLabel;
   }

   // Code to promote bytes to ints
   private void byteToInt(String loReg, String hiReg) {
	   String neg_label = getLabel();
	   String done_label = getLabel();

	   out.println("    # Promote byte to int");
	   out.println("    tst " + loReg);
	   out.println("    brlt " + neg_label);
	   out.println("    ldi " + hiReg + ", 0");
	   out.println("    jmp " + done_label);
	   out.println(neg_label + ":");
	   out.println("    ldi " + hiReg + ", hi8(-1)");
	   out.println(done_label + ":");
	   out.println();
   }

    public void defaultIn(Node node)
    {
        // Do nothing
    }

    public void defaultOut(Node node)
    {
        // Do nothing
    }

    public void inAndExp(AndExp node)
    {
	out.println("    # && operator");
	out.println("    # &&: left operand");
	out.println();
    }

    public void outAndExp(AndExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitAndExp(AndExp node)
    {
        inAndExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
        out.println("    # &&: if left operand is false do not eval right");
        out.println("    # load a one byte expression off stack");
        out.println("    pop    r24");
        out.println("    # push one byte expression onto stack");
        out.println("    push   r24");
        out.println("    # compare left exp with zero");
        out.println("    ldi r25, 0");
        out.println("    cp    r24, r25");

	String false_label = getLabel();
	String true_label = getLabel();
	out.println("    brne " + true_label);
	out.println("    jmp " + false_label);
	out.println();
	out.println(true_label + ":");
	out.println("    # &&: right operand");
	out.println();

        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }
	out.println("    # end label for &&");
	out.println(false_label + ":");
	out.println();
        outAndExp(node);
    }

    public void inArrayAssignStatement(ArrayAssignStatement node)
    {
        defaultIn(node);
    }

    public void outArrayAssignStatement(ArrayAssignStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void visitArrayAssignStatement(ArrayAssignStatement node)
    {
        inArrayAssignStatement(node);
        if(node.getIdLit() != null)
        {
            node.getIdLit().accept(this);
        }
        if(node.getIndex() != null)
        {
            node.getIndex().accept(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outArrayAssignStatement(node);
    }

    public void inArrayExp(ArrayExp node)
    {
        defaultIn(node);
    }

    public void outArrayExp(ArrayExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitArrayExp(ArrayExp node)
    {
        inArrayExp(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        if(node.getIndex() != null)
        {
            node.getIndex().accept(this);
        }
        outArrayExp(node);
    }

    public void inAssignStatement(AssignStatement node)
    {
        defaultIn(node);
    }

    public void outAssignStatement(AssignStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void visitAssignStatement(AssignStatement node)
    {
        inAssignStatement(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outAssignStatement(node);
    }

    public void inBlockStatement(BlockStatement node)
    {
        defaultIn(node);
    }

    public void outBlockStatement(BlockStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void visitBlockStatement(BlockStatement node)
    {
        inBlockStatement(node);
        {
            List<IStatement> copy = new ArrayList<IStatement>(node.getStatements());
            for(IStatement e : copy)
            {
                e.accept(this);
            }
        }
        outBlockStatement(node);
    }

    public void inBoolType(BoolType node)
    {
        defaultIn(node);
    }

    public void outBoolType(BoolType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitBoolType(BoolType node)
    {
        inBoolType(node);
        outBoolType(node);
    }

    public void inButtonExp(ButtonLiteral node)
    {
        defaultIn(node);
    }

    public void outButtonExp(ButtonLiteral node)
    {
	out.println("    # Button Literal " + node);
	out.println("    ldi    r24, " + node.getIntValue());
	out.println("    push   r24");
	out.println();
    }

    @Override
    public void visitButtonLiteral(ButtonLiteral node)
    {
        inButtonExp(node);
        outButtonExp(node);
    }

    public void inButtonType(ButtonType node)
    {
        defaultIn(node);
    }

    public void outButtonType(ButtonType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitButtonType(ButtonType node)
    {
        inButtonType(node);
        outButtonType(node);
    }

    public void inByteCast(ByteCast node)
    {
        defaultIn(node);
    }

    public void outByteCast(ByteCast node)
    {
	if (currentST.getExpType(node.getExp()) == Type.BYTE) {
		return;
	}
	out.println("    # Cast int to byte");
	out.println("    pop	r24");
	out.println("    pop	r25");
	out.println("    push	r24");
	out.println();
    }

    @Override
    public void visitByteCast(ByteCast node)
    {
        inByteCast(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outByteCast(node);
    }

    public void inByteType(ByteType node)
    {
        defaultIn(node);
    }

    public void outByteType(ByteType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitByteType(ByteType node)
    {
        inByteType(node);
        outByteType(node);
    }

    public void inCallExp(CallExp node)
    {
        defaultIn(node);
    }

    public void outCallExp(CallExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitCallExp(CallExp node)
    {
        inCallExp(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        {
            List<IExp> copy = new ArrayList<IExp>(node.getArgs());
            for(IExp e : copy)
            {
                e.accept(this);
            }
        }
        outCallExp(node);
    }

    public void inCallStatement(CallStatement node)
    {
        defaultIn(node);
    }

    public void outCallStatement(CallStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void visitCallStatement(CallStatement node)
    {
        inCallStatement(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        {
            List<IExp> copy = new ArrayList<IExp>(node.getArgs());
            for(IExp e : copy)
            {
                e.accept(this);
            }
        }
        outCallStatement(node);
    }

    public void inChildClassDecl(ChildClassDecl node)
    {
        defaultIn(node);
    }

    public void outChildClassDecl(ChildClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void visitChildClassDecl(ChildClassDecl node)
    {
        inChildClassDecl(node);
        {
            List<VarDecl> copy = new ArrayList<VarDecl>(node.getVarDecls());
            for(VarDecl e : copy)
            {
                e.accept(this);
            }
        }
        {
            List<MethodDecl> copy = new ArrayList<MethodDecl>(node.getMethodDecls());
            for(MethodDecl e : copy)
            {
                e.accept(this);
            }
        }
        outChildClassDecl(node);
    }

    public void inClassType(ClassType node)
    {
        defaultIn(node);
    }

    public void outClassType(ClassType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitClassType(ClassType node)
    {
        inClassType(node);
        outClassType(node);
    }

    public void inColorExp(ColorLiteral node)
    {
        defaultIn(node);
    }

    public void outColorExp(ColorLiteral node)
    {
	out.println("    # Push color onto stack");
	out.println("    ldi	r22," + node.getIntValue());
	out.println("    push	r22");
	out.println();
    }

    @Override
    public void visitColorLiteral(ColorLiteral node)
    {
        inColorExp(node);
        outColorExp(node);
    }

    public void inColorArrayType(ColorArrayType node)
    {
        defaultIn(node);
    }

    public void outColorArrayType(ColorArrayType node)
    {
        defaultOut(node);
    }

    public void visitColorArrayType(ColorArrayType node)
    {
        inColorArrayType(node);
        outColorArrayType(node);
    }

    public void inColorType(ColorType node)
    {
        defaultIn(node);
    }

    public void outColorType(ColorType node)
    {
        defaultOut(node);
    }

    public void visitColorType(ColorType node)
    {
        inColorType(node);
        outColorType(node);
    }

    public void inEqualExp(EqualExp node)
    {
	out.println("    # == operator");
	out.println("    # ==: left operand");
	out.println();
    }

    public void outEqualExp(EqualExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitEqualExp(EqualExp node)
    {
        inEqualExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
	out.println("    # ==: right operand");
	out.println();

        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }

	String done_label = getLabel();
	String false_label = getLabel();

	out.println("    # ==: pop both operands and promote bytes to ints");
	out.println("    # ==: compare operands");
	out.println();
	out.println("    pop r24");
	if (currentST.getExpType(node.getRExp()) == Type.INT) {
		out.println("     pop r25");
	} else {
		byteToInt("r24", "r25");
	}
	out.println("    pop r22");
	if (currentST.getExpType(node.getLExp()) == Type.INT) {
		out.println("     pop r23");
	} else {
		byteToInt("r22", "r23");
	}
	out.println("    cp r24, r22");
	out.println("    brne " + false_label);
	out.println("    cp r25, r23");
	out.println("    brne " + false_label);
	out.println("    ldi r24, 1");
	out.println("    jmp " + done_label);
	out.println(false_label + ":");
	out.println("    ldi r24, 0");
	out.println("    push r24");
	out.println("    # end label for ==");
	out.println(done_label + ":");
	out.println("    push r24");
	out.println();
	
        outEqualExp(node);
    }


    public void inFalseExp(FalseLiteral node)
    {
        defaultIn(node);
    }

    public void outFalseExp(FalseLiteral node)
    {
	out.println("    # Push false (0) onto stack");
	out.println("    ldi r24, 0");
	out.println("    push r24");
	out.println();
    }

    @Override
    public void visitFalseLiteral(FalseLiteral node)
    {
        inFalseExp(node);
        outFalseExp(node);
    }

    public void inFormal(Formal node)
    {
        defaultIn(node);
    }

    public void outFormal(Formal node)
    {
        defaultOut(node);
    }

    @Override
    public void visitFormal(Formal node)
    {
        inFormal(node);
        if(node.getType() != null)
        {
            node.getType().accept(this);
        }
        outFormal(node);
    }

    public void inIdLiteral(IdLiteral node)
    {
        defaultIn(node);
    }

    public void outIdLiteral(IdLiteral node)
    {
        defaultOut(node);
    }

    @Override
    public void visitIdLiteral(IdLiteral node)
    {
        inIdLiteral(node);
        outIdLiteral(node);
    }

    public void inIfStatement(IfStatement node)
    {
	out.println("    # if statement");
	out.println();
    }

    public void outIfStatement(IfStatement node)
    {
	    defaultOut(node);
    }

    @Override
    public void visitIfStatement(IfStatement node)
    {
        inIfStatement(node);

	// Get needed labels
	String else_label = getLabel();
	String then_label = getLabel();
	String done_label = getLabel();
	String zero_label = getLabel();
	String one_label = getLabel();
	String cond_label = getLabel();

        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }

	out.println("    pop	r24");
	out.println("    tst	r24");
	out.println("    breq	" + zero_label);
	out.println(one_label + ":");
	out.println("    ldi	r24, 1");
	out.println("    jmp	" + cond_label);
	out.println(zero_label + ":");
	out.println(cond_label + ":");

	out.println("    # push one byte expression onto stack");
	out.println("    push	r24");
	out.println();
	out.println("    # load condition and branch if false");
	out.println("    # load one byte expression off stack");
	out.println("    pop	r24");
	out.println("    # load zero into reg");
	out.println("    ldi	r25, 0");
	out.println();
	out.println("    # use cp to set SREG");
	out.println("    cp	r24, r25");
	out.println("    brne	" + then_label);
	out.println("    jmp	" + else_label);
	out.println();
	out.println("    # then label for if");
	out.println(then_label + ":");

        if(node.getThenStatement() != null)
        {
            node.getThenStatement().accept(this);
        }

	out.println("    jmp	" + done_label);
	out.println();
	out.println("    # else label for if");
	out.println(else_label + ":");

        if(node.getElseStatement() != null)
        {
            node.getElseStatement().accept(this);
        }

	out.println();
	out.println("    # done label for if");
	out.println(done_label + ":");
	out.println();
        outIfStatement(node);
    }

    public void inIntArrayType(IntArrayType node)
    {
        defaultIn(node);
    }

    public void outIntArrayType(IntArrayType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitIntArrayType(IntArrayType node)
    {
        inIntArrayType(node);
        outIntArrayType(node);
    }

    public void inIntegerExp(IntLiteral node)
    {
        defaultIn(node);
    }

    public void outIntegerExp(IntLiteral node)
    {
	out.println("    # Push integer " + node.getIntValue() + " onto stack");
	out.println("    ldi	r24,lo8(" + node.getIntValue() + ")");
	out.println("    ldi	r25,hi8(" + node.getIntValue() + ")");
	out.println("    push	r25");
	out.println("    push	r24");
	out.println();
    }

    @Override
    public void visitIntLiteral(IntLiteral node)
    {
        inIntegerExp(node);
        outIntegerExp(node);
    }

    public void inIntType(IntType node)
    {
        defaultIn(node);
    }

    public void outIntType(IntType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitIntType(IntType node)
    {
        inIntType(node);
        outIntType(node);
    }

    public void inLengthExp(LengthExp node)
    {
        defaultIn(node);
    }

    public void outLengthExp(LengthExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitLengthExp(LengthExp node)
    {
        inLengthExp(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outLengthExp(node);
    }

    public void inLtExp(LtExp node)
    {
        defaultIn(node);
    }

    public void outLtExp(LtExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitLtExp(LtExp node)
    {
        inLtExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }
        outLtExp(node);
    }

    public void inMainClass(MainClass node)
    {
        defaultIn(node);
    }

    public void outMainClass(MainClass node)
    {
        defaultOut(node);
    }

    @Override
    public void visitMainClass(MainClass node)
    {
        inMainClass(node);
        if(node.getStatement() != null)
        {
            node.getStatement().accept(this);
        }
        outMainClass(node);
    }

    public void inMeggyCheckButton(MeggyCheckButton node)
    {
        defaultIn(node);
    }

    public void outMeggyCheckButton(MeggyCheckButton node)
    {
	out.println("    # MeggyCheckButton");
	out.println("    call    _Z16CheckButtonsDownv");
	out.println("    lds     r24, " + getButtonName(node.getExp().toString()));
	out.println("    push    r24");
	out.println();
    }

    /* Convert Meggy Button Literal to Assembly name of button */
    private String getButtonName(String lit)
    {
	    String res = "";
	    switch (lit) {
		    case "Meggy.Button.A": res = "Button_A";
					   break;
		    case "Meggy.Button.B": res = "Button_B";
					   break;
		    case "Meggy.Button.Up": res = "Button_Up";
					   break;
		    case "Meggy.Button.Down": res = "Button_Down";
					   break;
		    case "Meggy.Button.Left": res = "Button_Left";
					   break;
		    case "Meggy.Button.Right": res = "Button_Right";
					   break;
		    default: break;
	    }
	    return res;
    }

    public void visitMeggyCheckButton(MeggyCheckButton node)
    {
        inMeggyCheckButton(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outMeggyCheckButton(node);
    }

    public void inMeggyDelay(MeggyDelay node)
    {
        defaultIn(node);
    }

    public void outMeggyDelay(MeggyDelay node)
    {
	    out.println("    # Call Meggy.delay()");
	    out.println("    pop    r24");
	    if (currentST.getExpType(node.getExp()) == Type.BYTE) {
		    byteToInt("r24", "r25");
	    } else {
		    out.println("    pop    r25");
	    }
	    out.println("    call   _Z8delay_msj");
	    out.println();
    }

    public void visitMeggyDelay(MeggyDelay node)
    {
        inMeggyDelay(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outMeggyDelay(node);
    }

    public void inMeggyGetPixel(MeggyGetPixel node)
    {
        defaultIn(node);
    }

    public void outMeggyGetPixel(MeggyGetPixel node)
    {
	out.println("    # MeggyGetPixel");
	out.println("    pop    r22");
	out.println("    pop    r24");
	out.println("    call   _Z6ReadPxhh");
	out.println("    push   r24");
	out.println();
    }

    public void visitMeggyGetPixel(MeggyGetPixel node)
    {
        inMeggyGetPixel(node);
        if(node.getXExp() != null)
        {
            node.getXExp().accept(this);
        }

        if(node.getYExp() != null)
        {
            node.getYExp().accept(this);
        }

        outMeggyGetPixel(node);
    }
    
    public void inMeggySetAuxLEDs(MeggySetAuxLEDs node)
    {
        defaultIn(node);
    }

    public void outMeggySetAuxLEDs(MeggySetAuxLEDs node)
    {
        defaultOut(node);
    }

    public void visitMeggySetAuxLEDs(MeggySetAuxLEDs node)
    {
        inMeggySetAuxLEDs(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outMeggySetAuxLEDs(node);
    }

    public void inMeggySetPixel(MeggySetPixel node)
    {
        defaultIn(node);
    }

    public void outMeggySetPixel(MeggySetPixel node)
    {
	out.println("    # Call Meggy.setPixel(x,y,color)");
	out.println("    pop	r20");
	out.println("    pop	r22");
	out.println("    pop	r24");
	out.println("    call	_Z6DrawPxhhh");
	out.println("    call	_Z12DisplaySlatev");
	out.println();
    }

    public void visitMeggySetPixel(MeggySetPixel node)
    {
        inMeggySetPixel(node);
        if(node.getXExp() != null)
        {
            node.getXExp().accept(this);
        }

        if(node.getYExp() != null)
        {
            node.getYExp().accept(this);
        }
        
        if(node.getColor() != null)
        {
            node.getColor().accept(this);
        }
        outMeggySetPixel(node);
    }

    public void inMeggyToneStart(MeggyToneStart node)
    {
        defaultIn(node);
    }

    public void outMeggyToneStart(MeggyToneStart node)
    {
        defaultOut(node);
    }

    public void visitMeggyToneStart(MeggyToneStart node)
    {
        inMeggyToneStart(node);
        if(node.getToneExp() != null)
        {
            node.getToneExp().accept(this);
        }

        if(node.getDurationExp() != null)
        {
            node.getDurationExp().accept(this);
        }
        outMeggyToneStart(node);
    }

    public void inMethodDecl(MethodDecl node)
    {
        defaultIn(node);
    }

    public void outMethodDecl(MethodDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void visitMethodDecl(MethodDecl node)
    {
        inMethodDecl(node);
        if(node.getType() != null)
        {
            node.getType().accept(this);
        }
        {
            List<Formal> copy = new ArrayList<Formal>(node.getFormals());
            for(Formal e : copy)
            {
                e.accept(this);
            }
        }
        {
            List<VarDecl> copy = new ArrayList<VarDecl>(node.getVarDecls());
            for(VarDecl e : copy)
            {
                e.accept(this);
            }
        }
        {
            List<IStatement> copy = new ArrayList<IStatement>(node.getStatements());
            for(IStatement e : copy)
            {
                e.accept(this);
            }
        }
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outMethodDecl(node);
    }

    public void inMinusExp(MinusExp node)
    {
        defaultIn(node);
    }

    public void outMinusExp(MinusExp node)
    {
	out.println("    # Subtract the two ints on the stack");
	out.println("    pop    r18");
	if (currentST.getExpType(node.getRExp()) == Type.BYTE) {
		byteToInt("r18", "r19");
	} else {
		out.println("    pop    r19");
	}
	out.println("    pop    r24");
	if (currentST.getExpType(node.getLExp()) == Type.BYTE) {
		byteToInt("r24", "r25");
	} else {
		out.println("    pop    r25");
	}
	out.println("    # Do subtract operation");
	out.println("    sub    r24, r18");
	out.println("    sbc    r25, r19");
	out.println("    # push two byte expression onto stack");
	out.println("    push   r25");
	out.println("    push   r24");
	out.println();
    }

    @Override
    public void visitMinusExp(MinusExp node)
    {
        inMinusExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }
        outMinusExp(node);
    }

    public void inMulExp(MulExp node)
    {
        defaultIn(node);
    }

    public void outMulExp(MulExp node)
    {
	out.println("    # Multiply two bytes on stack");
	out.println("    pop    r18");
	out.println("    pop    r24");
	out.println("    muls   r18, r24");
	out.println("    # Product is 2-byte int in r1:r0");
	out.println("    push   r1");
	out.println("    push   r0");
	out.println();
    }

    @Override
    public void visitMulExp(MulExp node)
    {
        inMulExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }
        outMulExp(node);
    }

    public void inNewArrayExp(NewArrayExp node)
    {
        defaultIn(node);
    }

    public void outNewArrayExp(NewArrayExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitNewArrayExp(NewArrayExp node)
    {
        inNewArrayExp(node);
        if(node.getType() != null)
        {
            node.getType().accept(this);
        }
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outNewArrayExp(node);
    }

    public void inNewExp(NewExp node)
    {
        defaultIn(node);
    }

    public void outNewExp(NewExp node)
    {
        defaultOut(node);
    }

    @Override
    public void visitNewExp(NewExp node)
    {
        inNewExp(node);
        outNewExp(node);
    }

    public void inNegExp(NegExp node)
    {
        defaultIn(node);
    }

    public void outNegExp(NegExp node)
    {
	    out.println("    # Negation");
	    out.println("    # Push 0 under top 2 bytes and do subtraction");
	    out.println("    pop r24");
	    if (currentST.getExpType(node.getExp()) == Type.INT) {
		    //byteToInt("r24", "r25");
	    //} else {
		    out.println("    pop r25");
	    }
	    out.println("    ldi r22, 0");
	    out.println("    push r22");
	    out.println("    push r22");
	    if (currentST.getExpType(node.getExp()) == Type.INT) {
		    out.println("    push r25");
	    }
	    out.println("    push r24");
	    //Create dummy 0 literal for subtraction
	    IntLiteral zero = new IntLiteral(node.getLine(), node.getPos(), "0", 0);
	    // Reuse code gen for subtraction
	    outMinusExp(new MinusExp(node.getLine(), node.getPos(),
				    zero, node.getExp()));
    }

    @Override
    public void visitNegExp(NegExp node)
    {
        inNegExp(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outNegExp(node);
    }
    
    public void inNotExp(NotExp node)
    {
        defaultIn(node);
    }

    public void outNotExp(NotExp node)
    {
	out.println("    # Not (!)");
	out.println("    pop r24");
	out.println("    ldi r22, 1");
	out.println("    eor r24, r22");
	out.println("    push r24");
	out.println();
    }

    @Override
    public void visitNotExp(NotExp node)
    {
        inNotExp(node);
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
        outNotExp(node);
    }
    
    public void inPlusExp(PlusExp node)
    {
        defaultIn(node);
    }


    public void outPlusExp(PlusExp node)
    {
	out.println("    # Add the two ints on the stack");
	out.println("    pop    r18");

	if (currentST.getExpType(node.getRExp()) == Type.BYTE) {
		byteToInt("r18", "r19");
	} else {
		out.println("    pop    r19");
	}

	out.println("    pop    r24");
	if (currentST.getExpType(node.getLExp()) == Type.BYTE) {
		byteToInt("r24", "r25");
	} else {
		out.println("    pop    r25");
	}

	out.println("    # Do add operation");
	out.println("    add    r24, r18");
	out.println("    adc    r25, r19");
	out.println("    # push two byte expression onto stack");
	out.println("    push   r25");
	out.println("    push   r24");
	out.println();
    }

    @Override
    public void visitPlusExp(PlusExp node)
    {
        inPlusExp(node);
        if(node.getLExp() != null)
        {
            node.getLExp().accept(this);
        }
        if(node.getRExp() != null)
        {
            node.getRExp().accept(this);
        }
        outPlusExp(node);
    }

    public void inProgram(Program node)
    {
	out.println("    .file  \"main.java\"");
	out.println("__SREG__ = 0x3f");
	out.println("__SP_H__ = 0x3e");
	out.println("__SP_L__ = 0x3d");
	out.println("__tmp_reg__ = 0");
	out.println("__zero_reg__ = 1");
	out.println("    .global __do_copy_data");
	out.println("    .global __do_clear_bss");
	out.println("    .text");
	out.println(".global main");
	out.println("    .type   main, @function");
	out.println("main:");
	out.println("    push r29");
	out.println("    push r28");
	out.println("    in r28,__SP_L__");
	out.println("    in r29,__SP_H__");
	out.println("/* prologue: function */");
	out.println("    call _Z18MeggyJrSimpleSetupv ");
	out.println("    /* Need to call this so that the meggy library gets set up */");
	out.println();
    }

    public void outProgram(Program node)
    {
	out.println("/* epilogue start */");
	out.println("    endLabel:");
	out.println("    jmp endLabel");
	out.println("    ret");
	out.println("    .size   main, .-main");
	out.flush();
    }

    @Override
    public void visitProgram(Program node)
    {
        inProgram(node);
        if(node.getMainClass() != null)
        {
            node.getMainClass().accept(this);
        }
        {
            List<IClassDecl> copy = new ArrayList<IClassDecl>(node.getClassDecls());
            for(IClassDecl e : copy)
            {
                e.accept(this);
            }
        }
        outProgram(node);
    }

    public void inThisExp(ThisLiteral node)
    {
        defaultIn(node);
    }

    public void outThisExp(ThisLiteral node)
    {
        defaultOut(node);
    }

    @Override
    public void visitThisLiteral(ThisLiteral node)
    {
        inThisExp(node);
        outThisExp(node);
    }

    public void inToneExp(ToneLiteral node)
    {
        defaultIn(node);
    }

    public void outToneExp(ToneLiteral node)
    {
        defaultOut(node);
    }

    @Override
    public void visitToneLiteral(ToneLiteral node)
    {
        inToneExp(node);
        outToneExp(node);
    }


    public void inToneType(ToneType node)
    {
        defaultIn(node);
    }

    public void outToneType(ToneType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitToneType(ToneType node)
    {
        inToneType(node);
        outToneType(node);
    }

    public void inTopClassDecl(TopClassDecl node)
    {
        defaultIn(node);
    }

    public void outTopClassDecl(TopClassDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void visitTopClassDecl(TopClassDecl node)
    {
        inTopClassDecl(node);
        {
            List<VarDecl> copy = new ArrayList<VarDecl>(node.getVarDecls());
            for(VarDecl e : copy)
            {
                e.accept(this);
            }
        }
        {
            List<MethodDecl> copy = new ArrayList<MethodDecl>(node.getMethodDecls());
            for(MethodDecl e : copy)
            {
                e.accept(this);
            }
        }
        outTopClassDecl(node);
    }

    public void inTrueExp(TrueLiteral node)
    {
        defaultIn(node);
    }

    public void outTrueExp(TrueLiteral node)
    {
	out.println("    # Push true (1) onto stack");
	out.println("    ldi r24, 1");
	out.println("    push r24");
	out.println();
    }

    @Override
    public void visitTrueLiteral(TrueLiteral node)
    {
        inTrueExp(node);
        outTrueExp(node);
    }

    public void inVarDecl(VarDecl node)
    {
        defaultIn(node);
    }

    public void outVarDecl(VarDecl node)
    {
        defaultOut(node);
    }

    @Override
    public void visitVarDecl(VarDecl node)
    {
        inVarDecl(node);
        if(node.getType() != null)
        {
            node.getType().accept(this);
        }
        outVarDecl(node);
    }

    public void inVoidType(VoidType node)
    {
        defaultIn(node);
    }

    public void outVoidType(VoidType node)
    {
        defaultOut(node);
    }

    @Override
    public void visitVoidType(VoidType node)
    {
        inVoidType(node);
        outVoidType(node);
    }

    public void inWhileStatement(WhileStatement node)
    {
        defaultIn(node);
    }

    public void outWhileStatement(WhileStatement node)
    {
        defaultOut(node);
    }

    @Override
    public void visitWhileStatement(WhileStatement node)
    {
	String start_label = getLabel();
	String cond_label = getLabel();
	String end_label = getLabel();

        inWhileStatement(node);
	out.println(start_label + ":");
        if(node.getExp() != null)
        {
            node.getExp().accept(this);
        }
	out.println("    # if not(condition)");
	out.println("    # load a one byte expression off stack");
	out.println("    pop    r24");
	out.println("    ldi    r25,0");
	out.println("    cp     r24, r25");
	out.println("    # WANT breq MJ_L2");
	out.println("    brne   MJ_L1");
	out.println("    jmp    MJ_L2");
	out.println();
	out.println("    # while loop body");
	out.println(cond_label + ":");

        if(node.getStatement() != null)
        {
            node.getStatement().accept(this);
        }
	out.println("    # jump to while test");
	out.println("    jmp    MJ_L0");
	out.println("");
	out.println("    # end of while");
	out.println(end_label + ":");
	out.println();
        outWhileStatement(node);
    }

}
