package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.ClientCommandBase;

public class CommandAutoFish extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "autofish";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            throw new CommandException("commands.not_allowed_hypixel");
        }

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autofish.usage");
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
            if ("disable".equalsIgnoreCase(args[0]))
            {
                if (IndicatiaEventHandler.autoFish)
                {
                    IndicatiaEventHandler.autoFish = !IndicatiaEventHandler.autoFish;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_fish_disabled")));
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.not_start_autofish")).setStyle(JsonUtils.red()));
                }
            }
            else if ("enable".equalsIgnoreCase(args[0]))
            {
                if (!IndicatiaEventHandler.autoFish)
                {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    boolean mainHand = player.getHeldItemMainhand().getItem() instanceof ItemFishingRod;
                    boolean offHand = player.getHeldItemOffhand().getItem() instanceof ItemFishingRod;

                    if (player.getHeldItemMainhand().getItem() instanceof ItemFishingRod)
                    {
                        offHand = false;
                    }

                    if (mainHand || offHand)
                    {
                        IndicatiaEventHandler.autoFish = !IndicatiaEventHandler.autoFish;
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_fish_enabled")));
                        return;
                    }
                    else
                    {
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.not_held_fishing_rod")).setStyle(JsonUtils.red()));
                        return;
                    }
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.already_start_autofish")).setStyle(JsonUtils.red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}