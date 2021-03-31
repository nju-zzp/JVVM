package com.njuse.jvmfinal.instructions.math.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

public class FCMPL extends NoOperandsInstruction {

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        float value2 = operandStack.popFloat();
        float value1 = operandStack.popFloat();
        if (Float.isNaN(value1) || Float.isNaN(value2)) {
            operandStack.pushInt(-1);
        } else if (value1 > value2) {
            operandStack.pushInt(1);
        } else if (value1 == value2) {
            operandStack.pushInt(0);
        } else if (value1 < value2) {
            operandStack.pushInt(-1);
        }
    }
}
