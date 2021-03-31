package com.njuse.jvmfinal.instructions.control;

public class IFLE extends IFCOND {
    @Override
    protected boolean condition(int value) {
        return value <= 0;
    }
}
