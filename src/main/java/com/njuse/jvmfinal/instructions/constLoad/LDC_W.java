package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;

public class LDC_W extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        Constant constant = frame.getRuntimeConstantPool().getConstant(index);
        if (constant instanceof IntWrapper) {
            operandStack.pushInt(((IntWrapper) constant).getValue());
        }
        else if (constant instanceof FloatWrapper) {
            operandStack.pushFloat(((FloatWrapper) constant).getValue());
        }
//        else if (constant instanceof StringWrapper) {
//            operandStack.pushObjectRef(new StringObject(((StringWrapper) constant).getValue()));
//        }
        else {
            throw new ClassFormatError();
        }
    }
}
