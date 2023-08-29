package com.red.plus.blue.oprCombatSimulator.specialrules;

import com.red.plus.blue.oprCombatSimulator.data.TestArmy;
import com.red.plus.blue.oprCombatSimulator.factories.SpecialRuleFactory;
import com.red.plus.blue.oprCombatSimulator.model.CombatContext;
import com.red.plus.blue.oprCombatSimulator.model.Hit;
import com.red.plus.blue.oprCombatSimulator.model.Roll;
import com.red.plus.blue.oprCombatSimulator.model.Weapon;
import com.red.plus.blue.oprCombatSimulator.service.SpecialRulesService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeadlyTests {

    @Autowired
    private SpecialRulesService specialRulesService;

    @ParameterizedTest
    @ValueSource(ints = {3, 6, 9})
    public void whenDeadlyIsApplied_thenWoundsAreMultiplied(final int deadly) {
        final var deadlyWeapon = Weapon.builder()
                .attacks(1)
                .specialRules(List.of(SpecialRuleFactory.deadly(deadly)))
                .build();
        // note: unit data doesn't matter
        final var attacker = TestArmy.simpleSingleModelUnit(deadlyWeapon).build();
        // note: unit data doesn't matter
        final var defender = TestArmy.simpleSingleModelUnit(15).build();

        final var hit = Hit.builder()
                .attackRoll(Roll.builder().value(6).build())
                .context(new CombatContext(attacker, defender))
                .build();

        final var woundGroup = specialRulesService.applyWoundMultipliers(deadlyWeapon.getSpecialRules(), hit);
        assertEquals(deadly, woundGroup.getCount());
    }
}
