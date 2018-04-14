package stevekung.mods.indicatia.config;

public enum HealthStatusMode
{
    DISABLED, ALWAYS, POINTED;

    private static final HealthStatusMode[] values = values();

    public static String getById(int mode)
    {
        return values[mode].toString().toLowerCase();
    }
}