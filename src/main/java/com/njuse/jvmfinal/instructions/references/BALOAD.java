package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.array.BooleanArrayObject;
import com.njuse.jvmfinal.memory.struct.array.ByteArrayObject;


public class BALOAD extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int index = operandStack.popInt();
        ArrayObject arrayObject = (ArrayObject) operandStack.popObjectRef();
        int value = 0;
        if (arrayObject == null) {
            throw new NullPointerException();
        } else if (index < 0 && index >= arrayObject.getLength()) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (arrayObject instanceof BooleanArrayObject) {
            value = ((BooleanArrayObject) arrayObject).getArray()[index] ? 1 : 0;
        } else if (arrayObject instanceof ByteArrayObject) {
            value = ((ByteArrayObject) arrayObject).getArray()[index];
        }
        operandStack.pushInt(value);
    }
}
