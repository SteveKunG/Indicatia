package com.stevekung.indicatia.handler;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.stevekungslib.keybinding.KeyBindingBase;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindingHandler
{
    public static KeyBinding KEY_QUICK_CONFIG;

    public static void init()
    {
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyBindingBase("key.quick_config.desc", IndicatiaMod.MOD_ID, GLFW.GLFW_KEY_F4);

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
    }
}