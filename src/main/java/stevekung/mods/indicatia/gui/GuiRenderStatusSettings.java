package stevekung.mods.indicatia.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.LangUtil;

@SideOnly(Side.CLIENT)
public class GuiRenderStatusSettings extends GuiScreen
{
    public void display()
    {
        IndicatiaMod.registerForgeEvent(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        IndicatiaMod.unregisterForgeEvent(this);
        IndicatiaMod.MC.displayGuiScreen(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, LangUtil.translate("gui.done")));
        this.buttonList.add(new GuiRenderStatusSliderInt(201, this.width / 2 - 100, this.height / 4 - 50, GuiRenderStatusSliderInt.Options.ARMOR_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(202, this.width / 2 - 100, this.height / 4 - 25, GuiRenderStatusSliderInt.Options.POTION_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(203, this.width / 2 - 100, this.height / 4, GuiRenderStatusSliderInt.Options.KEYSTOKE_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(204, this.width / 2 - 100, this.height / 4 + 25, GuiRenderStatusSliderInt.Options.MAX_POTION_DISPLAY));
        this.buttonList.add(new GuiRenderStatusSliderInt(205, this.width / 2 - 100, this.height / 4 + 50, GuiRenderStatusSliderInt.Options.POTION_LENGTH_Y_OFFSET));
        this.buttonList.add(new GuiRenderStatusSliderInt(206, this.width / 2 - 100, this.height / 4 + 75, GuiRenderStatusSliderInt.Options.POTION_LENGTH_Y_OFFSET_OVERLAP));
        this.buttonList.add(new GuiButton(208, this.width / 2 - 100, this.height / 4 + 100, "Keystroke Color Settings"));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 200)
            {
                ExtendedConfig.save();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 208)
            {
                this.mc.displayGuiScreen(new GuiKeystrokeColorSettings());
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
        this.drawCenteredString(this.fontRendererObj, "Render Status Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}