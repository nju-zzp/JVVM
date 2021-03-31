package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.memory.struct.JObject;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class JHeap {
    private static JHeap jHeap = new JHeap();  //单例设计模式
    private Set<JObject> objects;
    private int maxSize = 500;
    private int currentSize = 0;

    private JHeap () {
        objects = new LinkedHashSet<JObject>();
    }

    public static JHeap getInstance() {
        return jHeap;
    }

    public void addObject (JObject object) {
        if (currentSize >= maxSize) {
            throw new OutOfMemoryError();
        }
        objects.add(object);
        currentSize++;
    }
}
