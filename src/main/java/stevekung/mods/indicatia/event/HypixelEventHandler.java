package stevekung.mods.indicatia.event;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;

public class HypixelEventHandler
{
    private static final Pattern nickPattern = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
    private Minecraft mc;

    public HypixelEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (this.mc.player != null)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                HypixelEventHandler.getHypixelNickedPlayer(this.mc);
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event)
    {
        if (event.getButton() == GLFW.GLFW_PRESS && event.getAction() == GLFW.GLFW_MOUSE_BUTTON_2 && InfoUtils.INSTANCE.isHypixel() && ExtendedConfig.instance.rightClickToAddParty)
        {
            HypixelEventHandler.rightClickAddParty(this.mc);
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if (event.getMessage() == null)
        {
            return;
        }

        String unformattedText = event.getMessage().getUnformattedComponentText();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = HypixelEventHandler.nickPattern.matcher(unformattedText);

            if (event.getType() == ChatType.CHAT)
            {
                if (unformattedText.contains("Illegal characters in chat") || unformattedText.contains("A kick occurred in your connection, so you have been routed to limbo!") || unformattedText.contains("A kick occurred in your connection, so you were put in the"))
                {
                    event.setMessage(null);
                }
                else if (unformattedText.contains("You were spawned in Limbo."))
                {
                    event.setMessage(JsonUtils.create("You were spawned in Limbo.").setStyle(JsonUtils.green()));
                }
                else if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.instance.hypixelNickName = "";
                    ExtendedConfig.instance.save();
                }

                if (nickMatcher.matches())
                {
                    ExtendedConfig.instance.hypixelNickName = nickMatcher.group("nick");
                    ExtendedConfig.instance.save();
                }

                // https://gist.githubusercontent.com/minemanpi/72c38b0023f5062a5f3eba02a5132603/raw/triggers.txt
                List<String> message = new ArrayList<>();
                message.add("1st Killer - ");
                message.add("1st Place - ");
                message.add("Winner: ");
                message.add(" - Damage Dealt - ");
                message.add("Winning Team -");
                message.add("1st - ");
                message.add("Winners: ");
                message.add("Winner: ");
                message.add("Winning Team: ");
                message.add(" won the game!");
                message.add("Top Seeker: ");
                message.add("1st Place: ");
                message.add("Last team standing!");
                message.add("Winner #1 (");
                message.add("Top Survivors");
                message.add("Winners - ");
                message.add("WINNER!");

                message.forEach(text ->
                {
                    String messageToLower = TextFormatting.getTextWithoutFormattingCodes(text).toLowerCase();
                    String displayTitleMessage = TextFormatting.getTextWithoutFormattingCodes(unformattedText).toLowerCase();

                    if (displayTitleMessage.contains(messageToLower) && !IndicatiaConfig.GENERAL.autoGGMessage.get().isEmpty() && !IndicatiaEventHandler.printAutoGG)
                    {
                        IndicatiaEventHandler.printAutoGG = true;
                    }
                });
            }
        }
    }

    private static void getHypixelNickedPlayer(Minecraft mc)
    {
        if (InfoUtils.INSTANCE.isHypixel() && mc.currentScreen instanceof EditSignScreen)
        {
            EditSignScreen gui = (EditSignScreen) mc.currentScreen;

            if (gui.tileSign != null)
            {
                ExtendedConfig.instance.hypixelNickName = gui.tileSign.signText[0].getUnformattedComponentText();

                if (mc.player.ticksExisted % 40 == 0)
                {
                    ExtendedConfig.instance.save();
                }
            }
        }
    }

    public static void rightClickAddParty(Minecraft mc)
    {
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY)
        {
            EntityRayTraceResult result = (EntityRayTraceResult)mc.objectMouseOver;

            if (mc.player.getHeldItemMainhand().isEmpty() && result.getEntity() instanceof RemoteClientPlayerEntity)
            {
                RemoteClientPlayerEntity player = (RemoteClientPlayerEntity) result.getEntity();
                mc.player.sendChatMessage("/p " + player.getName());
            }
        }
    }
}