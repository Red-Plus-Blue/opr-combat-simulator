package com.red.plus.blue.oprCombatSimulator.specialrules;

import com.red.plus.blue.oprCombatSimulator.constants.SpecialRules;
import com.red.plus.blue.oprCombatSimulator.data.TestArmy;
import com.red.plus.blue.oprCombatSimulator.model.Roll;
import com.red.plus.blue.oprCombatSimulator.model.RollInformation;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.SpecialRulesService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BlastTests {

    @Autowired
    private SpecialRulesService specialRulesService;

    @ParameterizedTest
    @ValueSource(ints = {3, 6, 9})
    public void givenASize10Unit_whenHitWithBlast_thenAllHitsApplied(final int blast) {
        final var attacker = TestArmy.simpleT1Unit().build();
        final var defender = TestArmy.simpleT1Unit().build();
        // assume
        assertTrue(defender.getModelCount() > blast);

        final var blastWeapon = Weapon.builder()
                .attacks(1)
                .specialRules(List.of(SpecialRules.blast(blast)))
                .build();

        final var rollInformation = new RollInformation(
                Roll.builder().value(6).build(),
                attacker,
                defender
        );

        final var hits = specialRulesService.applyHitMultipliers(blastWeapon.getSpecialRules(), rollInformation);
        assertEquals(blast, hits.count());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 6, 9})
    public void givenASingleModelUnit_whenHitWithBlast_thenOnly1HitApplied(final int blast) {
        final var attacker = TestArmy.simpleT1Unit().build();
        final var defender = TestArmy.simpleSingleModelUnit().build();
        // assume
        assertTrue(defender.getModelCount() < blast);

        final var blastWeapon = Weapon.builder()
                .attacks(1)
                .specialRules(List.of(SpecialRules.blast(blast)))
                .build();

        final var rollInformation = new RollInformation(
                Roll.builder().value(6).build(),
                attacker,
                defender
        );

        final var hits = specialRulesService.applyHitMultipliers(blastWeapon.getSpecialRules(), rollInformation);
        assertEquals(1, hits.count());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10, 15, 20})
    public void givenASizeXUnit_whenHitWithBlast6_thenHitsCappedTo6(final int size) {
        final var blast = 6;
        final var attacker = TestArmy.simpleT1Unit().build();
        final var defender = TestArmy.testUnit(size, 3, 3).build();

        final var blastWeapon = Weapon.builder()
                .attacks(1)
                .specialRules(List.of(SpecialRules.blast(blast)))
                .build();

        final var rollInformation = new RollInformation(
                Roll.builder().value(6).build(),
                attacker,
                defender
        );

        final var hits = specialRulesService.applyHitMultipliers(blastWeapon.getSpecialRules(), rollInformation);
        assertEquals(Math.min(blast, size), hits.count());
    }
}
