package com.stevekung.indicatia.handler;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.stevekungslib.keybinding.KeyBindingBase;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindingHandler
{
    public static KeyBinding KEY_TOGGLE_SPRINT;
    public static KeyBinding KEY_TOGGLE_SNEAK;
    public static KeyBinding KEY_QUICK_CONFIG;

    public static void init()
    {
        KeyBindingHandler.KEY_TOGGLE_SPRINT = new KeyBindingBase("key.toggle_sprint.desc", IndicatiaMod.MOD_ID, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_S));
        KeyBindingHandler.KEY_TOGGLE_SNEAK = new KeyBindingBase("key.toggle_sneak.desc", IndicatiaMod.MOD_ID, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_LEFT_SHIFT));
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyBinding("key.quick_config.desc", GLFW.GLFW_KEY_F4, "key.indicatia.category");

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SPRINT);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SNEAK);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
    }
}