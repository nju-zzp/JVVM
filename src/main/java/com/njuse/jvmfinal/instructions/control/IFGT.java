package com.njuse.jvmfinal.instructions.control;

public class IFGT extends IFCOND {
    @Override
    protected boolean condition(int value) {
        return value > 0;
    }
}
