package com.stevekung.indicatia;

import net.minecraft.world.item.component.ResolvableProfile;

public interface PlayerHeadRenderInfoExtender
{
    default void indicatia$setResolvableProfile(ResolvableProfile resolvableProfile)
    {
        throw new AssertionError("Implemented via mixin");
    }

    default ResolvableProfile indicatia$resolvableProfile()
    {
        throw new AssertionError("Implemented via mixin");
    }
}