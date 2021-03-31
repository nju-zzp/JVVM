package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index16Instruction extends Instruction{
    public int index;  //操作数索引为short类型，占2byte即16bits

    public void fetchOperands(ByteBuffer reader) {
        index = (int) reader.getShort() & 0xFFFF;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (index & 0xFFFF);
    }
}
