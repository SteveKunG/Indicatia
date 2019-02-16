package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IGuiEventListener;
import stevekung.mods.stevekungslib.utils.LangUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiAutoLoginFunctionHelp extends GuiScreen
{
    private final boolean inGui;
    private final List<StringFunction> functionList = new ArrayList<>();
    private GuiFunctionHelpSlot functionHelpSlot;

    GuiAutoLoginFunctionHelp(boolean inGui)
    {
        this.inGui = inGui;
    }

    @Override
    public void initGui()
    {
        this.addButton(new GuiButton(0, this.width / 2 - 100, this.height - 38, this.inGui ? LangUtils.translate("gui.back") : LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                if (GuiAutoLoginFunctionHelp.this.inGui)
                {
                    GuiAutoLoginFunctionHelp.this.mc.displayGuiScreen(new GuiAutoLoginFunction());
                }
                else
                {
                    GuiAutoLoginFunctionHelp.this.mc.displayGuiScreen(null);
                }
            }
        });
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

        this.functionHelpSlot = new GuiFunctionHelpSlot(this, this.functionList, this.width, this.height);
        this.children.add(this.functionHelpSlot);
    }

    @Override
    @Nullable
    public IGuiEventListener getFocused()
    {
        return this.functionHelpSlot;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Auto Login Function", this.width / 2, 20, 16777215);

        if (this.functionHelpSlot != null)
        {
            this.functionHelpSlot.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.render(mouseX, mouseY, partialTicks);
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