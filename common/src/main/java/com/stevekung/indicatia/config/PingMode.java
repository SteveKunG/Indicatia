package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public enum PingMode
{
    PING("indicatia.ping"),
    PING_AND_DELAY("indicatia.ping_and_delay");

    private static final PingMode[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(PingMode::ordinal)).toArray(PingMode[]::new);
    private final String key;

    PingMode(String key)
    {
        this.key = key;
    }

    public String getTranslationKey()
    {
        return this.key;
    }

    public static PingMode byId(int id)
    {
        return VALUES[Mth.positiveModulo(id, VALUES.length)];
    }
}