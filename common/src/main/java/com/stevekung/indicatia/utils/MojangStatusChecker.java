package com.stevekung.indicatia.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stevekung.indicatia.core.Indicatia;

public enum MojangStatusChecker
{
    MAIN_WEBSITE("Main Website", "minecraft.net"),
    MC_SESSION_SERVER("Minecraft Session Server", "session.minecraft.net"),
    TEXTURES_SERVICE("Minecraft Textures Service", "textures.minecraft.net"),
    MOJANG_ACCOUNT_SERVICE("Mojang Account Service", "account.mojang.com"),
    MOJANG_SESSION_SERVER("Mojang Session Server", "sessionserver.mojang.com"),
    MOJANG_AUTHENTICATION_SERVER("Mojang Authentication Server", "authserver.mojang.com"),
    MOJANG_PUBLIC_API("Mojang Public API", "api.mojang.com"),
    MOJANG_MAIN_WEBSITE("Mojang Main Website", "mojang.com");

    private final String name;
    private final String serviceURL;
    public static final MojangStatusChecker[] VALUES = MojangStatusChecker.values();

    MojangStatusChecker(String name, String serviceURL)
    {
        this.name = name;
        this.serviceURL = serviceURL;
    }

    public String getName()
    {
        return this.name;
    }

    public MojangServerStatus getStatus()
    {
        try
        {
            URL url = new URL("http://status.mojang.com/check?service=" + this.serviceURL);
            JsonElement element = new JsonParser().parse(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).getAsJsonObject().get(this.serviceURL);
            return MojangServerStatus.get(element.getAsString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Indicatia.LOGGER.error("Couldn't get status data from Mojang Service!");
            return MojangServerStatus.UNKNOWN;
        }
    }
}