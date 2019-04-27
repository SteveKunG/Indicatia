package stevekung.mods.indicatia.utils;

import java.util.Arrays;

import net.minecraft.client.MinecraftClient;
import stevekung.mods.stevekungslib.utils.JsonUtils;

public class ThreadCheckMojangStatus extends Thread
{
    public ThreadCheckMojangStatus()
    {
        super("Mojang Status Check Thread");
    }

    @Override
    public void run()
    {
        Arrays.stream(MojangStatusChecker.values).forEach(checker ->
        {
            MojangServerStatus status = checker.getServiceStatus();
            MinecraftClient.getInstance().player.addChatMessage(JsonUtils.create(checker.getName() + ": ").append(JsonUtils.create(status.getColor() + status.getStatus())), false);
        });
    }
}