package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.classloader.classfileparser.FieldInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field extends ClassMember {
    private int slotID;  //该Field在类的静态成员变量表中，或者在对象实例的实例成员变量表中，的SlotID（ClassLoader的准备阶段确定的）
    private int constValueIndex;  //如果是final static 的 field，拥有constantValue属性，常量值为对常量池的索引

    public Field(FieldInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();
        if (info.getConstantValueAttr() != null) {
            constValueIndex = info.getConstantValueAttr().getConstantValueIndex();
        }
    }

}
