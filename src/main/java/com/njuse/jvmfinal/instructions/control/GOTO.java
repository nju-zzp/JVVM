package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.memory.JThread;
import com.njuse.jvmfinal.memory.StackFrame;

public class GOTO extends BranchInstruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        int originalNextPC = thread.getPC();
        int branchPC = originalNextPC - 3 + offset;
        thread.setPC(branchPC);
    }
}
