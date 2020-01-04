package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

public abstract class WorldProviderSpace extends Dimension
{
    public WorldProviderSpace(World world, DimensionType type)
    {
        super(world, type);
    }

    public abstract long getDayLength(); // just used only this method for calculate planet time
    public abstract CelestialBody getCelestialBody();
}