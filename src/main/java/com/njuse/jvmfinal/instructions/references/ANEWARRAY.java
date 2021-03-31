package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JHeap;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.struct.ArrayObject;

import java.io.IOException;

public class ANEWARRAY extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        ClassRef classRef = (ClassRef) frame.getRuntimeConstantPool().getConstant(index);
        JClass componentClazz = null;
        try {
            componentClazz = classRef.getResolvedClass();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int length = operandStack.popInt();

        JClass arrayClazz = componentClazz.getArrayClass();
        ArrayObject arrayObject = arrayClazz.newArrayObject(length);
        JHeap.getInstance().addObject(arrayObject);
        operandStack.pushObjectRef(arrayObject);

    }

}
