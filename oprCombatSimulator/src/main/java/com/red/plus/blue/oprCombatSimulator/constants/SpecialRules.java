package com.red.plus.blue.oprCombatSimulator.constants;

import com.red.plus.blue.oprCombatSimulator.model.SpecialRule;

public class SpecialRules {

    public static SpecialRule armorPiercing(final int magnitude) {
        return SpecialRule.builder()
                .applyDefenseModifier(rollInformation -> -magnitude)
                .build();
    }

    public static SpecialRule blast(final int magnitude) {
        return SpecialRule.builder()
                .applyHitMultiplier(rollInformation -> Math.min(magnitude, rollInformation.defender().getModelCount()))
                .build();
    }

    public static SpecialRule deadly(final int magnitude) {
        return SpecialRule.builder()
                .applyWoundMultiplier(rollInformation -> magnitude)
                .build();
    }
}
