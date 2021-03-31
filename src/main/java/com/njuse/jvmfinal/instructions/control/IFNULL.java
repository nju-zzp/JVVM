package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;

public class IFNULL extends BranchInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JThread thread = frame.getThread();
        int originalNextPC = thread.getPC();

        JObject objectRef = operandStack.popObjectRef();
        if (objectRef.isNull()) {
            thread.setPC(originalNextPC - 3 + offset);
        }
    }
}
