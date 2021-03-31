package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.struct.JObject;

import java.io.IOException;

public class INSTANCEOF extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        JObject objectRef = operandStack.popObjectRef();

        if (objectRef == null || objectRef.isNull()) {
            operandStack.pushInt(0);
        } else {
            ClassRef classRef = (ClassRef) frame.getRuntimeConstantPool().getConstant(index);
            try {
                JClass clazz = classRef.getResolvedClass();
                if (clazz == null) {
                    throw new IOException("clazz is null");
                }
                int value = objectRef.isInstanceOf(clazz) ? 1 : 0;
                operandStack.pushInt(value);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
