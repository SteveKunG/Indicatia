package com.stevekung.indicatia.command.arguments;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.stevekung.indicatia.config.IndicatiaSettings;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ProfileNameArgumentType implements ArgumentType<String>
{
    private static final SimpleCommandExceptionType INVALID_ARGS = new SimpleCommandExceptionType(new TranslatableComponent("argument.id.invalid"));
    private final Mode mode;

    private ProfileNameArgumentType(Mode mode)
    {
        this.mode = mode;
    }

    public static ProfileNameArgumentType create()
    {
        return ProfileNameArgumentType.create(Mode.NONE);
    }

    public static ProfileNameArgumentType create(Mode mode)
    {
        return new ProfileNameArgumentType(mode);
    }

    public static String getProfile(CommandContext<?> context, String name)
    {
        return context.getArgument(name, String.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
    {
        List<String> resList = Arrays.stream(IndicatiaSettings.USER_DIR.listFiles()).filter(file -> this.mode == Mode.REMOVE ? file.getName().endsWith(".dat") && !file.getName().equals("default.dat") : file.getName().endsWith(".dat")).map(file -> file.getName().replace(".dat", "")).collect(Collectors.toList());
        return this.mode == Mode.ADD ? Suggestions.empty() : ProfileNameArgumentType.suggestIterable(resList, builder);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException
    {
        return ProfileNameArgumentType.read(reader);
    }

    @Override
    public Collection<String> getExamples()
    {
        return Collections.singletonList("profile");
    }

    private static CompletableFuture<Suggestions> suggestIterable(Iterable<String> iterable, SuggestionsBuilder builder)
    {
        String typedString = builder.getRemaining().toLowerCase(Locale.ROOT);
        ProfileNameArgumentType.applySuggest(iterable, typedString, string1 -> string1, builder::suggest);
        return builder.buildFuture();
    }

    private static void applySuggest(Iterable<String> iterable, String typedString, Function<String, String> function, Consumer<String> consumer)
    {
        for (String name : iterable)
        {
            String name2 = function.apply(name);

            if (name2.startsWith(typedString))
            {
                consumer.accept(name);
            }
        }
    }

    private static String read(StringReader reader) throws CommandSyntaxException
    {
        int cursor = reader.getCursor();

        while (reader.canRead() && ResourceLocation.isAllowedInResourceLocation(reader.peek()))
        {
            reader.skip();
        }

        String string = reader.getString().substring(cursor, reader.getCursor());

        try
        {
            return string;
        }
        catch (ResourceLocationException e)
        {
            reader.setCursor(cursor);
            throw ProfileNameArgumentType.INVALID_ARGS.createWithContext(reader);
        }
    }

    @Nullable
    public static File getProfileFile(String name)
    {
        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (file.getName().equals(name + ".dat"))
            {
                return file;
            }
        }
        return null;
    }

    public enum Mode
    {
        NONE,
        ADD,
        REMOVE
    }
}