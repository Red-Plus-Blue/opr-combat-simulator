package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.exceptions.SimulationServiceException;
import com.red.plus.blue.oprCombatSimulator.model.Unit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class SimulationService {

    @Autowired
    protected CombatService combatService;

    @Autowired
    protected AttackService attackService;

    public double winRate(Unit unitA, Unit unitB, int iterations) {
        if (StringUtils.equals(unitA.getName(), unitB.getName())) {
            throw new SimulationServiceException("Units must have different names.");
        }
        final var random = new Random();
        return IntStream.range(0, iterations)
            .mapToObj(__ -> {
                final var swap = random.nextInt(2) > 0;
                return swap ?
                        combatService.doCombat(unitA, unitB) :
                        combatService.doCombat(unitB, unitA);
            })
            .filter(result -> StringUtils.equals(result.winner().getName(), unitA.getName()))
            .count() / (double) iterations * 100;
    }

    public <RESULT_T> List<List<RESULT_T>> qualityDefenseMatrix(
            BiFunction<Integer, Integer, Unit> attackerBuilder,
            BiFunction<Integer, Integer, Unit> defenderBuilder,
            BiFunction<Unit, Unit, RESULT_T> resultMapper) {
        final var qualities = IntStream.range(2, 7).boxed().toList();
        final var defenses = IntStream.range(2, 7).boxed().toList();
        return qualities.stream()
            .map(quality -> defenses.stream()
                .map(defense -> {
                    final var attacker = attackerBuilder.apply(quality, defense);
                    final var defender = defenderBuilder.apply(quality, defense);
                    return resultMapper.apply(attacker, defender);
                })
                .toList()
            ).toList();
    }

    public double averageHits(Unit unit, int iterations) {
        return IntStream.range(0, iterations)
            .map(__ -> unit.getWeaponGroups().stream()
                .mapToInt(weaponGroup -> (int) attackService.getHits(unit, weaponGroup).count())
                .sum()
            )
            .count() / (double) iterations * 100;
    }
}
