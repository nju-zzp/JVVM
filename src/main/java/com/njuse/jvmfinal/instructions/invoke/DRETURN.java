package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class DRETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        OperandStack operandStack = frame.getOperandStack();
        double value = operandStack.popDouble();
        int nextPC = frame.getReturnAddress();

        thread.setPC(nextPC);
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushDouble(value);
    }
}
