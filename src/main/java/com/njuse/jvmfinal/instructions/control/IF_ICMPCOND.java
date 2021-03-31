package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public abstract class IF_ICMPCOND extends BranchInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JThread thread = frame.getThread();
        int originalNextPC = thread.getPC();

        int value2 = operandStack.popInt();
        int value1 = operandStack.popInt();
        if (condition(value1, value2)) {
            thread.setPC(originalNextPC - 3 + offset);
        }
    }

    protected abstract boolean condition (int value1, int value2);
}
