package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class ISTORE_N extends STORE_N {
    public ISTORE_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        frame.getLocalVars().setInt(index, value);
    }
}
