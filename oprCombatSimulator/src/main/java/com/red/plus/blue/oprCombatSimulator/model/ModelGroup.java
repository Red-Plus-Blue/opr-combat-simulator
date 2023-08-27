package com.red.plus.blue.oprCombatSimulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class ModelGroup {

    protected int count;
    protected Model model;

    public List<WeaponGroup> getWeaponGroups() {
        return List.of(WeaponGroup.builder()
                .weapon(model.getWeapon())
                .count(count)
                .build());
    }
}
