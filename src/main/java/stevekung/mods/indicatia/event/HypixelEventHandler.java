package stevekung.mods.indicatia.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.InfoUtils;

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
        String unformattedText = event.getMessage().getUnformattedText();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = HypixelEventHandler.nickPattern.matcher(unformattedText);

            if (event.getType() == ChatType.CHAT)
            {
                if (nickMatcher.matches())
                {
                    ExtendedConfig.hypixelNickName = nickMatcher.group("nick");
                    ExtendedConfig.save();
                }
                if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.hypixelNickName = "";
                    ExtendedConfig.save();
                }
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
                ExtendedConfig.save();
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