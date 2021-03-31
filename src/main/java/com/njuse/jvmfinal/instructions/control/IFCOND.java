package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public abstract class IFCOND extends BranchInstruction {

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        JThread thread = frame.getThread();
        int originalNextPC = thread.getPC();

        int value = stack.popInt();
        if (condition(value)) {
            thread.setPC(originalNextPC - 3 + offset);
        }
    }

    protected abstract boolean condition(int value);
}
