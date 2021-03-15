package com.stevekung.indicatia.mixin;

import java.util.concurrent.ThreadPoolExecutor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.resources.ResourceLocation;

@Mixin(ServerSelectionList.class)
public interface InvokerServerSelectionList
{
    @Accessor("THREAD_POOL")
    static ThreadPoolExecutor getThreadPool()
    {
        return null;
    }

    @Accessor("ICON_MISSING")
    static ResourceLocation getMissingIcon()
    {
        return null;
    }

    @Accessor("ICON_OVERLAY_LOCATION")
    static ResourceLocation getOverlayIcon()
    {
        return null;
    }
}