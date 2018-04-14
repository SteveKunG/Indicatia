package stevekung.mods.stevekunglib.util;

public enum EnumToolSpeed
{
    WOOD(-3.2F),
    COMMON(-3.0F);

    private final float speed;

    private EnumToolSpeed(float speed)
    {
        this.speed = speed;
    }

    public float getSpeed()
    {
        return this.speed;
    }
}