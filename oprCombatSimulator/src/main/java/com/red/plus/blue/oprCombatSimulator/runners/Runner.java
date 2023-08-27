package com.red.plus.blue.oprCombatSimulator.runners;


import com.red.plus.blue.oprCombatSimulator.armies.SampleArmy;
import com.red.plus.blue.oprCombatSimulator.model.*;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import com.red.plus.blue.oprCombatSimulator.service.CombatService;
import com.red.plus.blue.oprCombatSimulator.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    protected AttackService attackService;

    @Autowired
    protected CombatService combatService;

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
                .groups(List.of(group))
                .quality(4)
                .defense(4)
                .build();
    }

    @Override
    public void run(final String... args) throws Exception {
        final var iterations = 10 * 1000; // Increase for more consistent results
        final var random = new Random();
        final var table = Table.<Integer, Integer, Double>builder()
                .title("[X]T(1) vs [1]T(X)")
                .rowHeaderGenerator(size -> "[" + ((size + 1) * 3) + "]T1")
                .columnHeaderGenerator(tough -> "[1]T(" + ((tough + 1) * 3) + ")")
                .columnGenerator(index -> ((index + 1) * 3))
                .rowGenerator(index -> ((index + 1) * 3))
                .cellMapper((size, tough) -> {
                    final var unitWithModels = unit(size, 1, 1);
                    final var unitWithTough = unit(1, tough, tough);
                    return IntStream.range(0, iterations)
                            .mapToObj(__ -> {
                                final var swap = random.nextInt(2) > 0;
                                return swap ?
                                    combatService.doCombat(unitWithModels, unitWithTough) :
                                    combatService.doCombat(unitWithTough, unitWithModels);
                            })
                            .filter(result -> result.winner().getGroups().get(0).getModel().getTough() == 1)
                            .count() / (double) iterations * 100;
                })
                .build()
                .compute(10, 10);

        table.print(10, number -> String.format("%3.3f", number));
    }
}
