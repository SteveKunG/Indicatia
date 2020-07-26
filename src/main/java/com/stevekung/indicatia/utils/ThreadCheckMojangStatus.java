package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

public class ThreadCheckMojangStatus implements Runnable
{
    @Override
    public void run()
    {
        for (MojangStatusChecker checker : MojangStatusChecker.VALUES)
        {
            ClientUtils.printClientMessage(JsonUtils.create(checker.getName() + ": ").append(checker.getStatus().getStatus().deepCopy().mergeStyle(checker.getStatus().getColor())));
        }
    }
}