package com.stevekung.indicatia.handler;

import org.lwjgl.glfw.GLFW;
import com.stevekung.indicatia.core.IndicatiaMod;
import me.shedaniel.architectury.registry.KeyBindings;
import net.minecraft.client.KeyMapping;

public class KeyBindingHandler
{
    public static KeyMapping KEY_QUICK_CONFIG;

    public static void init()
    {
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyMapping("key.quick_config.desc", GLFW.GLFW_KEY_F4, IndicatiaMod.MOD_ID);

        KeyBindings.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
    }
}