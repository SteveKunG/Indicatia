package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.YesNoScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TextFormat;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

@Environment(EnvType.CLIENT)
public class GuiExtendedConfig extends Screen
{
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
    public static boolean preview = false;
    private ButtonWidget resetButton;
    private ButtonWidget doneButton;

    static
    {
        OPTIONS.add(ExtendedConfig.Options.SWAP_INFO_POS);
        OPTIONS.add(ExtendedConfig.Options.HEALTH_STATUS);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_POSITION);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_ORDERING);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_DIRECTION);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_STATUS);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_POSITION);
        OPTIONS.add(ExtendedConfig.Options.POTION_HUD_STYLE);
        OPTIONS.add(ExtendedConfig.Options.POTION_HUD_POSITION);
        OPTIONS.add(ExtendedConfig.Options.CPS_POSITION);
        OPTIONS.add(ExtendedConfig.Options.CPS_OPACITY);
    }

    public GuiExtendedConfig()
    {
        super(JsonUtils.create("Indicatia Extended Config"));
    }

    @Override
    public void init()
    {
        GuiExtendedConfig.preview = false;
        int i = 0;

        for (ExtendedConfig.Options options : OPTIONS)
        {
            if (options.isDouble())
            {
                this.addButton(new GuiConfigSlider(options.getOrdinal(), this.width / 2 - 160 + i % 2 * 160, this.height / 6 - 17 + 24 * (i >> 1), 160, options));
            }
            else
            {
                GuiConfigButton button = new GuiConfigButton(this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160, ExtendedConfig.instance.getKeyBinding(options))
                {
                    @Override
                    public void onClick(double mouseX, double mouseY)
                    {
                        ExtendedConfig.instance.setOptionValue(options, 1);
                        this.setMessage(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(options.getOrdinal())));
                        ExtendedConfig.save();
                    }
                };
                this.addButton(button);
            }
            ++i;
        }
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.render_info.title"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(new GuiRenderInfoSettings(GuiExtendedConfig.this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.custom_color.title"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(new GuiCustomColorSettings(GuiExtendedConfig.this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.offset.title"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(new GuiOffsetSettings(GuiExtendedConfig.this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.hypixel.title"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(new GuiHypixelSettings(GuiExtendedConfig.this));
        }));
        this.addButton(new GuiConfigButton(this.width / 2 + 10, this.height / 6 + 103, 150, ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.PREVIEW))
        {
            @Override
            public void onClick(double mouseX, double mouseY)
            {
                ExtendedConfig.instance.setOptionValue(ExtendedConfig.Options.PREVIEW, 1);
                this.setMessage(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.PREVIEW));
            }
        });
        this.addButton(this.doneButton = new ButtonWidget(this.width / 2 - 100, this.height / 6 + 175, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(null);
        }));
        this.addButton(this.resetButton = new ButtonWidget(this.width / 2 + 10, this.height / 6 + 175, 100, 20, LangUtils.translate("extended_config.reset_config"), button ->
        {
            ExtendedConfig.save();
            GuiExtendedConfig.this.minecraft.openScreen(new YesNoScreen(this::resetConfig, LangUtils.translateComponent("menu.reset_config_confirm"), JsonUtils.create("")));
        }));
        this.resetButton.visible = false;
    }

    @Override
    public void tick()
    {
        boolean shift = ClientUtils.isShiftKeyDown();

        if (shift)
        {
            this.doneButton.setWidth(100);
            this.doneButton.x = this.width / 2 - 105;
            this.resetButton.visible = true;
        }
        else
        {
            this.doneButton.setWidth(200);
            this.doneButton.x = this.width / 2 - 100;
            this.resetButton.visible = false;
        }
    }

    private void resetConfig(boolean condition)
    {
        if (condition)
        {
            IndicatiaMod.saveResetFlag();
            this.minecraft.openScreen(null);
        }
        else
        {
            this.minecraft.openScreen(this);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_)
    {
        ExtendedConfig.save();
        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (!GuiExtendedConfig.preview)
        {
            this.renderBackground();
        }
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.main.title") + " : " + LangUtils.translate("extended_config.current_profile.info", TextFormat.YELLOW + ExtendedConfig.currentProfile + TextFormat.RESET), this.width / 2, 10, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}