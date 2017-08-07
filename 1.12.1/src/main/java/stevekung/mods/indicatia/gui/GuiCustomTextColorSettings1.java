package stevekung.mods.indicatia.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.LangUtil;

@SideOnly(Side.CLIENT)
public class GuiCustomTextColorSettings1 extends GuiScreen
{
    private GuiButton nextButton;
    private GuiButton prevButton;

    public void display()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        MinecraftForge.EVENT_BUS.unregister(this);
        IndicatiaMod.MC.displayGuiScreen(this);
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, LangUtil.translate("gui.done")));
        this.buttonList.add(this.nextButton = new GuiButton(201, this.width / 2 + 105, this.height - 120, 20, 20, ">"));
        this.buttonList.add(this.prevButton = new GuiButton(202, this.width / 2 - 125, this.height - 120, 20, 20, "<"));

        this.prevButton.enabled = false;

        // column 1
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 45, GuiCustomTextColorSliderInt.Options.FPS_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 65, GuiCustomTextColorSliderInt.Options.FPS_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 85, GuiCustomTextColorSliderInt.Options.FPS_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 105, GuiCustomTextColorSliderInt.Options.FPS_M40_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 125, GuiCustomTextColorSliderInt.Options.FPS_M40_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 145, GuiCustomTextColorSliderInt.Options.FPS_M40_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 165, GuiCustomTextColorSliderInt.Options.FPS_26_40_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 185, GuiCustomTextColorSliderInt.Options.FPS_26_40_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 210, 205, GuiCustomTextColorSliderInt.Options.FPS_26_40_B));

        // column 2
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 45, GuiCustomTextColorSliderInt.Options.XYZ_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 65, GuiCustomTextColorSliderInt.Options.XYZ_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 85, GuiCustomTextColorSliderInt.Options.XYZ_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 105, GuiCustomTextColorSliderInt.Options.XYZ_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 125, GuiCustomTextColorSliderInt.Options.XYZ_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 145, GuiCustomTextColorSliderInt.Options.XYZ_VALUE_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 165, GuiCustomTextColorSliderInt.Options.BIOME_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 185, GuiCustomTextColorSliderInt.Options.BIOME_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 - 105, 205, GuiCustomTextColorSliderInt.Options.BIOME_B));

        // column 3
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 45, GuiCustomTextColorSliderInt.Options.BIOME_VALUE_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 65, GuiCustomTextColorSliderInt.Options.BIOME_VALUE_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 85, GuiCustomTextColorSliderInt.Options.BIOME_VALUE_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 105, GuiCustomTextColorSliderInt.Options.PING_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 125, GuiCustomTextColorSliderInt.Options.PING_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 145, GuiCustomTextColorSliderInt.Options.PING_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 165, GuiCustomTextColorSliderInt.Options.PING_L200_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 185, GuiCustomTextColorSliderInt.Options.PING_L200_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 5, 205, GuiCustomTextColorSliderInt.Options.PING_L200_B));

        // column 4
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 45, GuiCustomTextColorSliderInt.Options.PING_200_300_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 65, GuiCustomTextColorSliderInt.Options.PING_200_300_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 85, GuiCustomTextColorSliderInt.Options.PING_200_300_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 105, GuiCustomTextColorSliderInt.Options.PING_300_500_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 125, GuiCustomTextColorSliderInt.Options.PING_300_500_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 145, GuiCustomTextColorSliderInt.Options.PING_300_500_B));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 165, GuiCustomTextColorSliderInt.Options.PING_M500_R));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 185, GuiCustomTextColorSliderInt.Options.PING_M500_G));
        this.buttonList.add(new GuiCustomTextColorSliderInt(this.width / 2 + 110, 205, GuiCustomTextColorSliderInt.Options.PING_M500_B));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
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
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 200)
            {
                ExtendedConfig.save();
                this.mc.displayGuiScreen(null);
            }
            if (button.id == 201)
            {
                ExtendedConfig.save();
                this.mc.displayGuiScreen(new GuiCustomTextColorSettings2());
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
        this.drawCenteredString(this.fontRenderer, "Custom Text Color Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}