package com.stevekung.indicatia.forge.core;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.core.Indicatia;
import com.stevekung.indicatia.forge.command.*;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.forge.event.HUDRenderEventHandler;
import com.stevekung.indicatia.forge.event.IndicatiaEventHandler;
import com.stevekung.indicatia.forge.key.KeypadChatKey;
import com.stevekung.stevekunglib.forge.client.ForgeKeyMappingBase;
import com.stevekung.stevekunglib.forge.utils.ForgeCommonUtils;
import com.stevekung.stevekunglib.forge.utils.ModVersionChecker;
import com.stevekung.stevekunglib.forge.utils.client.command.ClientCommands;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(Indicatia.MOD_ID)
public class IndicatiaForge
{
    public static final ModVersionChecker CHECKER = new ModVersionChecker(Indicatia.MOD_ID);

    static
    {
        Indicatia.KEY_ALT_OPEN_CHAT = new ForgeKeyMappingBase("key.alt_open_chat", new KeypadChatKey(), KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, "multiplayer");
    }

    public IndicatiaForge()
    {
        EventBuses.registerModEventBus(Indicatia.MOD_ID, ForgeCommonUtils.getModEventBus());
        Indicatia.init();
        ForgeCommonUtils.registerClientOnly();
        ForgeCommonUtils.addModListener(this::phaseOne);
        ForgeCommonUtils.addModListener(this::loadComplete);

        ForgeCommonUtils.registerConfig(ModConfig.Type.CLIENT, IndicatiaConfig.GENERAL_SPEC);
        ForgeCommonUtils.registerConfigScreen((mc, parent) -> ForgeCommonUtils.openConfigFile(parent, Indicatia.MOD_ID, ModConfig.Type.CLIENT));
        ForgeCommonUtils.registerModEventBus(IndicatiaConfig.class);
        ForgeCommonUtils.registerEventHandler(new IndicatiaEventHandler());
        ForgeCommonUtils.registerEventHandler(new HUDRenderEventHandler());
    }

    private void phaseOne(FMLClientSetupEvent event)
    {
        this.registerClientCommands();
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
        {
            IndicatiaForge.CHECKER.startCheck();
        }
    }

    private void registerClientCommands()
    {
        ClientCommands.register(new AFKCommand());
        ClientCommands.register(new AutoFishCommand());
        ClientCommands.register(new PingAllCommand());
        ClientCommands.register(new ProfileCommand());
        ClientCommands.register(new SlimeSeedCommand());
        Indicatia.LOGGER.info("Registering client side commands");
    }
}