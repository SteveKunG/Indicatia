package com.stevekung.indicatia;

import com.stevekung.indicatia.config.IndicatiaConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Indicatia
{
    public static final String MOD_ID = "indicatia";
    public static KeyMapping KEY_ALT_OPEN_CHAT;
    public static IndicatiaConfig CONFIG;

    private static final ResourceLocation RELOAD_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/reload.png");
    private static final Component RELOAD_COMPONENT = Component.translatable("menu.reload_resources");

    public static void initConfig()
    {
        AutoConfig.register(IndicatiaConfig.class, GsonConfigSerializer::new);
        Indicatia.CONFIG = AutoConfig.getConfigHolder(IndicatiaConfig.class).getConfig();
    }

    public static boolean canAddReloadButton(Screen screen)
    {
        return Indicatia.CONFIG.reloadResourcesButton && screen instanceof PackSelectionScreen && screen.getTitle().equals(Component.translatable("resourcePack.title"));
    }

    public static ImageButton getReloadResourcesButton(Screen screen, Minecraft minecraft)
    {
        var imageButton = new ImageButton(screen.width / 2 + 155, screen.height - 48, 20, 20, 0, 0, 20, Indicatia.RELOAD_TEXTURE, 32, 64, button -> minecraft.reloadResourcePacks(), Indicatia.RELOAD_COMPONENT);
        imageButton.setTooltip(Tooltip.create(RELOAD_COMPONENT));
        return imageButton;
    }
}