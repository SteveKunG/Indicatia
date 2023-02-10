package com.stevekung.indicatia.fabric;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.Indicatia;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.KeyMapping;

public class IndicatiaFabric implements ClientModInitializer
{
    static
    {
        Indicatia.KEY_ALT_OPEN_CHAT = new KeyMapping("key.alt_open_chat", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, KeyMapping.CATEGORY_MULTIPLAYER);
    }

    @Override
    public void onInitializeClient()
    {
        Indicatia.initConfig();
        KeyBindingHelper.registerKeyBinding(Indicatia.KEY_ALT_OPEN_CHAT);
        ScreenEvents.AFTER_INIT.register((minecraft, screen, scaledWidth, scaledHeight) ->
        {
            if (Indicatia.canAddReloadButton(screen))
            {
                Screens.getButtons(screen).add(Indicatia.getReloadResourcesButton(screen, minecraft));
            }
        });
    }
}