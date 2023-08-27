package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class AttackService {

    @Autowired
    protected DiceService diceService;

    @Autowired
    protected SpecialRulesService specialRulesService;

    public List<WoundGroup> attack(final Unit attacker, final Unit defender) {
        final Function<WeaponGroup, Stream<WoundGroup>> attackWithGroup = weaponGroup -> this.attackWithWeaponGroup(attacker, defender, weaponGroup);
        return attacker.getWeaponGroups().stream()
                .flatMap(attackWithGroup)
                .toList();
    }

    public Stream<WoundGroup> attackWithWeaponGroup(final Unit attacker, final Unit defender, WeaponGroup weaponGroup) {
        final var weapon = weaponGroup.getWeapon();
        final var specialRules = weapon.getSpecialRules();
        final var attacks = weaponGroup.getCount() * weapon.getAttacks();
        final Function<Roll, RollInformation> toRollInformation = roll -> new RollInformation(roll, attacker, defender);

        return IntStream.range(0, attacks)
                .mapToObj(__ -> diceService.d6())
                // remove misses
                .filter(attacker::isHit)
                .map(toRollInformation)
                // apply hit multipliers (blast, poison, etc.)
                .flatMap(information -> specialRulesService.applyHitMultipliers(weapon.getSpecialRules(), information))
                .map(__ -> diceService.d6())
                .map(toRollInformation)
                .map(information -> specialRulesService.applyDefenseModifiers(specialRules, information))
                // remove blocks
                .filter(roll -> !defender.isBlock(roll))
                .map(toRollInformation)
                // apply wound multipliers (deadly)
                .map(information -> specialRulesService.applyWoundMultipliers(weapon.getSpecialRules(), information));
    }
}
