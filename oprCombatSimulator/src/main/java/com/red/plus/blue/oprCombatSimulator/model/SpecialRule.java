package com.red.plus.blue.oprCombatSimulator.model;

import com.red.plus.blue.oprCombatSimulator.constants.HitFlags;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumSet;
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

    @Builder.Default
    protected Function<EnumSet<HitFlags>, EnumSet<HitFlags>> applyHitFlags = Function.identity();

}
