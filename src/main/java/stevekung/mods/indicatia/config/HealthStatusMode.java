package stevekung.mods.indicatia.config;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.math.MathHelper;

public enum HealthStatusMode
{
    DISABLED(0, "indicatia.disabled"),
    ALWAYS(1, "health_status.always"),
    POINTED(2, "health_status.pointed");

    private static final HealthStatusMode[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(HealthStatusMode::getId)).toArray(id -> new HealthStatusMode[id]);
    private final int id;
    private final String key;

    private HealthStatusMode(int id, String key)
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

    public static HealthStatusMode byId(int id)
    {
        return VALUES[MathHelper.intFloorDiv(id, VALUES.length)];
    }
}