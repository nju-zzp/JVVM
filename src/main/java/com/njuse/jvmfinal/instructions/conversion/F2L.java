package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class F2L extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        float value = operandStack.popFloat();
        long result = 0;
        if (Float.isNaN(value)) {
            result = 0;
        } else if (value >= Long.MAX_VALUE) {
            result = Long.MAX_VALUE;
        } else if (value <= Long.MIN_VALUE) {
            result = Long.MIN_VALUE;
        } else {
            result = (long) value;
        }
        operandStack.pushLong(result);
    }
}
