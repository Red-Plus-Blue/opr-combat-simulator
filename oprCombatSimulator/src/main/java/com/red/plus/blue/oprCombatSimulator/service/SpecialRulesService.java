package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SpecialRulesService {

    public Roll applyDefenseModifications(final List<SpecialRule> rules, final Roll roll) {
        // it's possible that this could become far more complex in the future
        // while this is technically a map => reduce, the reducer raises odd questions like:
        // how do we reduce across rolls that have two different values?

        final var modifiedRoll = new Roll(roll);
        for (var rule : rules) {
            rule.getApplyDefenseModifiers().apply(modifiedRoll);
        }
        return modifiedRoll;
    }

    public Stream<Hit> applyHitMultipliers(final List<SpecialRule> rules, final RollInformation rollInformation) {
        var hits = 1;
        for (var rule : rules) {
            hits *= rule.getApplyHitMultipliers().apply(rollInformation);
        }
        return IntStream.range(0, hits).mapToObj(__ -> new Hit());
    }

    public WoundGroup applyWoundMultipliers(final List<SpecialRule> rules, final RollInformation rollInformation) {
        var wounds = 1;
        for (var rule : rules) {
            wounds *= rule.getApplyWoundMultipliers().apply(rollInformation);
        }
        return WoundGroup.builder().count(wounds).build();
    }

}
