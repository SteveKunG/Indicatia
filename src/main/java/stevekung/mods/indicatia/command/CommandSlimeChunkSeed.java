package stevekung.mods.indicatia.command;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.ClientCommandBase;

public class CommandSlimeChunkSeed extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "slimeseed";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
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
                        ExtendedConfig.slimeChunkSeed = longSeed;
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_slime_seed", longSeed)));
                    }
                }
                catch (NumberFormatException e)
                {
                    ExtendedConfig.slimeChunkSeed = seed.hashCode();
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_slime_seed", seed.hashCode())));
                }
            }
            ExtendedConfig.save();
        }
    }
}