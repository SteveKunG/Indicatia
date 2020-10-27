package com.stevekung.indicatia.utils.event;

import java.util.List;

import com.stevekung.indicatia.utils.hud.InfoOverlay;

import net.minecraftforge.eventbus.api.Event;

public class InfoOverlayEvent extends Event
{
    private final List<InfoOverlay> infos;

    public InfoOverlayEvent(List<InfoOverlay> infos)
    {
        this.infos = infos;
    }

    public List<InfoOverlay> getInfos()
    {
        return this.infos;
    }
}