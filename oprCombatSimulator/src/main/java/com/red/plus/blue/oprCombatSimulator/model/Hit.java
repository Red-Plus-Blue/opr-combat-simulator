package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class Hit {

    protected Roll attackRoll;

    protected Roll defenseRoll;

    protected CombatContext context;

}
