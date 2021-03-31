package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;

public class ASTORE_N extends STORE_N{
    public ASTORE_N (int index) {
        checkIndex(index);
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JObject object = operandStack.popObjectRef();
        frame.getLocalVars().setObjectRef(index, object);
    }
}
