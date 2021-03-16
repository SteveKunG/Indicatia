package com.stevekung.indicatia.modmenu;

import java.io.IOException;

import com.stevekung.indicatia.command.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaFabricMod;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegrationIN implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen screen)
    {
        IndicatiaConfig config = IndicatiaFabricMod.CONFIG.getConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(LangUtils.translate("ui.indicatia.config.title"));
        builder.setSavingRunnable(() ->
        {
            try
            {
                IndicatiaFabricMod.CONFIG.saveConfig();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        ConfigEntryBuilder entry = ConfigEntryBuilder.create();
        ConfigCategory generalCategory = builder.getOrCreateCategory(TextComponentUtils.component("General Settings"));
        generalCategory.addEntry(entry.startIntSlider(LangUtils.translate("indicatia.configgui.afk_message_time"), 5, 5, 60).setSaveConsumer(value -> config.afkMessageTime = value).setDefaultValue(5).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_render_info"), config.enableRenderInfo).setSaveConsumer(value -> config.enableRenderInfo = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_blockhit_animation"), config.enableBlockhitAnimation).setSaveConsumer(value -> config.enableBlockhitAnimation = value).setDefaultValue(false).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_old_armor_render"), config.enableOldArmorRender).setSaveConsumer(value -> config.enableOldArmorRender = value).setDefaultValue(false).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_version_checker"), config.enableVersionChecker).setSaveConsumer(value -> config.enableVersionChecker = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_afk_message"), config.enableAFKMessage).setSaveConsumer(value -> config.enableAFKMessage = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_custom_player_list"), config.enableCustomPlayerList).setSaveConsumer(value -> config.enableCustomPlayerList = value).setDefaultValue(false).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.multiplayer_screen_enhancement"), config.multiplayerScreenEnhancement).setSaveConsumer(value -> config.multiplayerScreenEnhancement = value).setDefaultValue(false).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_confirm_to_disconnect"), config.enableConfirmToDisconnect).setSaveConsumer(value -> config.enableConfirmToDisconnect = value).setDefaultValue(false).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_vanilla_potion_hud"), config.enableVanillaPotionHUD).setSaveConsumer(value -> config.enableVanillaPotionHUD = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_boss_health_bar_render"), config.enableBossHealthBarRender).setSaveConsumer(value -> config.enableBossHealthBarRender = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_boss_health_status_render"), config.enableRenderBossHealthStatus).setSaveConsumer(value -> config.enableRenderBossHealthStatus = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_sidebar_scoreboard_render"), config.enableSidebarScoreboardRender).setSaveConsumer(value -> config.enableSidebarScoreboardRender = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_hypixel_chat_mode"), config.enableHypixelChatMode).setSaveConsumer(value -> config.enableHypixelChatMode = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_hypixel_dropdown_shortcut_game"), config.enableHypixelDropdownShortcutGame).setSaveConsumer(value -> config.enableHypixelDropdownShortcutGame = value).setDefaultValue(true).build());
        generalCategory.addEntry(entry.startBooleanToggle(LangUtils.translate("indicatia.configgui.enable_alternate_chat_key"), config.enableAlternateChatKey).setSaveConsumer(value -> config.enableAlternateChatKey = value).setDefaultValue(true).build());
        return builder.build();
    }
}