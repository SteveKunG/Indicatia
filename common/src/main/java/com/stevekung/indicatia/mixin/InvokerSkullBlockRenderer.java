package com.stevekung.indicatia.mixin;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;

@Mixin(SkullBlockRenderer.class)
public interface InvokerSkullBlockRenderer
{
    @Accessor("MODEL_BY_TYPE")
    static Map<SkullBlock.Type, SkullModel> getModelByType()
    {
        return null;
    }

    @Accessor("SKIN_BY_TYPE")
    static Map<SkullBlock.Type, ResourceLocation> getSkinByType()
    {
        return null;
    }

    @Invoker
    static RenderType invokeGetRenderType(SkullBlock.Type type, @Nullable GameProfile gameProfile)
    {
        return null;
    }
}