package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class I2D extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int oldValue = stack.popInt();
        stack.pushDouble((double) oldValue);
    }
}
