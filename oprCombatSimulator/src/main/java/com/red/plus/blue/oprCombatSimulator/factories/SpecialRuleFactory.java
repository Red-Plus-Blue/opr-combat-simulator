package com.red.plus.blue.oprCombatSimulator.factories;

import com.red.plus.blue.oprCombatSimulator.constants.HitFlags;
import com.red.plus.blue.oprCombatSimulator.constants.Version;
import com.red.plus.blue.oprCombatSimulator.model.SpecialRule;
import com.red.plus.blue.oprCombatSimulator.util.$;

import java.util.EnumSet;
import java.util.stream.Stream;

public class SpecialRuleFactory {

    protected static SpecialRule V2_5_POISON = SpecialRule.builder()
            .applyHitMultiplier(hit -> hit.getAttackRoll().isNatural6() ? 3 : 1)
            .build();

    protected static SpecialRule V3_0_POISON = SpecialRule.builder()
            .requiresDefenseReRoll(hit -> hit.getDefenseRoll().isNatural6())
            .build();

    protected static SpecialRule RENDING = SpecialRule.builder()
            .applyDefenseModifier(hit -> hit.getAttackRoll().isNatural6() ? -4 : 0)
            .applyHitFlags(set -> $.of(set).add(HitFlags.DISABLE_REGENERATION))
            .build();

    public static SpecialRule armorPiercing(final int magnitude) {
        return SpecialRule.builder()
                .applyDefenseModifier(rollInformation -> -magnitude)
                .build();
    }

    public static SpecialRule blast(final int magnitude) {
        return SpecialRule.builder()
                .applyHitMultiplier(hit -> Math.min(magnitude, hit.getContext().defender().getModelCount()))
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

    public static SpecialRule rending() {
        return RENDING;
    }
}
