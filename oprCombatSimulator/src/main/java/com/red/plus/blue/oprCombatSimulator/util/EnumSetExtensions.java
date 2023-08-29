package com.red.plus.blue.oprCombatSimulator.util;

import lombok.AllArgsConstructor;

import java.util.EnumSet;

@AllArgsConstructor
public class EnumSetExtensions<FLAG_T extends Enum<FLAG_T>> {

    protected EnumSet<FLAG_T> set;

    public EnumSet<FLAG_T> add(FLAG_T value) {
        if(set.contains(value)) {
            return set;
        }
        final var copy = EnumSet.copyOf(set);
        copy.add(value);
        return copy;
    }
}
