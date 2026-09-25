package com.stevekung.indicatia.mixin.accessor;

import java.util.Map;

import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkullBlockRenderer.class)
public interface SkullBlockRendererAccessor
{
    @Accessor("SKIN_BY_TYPE")
    static Map<SkullBlock.Type, Identifier> getSkinByType()
    {
        throw new AssertionError("Implemented via mixin");
    }
}