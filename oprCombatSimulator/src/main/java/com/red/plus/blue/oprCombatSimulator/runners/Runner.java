package com.red.plus.blue.oprCombatSimulator.runners;


import com.red.plus.blue.oprCombatSimulator.model.Model;
import com.red.plus.blue.oprCombatSimulator.model.ModelGroup;
import com.red.plus.blue.oprCombatSimulator.model.Unit;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import com.red.plus.blue.oprCombatSimulator.service.CombatService;
import com.red.plus.blue.oprCombatSimulator.service.SimulationService;
import com.red.plus.blue.oprCombatSimulator.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    protected AttackService attackService;

    @Autowired
    protected CombatService combatService;

    @Autowired
    protected SimulationService simulationService;

    protected static Unit unit(final int size, final int tough, final int attacks) {
        final var weapon = Weapon.builder()
                .attacks(attacks)
                .build();
        final var model = Model.builder()
                .tough(tough)
                .weapon(weapon)
                .build();
        final var group = ModelGroup.builder()
                .model(model)
                .count(size)
                .build();
        return Unit.builder()
                .name(UUID.randomUUID().toString())
                .groups(List.of(group))
                .quality(4)
                .defense(4)
                .build();
    }

    @Override
    public void run(final String... args) throws Exception {
        final var iterations = 10 * 1000; // Increase for more consistent results
        final var table = Table.<Integer, Integer, Double>builder()
                .title("[X]T(1) vs [1]T(X)")
                .rowHeaderGenerator(size -> "[" + ((size + 1) * 3) + "]T1")
                .columnHeaderGenerator(tough -> "[1]T(" + ((tough + 1) * 3) + ")")
                .columnGenerator(index -> ((index + 1) * 3))
                .rowGenerator(index -> ((index + 1) * 3))
                .cellMapper((size, tough) -> {
                    final var unitWithModels = unit(size, 1, 1);
                    final var unitWithTough = unit(1, tough, tough);
                    return simulationService.winRate(unitWithModels, unitWithTough, iterations);
                })
                .build()
                .compute(10, 10);

        table.print(10, number -> String.format("%3.3f", number));
    }
}
