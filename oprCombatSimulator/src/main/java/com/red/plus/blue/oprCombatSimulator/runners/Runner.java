package com.red.plus.blue.oprCombatSimulator.runners;


import com.red.plus.blue.oprCombatSimulator.armies.SampleArmy;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import com.red.plus.blue.oprCombatSimulator.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    protected AttackService attackService;

    @Override
    public void run(final String... args) throws Exception {
        final var iterations = 10 * 1000;
        var results = Table.<Integer, Integer, Double>builder()
                .rowHeaderGenerator(quality -> "Q" + (quality + 2))
                .columnHeaderGenerator(defense -> "D" + (defense + 2))
                .columnGenerator(index -> index + 2)
                .rowGenerator(index -> index + 2)
                .cellMapper((quality, defense) -> {
                    final var attacker = SampleArmy.sampleUnit(quality, defense).build();
                    final var defender = SampleArmy.sampleUnit(quality, defense).build();
                    return IntStream.range(0, iterations)
                            .mapToObj(__ -> (long) attackService.attack(attacker, defender).size())
                            .collect(Collectors.averagingDouble(number -> (double) number));
                })
                .build();

        results.compute(5, 5);
        results.print(10, number -> number.toString().substring(0, 4));
    }
}
