package com.njuse.jvmfinal.instructions.base;

import com.njuse.jvmfinal.memory.StackFrame;

import java.nio.ByteBuffer;

public abstract class Instruction {
    /**
     * 注意：指令执行时必须改变该thread 的 PC的值
     * @param frame
     */
    public abstract void execute (StackFrame frame);

    public abstract void fetchOperands (ByteBuffer reader);
}
