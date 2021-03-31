package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class ILOAD_N extends LOAD_N {

    public ILOAD_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int thisValue = frame.getLocalVars().getInt(index);
        operandStack.pushInt(thisValue);
    }
}
