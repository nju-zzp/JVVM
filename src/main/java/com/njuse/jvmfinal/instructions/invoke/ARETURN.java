package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;

public class ARETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        OperandStack operandStack = frame.getOperandStack();
        JObject objectRef = operandStack.popObjectRef();
        int nextPC = frame.getReturnAddress();

        thread.setPC(nextPC);
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushObjectRef(objectRef);
    }
}
