package com.red.plus.blue.oprCombatSimulator;

import com.red.plus.blue.oprCombatSimulator.data.TestArmy;
import com.red.plus.blue.oprCombatSimulator.model.Hit;
import com.red.plus.blue.oprCombatSimulator.model.Roll;
import com.red.plus.blue.oprCombatSimulator.model.WoundGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -4, 1, 4})
    public void givenANatural6_whenIsHit_thenReturnsTrue(final int modifier) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var natural6 = Roll.builder()
                .value(6)
                .modifier(modifier)
                .build();
        assertTrue(unit.isHit(natural6));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -4, 1, 4})
    public void givenANatural1_whenIsHit_thenReturnsFalse(final int modifier) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var natural1 = Roll.builder()
                .value(1)
                .modifier(modifier)
                .build();
        assertFalse(unit.isHit(natural1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -4, 1, 4})
    public void givenANatural6_whenIsBlock_thenReturnsTrue(final int modifier) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var natural6 = Roll.builder()
                .value(6)
                .modifier(modifier)
                .build();
        final var hit = Hit.builder()
                .defenseRoll(natural6)
                .build();
        assertTrue(unit.isBlock(hit));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -4, 1, 4})
    public void givenANatural1_whenIsBlock_thenReturnsFalse(final int modifier) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var natural1 = Roll.builder()
                .value(1)
                .modifier(modifier)
                .build();
        final var hit = Hit.builder()
                .defenseRoll(natural1)
                .build();
        assertFalse(unit.isBlock(hit));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void givenAT1Unit_whenTakeDamage_correctWoundsAreApplied(final int damage) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var unitSize = unit.getModelCount();
        final var damagedUnit = unit.takeDamage(damage);

        assertEquals(0, damagedUnit.getWounds());

        final var modelsLeft = unitSize - damage;
        assertEquals(modelsLeft, damagedUnit.getModelCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    public void givenAT3Unit_whenTakeDamage_correctWoundsAreApplied(final int damage) {
        final var unit = TestArmy.simpleT3Unit().build();
        final var unitSize = unit.getModelCount();
        final var damagedUnit = unit.takeDamage(damage);

        final var remainingWounds = damage % 3;
        assertEquals(remainingWounds, damagedUnit.getWounds());

        final var modelsLeft = unitSize - (damage / 3);
        assertEquals(modelsLeft, damagedUnit.getModelCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 9})
    public void whenT1UnitTakesLessDamageThanHealth_thenIsAliveReturnsTrue(final int damage) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var damagedUnit = unit.takeDamage(damage);
        assertTrue(damagedUnit.isAlive());
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 30})
    public void whenT1UnitTakesMoreDamageThanHealth_thenIsAliveReturnsFalse(final int damage) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var damagedUnit = unit.takeDamage(damage);
        assertFalse(damagedUnit.isAlive());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 6, 8})
    public void whenT3UnitTakesLessDamageThanHealth_thenIsAliveReturnsTrue(final int damage) {
        final var unit = TestArmy.simpleT3Unit().build();
        final var damagedUnit = unit.takeDamage(damage);
        assertTrue(damagedUnit.isAlive());
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 15, 30})
    public void whenT3UnitTakesMoreDamageThanHealth_thenIsAliveReturnsFalse(final int damage) {
        final var unit = TestArmy.simpleT3Unit().build();
        final var damagedUnit = unit.takeDamage(damage);
        assertFalse(damagedUnit.isAlive());
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 15, 100})
    public void givenAT1Unit_whenTakeMoreDamageThanHealth_thenModelsAreNotNegative(final int damage) {
        final var unit = TestArmy.simpleT1Unit().build();
        final var damagedUnit = unit.takeDamage(damage);
        assertEquals(0, damagedUnit.getModelCount());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 9, 12})
    public void givenAT3Unit_whenWoundGroupWithMoreDamageThanTough_thenWoundsDontCarryOver(final int wounds) {
        final var unit = TestArmy.simpleT3Unit().build();
        final var damagedUnit = unit.takeDamage(
                // simulate a hit with deadly(wounds)
                List.of(WoundGroup.builder().count(wounds).build())
        );
        assertEquals(0, damagedUnit.getWounds());
        assertEquals(2, damagedUnit.getModelCount());
    }

    @Test
    public void givenAT3Unit_whenWoundGroupsAppliedSequentially_thenUnitLosesModels() {
        final var unit = TestArmy.simpleT3Unit().build();
        final var deadly6Hit = WoundGroup.builder().count(6).build();

        final var unitAfter1Hit = unit.takeDamage(List.of(deadly6Hit));
        assertEquals(0, unitAfter1Hit.getWounds());
        assertEquals(2, unitAfter1Hit.getModelCount());

        final var unitAfter2Hits = unitAfter1Hit.takeDamage(List.of(deadly6Hit));
        assertEquals(0, unitAfter2Hits.getWounds());
        assertEquals(1, unitAfter2Hits.getModelCount());
        assertTrue(unitAfter2Hits.isAlive());

        final var unitAfter3Hits = unitAfter2Hits.takeDamage(List.of(deadly6Hit));
        assertEquals(0, unitAfter3Hits.getWounds());
        assertEquals(0, unitAfter3Hits.getModelCount());
        assertFalse(unitAfter3Hits.isAlive());

        final var unitAfter4Hits = unitAfter3Hits.takeDamage(List.of(deadly6Hit));
        assertEquals(0, unitAfter4Hits.getWounds());
        assertEquals(0, unitAfter4Hits.getModelCount());
        assertFalse(unitAfter4Hits.isAlive());
    }
}
