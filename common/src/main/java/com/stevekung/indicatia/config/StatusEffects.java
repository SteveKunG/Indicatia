package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public class StatusEffects
{
    public enum Style
    {
        DEFAULT("indicatia.default"),
        ICON_AND_TIME("potion_hud.icon_and_time");

        private static final Style[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(Style::ordinal)).toArray(Style[]::new);
        private final String key;

        Style(String key)
        {
            this.key = key;
        }

        public String getTranslationKey()
        {
            return this.key;
        }

        public static Style byId(int id)
        {
            return VALUES[Mth.positiveModulo(id, VALUES.length)];
        }
    }

    public enum Position
    {
        LEFT("indicatia.left"),
        RIGHT("indicatia.right"),
        HOTBAR_LEFT("indicatia.hotbar_left"),
        HOTBAR_RIGHT("indicatia.hotbar_right");

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