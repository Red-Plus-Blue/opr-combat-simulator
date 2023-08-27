package com.red.plus.blue.oprCombatSimulator.model;

import com.red.plus.blue.oprCombatSimulator.constants.Version;

public class SpecialRuleFactory {

    protected static SpecialRule V2_5_POISON =  SpecialRule.builder()
        .applyHitMultiplier(rollInformation -> rollInformation.roll().isNatural6() ? 3 : 1)
        .build();

    protected static SpecialRule V3_0_POISON =  SpecialRule.builder()
            .requiresDefenseReRoll(Roll::isNatural6)
            .build();

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

    public static SpecialRule poison() {
        return poison(Version.V3_0);
    }

    public static SpecialRule poison(Version version) {
        return version == Version.V2_5 ?
            V2_5_POISON :
            V3_0_POISON;
    }

    public static SpecialRule deadly(final int magnitude) {
        return SpecialRule.builder()
                .applyWoundMultiplier(rollInformation -> magnitude)
                .build();
    }
}
