package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.GuiCustomTextColorSettings1;
import stevekung.mods.indicatia.gui.GuiRenderStatusSettings;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandIndicatia extends ClientCommandBase
{
    @Override
    public String getCommandName()
    {
        return "indicatia";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;

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
                    ExtendedConfig.TOGGLE_SPRINT = true;
                    sender.addChatMessage(json.text("Enabled Toggle Sprint"));
                    ExtendedConfig.save();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.TOGGLE_SPRINT = false;
                    sender.addChatMessage(json.text("Disabled Toggle Sprint"));
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
                        ExtendedConfig.TOGGLE_SPRINT_USE_MODE = "key_binding";
                        sender.addChatMessage(json.text("Set toggle sprint to use Key Binding"));
                        ExtendedConfig.save();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.TOGGLE_SPRINT_USE_MODE = "command";
                        sender.addChatMessage(json.text("Set toggle sprint to use Command"));
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
                    ExtendedConfig.TOGGLE_SNEAK = true;
                    sender.addChatMessage(json.text("Enabled Toggle Sprint"));
                    ExtendedConfig.save();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.TOGGLE_SNEAK = false;
                    sender.addChatMessage(json.text("Disabled Toggle Sprint"));
                    ExtendedConfig.save();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length < 3 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatia.togglesneak.mode.usage");
                    }
                    if ("key_binding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.TOGGLE_SNEAK_USE_MODE = "key_binding";
                        sender.addChatMessage(json.text("Set toggle sneak to use Key Binding"));
                        ExtendedConfig.save();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.TOGGLE_SNEAK_USE_MODE = "command";
                        sender.addChatMessage(json.text("Set toggle sneak to use Command"));
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
            else if ("cps".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    throw new WrongUsageException("commands.indicatia.cps.usage");
                }

                if ("left".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.CPS_POSITION = "left";
                    sender.addChatMessage(json.text("Set CPS position to Left"));
                    ExtendedConfig.save();
                }
                else if ("right".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.CPS_POSITION = "right";
                    sender.addChatMessage(json.text("Set CPS position to Right"));
                    ExtendedConfig.save();
                }
                else if ("keystroke".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.CPS_POSITION = "keystroke";
                    sender.addChatMessage(json.text("Set CPS position to Keystroke"));
                    ExtendedConfig.save();
                }
                else if ("custom".equalsIgnoreCase(args[1]))
                {
                    ExtendedConfig.CPS_POSITION = "custom";
                    sender.addChatMessage(json.text("Set CPS position to Customize"));
                    ExtendedConfig.save();
                }
                else
                {
                    throw new WrongUsageException("commands.indicatia.cps.usage");
                }
            }
            else if (IndicatiaMod.isSteveKunG() && "auto_swim".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatia.autoswim.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatia.autoswim.usage");
                    }
                    ExtendedConfig.AUTO_SWIM = true;
                    sender.addChatMessage(json.text("Enabled Auto Swim"));
                    ExtendedConfig.save();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatia.autoswim.usage");
                    }
                    ExtendedConfig.AUTO_SWIM = false;
                    sender.addChatMessage(json.text("Disabled Auto Swim"));
                    ExtendedConfig.save();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 2 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatia.autoswim.mode.usage");
                    }

                    if ("key_binding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.AUTO_SWIM_USE_MODE = "key_binding";
                        sender.addChatMessage(json.text("Set auto swim to use Key Binding"));
                        ExtendedConfig.save();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedConfig.AUTO_SWIM_USE_MODE = "command";
                        sender.addChatMessage(json.text("Set auto swim to use Command"));
                        ExtendedConfig.save();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatia.autoswim.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatia.autoswim.usage");
                }
            }
            else if ("gui".equalsIgnoreCase(args[0]))
            {
                new GuiRenderStatusSettings().display();
            }
            else if ("color_gui".equalsIgnoreCase(args[0]))
            {
                new GuiCustomTextColorSettings1().display();
            }
            else
            {
                throw new WrongUsageException("commands.indicatia.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "toggle_sprint", "toggle_sneak", "cps", IndicatiaMod.isSteveKunG() ? "auto_swim" : "", "gui", "color_gui");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("toggle_sprint") || args[0].equalsIgnoreCase("toggle_sneak") || IndicatiaMod.isSteveKunG() && args[0].equalsIgnoreCase("auto_swim"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode");
            }
            if (args[0].equalsIgnoreCase("cps"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "left", "right", "keystroke", "custom");
            }
        }
        if (args.length == 3)
        {
            if ((args[0].equalsIgnoreCase("toggle_sprint") || args[0].equalsIgnoreCase("toggle_sneak") || IndicatiaMod.isSteveKunG() && args[0].equalsIgnoreCase("auto_swim")) && args[1].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "key_binding", "command");
            }
        }
        return super.addTabCompletionOptions(sender, args, pos);
    }
}