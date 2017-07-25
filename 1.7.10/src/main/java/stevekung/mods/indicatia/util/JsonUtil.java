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

    public ChatStyle colorFromConfig(String color)
    {
        return this.style().setColor(this.color(color));
    }

    private EnumChatFormatting color(String color)
    {
        if (color.equalsIgnoreCase("black"))
        {
            return EnumChatFormatting.BLACK;
        }
        else if (color.equalsIgnoreCase("dark_blue"))
        {
            return EnumChatFormatting.DARK_BLUE;
        }
        else if (color.equalsIgnoreCase("dark_green"))
        {
            return EnumChatFormatting.DARK_GREEN;
        }
        else if (color.equalsIgnoreCase("dark_aqua"))
        {
            return EnumChatFormatting.DARK_AQUA;
        }
        else if (color.equalsIgnoreCase("dark_red"))
        {
            return EnumChatFormatting.DARK_RED;
        }
        else if (color.equalsIgnoreCase("dark_purple"))
        {
            return EnumChatFormatting.DARK_PURPLE;
        }
        else if (color.equalsIgnoreCase("gold"))
        {
            return EnumChatFormatting.GOLD;
        }
        else if (color.equalsIgnoreCase("gray"))
        {
            return EnumChatFormatting.GRAY;
        }
        else if (color.equalsIgnoreCase("dark_gray"))
        {
            return EnumChatFormatting.DARK_GRAY;
        }
        else if (color.equalsIgnoreCase("blue"))
        {
            return EnumChatFormatting.BLUE;
        }
        else if (color.equalsIgnoreCase("green"))
        {
            return EnumChatFormatting.GREEN;
        }
        else if (color.equalsIgnoreCase("aqua"))
        {
            return EnumChatFormatting.AQUA;
        }
        else if (color.equalsIgnoreCase("red"))
        {
            return EnumChatFormatting.RED;
        }
        else if (color.equalsIgnoreCase("light_purple"))
        {
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        else if (color.equalsIgnoreCase("yellow"))
        {
            return EnumChatFormatting.YELLOW;
        }
        else
        {
            return EnumChatFormatting.WHITE;
        }
    }

    public ChatStyle white()
    {
        return this.style().setColor(EnumChatFormatting.WHITE);
    }

    public ChatStyle red()
    {
        return this.style().setColor(EnumChatFormatting.RED);
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