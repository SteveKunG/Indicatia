package stevekung.mods.indicatia.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class JsonUtil
{
    public ChatComponentText text(String text)
    {
        return new ChatComponentText(text);
    }

    public ChatStyle style()
    {
        return new ChatStyle();
    }

    public ChatStyle white()
    {
        return this.style().setColor(EnumChatFormatting.WHITE);
    }

    public ChatStyle red()
    {
        return this.style().setColor(EnumChatFormatting.RED);
    }

    public ChatStyle darkRed()
    {
        return this.style().setColor(EnumChatFormatting.DARK_RED);
    }

    public ChatStyle green()
    {
        return this.style().setColor(EnumChatFormatting.GREEN);
    }

    public ChatStyle gray()
    {
        return this.style().setColor(EnumChatFormatting.GRAY);
    }

    public ClickEvent click(ClickEvent.Action action, String url)
    {
        return new ClickEvent(action, url);
    }

    public HoverEvent hover(HoverEvent.Action action, IChatComponent text)
    {
        return new HoverEvent(action, text);
    }
}