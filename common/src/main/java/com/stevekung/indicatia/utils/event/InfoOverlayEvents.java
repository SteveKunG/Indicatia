package com.stevekung.indicatia.utils.event;

import java.util.List;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface InfoOverlayEvents
{
    Event<InfoOverlay> INFO_OVERLAY = EventFactory.createLoop();

    @Environment(EnvType.CLIENT)
    interface InfoOverlay
    {
        void addInfos(List<com.stevekung.indicatia.utils.hud.InfoOverlay> infos);
    }
}