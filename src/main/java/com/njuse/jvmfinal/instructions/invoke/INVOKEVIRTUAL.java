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

public class INVOKEVIRTUAL extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        MethodRef methodRef = (MethodRef) frame.getRuntimeConstantPool().getConstant(index);
        Method method = methodRef.resolveMethodRef();
        OperandStack operandStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        //跳过本地方法
        if (method.isNative()) {
            return;
        }

        //计算参数列表的slot数量，从操作数栈中取出参数
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = operandStack.popSlot();
        }

        //获取调用该方法的实际对象objectRef及其实际类型，并依据此动态分派，实现方法调用override
        JObject objectRef = operandStack.popObjectRef();
        JClass clazz = objectRef.getClazz();
        Method toInvoke = methodRef.resolveMethodRef(clazz);

        //设置新的栈帧，并push，改变PC值
        StackFrame newFrame = prepareNewFrame(frame, argc, argv, objectRef, toInvoke);
        thread.pushFrame(newFrame);
        thread.setPC(0);

    }

    /**
     * 设置新的方法的栈帧
     */
    private StackFrame prepareNewFrame (StackFrame nowFrame, int argc, Slot[] argv, JObject objectRef, Method toInvoke) {
        JThread thread = nowFrame.getThread();
        StackFrame newFrame = new StackFrame(thread, toInvoke, thread.getPC(), toInvoke.getMaxStack(), toInvoke.getMaxLocal());
        Vars locals = newFrame.getLocalVars();
        Slot objSlot = new Slot();
        objSlot.setObject(objectRef);
        locals.setSlot(0, objSlot);
        for (int i = 1; i < argc + 1; i++) {
            locals.setSlot(i, argv[argc - i]);
        }
        return newFrame;
    }
}
