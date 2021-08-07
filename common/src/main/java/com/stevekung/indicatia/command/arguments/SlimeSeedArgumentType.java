package com.stevekung.indicatia.command.arguments;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.TranslatableComponent;

public class SlimeSeedArgumentType implements ArgumentType<String>
{
    private static final SimpleCommandExceptionType ZERO_NOT_ALLOWED = new SimpleCommandExceptionType(new TranslatableComponent("commands.slime_seed.zero_not_allowed"));

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException
    {
        var text = reader.readString();

        if (text.equals("0"))
        {
            try
            {
                throw SlimeSeedArgumentType.ZERO_NOT_ALLOWED.create();
            }
            catch (Exception e)
            {
                return "1";
            }
        }
        else
        {
            return text;
        }
    }

    @Override
    public Collection<String> getExamples()
    {
        return Arrays.asList("words with spaces", "\"and symbols\"");
    }

    public static SlimeSeedArgumentType string()
    {
        return new SlimeSeedArgumentType();
    }

    public static String getString(CommandContext<?> context, final String name)
    {
        return context.getArgument(name, String.class);
    }
}