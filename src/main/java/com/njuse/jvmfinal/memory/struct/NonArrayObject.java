package com.njuse.jvmfinal.memory.struct;

import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NonArrayObject extends JObject {

    private Vars fields;

    public NonArrayObject (JClass clazz) {
        this.clazz = clazz;
        numInHeap ++;
        fields = new Vars(clazz.getInstanceSlotCount());
        initDefaultValue(clazz);
    }

    /**
     * 为实例对象分配Field的内存，并设置0值
     * @param clazz
     */
    private void initDefaultValue (JClass clazz) {
        while (clazz != null) {
            for (Field field : clazz.getFields()) {
                if (!field.isStatic()) {
                    String descriptor = field.getDescriptor();
                    switch (descriptor.charAt(0)) {
                        case 'Z':
                        case 'B':
                        case 'C':
                        case 'S':
                        case 'I':
                            this.fields.setInt(field.getSlotID(), 0);
                            break;
                        case 'F':
                            this.fields.setFloat(field.getSlotID(), 0);
                            break;
                        case 'J':
                            this.fields.setLong(field.getSlotID(), 0);
                            break;
                        case 'D':
                            this.fields.setDouble(field.getSlotID(), 0);
                            break;
                        default:
                            this.fields.setObjectRef(field.getSlotID(), new NullObject());
                            break;
                    }
                }
            }

            clazz = clazz.getSuperClass();
        }
    }

}
