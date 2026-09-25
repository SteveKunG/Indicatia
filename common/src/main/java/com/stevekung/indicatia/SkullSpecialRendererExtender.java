package com.stevekung.indicatia;

import net.minecraft.world.level.block.SkullBlock;

public interface SkullSpecialRendererExtender
{
    default void setKind(SkullBlock.Type type)
    {
        throw new AssertionError("Implemented via mixin");
    }
}