package stevekung.mods.indicatia.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunctionHelp.StringFunction;

@SideOnly(Side.CLIENT)
public class GuiFunctionHelpSlot extends GuiSlot
{
    private List<StringFunction> stringList;
    private GuiAutoLoginFunctionHelp parent;

    public GuiFunctionHelpSlot(GuiAutoLoginFunctionHelp guiAutoLoginFunctionHelp, List<StringFunction> functionList, int width, int height)
    {
        super(Minecraft.getMinecraft(), width, height, 32, height - 48, 13);
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
    protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks)
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
        this.mc.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + "\u25cf " + function + TextFormatting.RESET + "- " + desc, x, y, 16777215);
    }

    private void drawMainFunction(String text, int x, int y)
    {
        this.mc.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + "- " + text, x, y, 16777215);
    }
}