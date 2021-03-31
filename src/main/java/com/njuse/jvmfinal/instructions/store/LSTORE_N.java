package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class LSTORE_N extends STORE_N {
    public LSTORE_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        long value = operandStack.popLong();
        frame.getLocalVars().setLong(index, value);
    }
}
