package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class LLOAD_N extends LOAD_N {
    public LLOAD_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        long thisValue = frame.getLocalVars().getLong(index);
        operandStack.pushLong(thisValue);
    }
}
