package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JHeap;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.struct.JObject;

import java.io.IOException;

public class NEW extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        ClassRef classRef = (ClassRef) frame.getRuntimeConstantPool().getConstant(index);
        JClass targetClazz;
        JObject newObject;

        try {
            targetClazz = classRef.getResolvedClass();

            if (targetClazz.getInitState() == InitState.PREPARED) {
                thread.setPC(thread.getPC() - 3);
                targetClazz.initialClass(thread);
                return;
            }

            newObject = targetClazz.newObject();

            JHeap.getInstance().addObject(newObject);
            frame.getOperandStack().pushObjectRef(newObject);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
