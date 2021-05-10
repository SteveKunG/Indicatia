package com.stevekung.indicatia.handler;

import org.lwjgl.glfw.GLFW;
import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.stevekungslib.client.KeyMappingBase;
import com.stevekung.stevekungslib.utils.client.ClientRegistryUtils;
import net.minecraft.client.KeyMapping;

public class KeyBindingHandler
{
    public static KeyMapping KEY_QUICK_CONFIG;

    public static void init()
    {
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyMappingBase("key.quick_config.desc", GLFW.GLFW_KEY_F4, IndicatiaMod.MOD_ID);
        ClientRegistryUtils.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
    }
}