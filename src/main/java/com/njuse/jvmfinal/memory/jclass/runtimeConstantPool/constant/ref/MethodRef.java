package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
    }

    /**
     * 这个方法用来实现对象方法的动态查找，实现方法的动态分派
     * @param clazz 对象的引用
     */
    public Method resolveMethodRef(JClass clazz) {
        Method result = lookUpMethod(name, descriptor, clazz);
        return result;
    }

    /**
     * 这个方法用来解析methodRef对应的方法
     * 与上面的动态查找相比，这里的查找始终是从这个Ref对应的class开始查找的
     */
    public Method resolveMethodRef() {
        if (method != null) {
            return method;
        }
        try {
            resolveClassRef();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Method result = lookUpMethod(name, descriptor, clazz);
        this.method = result;
        return result;
    }

    private Method lookUpMethod(String name, String descriptor, JClass clazz) {
        Method result = null;

        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && m.getDescriptor().equals(descriptor)) {
                result = m;
            }
        }

        if (result == null && clazz.getSuperClass() != null) {
            result = lookUpMethod(name, descriptor, clazz.getSuperClass());
        }

        if (result == null) {
            for (JClass i : clazz.getInterfaces()) {
                Method method1 = lookUpMethod(name, descriptor, i);
                if (method1 != null) {
                    return method1;
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "MethodRef to " + className;
    }

}
