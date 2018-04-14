package stevekung.mods.indicatia.utils;

import net.minecraft.client.Minecraft;
import stevekung.mods.stevekunglib.util.JsonUtils;

public class ThreadCheckMojangStatus extends Thread
{
    public ThreadCheckMojangStatus()
    {
        super("Mojang Status Check Thread");
    }

    @Override
    public void run()
    {
        for (MojangStatusChecker checker : MojangStatusChecker.valuesCached())
        {
            MojangServerStatus status = checker.getServiceStatus();
            Minecraft.getMinecraft().player.sendMessage(JsonUtils.create(checker.getName() + ": ").appendSibling(JsonUtils.create(status.getColor() + status.getStatus())));
        }
    }
}