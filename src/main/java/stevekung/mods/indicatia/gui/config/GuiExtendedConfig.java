package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiExtendedConfig extends GuiScreen
{
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
    public static boolean preview = false;
    private GuiButton resetButton;
    private GuiButton doneButton;

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

    @Override
    public void initGui()
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
                GuiConfigButton button = new GuiConfigButton(options.getOrdinal(), this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160, ExtendedConfig.instance.getKeyBinding(options))
                {
                    @Override
                    public void onClick(double mouseX, double mouseY)
                    {
                        ExtendedConfig.instance.setOptionValue(options, 1);
                        this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
                        ExtendedConfig.save();
                    }
                };
                this.addButton(button);
            }
            ++i;
        }
        this.addButton(new GuiButton(100, this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.render_info.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(new GuiRenderInfoSettings(GuiExtendedConfig.this));
            }
        });
        this.addButton(new GuiButton(101, this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.custom_color.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(new GuiCustomColorSettings(GuiExtendedConfig.this));
            }
        });
        this.addButton(new GuiButton(102, this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.offset.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(new GuiOffsetSettings(GuiExtendedConfig.this));
            }
        });
        this.addButton(new GuiButton(103, this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.hypixel.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(new GuiHypixelSettings(GuiExtendedConfig.this));
            }
        });

        this.addButton(new GuiConfigButton(150, this.width / 2 + 10, this.height / 6 + 103, 150, ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.PREVIEW))
        {
            @Override
            public void onClick(double mouseX, double mouseY)
            {
                ExtendedConfig.instance.setOptionValue(ExtendedConfig.Options.PREVIEW, 1);
                this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.PREVIEW);
            }
        });
        this.addButton(this.doneButton = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 175, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(null);
            }
        });
        this.addButton(this.resetButton = new GuiButton(201, this.width / 2 + 10, this.height / 6 + 175, 100, 20, LangUtils.translate("extended_config.reset_config"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                ExtendedConfig.save();
                GuiExtendedConfig.this.mc.displayGuiScreen(new GuiYesNo(GuiExtendedConfig.this, LangUtils.translate("menu.reset_config_confirm"), "", 201));
            }
        });
        this.resetButton.visible = false;
    }

    @Override
    public void tick()
    {
        boolean shift = ClientUtils.isShiftKeyDown();

        if (shift)
        {
            this.doneButton.width = 100;
            this.doneButton.x = this.width / 2 - 105;
            this.resetButton.visible = true;
        }
        else
        {
            this.doneButton.width = 200;
            this.doneButton.x = this.width / 2 - 100;
            this.resetButton.visible = false;
        }
    }

    @Override
    public void confirmResult(boolean result, int id)
    {
        super.confirmResult(result, id);

        if (result)
        {
            IndicatiaMod.saveResetFlag();
            this.mc.displayGuiScreen(null);
        }
        else
        {
            this.mc.displayGuiScreen(this);
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
            this.drawDefaultBackground();
        }
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.main.title") + " : " + LangUtils.translate("extended_config.current_profile.info", TextFormatting.YELLOW + ExtendedConfig.currentProfile + TextFormatting.RESET), this.width / 2, 10, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}