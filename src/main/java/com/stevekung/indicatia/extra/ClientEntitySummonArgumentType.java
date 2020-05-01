package com.stevekung.indicatia.extra;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClientEntitySummonArgumentType implements ArgumentType<Identifier>
{
    private static final Collection<String> EXAMPLES = Arrays.asList("minecraft:pig", "cow");

    public static final DynamicCommandExceptionType NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(obj -> new TranslatableText("entity.notFound", new Object[]{obj}));

    public static ClientEntitySummonArgumentType create()
    {
        return new ClientEntitySummonArgumentType();
    }

    public static Identifier getEntitySummon(CommandContext<CottonClientCommandSource> commandContext_1, String string_1) throws CommandSyntaxException
    {
        return validate(commandContext_1.getArgument(string_1, Identifier.class));
    }

    public static Entity getEntity(CommandContext<CottonClientCommandSource> commandContext_1, String string_1) throws CommandSyntaxException
    {
        return Lists.newArrayList(MinecraftClient.getInstance().world.getEntities()).get(0);
    }

    private static Identifier validate(Identifier identifier) throws CommandSyntaxException
    {
        Registry.ENTITY_TYPE.getOrEmpty(identifier).filter(EntityType::isSummonable).orElseThrow(() -> NOT_FOUND_EXCEPTION.create(identifier));
        return identifier;
    }

    public Identifier method_9325(StringReader stringReader_1) throws CommandSyntaxException
    {
        return validate(Identifier.fromCommandInput(stringReader_1));
    }

    @Override
    public Collection<String> getExamples()
    {
        return EXAMPLES;
    }

    @Override
    public Identifier parse(StringReader var1) throws CommandSyntaxException
    {
        return this.method_9325(var1);
    }
}
