package com.red.plus.blue.oprCombatSimulator.specialrules;

import com.red.plus.blue.oprCombatSimulator.data.TestArmy;
import com.red.plus.blue.oprCombatSimulator.model.SpecialRuleFactory;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.AttackService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class BlastProbabilityTests {

    private static final List<List<BigDecimal>> probabilities = IntStream.range(2, 7).mapToObj(quality ->
            IntStream.range(2, 7).mapToObj(defense -> {
                var toHit = BigDecimal.valueOf(7 - quality)
                        .divide(BigDecimal.valueOf(6), 10, RoundingMode.HALF_UP);
                var toFailToBlock = BigDecimal.valueOf(defense - 1)
                        .divide(BigDecimal.valueOf(6), 10, RoundingMode.HALF_UP);
                // (7-Q / 6) * (D-1 / 6)
                return toHit.multiply(toFailToBlock);
            }).toList()
    ).toList();
    @Autowired
    private AttackService attackService;

    @Test
    public void givenBlast3_whenAttack_thenSimulatedProbabilitiesMatchStatistical() {
        final var errorBound = 0.015f;
        final var iterations = 100 * 1000;
        final var defenderSize = 10;
        final var attackerSize = 1;
        final var blast = 3;
        final var blastWeapon = Weapon.builder()
                .attacks(1)
                .specialRules(List.of(SpecialRuleFactory.blast(blast)))
                .build();
        final var qualities = IntStream.range(2, 7).boxed().toList();
        final var defenses = IntStream.range(2, 7).boxed().toList();

        qualities.forEach(quality ->
                defenses.forEach(defense -> {
                    final var statisticalProbability = probabilities
                            .get(quality - 2)
                            .get(defense - 2);

                    final var attacker = TestArmy.testUnit(attackerSize, quality, defense, blastWeapon).build();
                    final var defender = TestArmy.testUnit(defenderSize, quality, defense).build();
                    final var result = IntStream.range(0, iterations)
                            .mapToObj(__ -> attackService.attack(attacker, defender).size())
                            .collect(Collectors.averagingDouble(number -> (double) number));
                    final var expectedResult = statisticalProbability.doubleValue() * blast;
                    final var error = Math.abs(expectedResult - result);
                    final var message = String.format("Q%d/D%d w/ Blast(3) average damage should be within +-%f of %f. Got: %f",
                            quality,
                            defense,
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
