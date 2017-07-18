package stevekung.mods.indicatia.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
            if (InfoUtil.INSTANCE.isHypixel())
            {
                if (!ExtendedConfig.HYPIXEL_NICK_NAME.isEmpty())
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
            else
            {
                return IndicatiaMod.MC.getConnection().getPlayerInfo(IndicatiaMod.MC.thePlayer.getUniqueID()).getResponseTime();
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

    public String getCurrentGameTime(long worldTicks)
    {
        int hours = (int)((worldTicks / 1000 + 6) % 24);
        int minutes = (int)(60 * (worldTicks % 1000) / 1000);
        String sminutes = "" + minutes;
        String shours = "" + hours;
        String ampm = hours >= 12 ? "PM" : "AM";

        if (hours <= 9)
        {
            shours = 0 + "" + hours;
        }
        if (minutes <= 9)
        {
            sminutes = 0 + "" + minutes;
        }
        return "Game: " + shours + ":" + sminutes + " " + ampm;
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        int phase = moonPhaseFactors[mc.theWorld.provider.getMoonPhase(mc.theWorld.getWorldTime())];
        String status;

        switch (phase)
        {
        case 4:
        default:
            status = "Full Moon";
            break;
        case 3:
            status = "Waning Gibbous";
            break;
        case 2:
            status = "Last Quarter";
            break;
        case 1:
            status = "Waning Crescent";
            break;
        case 0:
            status = "New Moon";
            break;
        case -1:
            status = "Waxing Crescent";
            break;
        case -2:
            status = "First Quarter";
            break;
        case -3:
            status = "Waxing Gibbous";
            break;
        }
        return "Moon Phase: " + status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.bucketInt(pos.getX(), 16);
        int z = MathHelper.bucketInt(pos.getZ(), 16);
        Random rnd = new Random(ExtendedConfig.SLIME_CHUNK_SEED + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    public int getAlternatePotionHUDTextColor(Potion potion)
    {
        int color = 0;

        if (potion == MobEffects.ABSORPTION)
        {
            color = RenderUtil.rgbToDecimal(247, 219, 21);
        }
        else if (potion == MobEffects.REGENERATION)
        {
            color = RenderUtil.rgbToDecimal(244, 120, 226);
        }
        else if (potion == MobEffects.STRENGTH)
        {
            color = RenderUtil.rgbToDecimal(179, 55, 55);
        }
        else if (potion == MobEffects.SPEED)
        {
            color = RenderUtil.rgbToDecimal(120, 201, 224);
        }
        else if (potion == MobEffects.FIRE_RESISTANCE)
        {
            color = RenderUtil.rgbToDecimal(233, 157, 73);
        }
        else if (potion == MobEffects.RESISTANCE)
        {
            color = RenderUtil.rgbToDecimal(137, 140, 154);
        }
        else if (potion == MobEffects.JUMP_BOOST)
        {
            color = RenderUtil.rgbToDecimal(33, 251, 75);
        }
        else if (potion == MobEffects.NIGHT_VISION)
        {
            color = RenderUtil.rgbToDecimal(97, 97, 224);
        }
        else if (potion == MobEffects.WATER_BREATHING)
        {
            color = RenderUtil.rgbToDecimal(79, 122, 202);
        }
        else if (potion == MobEffects.SLOWNESS)
        {
            color = RenderUtil.rgbToDecimal(103, 123, 146);
        }
        else if (potion == MobEffects.HASTE)
        {
            color = RenderUtil.rgbToDecimal(182, 169, 80);
        }
        else if (potion == MobEffects.MINING_FATIGUE)
        {
            color = RenderUtil.rgbToDecimal(90, 81, 29);
        }
        else if (potion == MobEffects.NAUSEA)
        {
            color = RenderUtil.rgbToDecimal(125, 43, 108);
        }
        else if (potion == MobEffects.INVISIBILITY)
        {
            color = RenderUtil.rgbToDecimal(139, 142, 156);
        }
        else if (potion == MobEffects.BLINDNESS)
        {
            color = RenderUtil.rgbToDecimal(90, 90, 90);
        }
        else if (potion == MobEffects.HUNGER)
        {
            color = RenderUtil.rgbToDecimal(99, 133, 92);
        }
        else if (potion == MobEffects.WEAKNESS)
        {
            color = RenderUtil.rgbToDecimal(102, 108, 102);
        }
        else if (potion == MobEffects.POISON)
        {
            color = RenderUtil.rgbToDecimal(81, 152, 50);
        }
        else if (potion == MobEffects.WITHER)
        {
            color = RenderUtil.rgbToDecimal(105, 84, 80);
        }
        else if (potion == MobEffects.HEALTH_BOOST)
        {
            color = RenderUtil.rgbToDecimal(245, 124, 35);
        }
        else if (potion == MobEffects.GLOWING)
        {
            color = RenderUtil.rgbToDecimal(146, 158, 96);
        }
        else if (potion == MobEffects.LEVITATION)
        {
            color = RenderUtil.rgbToDecimal(204, 252, 252);
        }
        else if (potion == MobEffects.LUCK)
        {
            color = RenderUtil.rgbToDecimal(50, 151, 0);
        }
        else if (potion == MobEffects.UNLUCK)
        {
            color = RenderUtil.rgbToDecimal(190, 162, 76);
        }
        return color;
    }
}