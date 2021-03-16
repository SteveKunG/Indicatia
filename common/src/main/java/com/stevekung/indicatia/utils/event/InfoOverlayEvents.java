package com.stevekung.indicatia.utils.event;

import java.util.List;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;

public interface InfoOverlayEvents
{
    Event<InfoOverlay> INFO_OVERLAY = EventFactory.createLoop();

    interface InfoOverlay
    {
        void addInfos(List<com.stevekung.indicatia.utils.hud.InfoOverlay> infos);
    }
}