package com.stevekung.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigButtonListWidget;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public class RenderInfoSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.FPS);
        OPTIONS.add(ExtendedConfig.XYZ);
        OPTIONS.add(ExtendedConfig.DIRECTION);
        OPTIONS.add(ExtendedConfig.BIOME);
        OPTIONS.add(ExtendedConfig.PING);
        OPTIONS.add(ExtendedConfig.PING_TO_SECOND);
        OPTIONS.add(ExtendedConfig.SERVER_IP);
        OPTIONS.add(ExtendedConfig.SERVER_IP_MC);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_HUD);
        OPTIONS.add(ExtendedConfig.POTION_HUD);
        OPTIONS.add(ExtendedConfig.SLIME_CHUNK);
        OPTIONS.add(ExtendedConfig.REAL_TIME);
        OPTIONS.add(ExtendedConfig.GAME_TIME);
        OPTIONS.add(ExtendedConfig.GAME_WEATHER);
        OPTIONS.add(ExtendedConfig.MOON_PHASE);
        OPTIONS.add(ExtendedConfig.POTION_ICON);
        OPTIONS.add(ExtendedConfig.ALTERNATE_POTION_COLOR);
    }

    RenderInfoSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Render Info Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.instance.save();
            this.minecraft.openScreen(this.parent);
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 32, this.height - 32, 25);
        this.optionsRowList.addAll(OPTIONS.toArray(new ExtendedConfigOption[OPTIONS.size()]));
        this.children.add(this.optionsRowList);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.instance.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.render_info.title"), this.width / 2, 8, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}