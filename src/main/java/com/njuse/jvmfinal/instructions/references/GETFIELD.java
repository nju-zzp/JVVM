package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.NonArrayObject;

import java.io.IOException;

public class GETFIELD extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
        NonArrayObject objectRef = (NonArrayObject) operandStack.popObjectRef();

        FieldRef fieldRef = (FieldRef) frame.getRuntimeConstantPool().getConstant(index);
        Field field = null;
        try {
            field = fieldRef.getResolvedFieldRef();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int fieldSlotID = field.getSlotID();
        String type = field.getDescriptor();
        switch (type.charAt(0)) {
            case 'F':
                float value = objectRef.getFields().getFloat(fieldSlotID);
                operandStack.pushFloat(value);
                break;
            case 'D':
                double value2 = objectRef.getFields().getDouble(fieldSlotID);
                operandStack.pushDouble(value2);
                break;
            case 'J':
                long value3 = objectRef.getFields().getLong(fieldSlotID);
                operandStack.pushLong(value3);
                break;
            case 'L':
            case '[':
                JObject value4 = objectRef.getFields().getObjectRef(fieldSlotID);
                operandStack.pushObjectRef(value4);
                break;
            default:
                int value0 = objectRef.getFields().getInt(fieldSlotID);
                operandStack.pushInt(value0);
                break;
        }

    }
}
