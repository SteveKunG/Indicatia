package stevekung.mods.indicatia.event;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ConfigManagerIN;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;

public class HypixelEventHandler
{
    private static final Pattern nickPattern = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
    private Minecraft mc;

    public HypixelEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
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
    public void onMouseClick(MouseEvent event)
    {
        if (event.getButton() == 1 && event.isButtonstate() && InfoUtils.INSTANCE.isHypixel() && ExtendedConfig.rightClickToAddParty)
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

        String unformattedText = event.getMessage().getUnformattedText();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = HypixelEventHandler.nickPattern.matcher(unformattedText);

            if (event.getType() == ChatType.CHAT)
            {
                if (unformattedText.contains("Illegal characters in chat") || unformattedText.contains("A kick occurred in your connection, so you have been routed to limbo!"))
                {
                    event.setMessage(null);
                }
                else if (unformattedText.contains("You were spawned in Limbo."))
                {
                    event.setMessage(JsonUtils.create("You were spawned in Limbo.").setStyle(JsonUtils.green()));
                }
                else if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.hypixelNickName = "";
                    ExtendedConfig.save();
                }

                if (nickMatcher.matches())
                {
                    ExtendedConfig.hypixelNickName = nickMatcher.group("nick");
                    ExtendedConfig.save();
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

                    if (displayTitleMessage.contains(messageToLower) && !ConfigManagerIN.indicatia_general.autoGGMessage.isEmpty() && !IndicatiaEventHandler.printAutoGG)
                    {
                        IndicatiaEventHandler.printAutoGG = true;
                    }
                });
            }
        }
    }

    private static void getHypixelNickedPlayer(Minecraft mc)
    {
        if (InfoUtils.INSTANCE.isHypixel() && mc.currentScreen instanceof GuiEditSign)
        {
            GuiEditSign gui = (GuiEditSign) mc.currentScreen;

            if (gui.tileSign != null)
            {
                ExtendedConfig.hypixelNickName = gui.tileSign.signText[0].getUnformattedText();

                if (mc.player.ticksExisted % 40 == 0)
                {
                    ExtendedConfig.save();
                }
            }
        }
    }

    private static void rightClickAddParty(Minecraft mc)
    {
        if (mc.objectMouseOver != null)
        {
            switch (mc.objectMouseOver.typeOfHit)
            {
            case ENTITY:
            default:
                if (mc.player.getHeldItemMainhand().isEmpty() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityOtherPlayerMP)
                {
                    EntityOtherPlayerMP player = (EntityOtherPlayerMP) mc.objectMouseOver.entityHit;
                    mc.player.sendChatMessage("/p " + player.getName());
                }
            }
        }
    }
}