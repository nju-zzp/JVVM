package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class FSTORE_N extends STORE_N {
    public FSTORE_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        float value = operandStack.popFloat();
        frame.getLocalVars().setFloat(index, value);
    }
}
