package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.Unit;
import com.red.plus.blue.oprCombatSimulator.util.$;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CombatService {

    public record CombatResult(Unit winner) { }

    @Autowired
    protected AttackService attackService;

    public CombatResult doCombat(final Unit unitA, final Unit unitB) {
        var units = List.of(unitA, unitB);
        while(units.stream().allMatch(Unit::isAlive)) {
            final var defender = units.get(1);
            final var attacker =  $.head(units);
            final var wounds = attackService.attack(attacker, defender);
            units = List.of(defender.takeDamage(wounds), attacker);
        }
        final var winner = units.stream()
                .filter(Unit::isAlive)
                .findFirst()
                // it should not be possible for both units to kill each other
                .get();
        return new CombatResult(winner);
    }
}
