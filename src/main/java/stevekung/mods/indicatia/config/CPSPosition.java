package stevekung.mods.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.math.MathHelper;

public enum CPSPosition
{
    LEFT(0, "indicatia.default"),
    RIGHT(1, "potion_hud.icon_and_time"),
    KEYSTROKE(2, "potion_hud.icon_and_time"),
    CUSTOM(3, "potion_hud.icon_and_time");

    private static final CPSPosition[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(CPSPosition::getId)).toArray(id -> new CPSPosition[id]);
    private final int id;
    private final String key;

    private CPSPosition(int id, String key)
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

    public static CPSPosition byId(int id)
    {
        return VALUES[MathHelper.floorMod(id, VALUES.length)];
    }
}