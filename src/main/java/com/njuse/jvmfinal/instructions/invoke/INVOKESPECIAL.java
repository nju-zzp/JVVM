package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.Slot;
import com.njuse.jvmfinal.memory.struct.Vars;

public class INVOKESPECIAL extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        //获取方法符号引用并初步解析
        OperandStack operandStack = frame.getOperandStack();
        MethodRef methodRef = (MethodRef) frame.getRuntimeConstantPool().getConstant(index);
        Method originalMethod = methodRef.resolveMethodRef();
        if (originalMethod.isNative()) {
            return;
        }

        //确定调用方法的类
        JClass nowClazz = frame.getMethod().getClazz();
        JClass invokeClazz = null;
        if (nowClazz.isAccSuper() && ! originalMethod.getName().equals("<init>")) {
            if (originalMethod.getClazz().isInterface()) {
                invokeClazz = nowClazz.getSuperClass();
            } else if (nowClazz.getSuperClass() == originalMethod.getClazz()) {
                invokeClazz = nowClazz.getSuperClass();
            } else {
                invokeClazz = originalMethod.getClazz();
            }
        } else {
            invokeClazz = originalMethod.getClazz();
        }

        Method toInvoke = methodRef.resolveMethodRef(invokeClazz);

        if (toInvoke == null) {
            throw new NullPointerException("invoke Method can't be found");
        }

        //设置即将调用的方法的参数列表

        int argc = toInvoke.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = operandStack.popSlot();
        }
        JObject objectRef = operandStack.popObjectRef();
        Slot objSlot = new Slot();
        objSlot.setObject(objectRef);

        //设置新方法的栈帧
        JThread thread = frame.getThread();
        StackFrame newFrame = new StackFrame(thread, toInvoke, thread.getPC(), toInvoke.getMaxStack(), toInvoke.getMaxLocal());
        Vars locals = newFrame.getLocalVars();
        locals.setSlot(0, objSlot);
        for (int i = 1; i < argc + 1; i++) {
            locals.setSlot(i, argv[argc - i]);
        }

        //push栈帧并改变PC
        thread.pushFrame(newFrame);
        thread.setPC(0);

    }

}
