package stevekung.mods.indicatia.utils;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;

public class InfoUtil
{
    public static final InfoUtil INSTANCE = new InfoUtil();

    public int getPing()
    {
        if (IndicatiaMod.MC.getConnection().getPlayerInfo(IndicatiaMod.MC.thePlayer.getUniqueID()) != null)
        {
            if (GameProfileUtil.getUsername().equals(ExtendedConfig.HYPIXEL_NICK_NAME))
            {
                return IndicatiaMod.MC.getConnection().getPlayerInfo(IndicatiaMod.MC.thePlayer.getUniqueID()).getResponseTime();
            }
            else
            {
                for (Map.Entry<String, Integer> entry : CommonHandler.PLAYER_PING_MAP.entrySet())
                {
                    if (entry.getKey().contains(ExtendedConfig.HYPIXEL_NICK_NAME))
                    {
                        return entry.getValue();
                    }
                }
            }
        }
        return 0;
    }

    public boolean isHypixel()
    {
        return IndicatiaMod.MC.getCurrentServerData() != null && IndicatiaMod.MC.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
    }

    public String[] getColorCode()
    {
        return new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    }

    public String[] getJsonColor()
    {
        return new String[] {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};
    }

    public TextFormatting getTextColor(String color)
    {
        if (color.equals("black"))
        {
            return TextFormatting.BLACK;
        }
        else if (color.equals("dark_blue"))
        {
            return TextFormatting.DARK_BLUE;
        }
        else if (color.equals("dark_green"))
        {
            return TextFormatting.DARK_GREEN;
        }
        else if (color.equals("dark_aqua"))
        {
            return TextFormatting.DARK_AQUA;
        }
        else if (color.equals("dark_red"))
        {
            return TextFormatting.DARK_RED;
        }
        else if (color.equals("dark_purple"))
        {
            return TextFormatting.DARK_PURPLE;
        }
        else if (color.equals("gold"))
        {
            return TextFormatting.GOLD;
        }
        else if (color.equals("gray"))
        {
            return TextFormatting.GRAY;
        }
        else if (color.equals("dark_gray"))
        {
            return TextFormatting.DARK_GRAY;
        }
        else if (color.equals("blue"))
        {
            return TextFormatting.BLUE;
        }
        else if (color.equals("green"))
        {
            return TextFormatting.GREEN;
        }
        else if (color.equals("aqua"))
        {
            return TextFormatting.AQUA;
        }
        else if (color.equals("red"))
        {
            return TextFormatting.RED;
        }
        else if (color.equals("light_purple"))
        {
            return TextFormatting.LIGHT_PURPLE;
        }
        else if (color.equals("yellow"))
        {
            return TextFormatting.YELLOW;
        }
        return TextFormatting.WHITE;
    }

    public int getCPS()
    {
        Iterator<Long> iterator = CommonHandler.LEFT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return CommonHandler.LEFT_CLICK.size();
    }

    public int getRCPS()
    {
        Iterator<Long> iterator = CommonHandler.RIGHT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return CommonHandler.RIGHT_CLICK.size();
    }

    public String removeFormattingCodes(String text)
    {
        for (int i = 0; i < 10; i++)
        {
            text = text.replace("\u00a7" + i, "");
        }
        return text = text.replace("\u00a7" + "a", "").replace("\u00a7" + "b", "").replace("\u00a7" + "c", "").replace("\u00a7" + "d", "").replace("\u00a7" + "e", "").replace("\u00a7" + "f", "").replace("\u00a7" + "k", "").replace("\u00a7" + "l", "").replace("\u00a7" + "m", "").replace("\u00a7" + "n", "").replace("\u00a7" + "o", "").replace("\u00a7" + "r", "");
    }

    public void setOverlayMessage(String message, boolean isPlaying)
    {
        IndicatiaMod.MC.ingameGUI.setRecordPlaying(message, isPlaying);
    }

    public void setOverlayMessage(ITextComponent component, boolean isPlaying)
    {
        IndicatiaMod.MC.ingameGUI.setRecordPlaying(component, isPlaying);
    }
}