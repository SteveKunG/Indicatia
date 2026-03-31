package com.stevekung.indicatia.fabric;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.Indicatia;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.KeyMapping;

public class IndicatiaFabric implements ClientModInitializer
{
    static
    {
        Indicatia.KEY_ALT_OPEN_CHAT = new KeyMapping("key.alt_open_chat", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, KeyMapping.Category.MULTIPLAYER);
    }

    @Override
    public void onInitializeClient()
    {
        Indicatia.initConfig();
        KeyMappingHelper.registerKeyMapping(Indicatia.KEY_ALT_OPEN_CHAT);
        ScreenEvents.AFTER_INIT.register((minecraft, screen, _, _) ->
        {
            if (Indicatia.canAddReloadButton(screen))
            {
                Screens.getWidgets(screen).add(Indicatia.getReloadResourcesButton(screen, minecraft));
            }
        });
    }
}