package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.FieldrefInfo;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class FieldRef extends MemberRef {
    private Field field;

    public FieldRef(RuntimeConstantPool runtimeConstantPool, FieldrefInfo fieldrefInfo) {
        super(runtimeConstantPool, fieldrefInfo);
    }

    public Field getResolvedFieldRef() throws IOException, IllegalAccessException {
        if (field == null) {
            resolveFieldRef();
        }
        return field;
    }

    private void resolveFieldRef() throws IOException, IllegalAccessException {
        resolveClassRef();
        field = lookUpField(name, descriptor, clazz);
    }

    private Field lookUpField(String name, String descriptor, JClass clazz) {
        for (Field f : clazz.getFields()) {
            if (f.getDescriptor().equals(descriptor) && f.getName().equals(name)) {
                return f;
            }
        }
        for (JClass i : clazz.getInterfaces()) {
            Field field = lookUpField(name, descriptor, i);
            if (field != null) return field;
        }
        if (clazz.getSuperClass() != null) {
            return lookUpField(name, descriptor, clazz.getSuperClass());
        }
        return null;
    }

    @Override
    public String toString() {
        return "FieldRef to " + className;
    }
}
