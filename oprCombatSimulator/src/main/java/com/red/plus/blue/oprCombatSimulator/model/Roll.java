package com.red.plus.blue.oprCombatSimulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class Roll {

    protected int value;

    @Setter
    protected int modifier;

    public Roll(final Roll other) {
        this.value = other.value;
        this.modifier = other.modifier;
    }

    public int getModifiedValue() {
        return value + modifier;
    }

    public int getUnmodifiedValue() {
        return value;
    }

    public Roll addModifier(int change) {
        this.modifier += change;
        return this;
    }

    public boolean isNatural6() {
        return value == 6;
    }

    public boolean isNatural1() {
        return value == 1;
    }
}
