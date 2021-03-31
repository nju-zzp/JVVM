package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.array.IntArrayObject;

public class IASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int value = operandStack.popInt();
        int index = operandStack.popInt();
        IntArrayObject intArrayObject = (IntArrayObject) operandStack.popObjectRef();

        if (intArrayObject == null) {
            throw new NullPointerException();
        } else if (index < 0 && index >= intArrayObject.getLength()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            intArrayObject.getArray()[index] = value;
        }
    }
}
