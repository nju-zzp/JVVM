package com.njuse.jvmfinal.instructions.constLoad;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.memory.StackFrame;

public class ICONST_I extends NoOperandsInstruction {
    private int value;
    private static int[] valid = {-1, 0, 1, 2, 3, 4, 5};

    public ICONST_I (int value) {
        checkValue(value);
        this.value = value;
    }

    public static void checkValue (int i) {
        assert (i >= valid[0] && i <= valid[valid.length - 1]);
    }

    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushInt(value);
    }

    @Override
    public String toString() {
        String suffix = (value == -1) ? "M1" : "" + value;
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
