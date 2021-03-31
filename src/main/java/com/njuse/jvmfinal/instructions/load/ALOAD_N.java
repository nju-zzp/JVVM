package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;

public class ALOAD_N extends LOAD_N {
    public ALOAD_N (int index) {
        this.index = index;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JObject object = frame.getLocalVars().getObjectRef(index);
        operandStack.pushObjectRef(object);
    }
}
