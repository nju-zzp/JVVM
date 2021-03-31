package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.OperandStack;
import com.njuse.jvmfinal.memory.StackFrame;

import java.nio.ByteBuffer;

public class BIPUSH extends Instruction {
    private byte value;

    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushInt(value);
    }

    @Override
    public void fetchOperands(ByteBuffer reader) {
        value = reader.get();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " value : " + this.value;
    }
}
