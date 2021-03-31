package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class DLOAD_N extends LOAD_N {
    public DLOAD_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        double thisValue = frame.getLocalVars().getDouble(index);
        operandStack.pushDouble(thisValue);
    }
}
