package com.njuse.jvmfinal.memory.struct;

import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.classloader.classfilereader.loadtype.BootstrapLoader;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.struct.array.CharArrayObject;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Setter
@Getter
public class StringObject extends NonArrayObject{
    private String string;

    public StringObject (String string) throws IOException {
        super(ClassLoader.getInstance().loadClass("java/lang/String", BootstrapLoader.getInstance()));
        try {
            this.clazz = ClassLoader.getInstance().loadClass("java/lang/String", BootstrapLoader.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.string = string;

        int valueFieldSlotID = this.getFields().getMaxSize() - 1;
        for (Field field : clazz.getFields()) {
            if (field.getName().equals("value") && field.getDescriptor().equals("[C")) {
                valueFieldSlotID = field.getSlotID();
            }
        }

        CharArrayObject charArray = new CharArrayObject(string.length());
        charArray.setArray(string.toCharArray());

        this.getFields().setObjectRef(valueFieldSlotID, charArray);

    }
}
