package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Builder
@Getter
public class SpecialRule {

    @Builder.Default
    protected Function<Hit, Integer> applyDefenseModifier = __ -> 0;

    @Builder.Default
    protected Function<Hit, Integer> applyHitMultiplier = __ -> 1;

    @Builder.Default
    protected Function<Hit, Integer> applyWoundMultiplier = __ -> 1;

    @Builder.Default
    protected Function<Hit, Boolean> requiresDefenseReRoll = __ -> false;

}
