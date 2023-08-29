package com.red.plus.blue.oprCombatSimulator.model;

import com.red.plus.blue.oprCombatSimulator.constants.HitFlags;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumSet;

@Builder(toBuilder = true)
@Getter
public class Hit {

    protected Roll attackRoll;

    protected Roll defenseRoll;

    protected CombatContext context;

    @Builder.Default
    protected EnumSet<HitFlags> flags = EnumSet.noneOf(HitFlags.class);

}
