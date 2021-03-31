package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.Loader;
import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.JHeap;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.struct.ArrayObject;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NEWARRAY extends Instruction {
    private byte atype;

    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        int length = operandStack.popInt();

        //加载并获取该数组类
        JClass arrayClazz = getPrimitiveArrayClass(atype, frame.getMethod().getClazz().getDefineLoader());

        //创建数组对象
        ArrayObject arrayRef = arrayClazz.newArrayObject(length);

        JHeap.getInstance().addObject(arrayRef);
        operandStack.pushObjectRef(arrayRef);

    }

    @Override
    public void fetchOperands(ByteBuffer reader) {
        atype = reader.get();
    }

    /**
     * 加载并获取该数组类
     */
    private JClass getPrimitiveArrayClass (int atype, Loader initialLoader) {
        try {

            ClassLoader classLoader = ClassLoader.getInstance();

            switch (atype) {
                case 4:
                    return classLoader.loadClass("[Z", initialLoader);
                case 5:
                    return classLoader.loadClass("[C", initialLoader);
                case 6:
                    return classLoader.loadClass("[F", initialLoader);
                case 7:
                    return classLoader.loadClass("[D", initialLoader);
                case 8:
                    return classLoader.loadClass("[B", initialLoader);
                case 9:
                    return classLoader.loadClass("[S", initialLoader);
                case 10:
                    return classLoader.loadClass("[I", initialLoader);
                case 11:
                    return classLoader.loadClass("[J", initialLoader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("invalid atype");
    }
}
