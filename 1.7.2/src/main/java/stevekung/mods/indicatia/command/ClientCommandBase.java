package stevekung.mods.indicatia.command;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

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
            IChatComponent component1 = ClientCommandBase.newChatWithLinks(args[i]);
            component.appendSibling(component1);
        }
        return component;
    }

    protected static List<String> getListOfStringsMatchingLastWord(String[] stringList, Collection<?> collection)
    {
        String s = stringList[stringList.length - 1];
        List<String> list = Lists.newArrayList();

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

    protected static IChatComponent newChatWithLinks(String string)
    {
        // Includes ipv4 and domain pattern
        // Matches an ip (xx.xxx.xx.xxx) or a domain (something.com) with or
        // without a protocol or path.
        Pattern URL_PATTERN = Pattern.compile(
                //         schema                          ipv4            OR           namespace                 port     path         ends
                //   |-----------------|        |-------------------------|  |----------------------------|    |---------| |--|   |---------------|
                "((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_\\.]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00A7 \n]|$))",
                Pattern.CASE_INSENSITIVE);
        IChatComponent ichat = new ChatComponentText("");
        Matcher matcher = URL_PATTERN.matcher(string);
        int lastEnd = 0;
        // Find all urls
        while (matcher.find())
        {
            int start = matcher.start();
            int end = matcher.end();

            // Append the previous left overs.
            ichat.appendText(string.substring(lastEnd, start));
            lastEnd = end;
            String url = string.substring(start, end);
            IChatComponent link = new ChatComponentText(url);

            // Add schema so client doesn't crash.
            if (URI.create(url).getScheme() == null)
            {
                url = "http://" + url;
            }

            // Set the click event and append the link.
            ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
            link.getChatStyle().setChatClickEvent(click);
            ichat.appendSibling(link);
        }

        // Append the rest of the message.
        ichat.appendText(string.substring(lastEnd));
        return ichat;
    }
}