package com.red.plus.blue.oprCombatSimulator.utils;

import java.util.ArrayList;
import java.util.List;

public class $ {

    public static <T> List<T> tail(List<T> list) {
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        final var copy = new ArrayList<>(list);
        copy.remove(0);
        return copy;
    }

    public static <T> T head(List<T> list) {
        return list.get(0);
    }
}
