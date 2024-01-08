package com.stevekung.indicatia;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import com.mojang.logging.LogUtils;

public class IndicatiaMixinConfigPlugin implements IMixinConfigPlugin
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean found3DSkinLayers;

    static
    {
        found3DSkinLayers = findAndDetectModClass("dev.tr7zw.skinlayers.SkinLayersMod", "3D Skin Layers");
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
        if (mixinClassName.equals("com.stevekung.indicatia.mixin.renderer.MixinSkullBlockRenderer_3DSkinLayers"))
        {
            return found3DSkinLayers;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    static boolean findAndDetectModClass(String classPath, String modName)
    {
        var found = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath.replace('.', '/') + ".class") != null;
        LOGGER.info(found ? modName + " detected!" : modName + " not detected!");
        return found;
    }
}