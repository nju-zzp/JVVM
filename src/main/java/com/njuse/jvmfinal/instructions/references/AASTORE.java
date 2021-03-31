package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.array.IntArrayObject;
import com.njuse.jvmfinal.memory.struct.array.RefArrayObject;

public class AASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JObject objectRef =  operandStack.popObjectRef();
        int index = operandStack.popInt();
        RefArrayObject arrayObject = (RefArrayObject) operandStack.popObjectRef();

        if (arrayObject == null) {
            throw new NullPointerException();
        } else if (index < 0 && index >= arrayObject.getLength()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            arrayObject.getArray()[index] = objectRef;
        }
    }
}
