package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Model {
    @Builder.Default
    protected int tough = 1;
    protected Weapon weapon;
}
