package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.JHeap;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.array.RefArrayObject;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MULTIANEWARRAY extends Instruction {
    private int index;
    private int dimensions;

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

        //操作数栈中必须包含 dimensions 个数值，
        //数组每一个值代表每个维度中需要创建的元素数量
        // count0 描述第一个维度的长度，count1 描述第二个维度的长度
        int[] len = new int[dimensions];
        for (int i = dimensions - 1; i >= 0 ; i--) {
            len[i] = operandStack.popInt();
            if (len[i] < 0) {
                throw new NegativeArraySizeException();
            }
        }

        ArrayObject ref = this.createMultiDimensionArray(0, len, componentClazz);
        JHeap.getInstance().addObject(ref);
        operandStack.pushObjectRef(ref);
    }

    @Override
    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.getShort() & '\uffff';
        this.dimensions = reader.get() & 255;
    }

    /**
     * 创建多维数组对象
     * @param index 当前正在创建的数组的维数
     * @param lenArray 每一维的长度数组
     * @param arrClass
     * @return
     */
    private ArrayObject createMultiDimensionArray(int index, int[] lenArray, JClass arrClass) {
        int len = lenArray[index];
        ++index;
        ArrayObject arr = arrClass.newArrayObject(len);
        if (index <= lenArray.length - 1) {
            assert arr instanceof RefArrayObject;

            for(int i = 0; i < arr.getLength(); ++i) {
                ((RefArrayObject)arr).getArray()[i] = this.createMultiDimensionArray(index, lenArray, arrClass.getComponentClass());
            }
        }

        return arr;
    }
}
