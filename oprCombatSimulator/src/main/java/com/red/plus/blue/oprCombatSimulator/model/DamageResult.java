package com.red.plus.blue.oprCombatSimulator.model;

import java.util.List;

public record DamageResult(List<WoundGroup> woundGroups, ModelGroup group, int wounds) {
}

