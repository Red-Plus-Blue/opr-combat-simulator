package com.red.plus.blue.oprCombatSimulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@AllArgsConstructor
@Builder(toBuilder = true)
public class Roll {

    protected int value;

    @Setter
    protected int modifier;

    public int getModifiedValue() {
        return value + modifier;
    }

    public int getUnmodifiedValue() {
        return value;
    }

    public boolean isNatural6() {
        return value == 6;
    }

    public boolean isNatural1() {
        return value == 1;
    }
}
