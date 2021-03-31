package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.Slot;
import com.njuse.jvmfinal.memory.struct.Vars;

import java.nio.ByteBuffer;

public class INVOKEINTERFACE extends Index16Instruction {
    private int count;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        index = reader.getShort() & '\uffff';
        count = reader.get() & 0xFF;
        reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        InterfaceMethodRef methodRef = (InterfaceMethodRef) frame.getRuntimeConstantPool().getConstant(index);
        Method method = methodRef.resolveInterfaceMethod();
        OperandStack operandStack = frame.getOperandStack();
        JThread thread = frame.getThread();
        if (method.isNative()) {
            return;
        }

        //获取参数列表和ObjectRef
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = operandStack.popSlot();
        }

        JObject objectRef = operandStack.popObjectRef();
        JClass invokeClazz = objectRef.getClazz();
        Method toInvoke = methodRef.resolveInterfaceMethod(invokeClazz);

        //设置新的Frame
        StackFrame newFrame = new StackFrame(thread, toInvoke, thread.getPC(), toInvoke.getMaxStack(), toInvoke.getMaxLocal()+1);
        Vars locals = newFrame.getLocalVars();
        Slot objSlot = new Slot();
        objSlot.setObject(objectRef);
        locals.setSlot(0, objSlot);
        for (int i = 1; i < argc + 1; i++) {
            locals.setSlot(i, argv[argc - i]);
        }

        //push 并且改变PC
        thread.pushFrame(newFrame);
        thread.setPC(0);
    }
}
