package com.stevekung.indicatia.core;

import com.stevekung.indicatia.config.IndicatiaConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class Indicatia
{
    public static final String MOD_ID = "indicatia";
    public static KeyMapping KEY_ALT_OPEN_CHAT;
    public static IndicatiaConfig CONFIG;

    private static final ResourceLocation RELOAD_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/reload.png");
    private static final Component RELOAD_COMPONENT = new TranslatableComponent("menu.reload_resources");

    public static void initConfig()
    {
        AutoConfig.register(IndicatiaConfig.class, GsonConfigSerializer::new);
        Indicatia.CONFIG = AutoConfig.getConfigHolder(IndicatiaConfig.class).getConfig();
    }

    public static ImageButton getReloadResourcesButton(Screen screen, Minecraft minecraft)
    {
        return new ImageButton(screen.width / 2 + 155, screen.height - 48, 20, 20, 0, 0, 20, Indicatia.RELOAD_TEXTURE, 32, 64, button -> minecraft.reloadResourcePacks(), (button, poseStack, x, y) -> screen.renderTooltip(poseStack, Indicatia.RELOAD_COMPONENT, x, y), TextComponent.EMPTY);
    }
}