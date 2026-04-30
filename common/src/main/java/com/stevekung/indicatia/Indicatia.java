package com.stevekung.indicatia;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.utils.FlashbackHelper;
import com.stevekung.indicatia.utils.Platform;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.GameType;

public class Indicatia
{
    public static final String MOD_ID = "indicatia";
    public static KeyMapping KEY_ALT_OPEN_CHAT;
    public static IndicatiaConfig CONFIG;

    public static final boolean isFlashbackLoaded = Platform.isModLoaded("flashback");
    private static final WidgetSprites RELOAD_BUTTON_SPRITES = new WidgetSprites(Identifier.fromNamespaceAndPath(MOD_ID, "widget/reload"), Identifier.fromNamespaceAndPath(MOD_ID, "widget/reload_highlighted"));
    private static final Component RELOAD_COMPONENT = Component.translatable("menu.reload_resources");
    private static final Identifier PHANTOM_INDICATOR_SPRITE = Indicatia.id("hud/phantom_indicator");
    private static final Identifier PHANTOM_INDICATOR_OVERLAY_SPRITE = Indicatia.id("hud/phantom_indicator_overlay");

    public static void initConfig()
    {
        AutoConfig.register(IndicatiaConfig.class, GsonConfigSerializer::new);
        Indicatia.CONFIG = AutoConfig.getConfigHolder(IndicatiaConfig.class).getConfig();
    }

    public static boolean canAddReloadButton(Screen screen)
    {
        return Indicatia.CONFIG.enableReloadResourcesButton && screen instanceof PackSelectionScreen && screen.getTitle().equals(Component.translatable("resourcePack.title"));
    }

    public static ImageButton getReloadResourcesButton(Screen screen, Minecraft minecraft)
    {
        var imageButton = new ImageButton(screen.width / 2 + 155, screen.height - 26, 20, 20, RELOAD_BUTTON_SPRITES, _ -> minecraft.reloadResourcePacks(), Indicatia.RELOAD_COMPONENT);
        imageButton.setTooltip(Tooltip.create(RELOAD_COMPONENT));
        return imageButton;
    }

    public static void renderPhantomIndicator(GuiGraphicsExtractor guiGraphics, Minecraft minecraft)
    {
        if (Indicatia.CONFIG.phantomIndicator && (FlashbackHelper.isShowHotbar() || minecraft.gameMode.getPlayerMode() == GameType.SURVIVAL))
        {
            var width = guiGraphics.guiWidth() / 2;
            var timeSinceRest = minecraft.player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            var phantomStartSpawnTick = 72000;
            var textureSize = 18;
            var v = textureSize - (int) (textureSize * (1.0F - (float) Math.min(timeSinceRest, phantomStartSpawnTick) / phantomStartSpawnTick));

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PHANTOM_INDICATOR_SPRITE, textureSize, textureSize, 0, textureSize - v, width - 120, guiGraphics.guiHeight() - 28 - v, textureSize, v);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PHANTOM_INDICATOR_OVERLAY_SPRITE, textureSize, textureSize, 0, 0, width - 120, guiGraphics.guiHeight() - 46, textureSize, textureSize);
        }
    }

    public static Identifier id(String path)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}