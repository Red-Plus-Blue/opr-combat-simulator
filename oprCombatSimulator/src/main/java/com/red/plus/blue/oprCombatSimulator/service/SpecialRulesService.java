package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.Hit;
import com.red.plus.blue.oprCombatSimulator.model.SpecialRule;
import com.red.plus.blue.oprCombatSimulator.model.WoundGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SpecialRulesService {

    @Autowired
    protected DiceService diceService;

    public Hit applyDefenseModifiers(final List<SpecialRule> rules, final Hit hit) {
        if (CollectionUtils.isEmpty(rules)) {
            return hit;
        }

        final var modifier = rules.stream()
                .mapToInt(rule -> rule.getApplyDefenseModifier().apply(hit))
                .sum();
        // TODO: gross imperative code
        hit.getDefenseRoll().setModifier(modifier);
        return hit;
    }

    public Stream<Hit> applyHitMultipliers(final List<SpecialRule> rules, final Hit hit) {
        final var hits = rules.stream()
                .mapToInt(rule -> rule.getApplyHitMultiplier().apply(hit))
                .reduce(1, (left, right) -> left * right);
        return IntStream.range(0, hits).mapToObj(__ -> hit.toBuilder().build());
    }

    public WoundGroup applyWoundMultipliers(final List<SpecialRule> rules, final Hit hit) {
        if (CollectionUtils.isEmpty(rules)) {
            WoundGroup.builder().count(1).build();
        }

        final var wounds = rules.stream()
                .mapToInt(rule -> rule.getApplyWoundMultiplier().apply(hit))
                .reduce(1, (left, right) -> left * right);
        return WoundGroup.builder()
                .count(wounds)
                .build();
    }

    public Hit applyDefenseReRolls(final List<SpecialRule> rules, final Hit hit) {
        if (CollectionUtils.isEmpty(rules)) {
            return hit;
        }

        final var requiresReRoll = rules.stream()
                .anyMatch(rule -> rule.getRequiresDefenseReRoll().apply(hit));
        return requiresReRoll ?
                hit.toBuilder()
                        .defenseRoll(diceService.d6())
                        .build() :
                hit;
    }

    public Hit applyHitFlags(final List<SpecialRule> rules, final Hit hit) {
        if (CollectionUtils.isEmpty(rules)) {
            return hit;
        }

        var flags = hit.getFlags();
        for (var rule : rules) {
            flags = rule.getApplyHitFlags().apply(flags);
        }
        return hit.toBuilder()
                .flags(flags)
                .build();
    }

}
