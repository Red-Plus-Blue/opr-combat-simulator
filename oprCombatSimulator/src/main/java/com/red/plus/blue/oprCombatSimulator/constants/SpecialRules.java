package com.red.plus.blue.oprCombatSimulator.constants;

import com.red.plus.blue.oprCombatSimulator.model.SpecialRule;

public class SpecialRules {

    public static SpecialRule armorPiercing(final int magnitude) {
        return SpecialRule.builder()
                .applyDefenseModifiers(roll -> roll.addModifier(-magnitude))
                .build();
    }

    public static SpecialRule blast(final int magnitude) {
        return SpecialRule.builder()
                .applyHitMultipliers(rollInformation -> Math.min(magnitude, rollInformation.defender().getModelCount()))
                .build();
    }

    public static SpecialRule deadly(final int magnitude) {
        return SpecialRule.builder()
                .applyWoundMultipliers(rollInformation -> magnitude)
                .build();
    }
}
