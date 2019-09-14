package stevekung.mods.indicatia.utils;

import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

public class ThreadCheckMojangStatus extends Thread
{
    public ThreadCheckMojangStatus()
    {
        super("Mojang Status Check Thread");
    }

    @Override
    public void run()
    {
        for (MojangStatusChecker checker : MojangStatusChecker.VALUES)
        {
            ClientUtils.printClientMessage(JsonUtils.create(checker.getName() + ": ").appendSibling(JsonUtils.create(checker.getStatus().getColor() + checker.getStatus().getStatus())));
        }
    }
}