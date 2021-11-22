package com.stevekung.indicatia.handler;

import org.lwjgl.glfw.GLFW;
import com.stevekung.indicatia.core.Indicatia;
import com.stevekung.stevekunglib.client.KeyMappingBase;
import com.stevekung.stevekunglib.utils.client.ClientRegistryUtils;
import net.minecraft.client.KeyMapping;

public class KeyBindingHandler
{
    public static KeyMapping KEY_QUICK_CONFIG;
    public static KeyMapping KEY_PREVIOUS_PROFILE;
    public static KeyMapping KEY_NEXT_PROFILE;

    public static void init()
    {
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyMappingBase("key.quick_config", GLFW.GLFW_KEY_F4, Indicatia.MOD_ID);
        KeyBindingHandler.KEY_PREVIOUS_PROFILE = new KeyMappingBase("key.previous_profile", GLFW.GLFW_KEY_LEFT_BRACKET, Indicatia.MOD_ID);
        KeyBindingHandler.KEY_NEXT_PROFILE = new KeyMappingBase("key.next_profile", GLFW.GLFW_KEY_RIGHT_BRACKET, Indicatia.MOD_ID);
        ClientRegistryUtils.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
        ClientRegistryUtils.registerKeyBinding(KeyBindingHandler.KEY_PREVIOUS_PROFILE);
        ClientRegistryUtils.registerKeyBinding(KeyBindingHandler.KEY_NEXT_PROFILE);
        ClientRegistryUtils.registerKeyBinding(Indicatia.KEY_ALT_OPEN_CHAT);
    }
}