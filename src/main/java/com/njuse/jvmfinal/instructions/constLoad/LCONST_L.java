package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.StackFrame;

public class LCONST_L extends NoOperandsInstruction {
    private long value;
    private static int[] valid = {0, 1};

    public LCONST_L (int l) {
        checkValue(l);
        this.value = (long) l;
    }

    public static void checkValue (int l) {
        assert (l >= valid[0] && l <= valid[valid.length - 1]);
    }

    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushLong(value);
    }

    @Override
    public String toString() {
        String suffix = value + "";
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
