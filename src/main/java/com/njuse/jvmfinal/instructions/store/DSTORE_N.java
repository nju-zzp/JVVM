package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class DSTORE_N extends STORE_N {
    public DSTORE_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        double value = operandStack.popDouble();
        frame.getLocalVars().setDouble(index, value);
    }
}
