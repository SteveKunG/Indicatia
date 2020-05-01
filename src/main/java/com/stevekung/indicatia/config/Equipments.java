package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.math.MathHelper;

public class Equipments
{
    public enum Ordering
    {
        DEFAULT(0, "indicatia.default"),
        REVERSE(1, "equipment.reverse");

        private static final Ordering[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Ordering::getId)).toArray(id -> new Ordering[id]);
        private final int id;
        private final String key;

        private Ordering(int id, String key)
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

        public static Ordering byId(int id)
        {
            return VALUES[MathHelper.floorMod(id, VALUES.length)];
        }
    }

    public enum Direction
    {
        VERTICAL(0, "equipment.vertical"),
        HORIZONTAL(1, "equipment.horizontal");

        private static final Direction[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Direction::getId)).toArray(id -> new Direction[id]);
        private final int id;
        private final String key;

        private Direction(int id, String key)
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
            return VALUES[MathHelper.floorMod(id, VALUES.length)];
        }
    }

    public enum Status
    {
        DAMAGE_AND_MAX_DAMAGE(0, "equipment.damage_and_max_damage"),
        PERCENT(1, "equipment.percent"),
        ONLY_DAMAGE(2, "equipment.only_damage"),
        NONE(3, "indicatia.none");

        private static final Status[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Status::getId)).toArray(id -> new Status[id]);
        private final int id;
        private final String key;

        private Status(int id, String key)
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
            return VALUES[MathHelper.floorMod(id, VALUES.length)];
        }
    }

    public enum Position
    {
        LEFT(0, "indicatia.left"),
        RIGHT(1, "indicatia.right"),
        HOTBAR(2, "indicatia.hotbar");

        private static final Position[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Position::getId)).toArray(id -> new Position[id]);
        private final int id;
        private final String key;

        private Position(int id, String key)
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
            return VALUES[MathHelper.floorMod(id, VALUES.length)];
        }
    }
}