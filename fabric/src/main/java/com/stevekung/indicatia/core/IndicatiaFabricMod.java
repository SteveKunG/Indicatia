package com.stevekung.indicatia.core;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.command.*;
import com.stevekung.indicatia.command.config.ConfigHandlerIN;
import com.stevekung.indicatia.gui.exconfig.screen.ExtendedConfigScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;

public class IndicatiaFabricMod implements ClientModInitializer
{
    public static final ConfigHandlerIN CONFIG = new ConfigHandlerIN();

    @Override
    public void onInitializeClient()
    {
        IndicatiaMod.init();
        IndicatiaMod.isGalacticraftLoaded = FabricLoader.getInstance().isModLoaded("galacticraftcore");
        IndicatiaMod.keyBindAltChat = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.chatAlt", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, "key.categories.multiplayer"));

        new AFKCommand(ClientCommandManager.DISPATCHER);
        new AutoFishCommand(ClientCommandManager.DISPATCHER);
        new MojangStatusCheckCommand(ClientCommandManager.DISPATCHER);
        new PingAllCommand(ClientCommandManager.DISPATCHER);
        new ProfileCommand(ClientCommandManager.DISPATCHER);
        new SlimeSeedCommand(ClientCommandManager.DISPATCHER);

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (KeyBindingHandler.KEY_QUICK_CONFIG.consumeClick())
            {
                client.setScreen(new ExtendedConfigScreen());
            }
        });
    }
}