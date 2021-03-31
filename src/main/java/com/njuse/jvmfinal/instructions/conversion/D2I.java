package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class D2I extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        double value = operandStack.popDouble();
        int result = 0;
        if (Double.isNaN(value)) {
            result = 0;
        } else if (value >= Integer.MAX_VALUE) {
            result = Integer.MAX_VALUE;
        } else if (value <= Integer.MIN_VALUE) {
            result = Integer.MIN_VALUE;
        } else {
            result = (int) value;
        }
        operandStack.pushInt(result);
    }
}
