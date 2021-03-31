package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.StackFrame;

public class RETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        int nextPC = frame.getReturnAddress();
        thread.setPC(nextPC);
        thread.popFrame();
    }
}
