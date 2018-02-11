package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.GameProfileUtil;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandEntityDetector extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "entitydetect";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.entitydetect.usage");
        }
        else
        {
            if ("entity".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.entitydetect.entity.usage");
                }

                String input = args[1];
                sender.sendMessage(json.text("Detecting entity: " + input));
                ExtendedConfig.ENTITY_DETECT_TYPE = input;
                ExtendedConfig.save();
            }
            else if ("player".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.entitydetect.player.usage");
                }

                String input = args[1];

                if (GameProfileUtil.getUsername().equalsIgnoreCase(input))
                {
                    sender.sendMessage(json.text("Cannot set entity detector type to yourself!").setStyle(json.red()));
                    return;
                }
                else
                {
                    sender.sendMessage(json.text("Detecting player name: " + input));
                    ExtendedConfig.ENTITY_DETECT_TYPE = input;
                    ExtendedConfig.save();
                }
            }
            else if ("all".equalsIgnoreCase(args[0]) || "only_mob".equalsIgnoreCase(args[0]) || "only_creature".equalsIgnoreCase(args[0]) || "only_non_mob".equalsIgnoreCase(args[0]) || "only_player".equalsIgnoreCase(args[0]) || "only_mob".equalsIgnoreCase(args[0]))
            {
                sender.sendMessage(json.text(args[0].equalsIgnoreCase("all") ? "Set detect to all" : "Set detecting only: " + args[0]));
                ExtendedConfig.ENTITY_DETECT_TYPE = args[0];
                ExtendedConfig.save();
            }
            else if ("reset".equalsIgnoreCase(args[0]))
            {
                sender.sendMessage(json.text("Reset entity detector"));
                ExtendedConfig.ENTITY_DETECT_TYPE = "";
                ExtendedConfig.save();
            }
            else
            {
                throw new WrongUsageException("commands.entitydetect.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        NetHandlerPlayClient connection = IndicatiaMod.MC.player.connection;
        List<NetworkPlayerInfo> playerInfo = new ArrayList<>(connection.getPlayerInfoMap());
        playerInfo.removeIf(entry -> entry.getGameProfile().getName().equalsIgnoreCase(GameProfileUtil.getUsername()));

        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "entity", "player", "all", "only_mob", "only_creature", "only_non_mob", "only_player", "reset");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("entity"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
            }
            if (args[0].equalsIgnoreCase("player"))
            {
                List<String> playerList = new ArrayList<>();

                for (int i = 0; i < playerInfo.size(); ++i)
                {
                    if (i < playerInfo.size())
                    {
                        playerList.add(playerInfo.get(i).getGameProfile().getName());
                    }
                }
                return CommandBase.getListOfStringsMatchingLastWord(args, playerList);
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}