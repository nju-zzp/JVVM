package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.NonArrayObject;

import java.io.IOException;

public class PUTFIELD extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack operandStack = frame.getOperandStack();
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
                float value = operandStack.popFloat();
                NonArrayObject obj = (NonArrayObject) operandStack.popObjectRef();
                obj.getFields().setFloat(fieldSlotID, value);
                break;
            case 'D':
                double value2 = operandStack.popDouble();
                NonArrayObject obj2 = (NonArrayObject) operandStack.popObjectRef();
                obj2.getFields().setDouble(fieldSlotID, value2);
                break;
            case 'J':
                long value3 = operandStack.popLong();
                NonArrayObject obj3 = (NonArrayObject) operandStack.popObjectRef();
                obj3.getFields().setLong(fieldSlotID, value3);
                break;
            case 'L':
                JObject value4 = operandStack.popObjectRef();
                NonArrayObject obj4 = (NonArrayObject) operandStack.popObjectRef();
                obj4.getFields().setObjectRef(fieldSlotID, value4);
                break;
            case '[':
                ArrayObject arrayObject = (ArrayObject) operandStack.popObjectRef();
                NonArrayObject obj5 = (NonArrayObject) operandStack.popObjectRef();
                obj5.getFields().setObjectRef(fieldSlotID, arrayObject);
                break;
            default:
                int value0 = operandStack.popInt();
                NonArrayObject obj0 = (NonArrayObject) operandStack.popObjectRef();
                obj0.getFields().setInt(fieldSlotID, value0);
                break;
        }

    }
}
