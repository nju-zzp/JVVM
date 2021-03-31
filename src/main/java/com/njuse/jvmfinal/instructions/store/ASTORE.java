package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.JObject;

public class ASTORE extends Index8Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JObject object = operandStack.popObjectRef();
        frame.getLocalVars().setObjectRef(index, object);
    }
}
