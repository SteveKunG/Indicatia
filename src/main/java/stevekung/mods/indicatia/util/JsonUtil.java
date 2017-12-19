package stevekung.mods.indicatia.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class JsonUtil
{
    public TextComponentString text(String text)
    {
        return new TextComponentString(text);
    }

    public Style style()
    {
        return new Style();
    }

    public Style white()
    {
        return this.style().setColor(TextFormatting.WHITE);
    }

    public Style red()
    {
        return this.style().setColor(TextFormatting.RED);
    }

    public Style darkRed()
    {
        return this.style().setColor(TextFormatting.DARK_RED);
    }

    public Style green()
    {
        return this.style().setColor(TextFormatting.GREEN);
    }

    public Style gray()
    {
        return this.style().setColor(TextFormatting.GRAY);
    }

    public ClickEvent click(ClickEvent.Action action, String url)
    {
        return new ClickEvent(action, url);
    }

    public HoverEvent hover(HoverEvent.Action action, ITextComponent text)
    {
        return new HoverEvent(action, text);
    }
}