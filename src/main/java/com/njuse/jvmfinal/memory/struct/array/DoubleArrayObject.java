package com.njuse.jvmfinal.memory.struct.array;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Setter
@Getter
public class DoubleArrayObject extends ArrayObject {
    private double[] array;

    public DoubleArrayObject(int length) {
        super(length);
        try {
            this.clazz = ClassLoader.getInstance().loadClass("[D", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.array = new double[length];
        this.type = clazz.getName();
    }
}
