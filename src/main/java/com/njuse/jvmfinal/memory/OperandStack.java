package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.memory.struct.JObject;
import com.njuse.jvmfinal.memory.struct.Slot;
import lombok.Getter;
import lombok.Setter;

import java.util.EmptyStackException;

@Getter
@Setter
public class OperandStack {
    private int maxStackSize;
    private int top;
    private Slot[] slots;

    public OperandStack (int maxStackSize) {
        this.maxStackSize = maxStackSize;
        slots = new Slot[maxStackSize];
        for (int i = 0; i < maxStackSize; i++) {
            slots[i] = new Slot();
        }
        top = 0;
    }

    /**
     * 向操作数栈顶端push一个int型变量
     * @param value 变量的值
     */
    public void pushInt(int value) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setValue(value);
        top++;
    }

    /**
     * 从操作数栈顶端pop一个int型变量
     * @return 返回这个int的值
     */
    public int popInt() {
        top--;
        if (top < 0) throw new EmptyStackException();
        int ret = slots[top].getValue();
        slots[top] = new Slot();
        return ret;
    }

    public void pushFloat(float value) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setValue(Float.floatToIntBits(value));
        top++;
    }

    public float popFloat() {
        top--;
        if (top < 0) throw new EmptyStackException();
        float ret = Float.intBitsToFloat(slots[top].getValue());
        slots[top] = new Slot();
        return ret;
    }

    /**
     * 向操作数栈顶push一个 long 类型的变量
     * 注意：采用Big-Endian
     * @param value 变量的值
     */
    public void pushLong(long value) {
        int low = (int) value;
        int high = (int) (value >> 32);
        pushInt(high);
        pushInt(low);
    }

    /**
     * 从操作数栈顶端pop一个long型变量
     * 注意：采用Big-Endian
     * @return 返回这个long的值
     */
    public long popLong() {
        int low = popInt();
        int high = popInt();
        long ret = (((long) high) << 32) | ((long) low & 0x0FFFFFFFFL);
        return ret;
    }

    public void pushDouble(double value) {
        pushLong(Double.doubleToLongBits(value));
    }

    /**
     * 从操作数栈顶端pop一个double型变量
     * @return 返回这个double的值
     */
    public double popDouble() {
        long temp = popLong();
        return Double.longBitsToDouble(temp);
    }

    public void pushObjectRef(JObject ref) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setObject(ref);
        top++;
    }

    public JObject popObjectRef() {
        top--;
        if (top < 0) throw new EmptyStackException();
        JObject ret = slots[top].getObject();
        slots[top] = new Slot();
        return ret;
    }

    public void pushSlot(Slot slot) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top] = slot;
        top++;
    }

    public Slot popSlot() {
        top--;
        if (top < 0) throw new EmptyStackException();
        Slot ret = slots[top];
        slots[top] = new Slot();
        return ret;
    }
}
