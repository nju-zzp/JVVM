package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class FLOAD_N extends LOAD_N {
    public FLOAD_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        float thisValue = frame.getLocalVars().getFloat(index);
        operandStack.pushFloat(thisValue);
    }
}
