package stevekung.mods.indicatia.gui;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class GuiAutoLoginFunctionHelp extends Screen
{
    private final boolean inGui;
    private final List<StringFunction> functionList = new ArrayList<>();
    //    private GuiFunctionHelpSlot functionHelpSlot; TODO

    GuiAutoLoginFunctionHelp(boolean inGui)
    {
        super(JsonUtils.create("Auto Login Function Help"));
        this.inGui = inGui;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 38, 200, 20, this.inGui ? LangUtils.translate("gui.back") : LangUtils.translate("gui.done"), button ->
        {
            if (GuiAutoLoginFunctionHelp.this.inGui)
            {
                GuiAutoLoginFunctionHelp.this.minecraft.openScreen(new GuiAutoLoginFunction());
            }
            else
            {
                GuiAutoLoginFunctionHelp.this.minecraft.openScreen(null);
            }
        }));
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

        //        this.functionHelpSlot = new GuiFunctionHelpSlot(this, this.functionList, this.width, this.height);
        //        this.children.add(this.functionHelpSlot);
    }

    //    @Override
    //    public Element getFocused()
    //    {
    //        return this.functionHelpSlot;
    //    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.drawCenteredString(this.minecraft.textRenderer, "Auto Login Function", this.width / 2, 20, 16777215);

        //        if (this.functionHelpSlot != null)
        //        {
        //            this.functionHelpSlot.render(mouseX, mouseY, partialTicks);
        //        }
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
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