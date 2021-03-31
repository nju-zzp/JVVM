package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.InterfaceMethodrefInfo;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

@Getter
@Setter
public class InterfaceMethodRef extends MemberRef {
    private Method method;

    public InterfaceMethodRef(RuntimeConstantPool runtimeConstantPool, InterfaceMethodrefInfo interfaceMethodrefInfo) {
        super(runtimeConstantPool, interfaceMethodrefInfo);
        //method
    }

    public Method resolveInterfaceMethod (JClass clazz) {

        method = lookUpInterfaceMethod(name, descriptor, clazz);
        return method;
    }

    public Method resolveInterfaceMethod () {

        try {
            resolveClassRef();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Method result = lookUpInterfaceMethod(name, descriptor, this.clazz);
        this.method = result;
        return result;

    }

    private Method lookUpInterfaceMethod (String name, String descriptor, JClass clazz) {
        Method result = null;

        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && m.getDescriptor().equals(descriptor)) {
                result = m;
                break;
            }
        }

        if (result == null && clazz.getSuperClass() != null) {
            result = lookUpInterfaceMethod(name, descriptor, clazz.getSuperClass());
        }

        if (result == null) {
            for (JClass i : clazz.getInterfaces()) {
                Method method1 = lookUpInterfaceMethod(name, descriptor, i);
                if (method1 != null && !method1.isAbstract()) {
                    result = method1;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "InterfaceMethodRef to " + className;
    }

}
