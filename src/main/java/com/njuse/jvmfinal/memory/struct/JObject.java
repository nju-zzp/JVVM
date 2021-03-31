package com.njuse.jvmfinal.memory.struct;

import com.njuse.jvmfinal.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JObject {
    protected static int numInHeap = 0;  //下一个创建的新对象JObject在JHeap中的index
    // （放在JObject而不是放在JHeap，是为了方便于创建新对象时直接设置该新对象的Index，而不需要引用JHeap）

    protected JClass clazz;
    protected int heapIndex;  //该JObject在JHeap中的index
    protected boolean isNull = false;  //该对象是否是null对象

    public JObject () {
        this.heapIndex = numInHeap;
    }

    /**
     * 用于实现instanceOf指令
     * 判断该对象能否转化为指定的类型
     * @param clazz
     * @return
     */
    public boolean isInstanceOf(JClass clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

}
