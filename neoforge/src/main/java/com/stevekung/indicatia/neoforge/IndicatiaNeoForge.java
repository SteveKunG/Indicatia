package com.stevekung.indicatia.neoforge;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.config.IndicatiaConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Indicatia.MOD_ID)
public class IndicatiaNeoForge
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

    public IndicatiaNeoForge()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKey);
        NeoForge.EVENT_BUS.register(this);
        Indicatia.initConfig();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> AutoConfig.getConfigScreen(IndicatiaConfig.class, screen).get()));
    }

    private void onRegisterKey(RegisterKeyMappingsEvent event)
    {
        event.register(Indicatia.KEY_ALT_OPEN_CHAT);
    }

    @SubscribeEvent
    public void initScreen(ScreenEvent.Init.Pre event)
    {
        var screen = event.getScreen();

        if (Indicatia.canAddReloadButton(screen))
        {
            event.addListener(Indicatia.getReloadResourcesButton(screen, screen.getMinecraft()));
        }
    }
}