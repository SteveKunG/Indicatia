package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;

import com.stevekung.indicatia.PlayerHeadRenderInfoExtender;

import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
import net.minecraft.world.item.component.ResolvableProfile;

@Mixin(PlayerHeadSpecialRenderer.PlayerHeadRenderInfo.class)
public class MixinPlayerHeadRenderInfo implements PlayerHeadRenderInfoExtender
{
    private ResolvableProfile resolvableProfile;

    @Override
    public void indicatia$setResolvableProfile(ResolvableProfile resolvableProfile)
    {
        this.resolvableProfile = resolvableProfile;
    }

    @Override
    public ResolvableProfile indicatia$resolvableProfile()
    {
        return this.resolvableProfile;
    }
}