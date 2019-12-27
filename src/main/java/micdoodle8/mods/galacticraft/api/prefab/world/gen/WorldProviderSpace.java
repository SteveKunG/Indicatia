package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

public abstract class WorldProviderSpace extends Dimension
{
    public WorldProviderSpace(World world, DimensionType type, float light)
    {
        super(world, type, light);
    }

    public abstract long getDayLength(); // just used only this method for calculate planet time
}