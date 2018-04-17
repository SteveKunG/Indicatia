package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import stevekung.mods.indicatia.config.ConfigManagerIN;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class CommandAFK extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "afk";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.afk.usage");
        }
        else
        {
            if ("stop".equalsIgnoreCase(args[0]))
            {
                if (args.length > 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }
                if (IndicatiaEventHandler.isAFK)
                {
                    IndicatiaEventHandler.isAFK = false;
                    IndicatiaEventHandler.afkMoveTicks = 0;

                    if (ConfigManagerIN.indicatia_general.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().player.sendChatMessage(LangUtils.translate("message.stop_afk", IndicatiaEventHandler.afkReason, CommonUtils.ticksToElapsedTime(IndicatiaEventHandler.afkTicks)));
                    }
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.afk_not_in_use")).setStyle(JsonUtils.red()));
                }
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                if (!IndicatiaEventHandler.isAFK)
                {
                    ITextComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 1);
                    String reason = component.createCopy().getUnformattedText();
                    IndicatiaEventHandler.isAFK = true;
                    IndicatiaEventHandler.afkReason = reason;

                    if (reason.isEmpty())
                    {
                        reason = "";
                    }
                    else
                    {
                        reason = ", " + LangUtils.translate("message.afk_reason") + " : " + reason;
                    }

                    String message = LangUtils.translate("message.afk_for_now");

                    if (ConfigManagerIN.indicatia_general.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().player.sendChatMessage(message + reason);
                    }
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.afk_in_use")).setStyle(JsonUtils.red()));
                }
            }
            else if ("change_reason".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }

                if (IndicatiaEventHandler.isAFK)
                {
                    String oldReason = IndicatiaEventHandler.afkReason;
                    String newReason = ClientCommandBase.getChatComponentFromNthArg(args, 1).createCopy().getUnformattedText();
                    IndicatiaEventHandler.afkReason = newReason;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.change_reason", oldReason, newReason)));
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.afk_not_in_use")).setStyle(JsonUtils.red()));
                }
            }
            else if ("mode".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }

                if ("idle".equalsIgnoreCase(args[1]))
                {
                    IndicatiaEventHandler.afkMode = "idle";
                    IndicatiaEventHandler.afkMoveTicks = 0;
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_afk_mode", IndicatiaEventHandler.afkMode)));
                }
                else if ("move".equalsIgnoreCase(args[1]))
                {
                    IndicatiaEventHandler.afkMode = "move";
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_afk_mode", IndicatiaEventHandler.afkMode)));
                }
                else if ("360".equalsIgnoreCase(args[1]))
                {
                    IndicatiaEventHandler.afkMode = "360";
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_afk_mode", IndicatiaEventHandler.afkMode)));
                }
                else if ("360_move".equalsIgnoreCase(args[1]))
                {
                    IndicatiaEventHandler.afkMode = "360_move";
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.set_afk_mode", IndicatiaEventHandler.afkMode)));
                }
                else
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }
            }
            else
            {
                throw new WrongUsageException("commands.afk.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop", "mode", "change_reason");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "idle", "move", "360", "360_move");
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}