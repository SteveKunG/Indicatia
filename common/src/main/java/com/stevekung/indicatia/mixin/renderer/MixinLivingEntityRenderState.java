package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import com.stevekung.indicatia.LivingEntityRenderStateExtender;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(LivingEntityRenderState.class)
public class MixinLivingEntityRenderState implements LivingEntityRenderStateExtender
{
    @Unique
    private boolean glint;

    @Unique
    private boolean playerHead;

    @Override
    public void indicatia$setPlayerHead(boolean playerHead)
    {
        this.playerHead = playerHead;
    }

    @Override
    public boolean indicatia$isPlayerHead()
    {
        return this.playerHead;
    }

    @Override
    public void indicatia$setHeadGlint(boolean glint)
    {
        this.glint = glint;
    }

    @Override
    public boolean indicatia$hasHeadGlint()
    {
        return this.glint;
    }
}