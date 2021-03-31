package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;
import com.njuse.jvmfinal.memory.struct.Vars;

import java.nio.ByteBuffer;

public class IINC extends Index8Instruction {
    private byte constValue;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        index = (int) reader.get() & 0xFF;
        constValue = reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        Vars localVars = frame.getLocalVars();
        int originalValue = localVars.getInt(index);
        int newValue = originalValue + (int) constValue;
        localVars.setInt(index, newValue);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (index & 0XFF) + "const: " + constValue;
    }
}
