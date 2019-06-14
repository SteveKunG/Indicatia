package stevekung.mods.indicatia.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunctionHelp.StringFunction;
import stevekung.mods.indicatia.gui.GuiFunctionHelpSlot.FunctionEntry;

@OnlyIn(Dist.CLIENT)
public class GuiFunctionHelpSlot extends ExtendedList<FunctionEntry>
{
    private List<StringFunction> stringList;
    private GuiAutoLoginFunctionHelp parent;

    GuiFunctionHelpSlot(GuiAutoLoginFunctionHelp parent, List<StringFunction> functionList, int width, int height)
    {
        super(Minecraft.getInstance(), width, height, 32, height - 48, 13);
        this.stringList = functionList;
        this.parent = parent;
    }

    @Override
    protected int getMaxPosition()
    {
        return 5 + this.getItemCount() * 13;
    }

    @Override
    protected void renderHoleBackground(int startY, int endY, int startAlpha, int endAlpha)
    {
        this.parent.drawCenteredString(this.minecraft.fontRenderer, "Auto Login Function", this.width / 2, 16, 16777215);
        super.renderHoleBackground(startY, endY, startAlpha, endAlpha);
    }

    @Override
    protected int getScrollbarPosition()
    {
        return this.width / 2 + 180;
    }

    private void drawFunction(String function, String desc, int x, int y)
    {
        this.minecraft.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + "\u25cf " + function + TextFormatting.RESET + "- " + desc, x, y, 16777215);
    }

    private void drawMainFunction(String text, int x, int y)
    {
        this.minecraft.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + "- " + text, x, y, 16777215);
    }

    @OnlyIn(Dist.CLIENT)
    public class FunctionEntry extends ExtendedList.AbstractListEntry<FunctionEntry>
    {
        @Override
        public void render(int index, int rowTop, int rowLeft, int rowWidth, int yPos, int mouseX, int mouseY, boolean isMouseOver, float partialTicks)
        {
            StringFunction function = GuiFunctionHelpSlot.this.stringList.get(index);

            if (function.desc == null)
            {
                GuiFunctionHelpSlot.this.drawMainFunction(function.function, rowLeft - 60, yPos);
            }
            else
            {
                GuiFunctionHelpSlot.this.drawFunction(function.function, function.desc, rowLeft - 50, yPos);
            }
        }
    }
}