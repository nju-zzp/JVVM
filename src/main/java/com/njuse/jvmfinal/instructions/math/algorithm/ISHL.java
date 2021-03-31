package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class ISHL extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value2 = operandStack.popInt();
        int value1 = operandStack.popInt();
        int s = value2 & 0x1F;
        int result = value1 << s;
        operandStack.pushInt(result);
    }
}
