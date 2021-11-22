package com.stevekung.indicatia.fabric.mixin.server;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.google.common.collect.Maps;
import com.stevekung.indicatia.fabric.utils.IMinecraftServerTick;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements IMinecraftServerTick
{
    @Unique
    final Map<ResourceKey<Level>, long[]> perWorldTickTimes = Maps.newIdentityHashMap();

    @Unique
    long tickStart;

    @Shadow
    int tickCount;

    @Inject(method = "tickChildren", at = @At(value = "INVOKE", target = "net/minecraft/util/profiling/ProfilerFiller.push(Ljava/util/function/Supplier;)V", shift = At.Shift.AFTER))
    private void indicatia$addStartTickTime(BooleanSupplier supplier, CallbackInfo info)
    {
        this.tickStart = Util.getNanos();
    }

    @Inject(method = "tickChildren", at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerLevel.tick(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$addGetTickTime(BooleanSupplier supplier, CallbackInfo info, Iterator<?> iterator, ServerLevel serverLevel)
    {
        this.perWorldTickTimes.computeIfAbsent(serverLevel.dimension(), k -> new long[100])[this.tickCount % 100] = Util.getNanos() - this.tickStart;
    }

    @Override
    public long[] getTickTime(ResourceKey<Level> dim)
    {
        return this.perWorldTickTimes.get(dim);
    }
}