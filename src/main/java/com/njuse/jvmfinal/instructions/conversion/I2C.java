package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class I2C extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int oldValue = stack.popInt();
        byte temp = (byte) oldValue;
        int newValue = ((int) temp) & 0x000000FF;
        stack.pushInt(newValue);
    }
}
