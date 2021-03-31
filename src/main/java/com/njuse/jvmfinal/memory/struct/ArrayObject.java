package com.njuse.jvmfinal.memory.struct;

import com.njuse.jvmfinal.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrayObject extends JObject{
    private int length;
    protected String type;

    public ArrayObject (int length) {
        this.length = length;
        numInHeap++;
    }

}
