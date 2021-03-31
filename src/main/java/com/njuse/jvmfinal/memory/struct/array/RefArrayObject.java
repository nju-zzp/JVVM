package com.njuse.jvmfinal.memory.struct.array;

import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.struct.ArrayObject;
import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.NullObject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefArrayObject extends ArrayObject{
    private JObject[] array;

    public RefArrayObject(int length, JClass clazz) {
        super(length);
        this.clazz = clazz;
        this.array = new JObject[length];
        this.type = clazz.getName();
    }
}
