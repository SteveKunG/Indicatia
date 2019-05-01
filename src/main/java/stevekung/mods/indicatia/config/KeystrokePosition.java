package stevekung.mods.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.math.MathHelper;

public enum KeystrokePosition
{
    LEFT(0, "indicatia.left"),
    RIGHT(1, "indicatia.right");

    private static final KeystrokePosition[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(KeystrokePosition::getId)).toArray(id -> new KeystrokePosition[id]);
    private final int id;
    private final String key;

    private KeystrokePosition(int id, String key)
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

    public static KeystrokePosition byId(int id)
    {
        return VALUES[MathHelper.floorMod(id, VALUES.length)];
    }
}