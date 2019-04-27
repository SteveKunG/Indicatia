package stevekung.mods.indicatia.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TextComponent;

public class ClientCommandUtils
{
    public static TextComponent getMessageArgument(CommandContext<CommandSource> context, String arg) throws CommandSyntaxException
    {
        return context.getArgument(arg, MessageArgumentType.MessageFormat.class).format((ServerCommandSource)context.getSource(), ((ServerCommandSource)context.getSource()).hasPermissionLevel(0));
    }
}