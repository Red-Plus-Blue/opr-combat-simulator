package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Builder
@Getter
public class SpecialRule {

    @Builder.Default
    protected Function<RollInformation, Integer> applyDefenseModifier = __ -> 0;

    @Builder.Default
    protected Function<RollInformation, Integer> applyHitMultiplier = __ -> 1;

    @Builder.Default
    protected Function<RollInformation, Integer> applyWoundMultiplier = __ -> 1;

    @Builder.Default
    protected Function<Roll, Boolean> requiresDefenseReRoll = __ -> false;

}
