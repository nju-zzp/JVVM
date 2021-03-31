package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.array.CharArrayObject;

public class CALOAD extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int index = operandStack.popInt();
        CharArrayObject arrayObject = (CharArrayObject) operandStack.popObjectRef();
        if (arrayObject == null) {
            throw new NullPointerException();
        } else if (index < 0 && index >= arrayObject.getLength()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int value = arrayObject.getArray()[index] & 0x0000FFFF;
            operandStack.pushInt(value);
        }
    }
}
