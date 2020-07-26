package com.stevekung.indicatia.command.arguments;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.util.text.TranslationTextComponent;

public class SlimeSeedArgumentType implements ArgumentType<String>
{
    private static final DynamicCommandExceptionType ZERO_NOT_ALLOWED = new DynamicCommandExceptionType(obj -> new TranslationTextComponent("commands.slime_seed.zero_not_allowed"));

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException
    {
        String text = reader.readString();

        if (text.equals("0"))
        {
            throw SlimeSeedArgumentType.ZERO_NOT_ALLOWED.create(text);
        }
        return text;
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