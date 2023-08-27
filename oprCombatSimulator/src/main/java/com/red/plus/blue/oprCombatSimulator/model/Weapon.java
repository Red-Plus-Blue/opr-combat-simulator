package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Weapon {

    @Builder.Default
    protected int attacks = 1;

    @Builder.Default
    protected List<SpecialRule> specialRules = new ArrayList<>();
}
