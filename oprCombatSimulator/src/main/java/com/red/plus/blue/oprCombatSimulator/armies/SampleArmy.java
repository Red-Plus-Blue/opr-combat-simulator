package com.red.plus.blue.oprCombatSimulator.armies;

import com.red.plus.blue.oprCombatSimulator.model.Model;
import com.red.plus.blue.oprCombatSimulator.model.ModelGroup;
import com.red.plus.blue.oprCombatSimulator.model.Unit;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;

import java.util.List;

public class SampleArmy {

    public static Unit.UnitBuilder sampleUnit(final int quality, final int defense) {
        final var weapon = Weapon.builder().attacks(1).build();
        final var model = Model.builder().weapon(weapon).build();
        final var modelGroup = ModelGroup.builder()
                .count(10)
                .model(model)
                .build();

        return Unit.builder()
                .quality(quality)
                .defense(defense)
                .groups(List.of(modelGroup));
    }
}
