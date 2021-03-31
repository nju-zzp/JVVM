package com.njuse.jvmfinal.memory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JThread {
    public int PC;  //程序计数器：设为public，便于修改（但是可能会有安全风险？）
    private VMStack vmStack;

    public JThread () {
        this.PC = 0;
        this.vmStack = new VMStack();
    }

    public void pushFrame(StackFrame frame) {
        this.vmStack.pushFrame(frame);
    }

    public void popFrame() {
        this.vmStack.popFrame();
    }

    public StackFrame getTopFrame () {
        return this.vmStack.getTopFrame();
    }
}
