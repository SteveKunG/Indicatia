package stevekung.mods.indicatia.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.ClientCommandBase;

public class CommandIndicatia extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "indicatia";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("in");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.indicatia.usage");
        }
        else
        {
            if ("toggle_sprint".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatia.togglesprint.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.toggleSprint = true;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sprint_enabled")));
                    ExtendedConfig.save();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.toggleSprint = false;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sprint_disabled")));
                    ExtendedConfig.save();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length < 3 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatia.togglesprint.mode.usage");
                    }

                    if ("key_binding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.toggleSprintUseMode = "key_binding";
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sprint_set") + LangUtils.translate("message.key_binding")));
                        ExtendedConfig.save();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.toggleSprintUseMode = "command";
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sprint_set") + LangUtils.translate("message.command")));
                        ExtendedConfig.save();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatia.togglesprint.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatia.togglesprint.usage");
                }
            }
            else if ("toggle_sneak".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatia.togglesneak.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.toggleSneak = true;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sneak_enabled")));
                    ExtendedConfig.save();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.toggleSneak = false;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sneak_disabled")));
                    ExtendedConfig.save();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length < 3 || args.length > 3)
                    {
                        throw new WrongUsageException(LangUtils.translate("message.toggle_sneak_set") + LangUtils.translate("message.key_binding"));
                    }
                    if ("key_binding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.toggleSneakUseMode = "key_binding";
                        sender.sendMessage(JsonUtils.create("Set toggle sneak to use Key Binding"));
                        ExtendedConfig.save();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.toggleSneakUseMode = "command";
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.toggle_sneak_set") + LangUtils.translate("message.command")));
                        ExtendedConfig.save();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatia.togglesneak.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatia.togglesneak.usage");
                }
            }
            else if ("gui".equalsIgnoreCase(args[0]))
            {
                GuiExtendedConfig options = new GuiExtendedConfig();
                options.display();
            }
            else
            {
                throw new WrongUsageException("commands.indicatia.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "toggle_sprint", "toggle_sneak", "gui");
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("toggle_sprint") || args[0].equalsIgnoreCase("toggle_sneak"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode");
            }
        }
        else if (args.length == 3)
        {
            if ((args[0].equalsIgnoreCase("toggle_sprint") || args[0].equalsIgnoreCase("toggle_sneak")) && args[1].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "key_binding", "command");
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}