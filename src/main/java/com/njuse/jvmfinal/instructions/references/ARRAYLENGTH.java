package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.array.CharArrayObject;

public class ARRAYLENGTH extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        ArrayObject arrayRef = (ArrayObject) operandStack.popObjectRef();
        operandStack.pushInt(arrayRef.getLength());
    }
}
