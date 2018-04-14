package stevekung.mods.indicatia.config;

public enum KeystrokePosition
{
    RIGHT, LEFT;

    private static final KeystrokePosition[] values = values();

    public static String getById(int mode)
    {
        return values[mode].toString().toLowerCase();
    }
}