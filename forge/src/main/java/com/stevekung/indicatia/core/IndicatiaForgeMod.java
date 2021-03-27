package com.stevekung.indicatia.core;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.command.*;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.event.HUDRenderEventHandler;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.key.KeypadChatKey;
import com.stevekung.stevekungslib.utils.ForgeCommonUtils;
import com.stevekung.stevekungslib.utils.ModVersionChecker;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(IndicatiaMod.MOD_ID)
public class IndicatiaForgeMod
{
    public static final ModVersionChecker CHECKER = new ModVersionChecker(IndicatiaMod.MOD_ID);

    static
    {
        IndicatiaMod.keyBindAltChat = new KeyMapping("key.chatAlt", new KeypadChatKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, "key.categories.multiplayer");
    }

    public IndicatiaForgeMod()
    {
        EventBuses.registerModEventBus(IndicatiaMod.MOD_ID, ForgeCommonUtils.getModEventBus());
        IndicatiaMod.init();
        ForgeCommonUtils.addModListener(this::phaseOne);
        ForgeCommonUtils.addModListener(this::loadComplete);

        ForgeCommonUtils.registerConfig(ModConfig.Type.CLIENT, IndicatiaConfig.GENERAL_BUILDER);
        ForgeCommonUtils.registerModEventBus(IndicatiaConfig.class);
        ForgeCommonUtils.registerEventHandler(new IndicatiaEventHandler());
        ForgeCommonUtils.registerEventHandler(new HUDRenderEventHandler());

        IndicatiaMod.isGalacticraftLoaded = ModList.get().isLoaded("galacticraftcore");
    }

    private void phaseOne(FMLClientSetupEvent event)
    {
        this.registerClientCommands();
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
        {
            IndicatiaForgeMod.CHECKER.startCheck();
        }
    }

    private void registerClientCommands()
    {
        ClientCommands.register(new AFKCommand());
        ClientCommands.register(new AutoFishCommand());
        ClientCommands.register(new MojangStatusCheckCommand());
        ClientCommands.register(new PingAllCommand());
        ClientCommands.register(new ProfileCommand());
        ClientCommands.register(new SlimeSeedCommand());
        IndicatiaMod.LOGGER.info("Registering client side commands");
    }
}