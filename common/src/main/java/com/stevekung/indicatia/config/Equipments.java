package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public class Equipments
{
    public enum Direction
    {
        VERTICAL("equipment.vertical"),
        HORIZONTAL("equipment.horizontal");

        private static final Direction[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Direction::ordinal)).toArray(Direction[]::new);
        private final String key;

        Direction(String key)
        {
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public static Direction byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Status
    {
        DAMAGE_AND_MAX_DAMAGE("equipment.damage_and_max_damage"),
        PERCENT("equipment.percent"),
        DAMAGE("equipment.damage"),
        NONE("indicatia.none"),
        AMOUNT("equipment.amount"),
        AMOUNT_AND_STACK("equipment.amount_and_stack");

        private static final Status[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Status::ordinal)).toArray(Status[]::new);
        private final String key;

        Status(String key)
        {
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public static Status byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Position
    {
        LEFT("indicatia.left"),
        RIGHT("indicatia.right"),
        HOTBAR("indicatia.hotbar");

        private static final Position[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Position::ordinal)).toArray(Position[]::new);
        private final String key;

        Position(String key)
        {
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public static Position byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }
}