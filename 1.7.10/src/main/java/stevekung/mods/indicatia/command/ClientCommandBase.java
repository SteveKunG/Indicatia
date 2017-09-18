package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
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

    protected static IChatComponent getChatComponentFromNthArg(String[] args, int index)
    {
        IChatComponent component = new ChatComponentText("");

        for (int i = index; i < args.length; ++i)
        {
            if (i > index)
            {
                component.appendText(" ");
            }
            IChatComponent component1 = ForgeHooks.newChatWithLinks(args[i]);
            component.appendSibling(component1);
        }
        return component;
    }

    protected static List<String> getListOfStringsMatchingLastWord(String[] stringList, Collection<?> collection)
    {
        String s = stringList[stringList.length - 1];
        List<String> list = new ArrayList<>();

        if (!collection.isEmpty())
        {
            for (String s1 : Iterables.transform(collection, Functions.toStringFunction()))
            {
                if (doesStringStartWith(s, s1))
                {
                    list.add(s1);
                }
            }
            if (list.isEmpty())
            {
                for (Object object : collection)
                {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath()))
                    {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }
}