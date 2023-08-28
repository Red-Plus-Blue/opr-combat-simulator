package com.red.plus.blue.oprCombatSimulator.model;

import com.red.plus.blue.oprCombatSimulator.util.$;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@Builder(toBuilder = true, builderMethodName = "$hiddenBuilder", builderClassName = "UnitBuilder")
public class Unit {

    @Builder.Default
    protected String name = "Unit";
    @Builder.Default
    protected int quality = 3;
    @Builder.Default
    protected int defense = 3;
    @Builder.Default
    protected int wounds = 0;
    @Builder.Default
    protected List<ModelGroup> groups = new ArrayList<>();

    public static CustomUnitBuilder builder() {
        return new CustomUnitBuilder();
    }

    protected void postConstruct() {
        groups = groups.stream()
                .sorted(Comparator.comparingInt(group -> group.getModel().getTough()))
                .toList();
    }

    public Unit takeDamage(int amount) {
        final var wound = WoundGroup.builder().count(1).build();
        final var woundGroups = IntStream.range(0, amount).mapToObj(__ -> wound).toList();
        return takeDamage(woundGroups);
    }

    public Unit takeDamage(List<WoundGroup> woundGroups) {
        final var modelGroup = this.groups.stream()
                .findFirst()
                .orElse(null);

        if (modelGroup == null || CollectionUtils.isEmpty(woundGroups)) {
            return this;
        }

        final var damageResult = computeDamageResult(woundGroups, modelGroup, this.wounds);
        final var groups = damageResult.group().getCount() > 0 ?
                Stream.concat(Stream.of(damageResult.group()), this.groups.stream().skip(1)).toList() :
                $.tail(this.groups);

        return this.toBuilder()
                .groups(groups)
                .wounds(damageResult.wounds())
                .build()
                .takeDamage(damageResult.woundGroups());
    }

    public DamageResult computeDamageResult(List<WoundGroup> woundGroups, ModelGroup modelGroup, int wounds) {
        if (CollectionUtils.isEmpty(woundGroups) || modelGroup.getCount() <= 0) {
            return new DamageResult(woundGroups, modelGroup, wounds);
        }
        if (wounds + $.head(woundGroups).getCount() >= modelGroup.getModel().getTough()) {
            return computeDamageResult(
                    $.tail(woundGroups),
                    modelGroup.toBuilder().count(modelGroup.getCount() - 1).build(),
                    0
            );
        }
        return computeDamageResult($.tail(woundGroups), modelGroup, $.head(woundGroups).getCount() + wounds);
    }

    public boolean isHit(final Roll roll) {
        if (roll.isNatural6()) return true;
        if (roll.isNatural1()) return false;
        return roll.getModifiedValue() >= quality;
    }

    public boolean isBlock(final Roll roll) {
        if (roll.isNatural6()) return true;
        if (roll.isNatural1()) return false;
        return roll.getModifiedValue() >= defense;
    }

    public boolean isAlive() {
        return this.getModelCount() > 0;
    }

    public List<WeaponGroup> getWeaponGroups() {
        return groups.stream()
                .flatMap(group -> group.getWeaponGroups().stream())
                .toList();
    }

    public int getModelCount() {
        return this.groups.stream().mapToInt(ModelGroup::getCount).sum();
    }

    public static class CustomUnitBuilder extends UnitBuilder {

        @Override
        public Unit build() {
            Unit unit = super.build();
            unit.postConstruct();
            return unit;
        }
    }
}
