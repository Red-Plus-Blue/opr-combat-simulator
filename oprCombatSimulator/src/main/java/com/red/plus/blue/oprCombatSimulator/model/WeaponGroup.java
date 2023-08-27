package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeaponGroup {
    protected int count;
    protected Weapon weapon;
}
