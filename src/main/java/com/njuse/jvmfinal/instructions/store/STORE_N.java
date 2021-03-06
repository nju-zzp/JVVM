package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;

public abstract class STORE_N extends NoOperandsInstruction {
    protected int index;
    protected static int[] valid = {0, 1, 2, 3};

    public static void checkIndex(int i) {
        assert (i >= valid[0] && i <= valid[valid.length - 1]);
    }

    @Override
    public String toString() {
        String suffix = index + "";
        return this.getClass().getSimpleName().replace("N", suffix);
    }

}
