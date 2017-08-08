package stevekung.mods.indicatia.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.util.LangUtil;

@SideOnly(Side.CLIENT)
public class GuiCustomTextColorSettings2 extends GuiScreen
{
    private GuiButton nextButton;
    private GuiButton prevButton;

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, LangUtil.translate("gui.done")));
        this.buttonList.add(this.nextButton = new GuiButton(201, this.width / 2 + 105, this.height - 120, 20, 20, ">"));
        this.buttonList.add(this.prevButton = new GuiButton(202, this.width / 2 - 125, this.height - 120, 20, 20, "<"));

        this.nextButton.enabled = false;

        // column 1
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 45, GuiCustomTextColorSliderInt.Options.IP_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 65, GuiCustomTextColorSliderInt.Options.IP_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 85, GuiCustomTextColorSliderInt.Options.IP_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 105, GuiCustomTextColorSliderInt.Options.IP_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 125, GuiCustomTextColorSliderInt.Options.IP_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 145, GuiCustomTextColorSliderInt.Options.IP_VALUE_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 165, GuiCustomTextColorSliderInt.Options.CPS_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 185, GuiCustomTextColorSliderInt.Options.CPS_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 205, GuiCustomTextColorSliderInt.Options.CPS_B));

        // column 2
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 45, GuiCustomTextColorSliderInt.Options.CPS_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 65, GuiCustomTextColorSliderInt.Options.CPS_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 85, GuiCustomTextColorSliderInt.Options.CPS_VALUE_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 105, GuiCustomTextColorSliderInt.Options.RCPS_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 125, GuiCustomTextColorSliderInt.Options.RCPS_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 145, GuiCustomTextColorSliderInt.Options.RCPS_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 165, GuiCustomTextColorSliderInt.Options.RCPS_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 185, GuiCustomTextColorSliderInt.Options.RCPS_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 205, GuiCustomTextColorSliderInt.Options.RCPS_VALUE_B));

        // column 3
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 45, GuiCustomTextColorSliderInt.Options.TOP_DONATE_NAME_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 65, GuiCustomTextColorSliderInt.Options.TOP_DONATE_NAME_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 85, GuiCustomTextColorSliderInt.Options.TOP_DONATE_NAME_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 105, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_NAME_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 125, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_NAME_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 145, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_NAME_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 165, GuiCustomTextColorSliderInt.Options.TOP_DONATE_COUNT_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 185, GuiCustomTextColorSliderInt.Options.TOP_DONATE_COUNT_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 205, GuiCustomTextColorSliderInt.Options.TOP_DONATE_COUNT_B));

        // column 4
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 45, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_COUNT_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 65, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_COUNT_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 85, GuiCustomTextColorSliderInt.Options.RECENT_DONATE_COUNT_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 105, GuiCustomTextColorSliderInt.Options.SLIME_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 125, GuiCustomTextColorSliderInt.Options.SLIME_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 145, GuiCustomTextColorSliderInt.Options.SLIME_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 165, GuiCustomTextColorSliderInt.Options.SLIME_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 185, GuiCustomTextColorSliderInt.Options.SLIME_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 205, GuiCustomTextColorSliderInt.Options.SLIME_VALUE_B));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        if (keyCode == 1 || keyCode == 28)
        {
            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 200)
            {
                ExtendedConfig.save();
                this.mc.displayGuiScreen(null);
            }
            if (button.id == 202)
            {
                ExtendedConfig.save();
                this.mc.displayGuiScreen(new GuiCustomTextColorSettings1());
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawCenteredString(this.fontRendererObj, "Custom Text Color Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}