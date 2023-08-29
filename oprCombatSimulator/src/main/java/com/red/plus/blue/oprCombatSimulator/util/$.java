package com.red.plus.blue.oprCombatSimulator.util;

import java.util.ArrayList;
import java.util.EnumSet;
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

    public static <T> List<T> cycled(List<T> list) {
        final var copy = $.tail(list);
        copy.add($.head(list));
        return copy;
    }

    public static <FLAG_T extends Enum<FLAG_T>> EnumSetExtensions<FLAG_T> of(EnumSet<FLAG_T> set) {
        return new EnumSetExtensions<>(set);
    }
}
