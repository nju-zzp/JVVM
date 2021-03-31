package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index8Instruction extends Instruction{
    public int index;  //操作数索引为byte类型，只占1byte即8bits

    @Override
    public void fetchOperands(ByteBuffer reader) {
        index = (int) reader.get() & 0xFF;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (index & 0XFF);
    }
}
