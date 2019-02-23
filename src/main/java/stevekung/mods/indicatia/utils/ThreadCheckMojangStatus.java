package stevekung.mods.indicatia.utils;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
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
            Minecraft.getInstance().player.sendMessage(JsonUtils.create(checker.getName() + ": ").appendSibling(JsonUtils.create(status.getColor() + status.getStatus())));
        });
    }
}