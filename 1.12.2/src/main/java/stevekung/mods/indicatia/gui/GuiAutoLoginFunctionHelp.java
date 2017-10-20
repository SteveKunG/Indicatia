package stevekung.mods.indicatia.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.LangUtil;

public class GuiAutoLoginFunctionHelp extends GuiScreen
{
    private GuiButton backBtn;
    private final boolean inGui;
    private List<StringFunction> functionList = new ArrayList<>();
    private ScaledResolution res;
    private GuiFunctionHelpSlot functionHelpSlot;

    public GuiAutoLoginFunctionHelp(boolean inGui)
    {
        this.inGui = inGui;
        this.res = new ScaledResolution(IndicatiaMod.MC);
    }

    public void display()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        IndicatiaMod.MC.displayGuiScreen(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(this.backBtn = new GuiButton(0, this.width / 2 - 100, this.res.getScaledHeight() - 60, this.inGui ? LangUtil.translate("gui.back") : LangUtil.translate("gui.done")));
        this.functionList.clear();
        this.functionList.add(new StringFunction("Movement", null));
        this.functionList.add(new StringFunction("forward:<tick> ", "Move Forward"));
        this.functionList.add(new StringFunction("forward_after:<tick> ", "Move Forward (After run command)"));
        this.functionList.add(new StringFunction("sprint:<boolean> ", "Sprinting"));

        this.functionList.add(new StringFunction("Command", null));
        this.functionList.add(new StringFunction("command:<string> ", "Command to execute"));
        this.functionList.add(new StringFunction("command_delay:<tick> ", "Delay before executing command"));

        this.functionList.add(new StringFunction("Rotation", null));
        this.functionList.add(new StringFunction("rotation:<boolean> ", "Use custom rotation"));
        this.functionList.add(new StringFunction("pitch:<double> ", "Rotation Pitch of the player"));
        this.functionList.add(new StringFunction("yaw:<double> ", "Rotation Yaw of the player"));

        this.functionList.add(new StringFunction("Misc", null));
        this.functionList.add(new StringFunction("right_click:<boolean> ", "Use Right Click"));
        this.functionList.add(new StringFunction("right_click_delay:<tick> ", "Delay before use right click"));
        this.functionList.add(new StringFunction("function_delay:<tick> ", "Delay before run function"));

        this.functionHelpSlot = new GuiFunctionHelpSlot(this.mc, this, this.functionList, this.width, this.height);
        this.functionHelpSlot.registerScrollButtons(1, 1);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        if (this.functionHelpSlot != null)
        {
            this.functionHelpSlot.handleMouseInput();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            if (this.inGui)
            {
                this.mc.displayGuiScreen(new GuiAutoLoginFunction());
            }
            else
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Auto Login Function", this.width / 2, 20, 16777215);

        if (this.functionHelpSlot != null)
        {
            this.functionHelpSlot.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    static class StringFunction
    {
        String function;
        String desc;

        StringFunction(String function, String desc)
        {
            this.function = function;
            this.desc = desc;
        }
    }
}