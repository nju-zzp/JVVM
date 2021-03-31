package com.njuse.jvmfinal.memory.struct.array;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Setter
@Getter
public class BooleanArrayObject extends ArrayObject {
    private boolean[] array;

    public BooleanArrayObject(int length) {
        super(length);
        try {
            this.clazz = ClassLoader.getInstance().loadClass("[Z", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.array = new boolean[length];
        this.type = clazz.getName();
    }
}
