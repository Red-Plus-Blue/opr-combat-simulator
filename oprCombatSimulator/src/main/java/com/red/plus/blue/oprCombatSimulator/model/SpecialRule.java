package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Builder
@Getter
public class SpecialRule {

    @Builder.Default
    protected Function<Roll, Roll> applyDefenseModifiers = Function.identity();

    @Builder.Default
    protected Function<RollInformation, Integer> applyHitMultipliers = __ -> 1;

    @Builder.Default
    protected Function<RollInformation, Integer> applyWoundMultipliers = __ -> 1;

}
