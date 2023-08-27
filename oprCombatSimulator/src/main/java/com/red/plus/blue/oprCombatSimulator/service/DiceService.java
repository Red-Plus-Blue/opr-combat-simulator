package com.red.plus.blue.oprCombatSimulator.service;

import com.red.plus.blue.oprCombatSimulator.model.Roll;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DiceService {

    protected final Random random;

    public DiceService() {
        random = new Random();
    }

    public Roll d6() {
        final var value = 1 + (random.nextInt(6));
        return Roll.builder()
                .value(value)
                .build();
    }
}
