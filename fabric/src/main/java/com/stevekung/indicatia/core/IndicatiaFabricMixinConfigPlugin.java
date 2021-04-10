package com.stevekung.indicatia.core;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import com.google.common.collect.Lists;
import com.stevekung.stevekungslib.utils.LoggerBase;

public class IndicatiaFabricMixinConfigPlugin implements IMixinConfigPlugin
{
    static final LoggerBase LOGGER = new LoggerBase("Indicatia:Fabric MixinConfig");
    static boolean foundOptifine;

    static
    {
        foundOptifine = findAndDetectModClass("me/modmuss50/optifabric/mod/OptifabricSetup.class", "OptiFabric");
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins()
    {
        List<String> mixins = Lists.newArrayList();

        if (!foundOptifine)
        {
            mixins.add("renderer.entity.layers.MixinHumanoidArmorLayer");
        }
        return mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static boolean findAndDetectModClass(String classPath, String modName)
    {
        boolean found = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath) != null;
        LOGGER.info(found ? modName + " detected!" : modName + " not detected!");
        return found;
    }
}