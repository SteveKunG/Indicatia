package com.stevekung.indicatia.forge;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.config.IndicatiaConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Indicatia.MOD_ID)
public class IndicatiaForge
{
    static
    {
        Indicatia.KEY_ALT_OPEN_CHAT = new KeyMapping("key.alt_open_chat", new IKeyConflictContext()
        {
            @Override
            public boolean isActive()
            {
                return !KeyConflictContext.GUI.isActive();
            }

            @Override
            public boolean conflicts(IKeyConflictContext other)
            {
                return false;
            }
        }, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ENTER, KeyMapping.CATEGORY_MULTIPLAYER);
    }

    public IndicatiaForge()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKey);
        MinecraftForge.EVENT_BUS.register(this);
        Indicatia.initConfig();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> AutoConfig.getConfigScreen(IndicatiaConfig.class, screen).get()));
    }

    private void onRegisterKey(RegisterKeyMappingsEvent event)
    {
        event.register(Indicatia.KEY_ALT_OPEN_CHAT);
    }

    @SubscribeEvent
    public void initScreen(ScreenEvent.Init event)
    {
        var screen = event.getScreen();

        if (Indicatia.canAddReloadButton(screen))
        {
            event.addListener(Indicatia.getReloadResourcesButton(screen, screen.getMinecraft()));
        }
    }
}