package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.Vars;

import java.io.IOException;

public class PUTSTATIC extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        FieldRef fieldRef = (FieldRef) frame.getRuntimeConstantPool().getConstant(index);
        Field field = null;
        try {
            field = fieldRef.getResolvedFieldRef();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        JClass targetClazz = field.getClazz();

        if (targetClazz.getInitState() == InitState.PREPARED) {
            thread.setPC(thread.getPC() - 3);  //opcode + operand = 3bytes
            targetClazz.initialClass(thread);
            return;
        }

        if (!field.isStatic()) {
            throw new IncompatibleClassChangeError();
        }
        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();
        Vars staticVars = targetClazz.getStaticVars();
        OperandStack operandStack = frame.getOperandStack();

        switch (descriptor.charAt(0)) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                int value1 = operandStack.popInt();
                staticVars.setInt(slotID, value1);
                break;
            case 'F':
                float value2 = operandStack.popFloat();
                staticVars.setFloat(slotID, value2);
                break;
            case 'J':
                long value3 = operandStack.popLong();
                staticVars.setLong(slotID, value3);
                break;
            case 'D':
                double value4 = operandStack.popDouble();
                staticVars.setDouble(slotID, value4);
                break;
            case 'L':
            case '[':
                JObject objectRef = operandStack.popObjectRef();
                staticVars.setObjectRef(slotID, objectRef);
                break;
            default:
        }
    }
}
