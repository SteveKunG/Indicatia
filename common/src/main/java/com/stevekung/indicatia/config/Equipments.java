package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public class Equipments
{
    public enum Direction
    {
        VERTICAL(0, "equipment.vertical"),
        HORIZONTAL(1, "equipment.horizontal");

        private static final Direction[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Direction::getId)).toArray(Direction[]::new);
        private final int id;
        private final String key;

        Direction(int id, String key)
        {
            this.id = id;
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public int getId()
        {
            return this.id;
        }

        public static Direction byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Status
    {
        DAMAGE_AND_MAX_DAMAGE(0, "equipment.damage_and_max_damage"),
        PERCENT(1, "equipment.percent"),
        DAMAGE(2, "equipment.damage"),
        NONE(3, "indicatia.none"),
        AMOUNT(4, "equipment.amount"),
        AMOUNT_AND_STACK(5, "equipment.amount_and_stack");

        private static final Status[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Status::getId)).toArray(Status[]::new);
        private final int id;
        private final String key;

        Status(int id, String key)
        {
            this.id = id;
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public int getId()
        {
            return this.id;
        }

        public static Status byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Position
    {
        LEFT(0, "indicatia.left"),
        RIGHT(1, "indicatia.right"),
        HOTBAR(2, "indicatia.hotbar");

        private static final Position[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Position::getId)).toArray(Position[]::new);
        private final int id;
        private final String key;

        Position(int id, String key)
        {
            this.id = id;
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public int getId()
        {
            return this.id;
        }

        public static Position byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }
}