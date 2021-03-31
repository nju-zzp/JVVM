package com.njuse.jvmfinal.memory.struct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slot {
    JObject object;
    Integer value;

    public Slot clone () {
        Slot newSlot = new Slot();
        newSlot.setObject(object);
        newSlot.setValue(value);
        return newSlot;
    }
}
