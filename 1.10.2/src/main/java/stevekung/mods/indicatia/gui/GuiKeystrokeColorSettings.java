package stevekung.mods.indicatia.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.utils.LangUtil;

@SideOnly(Side.CLIENT)
public class GuiKeystrokeColorSettings extends GuiScreen
{
    private GuiButton lmbrmbRainbow;
    private GuiButton cpsRainbow;
    private GuiButton wasdRainbow;
    private GuiButton blockRainbow;
    private GuiButton sprintRainbow;
    private GuiButton sneakRainbow;

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
        int buttonId = 0;
        int x = this.width / 2;
        int y = this.height / 4;
        this.buttonList.add(new GuiButton(buttonId++, x - 100, this.height - 100, LangUtil.translate("gui.done")));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y - 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_LMBRMB_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y - 25, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_LMBRMB_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_LMBRMB_BLUE));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y - 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_CPS_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y - 25, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_CPS_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_CPS_BLUE));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y - 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_WASD_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y - 25, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_WASD_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_WASD_BLUE));

        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y + 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SPRINT_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y + 75, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SPRINT_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 155, y + 100, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SPRINT_BLUE));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y + 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SNEAK_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y + 75, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SNEAK_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x - 50, y + 100, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_SNEAK_BLUE));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y + 50, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_BLOCK_RED));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y + 75, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_BLOCK_GREEN));
        this.buttonList.add(new GuiRenderStatusSliderInt(buttonId++, x + 55, y + 100, 100, GuiRenderStatusSliderInt.Options.KEYSTROKE_BLOCK_BLUE));

        this.buttonList.add(this.lmbrmbRainbow = new GuiButton(buttonId++, x - 155, y + 25, 100, 20, ""));
        this.buttonList.add(this.cpsRainbow = new GuiButton(buttonId++, x - 50, y + 25, 100, 20, ""));
        this.buttonList.add(this.wasdRainbow = new GuiButton(buttonId++, x + 55, y + 25, 100, 20, ""));
        this.buttonList.add(this.sprintRainbow = new GuiButton(buttonId++, x - 155, y + 125, 100, 20, ""));
        this.buttonList.add(this.sneakRainbow = new GuiButton(buttonId++, x - 50, y + 125, 100, 20, ""));
        this.buttonList.add(this.blockRainbow = new GuiButton(buttonId++, x + 55, y + 125, 100, 20, ""));

        this.lmbrmbRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW);
        this.cpsRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_CPS_RAINBOW);
        this.wasdRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_WASD_RAINBOW);
        this.sprintRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW);
        this.sneakRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW);
        this.blockRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW);
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
    public void updateScreen()
    {
        this.lmbrmbRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW);
        this.cpsRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_CPS_RAINBOW);
        this.wasdRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_WASD_RAINBOW);
        this.sprintRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW);
        this.sneakRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW);
        this.blockRainbow.displayString = "Rainbow: " + this.getBooleanColor(ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
            case 0:
                new GuiRenderStatusSettings().display();
                ExtendedConfig.save();
                break;
            case 19:
                ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW = !ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW;
                ExtendedConfig.save();
                break;
            case 20:
                ExtendedConfig.KEYSTROKE_CPS_RAINBOW = !ExtendedConfig.KEYSTROKE_CPS_RAINBOW;
                ExtendedConfig.save();
                break;
            case 21:
                ExtendedConfig.KEYSTROKE_WASD_RAINBOW = !ExtendedConfig.KEYSTROKE_WASD_RAINBOW;
                ExtendedConfig.save();
                break;
            case 22:
                ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW = !ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW;
                ExtendedConfig.save();
                break;
            case 23:
                ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW = !ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW;
                ExtendedConfig.save();
                break;
            case 24:
                ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW = !ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW;
                ExtendedConfig.save();
                break;
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
        this.drawCenteredString(this.fontRendererObj, "Keystroke Color Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getBooleanColor(boolean value)
    {
        return value ? TextFormatting.GREEN + "true": TextFormatting.RED + "false";
    }
}