package com.njuse.jvmfinal.instructions.math.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class LCMP extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        long value2 = operandStack.popLong();
        long value1 = operandStack.popLong();
        if (value1 > value2) {
            operandStack.pushInt(1);
        } else if (value1 == value2) {
            operandStack.pushInt(0);
        } else if (value1 < value2) {
            operandStack.pushInt(-1);
        }
    }
}
