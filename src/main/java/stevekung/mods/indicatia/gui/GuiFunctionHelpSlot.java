package stevekung.mods.indicatia.gui;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunctionHelp.StringFunction;

@SideOnly(Side.CLIENT)
public class GuiFunctionHelpSlot extends GuiSlot
{
    private List<StringFunction> stringList;
    private GuiAutoLoginFunctionHelp parent;

    public GuiFunctionHelpSlot(Minecraft mc, GuiAutoLoginFunctionHelp guiAutoLoginFunctionHelp, List<StringFunction> functionList, int width, int height)
    {
        super(mc, width, height, 32, height - 64, 13);
        this.stringList = functionList;
        this.parent = guiAutoLoginFunctionHelp;
        this.setShowSelectionBox(false);
    }

    @Override
    protected int getSize()
    {
        return this.stringList.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return false;
    }

    @Override
    protected int getContentHeight()
    {
        return 5 + this.getSize() * 13;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, Tessellator tessellator, int mouseXIn, int mouseYIn)
    {
        StringFunction function = this.stringList.get(entryID);

        if (function.desc == null)
        {
            this.drawMainFunction(function.function, insideLeft - 60, yPos);
        }
        else
        {
            this.drawFunction(function.function, function.desc, insideLeft - 50, yPos);
        }
    }

    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
    {
        this.parent.drawCenteredString(this.mc.fontRenderer, "Auto Login Function", this.width / 2, 16, 16777215);
        super.overlayBackground(startY, endY, startAlpha, endAlpha);
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {}

    @Override
    protected int getScrollBarX()
    {
        return this.width / 2 + 180;
    }

    private void drawFunction(String function, String desc, int x, int y)
    {
        this.mc.fontRenderer.drawStringWithShadow(EnumChatFormatting.YELLOW + "\u25cf " + function + EnumChatFormatting.RESET + "- " + desc, x, y, 16777215);
    }

    private void drawMainFunction(String text, int x, int y)
    {
        this.mc.fontRenderer.drawStringWithShadow(EnumChatFormatting.YELLOW + "- " + text, x, y, 16777215);
    }
}