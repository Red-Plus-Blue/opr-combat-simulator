package com.red.plus.blue.oprCombatSimulator.runners;


import com.red.plus.blue.oprCombatSimulator.armies.SampleArmy;
import com.red.plus.blue.oprCombatSimulator.model.Unit;
import com.red.plus.blue.oprCombatSimulator.model.WoundGroup;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import com.red.plus.blue.oprCombatSimulator.service.CombatService;
import com.red.plus.blue.oprCombatSimulator.service.SimulationService;
import com.red.plus.blue.oprCombatSimulator.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    protected AttackService attackService;

    @Autowired
    protected CombatService combatService;

    @Autowired
    protected SimulationService simulationService;

    @Override
    public void run(final String... args) throws Exception {
        final var iterations = 10 * 1000; // Increase for more consistent results
        final BiFunction<Integer, Integer, Unit> attacker = (quality, defense) -> SampleArmy.sampleUnit(quality, defense).build();
        final BiFunction<Integer, Integer, Unit> defender = (quality, defense) -> SampleArmy.sampleUnit(quality, defense).build();
        final BiFunction<Unit, Unit, Double> getAverageDamage = (attackingUnit, defendingUnit) -> IntStream.range(0, iterations)
                .map(__ -> attackService.attack(attackingUnit, defendingUnit).stream().mapToInt(WoundGroup::getCount).sum())
                .sum() / (double) iterations;

        final BiFunction<Unit, Unit, Double> getAverageHits = (attackingUnit, defendingUnit) -> IntStream.range(0, iterations)
                .mapToLong(__ -> attackingUnit.getWeaponGroups().stream().flatMap(weaponGroup -> attackService.getHits(attackingUnit, weaponGroup)).count())
                .sum() / (double) iterations;

        final var averageDamage = simulationService.qualityDefenseMatrix(attacker, defender, getAverageDamage);

        final var averageDamageTable = Table.<Integer, Integer, Double>builder()
                .title("Average Damage w/ size 10 unit A1")
                .columnGenerator(index -> index)
                .rowGenerator(index -> index)
                .rowHeaderGenerator(index -> "Q" + (index + 2))
                .columnHeaderGenerator(index -> "D" + (index + 2))
                .cellMapper((quality, defense) -> averageDamage.get(quality).get(defense))
                .build()
                .compute(5, 5);

        averageDamageTable.print(10, number -> String.format("%3.2f", number));

        final var averageHits = simulationService.qualityDefenseMatrix(attacker, defender, getAverageHits);

        final var averageHitsTable = Table.<Integer, Integer, Double>builder()
                .title("Average hits w/ size 10 unit A1")
                .columnGenerator(index -> index)
                .rowGenerator(index -> index)
                .rowHeaderGenerator(index -> "Q" + (index + 2))
                .columnHeaderGenerator(index -> "D" + (index + 2))
                .cellMapper((quality, defense) -> averageHits.get(quality).get(defense))
                .build()
                .compute(5, 5);

        averageHitsTable.print(10, number -> String.format("%3.2f", number));
    }
}
