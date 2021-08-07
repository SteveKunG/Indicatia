package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

public class ThreadCheckMojangStatus implements Runnable
{
    public ThreadCheckMojangStatus()
    {
        this.run();
    }

    @Override
    public void run()
    {
        for (var checker : MojangStatusChecker.VALUES)
        {
            ClientUtils.printClientMessage(TextComponentUtils.component(checker.getName() + ": ").append(checker.getStatus().getStatus()));
        }
    }
}