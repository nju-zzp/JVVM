package com.njuse.jvmfinal.instructions.control;

public class IFNE extends IFCOND {
    @Override
    protected boolean condition(int value) {
        return value != 0;
    }
}
