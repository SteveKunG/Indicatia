package stevekung.mods.indicatia.config;

public enum CPSPosition
{
    LEFT, RIGHT, KEYSTROKE, CUSTOM;

    private static final CPSPosition[] values = values();

    public static String getById(int mode)
    {
        return values[mode].toString().toLowerCase();
    }
}