package com.njuse.jvmfinal.memory.struct;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Vars {
    private Slot[] slots;
    private int maxSize;

    public Vars () {}

    public Vars (int maxSize) {
        this.maxSize = maxSize;
        slots = new Slot[maxSize];

        for (int i = 0; i < maxSize; i++) {
            slots[i] = new Slot();
        }
    }

    public void setInt (int slotID, int value) {
        slots[slotID].setValue(value);
    }

    public int getInt (int slotID) {
        return slots[slotID].getValue();
    }

    public void setFloat (int slotID, float value) {
        slots[slotID].setValue(Float.floatToIntBits(value));
    }

    public float getFloat (int slotID) {
        return Float.intBitsToFloat(slots[slotID].getValue());
    }

    //Big-Endian 大端 高字节低地址 低字节高地址
    public void setLong (int slotID, long value) {
        slots[slotID].setValue((int)(value >> 32));
        slots[slotID + 1].setValue((int)value);
    }

    //Big-Endian 大端 高字节低地址 低字节高地址
    public Long getLong (int slotID) {
        int high = slots[slotID].getValue();
        int low = slots[slotID + 1].getValue();
        return (((long)high) << 32) | ((long) low & 0x0ffffffffL);
    }

    public void setDouble (int slotID, double value) {
        this.setLong(slotID, Double.doubleToLongBits(value));
    }

    public double getDouble (int slotID) {
        return Double.longBitsToDouble(this.getLong(slotID));
    }

    public void setObjectRef (int slotID, JObject objectRef) {
        slots[slotID].setObject(objectRef);
    }

    public JObject getObjectRef (int slotID) {
        return slots[slotID].getObject();
    }

    public void setStringObject (int slotID, String string) {
        try {
            slots[slotID].setObject(new StringObject(string));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStringObject (int slotID, StringObject stringObject) {
        slots[slotID].setObject(stringObject);
    }

    public String getStringObjectValue (int slotID) {
        return ((StringObject)slots[slotID].getObject()).getString();
    }

    public void setSlot (int slotID, Slot slot) {
        slots[slotID] = slot;
    }

}
