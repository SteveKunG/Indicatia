package com.stevekung.indicatia.forge.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class IndicatiaConfig
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final IndicatiaConfig CONFIG;

    public final ForgeConfigSpec.BooleanValue enableAlternateChatKey;
    public final ForgeConfigSpec.BooleanValue displayDurationOnTopRightPotionHUD;
    public final ForgeConfigSpec.BooleanValue confirmationOnDisconnect;
    public final ForgeConfigSpec.BooleanValue enableEnchantedRenderingOnSkull;
    public final ForgeConfigSpec.BooleanValue reloadResourcesButton;

    static
    {
        var specPair = new ForgeConfigSpec.Builder().configure(IndicatiaConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    IndicatiaConfig(ForgeConfigSpec.Builder builder)
    {
        this.enableAlternateChatKey = builder.comment("This allowed to use Numpad Enter key to open the chat.").translation("text.autoconfig.indicatia.option.general.enableAlternateChatKey").define("enableAlternateChatKey", true);
        this.displayDurationOnTopRightPotionHUD = builder.comment("Display potion duration time on top right Potion HUD.").translation("text.autoconfig.indicatia.option.general.displayDurationOnTopRightPotionHUD").define("displayDurationOnTopRightPotionHUD", true);
        this.confirmationOnDisconnect = builder.comment("Enable confirmation screen on pressing disconnect button.").translation("text.autoconfig.indicatia.option.general.confirmationOnDisconnect").define("confirmationOnDisconnect", true);
        this.enableEnchantedRenderingOnSkull = builder.comment("Enable enchant effect rendering on skull.").translation("text.autoconfig.indicatia.option.general.enableEnchantedRenderingOnSkull").define("enableEnchantedRenderingOnSkull", true);
        this.reloadResourcesButton = builder.comment("Enable reload resources button on a pack selection screen.").translation("text.autoconfig.indicatia.option.general.reloadResourcesButton").define("reloadResourcesButton", true);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event)
    {
        LOGGER.info("Loaded config file {}", event.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(ModConfigEvent.Reloading event)
    {
        LOGGER.info("Indicatia config just got changed on the file system");
    }
}