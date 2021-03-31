package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public abstract class SymRef implements Constant {
    public RuntimeConstantPool runtimeConstantPool;
    public String className;    //format : java/lang/Object
    public JClass clazz;

    public void resolveClassRef () throws IOException, IllegalAccessException {
        JClass caller = this.runtimeConstantPool.getClazz();
        JClass resolvedClass = ClassLoader.getInstance().loadClass(className, caller.getDefineLoader());
        if (resolvedClass.isAccessibleTo(caller)) {
            this.clazz = resolvedClass;
        } else {
            throw new IllegalAccessException();
        }
    }

    public JClass getResolvedClass() throws IOException, IllegalAccessException {
        if (clazz == null) {
            resolveClassRef();
        }
        return clazz;
    }

}
