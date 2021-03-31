package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class BranchInstruction extends Instruction{
    protected int offset;  //偏移量offset为unsigned short类型

    public void fetchOperands(ByteBuffer reader) {
        offset = reader.getShort();
    }

    public String toString() {
        return this.getClass().getSimpleName() + " offset: " + offset;
    }
}
