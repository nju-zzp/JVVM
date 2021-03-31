package com.njuse.jvmfinal.instructions.control;

public class IFLT extends IFCOND {
    @Override
    protected boolean condition(int value) {
        return value < 0;
    }
}
