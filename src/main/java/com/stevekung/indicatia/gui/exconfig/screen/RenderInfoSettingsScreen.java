package com.stevekung.indicatia.gui.exconfig.screen;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigButtonListWidget;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderInfoSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.FPS, ExtendedConfig.XYZ, ExtendedConfig.DIRECTION, ExtendedConfig.BIOME, ExtendedConfig.PING, ExtendedConfig.PING_TO_SECOND, ExtendedConfig.SERVER_IP,
            ExtendedConfig.SERVER_IP_MC, ExtendedConfig.EQUIPMENT_HUD, ExtendedConfig.POTION_HUD, ExtendedConfig.CPS, ExtendedConfig.RCPS, ExtendedConfig.SLIME_CHUNK, ExtendedConfig.REAL_TIME, ExtendedConfig.GAME_TIME, ExtendedConfig.GAME_WEATHER,
            ExtendedConfig.MOON_PHASE, ExtendedConfig.POTION_ICON, ExtendedConfig.TPS, ExtendedConfig.TPS_ALL_DIMS, ExtendedConfig.ALTERNATE_POTION_COLOR };

    public RenderInfoSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Render Info Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 100, this.height - 25, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 16, this.height - 30, 25);
        this.optionsRowList.addAll(OPTIONS);
        this.children.add(this.optionsRowList);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.render_info.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}