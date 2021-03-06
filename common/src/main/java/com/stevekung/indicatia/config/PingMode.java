package com.stevekung.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.Mth;

public enum PingMode
{
    PING(0, "indicatia.ping"),
    PING_AND_DELAY(1, "indicatia.ping_and_delay");

    private static final PingMode[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(PingMode::getId)).toArray(PingMode[]::new);
    private final int id;
    private final String key;

    PingMode(int id, String key)
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

    public static PingMode byId(int id)
    {
        return VALUES[Mth.positiveModulo(id, VALUES.length)];
    }
}