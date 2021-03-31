package com.njuse.jvmfinal.instructions.operand;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.StackFrame;

public class NOP extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        // do nothing
    }
}
