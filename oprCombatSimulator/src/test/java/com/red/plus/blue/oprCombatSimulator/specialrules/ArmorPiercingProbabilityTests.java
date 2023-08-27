package com.red.plus.blue.oprCombatSimulator.specialrules;

import com.red.plus.blue.oprCombatSimulator.constants.SpecialRules;
import com.red.plus.blue.oprCombatSimulator.data.TestArmy;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ArmorPiercingProbabilityTests {

    @Autowired
    private AttackService attackService;

    private static List<List<BigDecimal>> getProbabilities(final int armorPiercing) {
        return IntStream.range(2, 7).mapToObj(quality ->
                IntStream.range(2, 7).mapToObj(defense -> {
                    final var toHit = BigDecimal.valueOf(7 - quality)
                            .divide(BigDecimal.valueOf(6), 10, RoundingMode.HALF_UP);
                    final var effectiveDefense = Math.min(5, (defense + armorPiercing - 1));
                    final var toFailToBlock = BigDecimal.valueOf(effectiveDefense)
                            .divide(BigDecimal.valueOf(6), 10, RoundingMode.HALF_UP);
                    // (7-Q / 6) * (min(5, D+AP-1) / 6)
                    return toHit.multiply(toFailToBlock);
                }).toList()
        ).toList();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenArmorPiercing_whenAttack_thenSimulatedProbabilitiesMatchStatistical(final int armorPiercing) {
        final var errorBound = 0.015f;
        final var iterations = 100 * 1000;
        final var models = 10;
        final var qualities = IntStream.range(2, 7).boxed().toList();
        final var defenses = IntStream.range(2, 7).boxed().toList();

        qualities.forEach(quality ->
                defenses.forEach(defense -> {
                    final var statisticalProbability = getProbabilities(armorPiercing)
                            .get(quality - 2)
                            .get(defense - 2);
                    final var weapon = Weapon.builder()
                            .attacks(1)
                            .specialRules(List.of(SpecialRules.armorPiercing(armorPiercing)))
                            .build();
                    final var attacker = TestArmy.testUnit(models, quality, defense, weapon).build();
                    final var defender = TestArmy.testUnit(models, quality, defense).build();
                    final var result = IntStream.range(0, iterations)
                            .mapToObj(__ -> attackService.attack(attacker, defender).size())
                            .collect(Collectors.averagingDouble(number -> (double) number));
                    final var expectedResult = statisticalProbability.doubleValue() * models;
                    final var error = Math.abs(expectedResult - result);
                    final var message = String.format("Q%d/D%d at AP(%d) average damage should be within +-%f of %f. Got: %f",
                            quality,
                            defense,
                            armorPiercing,
                            errorBound,
                            expectedResult,
                            result
                    );
                    System.out.println(message);
                    Assertions.assertTrue(error <= errorBound, message);
                })
        );
    }
}
