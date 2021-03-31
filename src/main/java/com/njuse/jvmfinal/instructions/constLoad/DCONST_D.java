package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.StackFrame;

public class DCONST_D extends NoOperandsInstruction {
    private double value;
    private static int[] valid = {0, 1};

    public DCONST_D (int d) {
        checkValue(d);
        this.value = (double) d;
    }

    public static void checkValue (int d) {
        assert (d >= valid[0] && d <= valid[valid.length - 1]);
    }

    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushDouble(value);
    }

    @Override
    public String toString() {
        String suffix = (int)value + "";
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
