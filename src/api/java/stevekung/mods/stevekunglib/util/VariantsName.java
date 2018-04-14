package stevekung.mods.stevekunglib.util;

@Deprecated //TODO Remove in 1.13
public class VariantsName
{
    private final String[] name;

    public VariantsName(String... name)
    {
        this.name = name;
    }

    public String[] getNameList()
    {
        return this.name;
    }
}