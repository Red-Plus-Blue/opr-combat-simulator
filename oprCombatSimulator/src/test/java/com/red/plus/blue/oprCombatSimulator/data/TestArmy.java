package com.red.plus.blue.oprCombatSimulator.data;

import com.red.plus.blue.oprCombatSimulator.model.Model;
import com.red.plus.blue.oprCombatSimulator.model.ModelGroup;
import com.red.plus.blue.oprCombatSimulator.model.Unit;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;

import java.util.List;

public class TestArmy {

    public static Unit.UnitBuilder testUnit(final int size, final int quality, final int defense) {
        final var defaultWeapon = Weapon.builder().attacks(1).build();
        return testUnit(size, quality, defense, defaultWeapon);
    }

    public static Unit.UnitBuilder testUnit(final int size, final int quality, final int defense, Weapon weapon) {
        final var model = Model.builder().weapon(weapon).build();
        final var modelGroup = ModelGroup.builder()
                .count(size)
                .model(model)
                .build();

        return Unit.builder()
                .quality(quality)
                .defense(defense)
                .groups(List.of(modelGroup));
    }

    public static Unit.UnitBuilder simpleT1Unit() {
        final var weapon = Weapon.builder().attacks(1).build();
        return simpleT1Unit(weapon);
    }

    public static Unit.UnitBuilder simpleT1Unit(final Weapon weapon) {
        final var model = Model.builder().weapon(weapon).build();
        final var modelGroup = ModelGroup.builder()
                .count(10)
                .model(model)
                .build();
        return Unit.builder()
                .quality(3)
                .defense(3)
                .groups(List.of(modelGroup));
    }

    public static Unit.UnitBuilder simpleT3Unit() {
        final var weapon = Weapon.builder().attacks(1).build();
        return simpleT3Unit(weapon);
    }

    public static Unit.UnitBuilder simpleT3Unit(final Weapon weapon) {
        final var model = Model.builder()
                .weapon(weapon)
                .tough(3)
                .build();
        final var modelGroup = ModelGroup.builder()
                .count(3)
                .model(model)
                .build();
        return Unit.builder()
                .quality(3)
                .defense(3)
                .groups(List.of(modelGroup));
    }


    public static Unit.UnitBuilder simpleSingleModelUnit() {
        final var weapon = Weapon.builder().attacks(1).build();
        final var model = Model.builder().weapon(weapon).build();
        final var modelGroup = ModelGroup.builder()
                .count(1)
                .model(model)
                .build();
        return Unit.builder()
                .quality(3)
                .defense(3)
                .groups(List.of(modelGroup));
    }

    public static Unit.UnitBuilder simpleSingleModelUnit(final Weapon weapon) {
        final var model = Model.builder().weapon(weapon).build();
        final var modelGroup = ModelGroup.builder()
                .count(1)
                .model(model)
                .build();
        return Unit.builder()
                .quality(3)
                .defense(3)
                .groups(List.of(modelGroup));
    }

    public static Unit.UnitBuilder simpleSingleModelUnit(int tough) {
        final var weapon = Weapon.builder().attacks(1).build();
        final var model = Model.builder().weapon(weapon).tough(tough).build();
        final var modelGroup = ModelGroup.builder()
                .count(1)
                .model(model)
                .build();
        return Unit.builder()
                .quality(3)
                .defense(3)
                .groups(List.of(modelGroup));
    }
}
