package stevekung.mods.indicatia.command;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.JsonUtil;

public class CommandSlimeChunkSeed extends ClientCommandBase
{
    @Override
    public String getCommandName()
    {
        return "slimeseed";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = new JsonUtil();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.slimeseed.usage");
        }
        else
        {
            String seed = args[0];

            if (!StringUtils.isEmpty(seed))
            {
                try
                {
                    long longSeed = Long.parseLong(seed);

                    if (longSeed != 0L)
                    {
                        ExtendedConfig.SLIME_CHUNK_SEED = longSeed;
                        sender.addChatMessage(json.text("Set slime chunk seed to " + longSeed));
                    }
                }
                catch (NumberFormatException e)
                {
                    ExtendedConfig.SLIME_CHUNK_SEED = seed.hashCode();
                    sender.addChatMessage(json.text("Set slime chunk seed to " + seed.hashCode()));
                }
            }
            ExtendedConfig.save();
        }
    }
}