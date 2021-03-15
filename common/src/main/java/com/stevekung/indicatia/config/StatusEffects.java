package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public class StatusEffects
{
    public enum Style
    {
        DEFAULT(0, "indicatia.default"),
        ICON_AND_TIME(1, "potion_hud.icon_and_time");

        private static final Style[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Style::getId)).toArray(Style[]::new);
        private final int id;
        private final String key;

        Style(int id, String key)
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

        public static Style byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Position
    {
        LEFT(0, "indicatia.left"),
        RIGHT(1, "indicatia.right"),
        HOTBAR_LEFT(2, "indicatia.hotbar_left"),
        HOTBAR_RIGHT(3, "indicatia.hotbar_right");

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