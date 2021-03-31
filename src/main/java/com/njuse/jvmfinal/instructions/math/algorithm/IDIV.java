package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class IDIV extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value2 = operandStack.popInt();
        int value1 = operandStack.popInt();
        if (value2 == 0) {
            throw new ArithmeticException();
        } else {
            operandStack.pushInt(value1 / value2);
        }
    }
}
