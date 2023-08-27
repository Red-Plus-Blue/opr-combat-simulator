package com.red.plus.blue.oprCombatSimulator.runners;


import com.red.plus.blue.oprCombatSimulator.armies.SampleArmy;
import com.red.plus.blue.oprCombatSimulator.constants.Version;
import com.red.plus.blue.oprCombatSimulator.model.SpecialRuleFactory;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import com.red.plus.blue.oprCombatSimulator.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    protected AttackService attackService;

    @Override
    public void run(final String... args) throws Exception {
        final var iterations = 1 * 1000; // Increase for more consistent results
        final var baseDamageTable = Table.<Integer, Integer, Double>builder()
                .title("Base Damage w/ 10 models A1")
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
                .build()
                .compute(5, 5);

        baseDamageTable.print(10, number -> String.format("%3.3f", number));

        final var poisonDamageTable = baseDamageTable.toBuilder()
                .title("Damage w/ Poison w/ 10 models A1")
                .cellMapper((quality, defense) -> {
                    final var poisonWeapon = Weapon.builder()
                            .specialRules(List.of(SpecialRuleFactory.poison()))
                            .build();
                    final var attacker = SampleArmy.sampleUnit(quality, defense, poisonWeapon).build();
                    final var defender = SampleArmy.sampleUnit(quality, defense).build();
                    return IntStream.range(0, iterations)
                            .mapToObj(__ -> (long) attackService.attack(attacker, defender).size())
                            .collect(Collectors.averagingDouble(number -> (double) number));
                })
                .build()
                .compute(5, 5);

        poisonDamageTable.print(10, number -> String.format("%3.3f", number));

        final var percentIncreaseTable = poisonDamageTable.toBuilder()
                .title("% Increase w/ Poison w/ 10 models A1")
                .columnGenerator(index -> index)
                .rowGenerator(index -> index)
                .cellMapper((row, column) -> {
                    final var baseDamage = baseDamageTable.get(row, column);
                    final var poisonDamage =  poisonDamageTable.get(row, column);
                    return 100f * (poisonDamage - baseDamage) / baseDamage;
                })
                .build()
                .compute(5, 5);

        percentIncreaseTable.print(10, number -> String.format("%3.0f%%", number));
    }
}
