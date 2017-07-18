package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.ForgeHooks;

public abstract class ClientCommandBase extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + this.getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    protected IChatComponent getChatComponentFromNthArg(String[] args, int index)
    {
        IChatComponent itextcomponent = new ChatComponentText("");

        for (int i = index; i < args.length; ++i)
        {
            if (i > index)
            {
                itextcomponent.appendText(" ");
            }
            IChatComponent itextcomponent1 = ForgeHooks.newChatWithLinks(args[i]);
            itextcomponent.appendSibling(itextcomponent1);
        }
        return itextcomponent;
    }
}