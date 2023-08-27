package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SpecialRulesService {

    public Roll applyDefenseModifiers(final List<SpecialRule> rules, final RollInformation rollInformation) {
        final var modifier = rules.stream()
                .mapToInt(rule -> rule.getApplyDefenseModifier().apply(rollInformation))
                .sum();
        return rollInformation.roll()
                .toBuilder()
                .modifier(modifier)
                .build();
    }

    public Stream<Hit> applyHitMultipliers(final List<SpecialRule> rules, final RollInformation rollInformation) {
        final var hits = rules.stream()
                .mapToInt(rule -> rule.getApplyHitMultiplier().apply(rollInformation))
                .reduce(1, (left, right) -> left * right);
        return IntStream.range(0, hits).mapToObj(__ -> new Hit());
    }

    public WoundGroup applyWoundMultipliers(final List<SpecialRule> rules, final RollInformation rollInformation) {
        final var wounds = rules.stream()
                .mapToInt(rule -> rule.getApplyWoundMultiplier().apply(rollInformation))
                .reduce(1, (left, right) -> left * right);
        return WoundGroup.builder()
                .count(wounds)
                .build();
    }

}
