package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.StackFrame;

public class FCONST_F extends NoOperandsInstruction {
    private float value;
    private static int[] valid = {0, 1, 2};

    public FCONST_F (int f) {
        checkValue(f);
        this.value = (float) f;
    }

    public static void checkValue (int f) {
        assert (f >= valid[0] && f <= valid[valid.length - 1]);
    }

    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushFloat(value);
    }

    @Override
    public String toString() {
        String suffix = (int)value + "";
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
