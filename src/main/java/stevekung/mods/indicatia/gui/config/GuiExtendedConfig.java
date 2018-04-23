package stevekung.mods.indicatia.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekunglib.utils.ClientUtils;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

@SideOnly(Side.CLIENT)
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

    public void display()
    {
        CommonUtils.registerEventHandler(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        CommonUtils.unregisterEventHandler(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void initGui()
    {
        GuiExtendedConfig.preview = false;
        int i = 0;

        for (ExtendedConfig.Options options : OPTIONS)
        {
            if (options.isFloat())
            {
                this.buttonList.add(new GuiConfigSlider(options.getOrdinal(), this.width / 2 - 160 + i % 2 * 160, this.height / 6 - 17 + 24 * (i >> 1), 160, options));
            }
            else
            {
                GuiConfigButton button = new GuiConfigButton(options.getOrdinal(), this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160, options, ExtendedConfig.instance.getKeyBinding(options));
                this.buttonList.add(button);
            }
            ++i;
        }
        this.buttonList.add(new GuiButton(100, this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.render_info.title")));
        this.buttonList.add(new GuiButton(101, this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.custom_color.title")));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.offset.title")));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.hypixel.title")));

        this.buttonList.add(new GuiConfigButton(150, this.width / 2 + 10, this.height / 6 + 103, 150, ExtendedConfig.Options.PREVIEW, ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.PREVIEW)));
        this.buttonList.add(this.doneButton = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 175, LangUtils.translate("gui.done")));
        this.buttonList.add(this.resetButton = new GuiButton(201, this.width / 2 + 10, this.height / 6 + 175, 100, 20, LangUtils.translate("extended_config.reset_config")));
        this.resetButton.visible = false;
    }

    @Override
    public void updateScreen()
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
    public void confirmClicked(boolean result, int id)
    {
        super.confirmClicked(result, id);

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
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            ExtendedConfig.save();

            if ((button.id < 100 || button.id == 150) && button instanceof GuiConfigButton)
            {
                ExtendedConfig.Options options = ((GuiConfigButton)button).getOption();
                ExtendedConfig.instance.setOptionValue(options, 1);
                button.displayString = ExtendedConfig.instance.getKeyBinding(button.id == 150 ? ExtendedConfig.Options.PREVIEW : ExtendedConfig.Options.byOrdinal(button.id));
            }
            if (button.id == 100)
            {
                this.mc.displayGuiScreen(new GuiRenderInfoSettings(this));
            }
            if (button.id == 101)
            {
                this.mc.displayGuiScreen(new GuiCustomColorSettings(this));
            }
            if (button.id == 102)
            {
                this.mc.displayGuiScreen(new GuiOffsetSettings(this));
            }
            if (button.id == 103)
            {
                this.mc.displayGuiScreen(new GuiHypixelSettings(this));
            }
            if (button.id == this.doneButton.id)
            {
                this.mc.displayGuiScreen(null);
            }
            if (button.id == this.resetButton.id)
            {
                this.mc.displayGuiScreen(new GuiYesNo(this, LangUtils.translate("message.reset_config_confirm"), "", 201));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (!GuiExtendedConfig.preview)
        {
            this.drawDefaultBackground();
        }
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.main.title") + " : " + LangUtils.translate("extended_config.current_profile.info", TextFormatting.YELLOW + ExtendedConfig.currentProfile + TextFormatting.RESET), this.width / 2, 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}