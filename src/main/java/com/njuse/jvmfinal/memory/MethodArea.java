package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class MethodArea {
    private static MethodArea methodArea = new MethodArea(); //单例设计模式，仅存在一个MethodArea

    private HashMap<String, JClass> classMap = new HashMap<String, JClass>();

    public static MethodArea getInstance() {
        return methodArea;
    }

    private MethodArea () {}

    public void addClass (String name, JClass clazz) {
        classMap.put(name, clazz);
    }

    public JClass findClass (String name) {
        JClass result = null;
        for (String eachName : classMap.keySet()) {
            if (name.equals(eachName)) {
                result = classMap.get(name);
            }
        }
        return result;
    }
}
