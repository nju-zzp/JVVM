package com.njuse.jvmfinal.instructions.control;

public class IF_ICMPEQ extends IF_ICMPCOND {
    @Override
    protected boolean condition(int value1, int value2) {
        return value1 == value2;
    }
}
