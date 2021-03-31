package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class I2S extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int oldValue = stack.popInt();
        short newValue = (short) oldValue;
        stack.pushInt(newValue);
    }
}
