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
        // TODO: at some point probably need to include special rules from the unit/model
        final var specialRules = weapon.getSpecialRules();
        final var context = new CombatContext(attacker, defender);
        final Function<Roll, Hit> toHit = roll -> Hit.builder()
                .context(context)
                .attackRoll(roll)
                .build();

        return getHits(attacker, weaponGroup)
                .map(toHit)
                // apply hit multipliers (ex. blast, v2.5 poison, etc.)
                .flatMap(hit -> specialRulesService.applyHitMultipliers(weapon.getSpecialRules(), hit))
                .map(hit -> hit.toBuilder()
                        .defenseRoll(diceService.d6())
                        .build())
                // apply re-rolls to the defender (ex. v3 poison)
                .map(hit -> specialRulesService.applyDefenseReRolls(specialRules, hit))
                // apply modifiers to the defender (ex. ap)
                .map(hit -> specialRulesService.applyDefenseModifiers(specialRules, hit))
                // remove blocks
                .filter(hit -> !defender.isBlock(hit))
                // apply wound multipliers (ex. deadly)
                .map(hit -> specialRulesService.applyWoundMultipliers(weapon.getSpecialRules(), hit));
    }

    public Stream<Roll> getHits(final Unit attacker, WeaponGroup weaponGroup) {
        final var weapon = weaponGroup.getWeapon();
        final var attacks = weaponGroup.getCount() * weapon.getAttacks();

        return IntStream.range(0, attacks)
                .mapToObj(__ -> diceService.d6())
                // remove misses
                .filter(attacker::isHit);
    }
}
