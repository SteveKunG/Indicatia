package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import net.minecraft.world.WorldProvider;

public abstract class WorldProviderSpace extends WorldProvider
{
    public abstract long getDayLength(); // just used only this method for calculate planet time
}