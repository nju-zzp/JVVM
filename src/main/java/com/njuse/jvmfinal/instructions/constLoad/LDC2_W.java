package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;

public class LDC2_W extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        Constant constant = frame.getRuntimeConstantPool().getConstant(index);
        if (constant instanceof LongWrapper) {
            operandStack.pushLong(((LongWrapper) constant).getValue());
        }
        else if (constant instanceof DoubleWrapper) {
            operandStack.pushDouble(((DoubleWrapper) constant).getValue());
        }
        else {
            throw new ClassFormatError();
        }

    }
}
