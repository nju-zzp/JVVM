package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.array.ByteArrayObject;

public class BASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        int index = operandStack.popInt();
        ByteArrayObject arrayObject = (ByteArrayObject) operandStack.popObjectRef();
        if (arrayObject == null) {
            throw new NullPointerException();
        } else if (index < 0 && index >= arrayObject.getLength()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            arrayObject.getArray()[index] = (byte)value;
        }

    }
}
