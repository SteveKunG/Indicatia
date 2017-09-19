package stevekung.mods.indicatia.util;

public enum EnumMCVersion
{
    MC_1_7_10("1.7.10"),
    MC_1_8_9("1.8.9"),
    MC_1_10_2("1.10.2"),
    MC_1_11_2("1.11.2"),
    MC_1_12("1.12 1.12.1 1.12.2");

    private String version;
    private static EnumMCVersion[] values = EnumMCVersion.values();

    private EnumMCVersion(String version)
    {
        this.version = version;
    }

    public static EnumMCVersion[] valuesCached()
    {
        return EnumMCVersion.values;
    }

    public String getVersion()
    {
        return this.version;
    }
}