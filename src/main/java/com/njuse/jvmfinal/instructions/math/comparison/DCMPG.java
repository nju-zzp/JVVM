package com.njuse.jvmfinal.instructions.math.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class DCMPG extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double value2 = stack.popDouble();
        double value1 = stack.popDouble();
        if (Double.isNaN(value1) || Double.isNaN(value2)) {
            stack.pushInt(1);
        } else if (value1 > value2) {
            stack.pushInt(1);
        } else if (value1 == value2) {
            stack.pushInt(0);
        } else if (value1 < value2) {
            stack.pushInt(-1);
        }
    }
}
