package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import net.minecraft.world.dimension.Dimension;

public abstract class WorldProviderSpace extends Dimension
{
    public abstract long getDayLength(); // just used only this method for calculate planet time
}